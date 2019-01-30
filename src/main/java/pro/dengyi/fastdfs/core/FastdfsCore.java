package pro.dengyi.fastdfs.core;

import com.sun.istack.internal.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import pro.dengyi.fastdfs.config.FastdfsConfiguration;
import pro.dengyi.fastdfs.constantenum.CommonLength;
import pro.dengyi.fastdfs.constantenum.ControlCode;
import pro.dengyi.fastdfs.constantenum.SystemStatus;
import pro.dengyi.fastdfs.entity.BasicStorageInfo;
import pro.dengyi.fastdfs.entity.ReceiveData;
import pro.dengyi.fastdfs.entity.StorageInfo;
import pro.dengyi.fastdfs.exception.FastdfsException;
import pro.dengyi.fastdfs.threads.UploadMetadataThread;
import pro.dengyi.fastdfs.utils.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * tracker核心类
 *
 * @author 邓艺
 * @version v1.0
 * @date 2018-12-25 16:15
 */
@Slf4j
public class FastdfsCore {

    /**
     * 将存储服务器从集群中删除
     * <br/>
     * 在删除的时候，因为tracker集群之间是不通信的，所以要分别去删除
     *
     * @param groupName 组名
     * @param storageIpAddr 存储服务器ip
     * @return boolean
     * @author 邓艺
     * @date 2019/1/20 21:38
     */
    public Boolean doDeleteStorage(FastdfsConfiguration fastdfsConfiguration, String groupName, String storageIpAddr) {
        //获取全部tracker的socket连接
        List<Socket> allTrackerSocket = TrackerUtil.getAllTrackerSocket(fastdfsConfiguration.getTrackers());
        if (CollectionUtils.isNotEmpty(allTrackerSocket)) {
            //定义删除成功标识
            boolean flag = false;
            for (Socket trackerSocket : allTrackerSocket) {
                byte[] standardGroupNameBytes = new byte[CommonLength.GROUP_NAME_MAX_LENGTH.getLength()];
                byte[] groupNameBytes = groupName.getBytes(StandardCharsets.UTF_8);
                System.arraycopy(groupNameBytes, 0, standardGroupNameBytes, 0, groupNameBytes.length);
                byte[] storageIpAddrBytes = storageIpAddr.getBytes(StandardCharsets.UTF_8);
                //获取报文头
                byte[] protoHeader = ProtocolUtil
                        .getProtoHeader(ControlCode.TRACKER_DELETE_STORAGE.getValue(), (long) standardGroupNameBytes.length + storageIpAddrBytes.length,
                                (byte) 0);
                try {
                    trackerSocket.getOutputStream().write(protoHeader);
                    ReceiveData responseData = ProtocolUtil.getResponseData(trackerSocket.getInputStream(), (byte) 100, (long) -1);
                    flag = responseData.getErrorNo() == 0;
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
            return flag;
        } else {
            return false;
        }

    }

    /**
     * 获取存储组下指定ip的storage的信息
     *
     * @param groupName 组名不能为空
     * @param storageIpAddr 存储服务器的IP地址,可以为空
     * @return List  StorageInfo集合
     * @author 邓艺
     * @date 2019/1/18 12:38
     */
    public List<StorageInfo> doGetAllStorageInfo(@NotNull String groupName, String storageIpAddr, @NotNull FastdfsConfiguration fastdfsConfiguration)
            throws FastdfsException, IOException {
        if (StringUtils.isBlank(groupName)) {
            throw new FastdfsException("获取storage信息时，groupName不能为空");
        } else {
            //创建trackersocket
            Socket trackerSocket = TrackerUtil.getTrackerSocket(fastdfsConfiguration.getTrackers());
            byte[] protoHeader = null;
            byte[] wholeMessage = null;
            byte[] standardGroupNameByteArray = new byte[CommonLength.MAX_GROUPNAME_LENGTH.getLength()];
            byte[] groupNameBytes = groupName.getBytes(StandardCharsets.UTF_8);
            System.arraycopy(groupNameBytes, 0, standardGroupNameByteArray, 0, groupNameBytes.length);
            if (StringUtils.isNotBlank(storageIpAddr)) {
                byte[] storageIpAddrBytes = storageIpAddr.getBytes(StandardCharsets.UTF_8);
                protoHeader = ProtocolUtil.getProtoHeader(ControlCode.TRACKER_GET_ALL_STORAGEINFO.getValue(), (long) 16 + storageIpAddrBytes.length,
                        SystemStatus.SUCCESS.getValue());
                wholeMessage = new byte[42];
                System.arraycopy(protoHeader, 0, wholeMessage, 0, 10);
                System.arraycopy(standardGroupNameByteArray, 0, wholeMessage, 10, 16);
                System.arraycopy(storageIpAddrBytes, 0, wholeMessage, 26, storageIpAddrBytes.length);
            } else {
                protoHeader = ProtocolUtil.getProtoHeader(ControlCode.TRACKER_GET_ALL_STORAGEINFO.getValue(), 16L, SystemStatus.SUCCESS.getValue());
                wholeMessage = new byte[26];
                System.arraycopy(protoHeader, 0, wholeMessage, 0, 10);
                System.arraycopy(standardGroupNameByteArray, 0, wholeMessage, 10, 16);
            }
            assert trackerSocket != null;
            trackerSocket.getOutputStream().write(wholeMessage);
            ReceiveData responseData = ProtocolUtil.getResponseData(trackerSocket.getInputStream(), ControlCode.SERVER_RESPONSE.getValue(), (long) -1);
            trackerSocket.close();
            return ResponseDataUtil.getAllStorageInfo(responseData.getBody());
        }
    }

    /**
     * 执行上传文件
     *
     * @param fileBytes 文件字节数组
     * @param waterMarkFileBytes 水印文件字节数组
     * @param fileName 文件名
     * @param metadata 元数据
     * @return java.lang.String
     * @author 邓艺
     * @date 2019/1/25 22:34
     */
    public String[] doUploadFile(@NotNull byte[] fileBytes, byte[] waterMarkFileBytes, String fileName, List<String> metadata,
            FastdfsConfiguration fastdfsConfiguration) {
        String groupName = null;
        String remoteFilename = null;
        BasicStorageInfo uploadStorage = null;
        String[] results = new String[2];
        try {
            //获取上传文件目的地storage
            uploadStorage = getUploadStorage(fastdfsConfiguration);
            Socket storageSocket = new Socket(uploadStorage.getIp(), Math.toIntExact(uploadStorage.getPort()));
            storageSocket.setSoTimeout(fastdfsConfiguration.getNetworkTimeOut());
            //上传普通文件
            //1. 第一位为存储路径，后8位长度
            byte[] bodyDataHeader = new byte[1 + 8];
            bodyDataHeader[0] = 0;
            byte[] fileLengthByteArray = ProtocolUtil.long2ByteArray((long) fileBytes.length);
            System.arraycopy(fileLengthByteArray, 0, bodyDataHeader, 1, 8);
            //标准后缀名字节数组
            byte[] standardExtNameByteArry = new byte[6];
            byte[] realNameByteArray = FileNameUtil.getExtNameWithDot(fileName).substring(1).getBytes(StandardCharsets.UTF_8);
            System.arraycopy(realNameByteArray, 0, standardExtNameByteArry, 0, realNameByteArray.length);

            //1. 产生报文头部
            byte[] protoHeader = ProtocolUtil
                    .getProtoHeader(ControlCode.UPLOAD.getValue(), (long) (bodyDataHeader.length + standardExtNameByteArry.length + fileBytes.length),
                            SystemStatus.SUCCESS.getValue());
            //2. 完整报文实际长度为头长度10+存储路径1+体长度8+扩展名长度6
            byte[] wholeMessage = new byte[25];
            System.arraycopy(protoHeader, 0, wholeMessage, 0, 10);
            System.arraycopy(bodyDataHeader, 0, wholeMessage, 10, 9);
            System.arraycopy(standardExtNameByteArry, 0, wholeMessage, 19, 6);
            //发送数据
            OutputStream outputStream = storageSocket.getOutputStream();
            outputStream.write(wholeMessage);
            outputStream.write(fileBytes);
            //获取响应值
            ReceiveData responseData = ProtocolUtil.getResponseData(storageSocket.getInputStream(), (byte) 100, (long) -1);
            groupName = new String(responseData.getBody(), 0, 16).trim();
            remoteFilename = new String(responseData.getBody(), 16, responseData.getBody().length - 16);
            //主文件地址
            results[0] = fastdfsConfiguration.getAccessHead() + uploadStorage.getIp() + ":" + fastdfsConfiguration.getAccessPort() + "/" + groupName + "/"
                    + remoteFilename;
            //TODO 上传缩略图
            results[1] = fastdfsConfiguration.getAccessHead() + uploadStorage.getIp() + ":" + fastdfsConfiguration.getAccessPort() + "/" + doUploadSlaveFile(
                    storageSocket, remoteFilename, null, null, fileBytes, fileName, waterMarkFileBytes, fastdfsConfiguration);
            //TODO 启动metadata上传线程
            if (CollectionUtils.isNotEmpty(metadata)) {
                new Thread(new UploadMetadataThread(groupName, remoteFilename, metadata, storageSocket)).start();
            }
            storageSocket.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return results;
    }

    /**
     * 上传主从文件
     * <br/>
     * 目的只是为了上传缩略图，所以很多处理方式将写死
     * 只为图片上传缩略图，其他文件将不进行处理
     *
     * @param storageSocket 存储服务器socket连接
     * @param fileBytes
     * @param waterMarkFileBytes
     * @param fastdfsConfiguration
     * @return java.lang.String
     * @author 邓艺
     * @date 2019/1/28 16:33
     */
    public String doUploadSlaveFile(Socket storageSocket, String masterFileName, String suffixName, String extName, byte[] fileBytes, String fileName,
            byte[] waterMarkFileBytes, FastdfsConfiguration fastdfsConfiguration) {
        //是否需要上传缩略图
        if (fastdfsConfiguration.getOpenThumbnail()) {
            //判断文件是否是图片
            if (ThumbnailUtil.isPicture(fileName)) {
                //带水印的缩略图文件字节数组
                byte[] thumbnailBytes = null;
                switch (fastdfsConfiguration.getThumbnailStrategy()) {
                case 0:
                    //判断是否要添加水印
                    thumbnailBytes = ThumbnailUtil.getThumbnail(fileBytes, waterMarkFileBytes, fastdfsConfiguration);
                    break;
                case 1:
                    thumbnailBytes = ThumbnailUtil.getThumbnailBasedOnWidth(fileBytes, waterMarkFileBytes, fastdfsConfiguration);
                    break;
                case 2:
                    thumbnailBytes = ThumbnailUtil.getThumbnailBasedOnHeight(fileBytes, waterMarkFileBytes, fastdfsConfiguration);
                    break;
                default:
                    log.error("缩略图策略配置错误");
                }
                //TODO 上传缩略图
                if (ArrayUtils.isNotEmpty(thumbnailBytes)) {
                    //主文件名数组
                    byte[] masterFileNameBytes = masterFileName.getBytes(StandardCharsets.UTF_8);
                    byte[] masterFileNameLengthByteArray = ProtocolUtil.long2ByteArray((long) masterFileName.length());
                    byte[] bodyDataHeader = new byte[16];
                    //封装主文件名长度
                    System.arraycopy(masterFileNameLengthByteArray, 0, bodyDataHeader, 0, 8);
                    byte[] fileLengthByteArray = ProtocolUtil.long2ByteArray((long) thumbnailBytes.length);
                    System.arraycopy(fileLengthByteArray, 0, bodyDataHeader, 8, 8);
                    //标准后缀名字节数组
                    byte[] standardExtNameByteArry = new byte[6];
                    byte[] realNameByteArray = FileNameUtil.getExtNameWithDot(fileName).substring(1).getBytes(StandardCharsets.UTF_8);
                    System.arraycopy(realNameByteArray, 0, standardExtNameByteArry, 0, realNameByteArray.length);
                    //标准从文件后缀名
                    byte[] standardSuffixName = new byte[16];
                    //如果没有传入扩展名就直接使用比例
                    //TODO 后缀名中不能包含星号
                    if (StringUtils.isBlank(suffixName)) {
                        byte[] defaultSuffixNameBytes = ("_" + fastdfsConfiguration.getThumbnailWidth() + "X" + fastdfsConfiguration.getThumbnailHeight())
                                .getBytes(StandardCharsets.UTF_8);
                        System.arraycopy(defaultSuffixNameBytes, 0, standardSuffixName, 0, defaultSuffixNameBytes.length);

                    } else {
                        byte[] suffixNameBytes = suffixName.getBytes(StandardCharsets.UTF_8);
                        System.arraycopy(suffixNameBytes, 0, standardSuffixName, 0, suffixNameBytes.length);
                    }

                    byte[] protoHeader = ProtocolUtil.getProtoHeader(ControlCode.UPLOAD_SLAVE.getValue(),
                            (long) bodyDataHeader.length + standardSuffixName.length + standardExtNameByteArry.length + masterFileNameBytes.length
                                    + thumbnailBytes.length, SystemStatus.SUCCESS.getValue());

                    //封装数据
                    byte[] wholeMessage = new byte[protoHeader.length + bodyDataHeader.length + standardSuffixName.length + standardExtNameByteArry.length
                            + masterFileNameBytes.length];
                    System.arraycopy(protoHeader, 0, wholeMessage, 0, 10);
                    System.arraycopy(bodyDataHeader, 0, wholeMessage, 10, 16);
                    System.arraycopy(standardSuffixName, 0, wholeMessage, 26, 16);
                    System.arraycopy(standardExtNameByteArry, 0, wholeMessage, 42, 6);
                    System.arraycopy(masterFileNameBytes, 0, wholeMessage, 48, masterFileNameBytes.length);

                    try {
                        storageSocket.getOutputStream().write(wholeMessage);
                        storageSocket.getOutputStream().write(thumbnailBytes);

                        ReceiveData responseData = ProtocolUtil.getResponseData(storageSocket.getInputStream(), (byte) 100, (long) -1);
                        String groupName = new String(responseData.getBody(), 0, 16).trim();
                        String remoteFilename = new String(responseData.getBody(), 16, responseData.getBody().length - 16);
                        return groupName + "/" + remoteFilename;
                    } catch (IOException e) {
                        log.error(e.getMessage());
                    }

                }

            }
        }
        return null;
    }

    /**
     * 获取metadata
     *
     * @param groupName 组名
     * @param remoteFileName 远程文件名
     * @return List metadata集合
     * @author 邓艺
     * @date 2019/1/27 14:59
     */
    public List<String> doGetMetadata(@NotNull String groupName, @NotNull String remoteFileName, FastdfsConfiguration fastdfsConfiguration) {
        BasicStorageInfo updateStorage = getUpdateStorage(groupName, remoteFileName, fastdfsConfiguration);
        byte[] standardGroupNameBytes = new byte[CommonLength.GROUP_NAME_MAX_LENGTH.getLength()];
        byte[] groupNameBytes = groupName.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(groupNameBytes, 0, standardGroupNameBytes, 0, groupNameBytes.length);
        byte[] remoteFileNameBytes = remoteFileName.getBytes(StandardCharsets.UTF_8);
        byte[] protoHeader = ProtocolUtil
                .getProtoHeader(ControlCode.GET_METADATA.getValue(), (long) standardGroupNameBytes.length + remoteFileNameBytes.length, (byte) 0);

        byte[] wholeMessage = new byte[protoHeader.length + standardGroupNameBytes.length + remoteFileNameBytes.length];
        System.arraycopy(protoHeader, 0, wholeMessage, 0, 10);
        System.arraycopy(standardGroupNameBytes, 0, wholeMessage, 10, 16);
        System.arraycopy(remoteFileNameBytes, 0, wholeMessage, 26, remoteFileNameBytes.length);
        try {
            Socket socket = new Socket(updateStorage.getIp(), Math.toIntExact(updateStorage.getPort()));
            socket.getOutputStream().write(wholeMessage);
            //获取返回值
            ReceiveData responseData = ProtocolUtil.getResponseData(socket.getInputStream(), ControlCode.SERVER_RESPONSE.getValue(), (long) -1);

        } catch (IOException e) {
            log.error("获取metadata时异常:" + e.getMessage());
        }
        return null;
    }

    /**
     * 获取文件上传目的地服务器
     *
     * @param
     * @param fastdfsConfiguration
     * @return pro.dengyi.fastdfs.entity.BasicStorageInfo
     * @author 邓艺
     * @date 2019/1/22 10:56
     */
    public BasicStorageInfo getUploadStorage(FastdfsConfiguration fastdfsConfiguration) throws IOException {
        Socket trackerSocket = TrackerUtil.getTrackerSocket(fastdfsConfiguration.getTrackers());
        //查询上传时不需要封装数据
        byte[] protoHeader = ProtocolUtil.getProtoHeader(ControlCode.TRACKER_QUERY_UPLOAD_STORAGE.getValue(), (long) 0, SystemStatus.SUCCESS.getValue());
        assert trackerSocket != null;
        trackerSocket.getOutputStream().write(protoHeader);
        ReceiveData responseData = ProtocolUtil.getResponseData(trackerSocket.getInputStream(), (byte) 100, (long) 40);
        //TODO 关流时异常处理
        trackerSocket.close();
        return ResponseDataUtil.putDataInToBasicStorageInfo(responseData.getBody(), 0, false);
    }

    /**
     * 通过tracker查询从文件上传storage
     *
     * @param fastdfsConfiguration 配置对象
     * @param groupName 组名
     * @param masterFileName 主文件名
     * @return pro.dengyi.fastdfs.entity.BasicStorageInfo
     * @author 邓艺
     * @date 2019/1/25 22:57
     */
    public BasicStorageInfo getUploadSlaveFileStorage(FastdfsConfiguration fastdfsConfiguration, @NotNull String groupName, @NotNull String masterFileName)
            throws IOException {
        Socket trackerSocket = TrackerUtil.getTrackerSocket(fastdfsConfiguration.getTrackers());
        byte[] standardGroupNameByteArray = new byte[CommonLength.MAX_GROUPNAME_LENGTH.getLength()];
        byte[] groupNameBytes = groupName.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(groupNameBytes, 0, standardGroupNameByteArray, 0, groupNameBytes.length);
        byte[] masterFileNameBytes = masterFileName.getBytes(StandardCharsets.UTF_8);
        byte[] protoHeader = ProtocolUtil
                .getProtoHeader(ControlCode.TRACKER_QUERY_UPDATE_STORAGE.getValue(), (long) (standardGroupNameByteArray.length + masterFileNameBytes.length),
                        SystemStatus.SUCCESS.getValue());
        //完整报文组装
        byte[] wholeMessage = new byte[26 + masterFileNameBytes.length];
        System.arraycopy(protoHeader, 0, wholeMessage, 0, 10);
        System.arraycopy(standardGroupNameByteArray, 0, wholeMessage, 10, 16);
        System.arraycopy(masterFileNameBytes, 0, wholeMessage, 26, masterFileNameBytes.length);
        trackerSocket.getOutputStream().write(wholeMessage);
        ReceiveData responseData = ProtocolUtil.getResponseData(trackerSocket.getInputStream(), ControlCode.SERVER_RESPONSE.getValue(), (long) -1);
        trackerSocket.close();
        //39 =组名16+ip15+长度8
        int serverCount = responseData.getBody().length / 39;
        Random random = new Random();
        int r = random.nextInt(serverCount);
        return ResponseDataUtil.putDataInToBasicStorageInfo(responseData.getBody(), r * 39, false);
    }

    /**
     * 获取下载时storage服务器
     *
     * @param groupName 组名
     * @param remoteFileName 远程文件名
     * @param fastdfsConfiguration 配置对象
     * @return pro.dengyi.fastdfs.entity.BasicStorageInfo 当失败时返回null
     * @author 邓艺
     * @date 2019/1/24 9:50
     */
    public List<BasicStorageInfo> getDownloadStorage(String groupName, String remoteFileName, FastdfsConfiguration fastdfsConfiguration) {
        //组装报文
        byte[] standardGroupNameByteArray = new byte[CommonLength.MAX_GROUPNAME_LENGTH.getLength()];
        byte[] groupNameBytes = groupName.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(groupNameBytes, 0, standardGroupNameByteArray, 0, groupNameBytes.length);
        byte[] remoteFileNameBytes = remoteFileName.getBytes(StandardCharsets.UTF_8);
        byte[] protoHeader = ProtocolUtil
                .getProtoHeader(ControlCode.TRACKER_QUERY_DOWNLOAD_STORAGE.getValue(), (long) standardGroupNameByteArray.length + remoteFileNameBytes.length,
                        SystemStatus.SUCCESS.getValue());
        //3. 生成完整数据包
        byte[] wholeMessage = new byte[standardGroupNameByteArray.length + remoteFileNameBytes.length + protoHeader.length];
        //头
        System.arraycopy(protoHeader, 0, wholeMessage, 0, protoHeader.length);
        //组名
        System.arraycopy(standardGroupNameByteArray, 0, wholeMessage, protoHeader.length, standardGroupNameByteArray.length);
        //文件名
        System.arraycopy(remoteFileNameBytes, 0, wholeMessage, protoHeader.length + standardGroupNameByteArray.length, remoteFileNameBytes.length);
        //获取tracker连接
        try {
            Socket trackerSocket = TrackerUtil.getTrackerSocket(fastdfsConfiguration.getTrackers());
            assert trackerSocket != null;
            trackerSocket.getOutputStream().write(wholeMessage);
            ReceiveData responseData = ProtocolUtil.getResponseData(trackerSocket.getInputStream(), ControlCode.SERVER_RESPONSE.getValue(), (long) -1);
            //TODO 处理关闭时异常
            //将返回的storage信息封装
            trackerSocket.close();
            int number = responseData.getBody().length / 39;
            List<BasicStorageInfo> basicStorageInfos = new ArrayList<>(number);
            for (int i = 0; i < number; i++) {
                basicStorageInfos.add(ResponseDataUtil.putDataInToBasicStorageInfo(responseData.getBody(), 0, false));
            }
            return basicStorageInfos;
        } catch (IOException e) {
            log.error("获取下载storage时socket异常");
            return null;
        }

    }

    /**
     * 下载文件
     * <br/>
     * 如果下载失败，返回null
     *
     * @param fileUrl 文件名
     * @param offSet 偏移量
     * @param downloadBytes 下载文件字节数
     * @param fastdfsConfiguration 配置对象
     * @return byte[] 字节数组
     * @author 邓艺
     * @date 2019/1/24 10:47
     */
    public byte[] doDownloadFile(String fileUrl, Long offSet, Long downloadBytes, FastdfsConfiguration fastdfsConfiguration) {
        //获取下载文件storage信息
        String[] groupNameAndRemoteFileName = FileNameUtil.getGroupNameAndRemoteFileName(fileUrl);
        List<BasicStorageInfo> downloadStorageInfos = getDownloadStorage(groupNameAndRemoteFileName[0], groupNameAndRemoteFileName[1], fastdfsConfiguration);
        //发送下载报文
        byte[] offSetByteArray = ProtocolUtil.long2ByteArray(offSet);
        byte[] byteArray = ProtocolUtil.long2ByteArray(downloadBytes);
        byte[] standardGroupNameByteArray = new byte[CommonLength.GROUP_NAME_MAX_LENGTH.getLength()];
        byte[] groupNameBytes = groupNameAndRemoteFileName[0].getBytes(StandardCharsets.UTF_8);
        System.arraycopy(groupNameBytes, 0, standardGroupNameByteArray, 0, groupNameBytes.length);
        byte[] remoteFileNameBytes = groupNameAndRemoteFileName[1].getBytes(StandardCharsets.UTF_8);
        //下载报文头
        byte[] protoHeader = ProtocolUtil.getProtoHeader(ControlCode.DOWNLOAD.getValue(),
                (long) offSetByteArray.length + byteArray.length + standardGroupNameByteArray.length + remoteFileNameBytes.length, (byte) 0);
        byte[] wholeMessage = new byte[protoHeader.length + offSetByteArray.length + byteArray.length + standardGroupNameByteArray.length
                + remoteFileNameBytes.length];
        //报文组装
        System.arraycopy(protoHeader, 0, wholeMessage, 0, protoHeader.length);
        System.arraycopy(offSetByteArray, 0, wholeMessage, protoHeader.length, offSetByteArray.length);
        System.arraycopy(byteArray, 0, wholeMessage, protoHeader.length + offSetByteArray.length, byteArray.length);
        System.arraycopy(standardGroupNameByteArray, 0, wholeMessage, protoHeader.length + offSetByteArray.length + byteArray.length,
                standardGroupNameByteArray.length);
        System.arraycopy(remoteFileNameBytes, 0, wholeMessage,
                protoHeader.length + offSetByteArray.length + byteArray.length + standardGroupNameByteArray.length, remoteFileNameBytes.length);
        //将数据写出
        try {
            Socket downloadStorage = new Socket(downloadStorageInfos.get(0).getIp(), Math.toIntExact(downloadStorageInfos.get(0).getPort()));
            downloadStorage.getOutputStream().write(wholeMessage);
            //接受数据
            ReceiveData responseData = ProtocolUtil.getResponseData(downloadStorage.getInputStream(), ControlCode.SERVER_RESPONSE.getValue(), (long) -1);
            downloadStorage.close();
            return responseData.getBody();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 通过tracker获取可更新或删除文件的storage
     * <br>
     * 在删除文件及上传metadata时通过此方法获取storage
     *
     * @param groupName 组名
     * @param remoteFileName 远程文件名
     * @return BasicStorageInfo storage基础信息封装类
     * @author 邓艺
     * @date 2019/1/22 13:40
     */
    private BasicStorageInfo getUpdateStorage(String groupName, String remoteFileName, FastdfsConfiguration fastdfsConfiguration) {
        //不判断groupname的长度，因为是文件服务器返回的
        //1. 封装组名byte进标准16位长度数组
        byte[] standardGroupNameByteArray = new byte[CommonLength.MAX_GROUPNAME_LENGTH.getLength()];
        byte[] groupNameBytes = groupName.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(groupNameBytes, 0, standardGroupNameByteArray, 0, groupNameBytes.length);
        byte[] remoteFileNameByteArray = remoteFileName.getBytes(StandardCharsets.UTF_8);
        //2. 生成报文头
        byte[] protoHeader = ProtocolUtil.getProtoHeader(ControlCode.TRACKER_QUERY_UPDATE_STORAGE.getValue(),
                (long) (CommonLength.MAX_GROUPNAME_LENGTH.getLength() + remoteFileNameByteArray.length), SystemStatus.SUCCESS.getValue());
        //3. 生成完整数据包
        byte[] wholeMessage = new byte[CommonLength.MAX_GROUPNAME_LENGTH.getLength() + remoteFileNameByteArray.length
                + CommonLength.STANDARD_PROTOCOL_HEAD_LENGTH.getLength()];
        //头
        System.arraycopy(protoHeader, 0, wholeMessage, 0, CommonLength.STANDARD_PROTOCOL_HEAD_LENGTH.getLength());
        //组名
        System.arraycopy(standardGroupNameByteArray, 0, wholeMessage, CommonLength.STANDARD_PROTOCOL_HEAD_LENGTH.getLength(),
                CommonLength.MAX_GROUPNAME_LENGTH.getLength());
        //文件名
        System.arraycopy(remoteFileNameByteArray, 0, wholeMessage,
                CommonLength.STANDARD_PROTOCOL_HEAD_LENGTH.getLength() + CommonLength.MAX_GROUPNAME_LENGTH.getLength(), remoteFileNameByteArray.length);
        //4. 获取trackersocket连接并写出
        Socket trackerSocket = TrackerUtil.getTrackerSocket(fastdfsConfiguration.getTrackers());
        try {
            trackerSocket.getOutputStream().write(wholeMessage);
            //5. 获取响应数据
            ReceiveData responseData = ProtocolUtil.getResponseData(trackerSocket.getInputStream(), (byte) 100, (long) -1);
            trackerSocket.close();
            return ResponseDataUtil.putDataInToBasicStorageInfo(responseData.getBody(), 0, false);

        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return null;
    }

    /**
     * 删除文件
     *
     * @param groupName 组名
     * @param remoteFileName 远程文件名
     * @return java.lang.Boolean
     * @author 邓艺
     * @date 2019/1/22 13:02
     */
    public Boolean doDeleteFile(String groupName, String remoteFileName, FastdfsConfiguration fastdfsConfiguration) {
        //通过tracker获取
        BasicStorageInfo updateStorage = getUpdateStorage(groupName, remoteFileName, fastdfsConfiguration);
        //获取storage socket然后进行删除
        try {
            Socket storageSocket = new Socket(updateStorage.getIp(), Math.toIntExact(updateStorage.getPort()));
            //1. 报文
            byte[] standardGroupNameByteArray = new byte[CommonLength.MAX_GROUPNAME_LENGTH.getLength()];
            byte[] groupNameBytes = groupName.getBytes(StandardCharsets.UTF_8);
            System.arraycopy(groupNameBytes, 0, standardGroupNameByteArray, 0, groupNameBytes.length);
            byte[] standardRemoteFileNameByteArray = remoteFileName.getBytes(StandardCharsets.UTF_8);
            //2.报文头生成
            byte[] protoHeader = ProtocolUtil.getProtoHeader(ControlCode.DELETE.getValue(),
                    (long) (CommonLength.MAX_GROUPNAME_LENGTH.getLength() + standardRemoteFileNameByteArray.length), SystemStatus.SUCCESS.getValue());
            //3. 生成完整数据包
            byte[] wholeMessage = new byte[CommonLength.MAX_GROUPNAME_LENGTH.getLength() + standardRemoteFileNameByteArray.length
                    + CommonLength.STANDARD_PROTOCOL_HEAD_LENGTH.getLength()];
            //头
            System.arraycopy(protoHeader, 0, wholeMessage, 0, CommonLength.STANDARD_PROTOCOL_HEAD_LENGTH.getLength());
            //组名
            System.arraycopy(standardGroupNameByteArray, 0, wholeMessage, CommonLength.STANDARD_PROTOCOL_HEAD_LENGTH.getLength(),
                    CommonLength.MAX_GROUPNAME_LENGTH.getLength());
            //文件名
            System.arraycopy(standardRemoteFileNameByteArray, 0, wholeMessage,
                    CommonLength.STANDARD_PROTOCOL_HEAD_LENGTH.getLength() + CommonLength.MAX_GROUPNAME_LENGTH.getLength(),
                    standardRemoteFileNameByteArray.length);
            //4. 将数据写出
            storageSocket.getOutputStream().write(wholeMessage);
            //5. 读取返回数据
            ReceiveData responseData = ProtocolUtil.getResponseData(storageSocket.getInputStream(), (byte) 100, (long) 0);
            storageSocket.close();
            if (responseData.getErrorNo() == 0) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            log.error("创建storage时异常");
        }
        return false;
    }

}
