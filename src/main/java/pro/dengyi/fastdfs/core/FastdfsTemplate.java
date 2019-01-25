package pro.dengyi.fastdfs.core;

import com.sun.istack.internal.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import pro.dengyi.fastdfs.config.FastdfsConfiguration;
import pro.dengyi.fastdfs.constantenum.CommonLength;
import pro.dengyi.fastdfs.constantenum.ControlCode;
import pro.dengyi.fastdfs.constantenum.SystemStatus;
import pro.dengyi.fastdfs.entity.BasicStorageInfo;
import pro.dengyi.fastdfs.entity.ReceiveData;
import pro.dengyi.fastdfs.entity.StorageGroupInfo;
import pro.dengyi.fastdfs.entity.StorageInfo;
import pro.dengyi.fastdfs.exception.FastdfsException;
import pro.dengyi.fastdfs.threads.UploadMetadataThread;
import pro.dengyi.fastdfs.utils.FileNameUtil;
import pro.dengyi.fastdfs.utils.ProtocolUtil;
import pro.dengyi.fastdfs.utils.ResponseDataUtil;
import pro.dengyi.fastdfs.utils.TrackerUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * fastdfs操作模板类
 *
 * @author 邓艺
 * @version v1.0
 * @date 2019-01-20 17:29
 */
@Slf4j
public class FastdfsTemplate extends FastdfsCore {
    private FastdfsConfiguration fastdfsConfiguration;

    /**
     * 构造器
     *
     * @param fastdfsConfiguration 文件系统配置对象
     */
    public FastdfsTemplate(FastdfsConfiguration fastdfsConfiguration) {
        this.fastdfsConfiguration = fastdfsConfiguration;
    }

    /**
     * 获取所有存储组的信息
     *
     * @return List 存储对象集合（如果异常将返回null）
     * @author 邓艺
     * @date 2019/1/23 21:46
     */
    public List<StorageGroupInfo> getAllStorageGroupInfo() {
        Socket trackerSocket = TrackerUtil.getTrackerSocket(fastdfsConfiguration.getTrackers());
        byte[] protoHeader = ProtocolUtil.getProtoHeader(ControlCode.TRACKER_GET_ALL_GROUPINFO.getValue(), 0L, SystemStatus.SUCCESS.getValue());
        assert trackerSocket != null;
        try {
            trackerSocket.getOutputStream().write(protoHeader);
        } catch (IOException e) {
            log.error("向tracker写出数据时异常");
        }
        //-1 代表读取所有数据
        ReceiveData responseData = null;
        try {
            responseData = ProtocolUtil.getResponseData(trackerSocket.getInputStream(), ControlCode.SERVER_RESPONSE.getValue(), (long) -1);
            try {
                trackerSocket.close();
            } catch (IOException e) {
                log.error("关闭tracker时异常");
                trackerSocket = null;
            }
        } catch (IOException e) {
            log.error("从tracker获取输入流时异常");
        }
        if (responseData != null) {
            return ResponseDataUtil.getAllStorageGroupInfo(responseData.getBody());
        } else {
            return null;
        }
    }

    /**
     * 查询存储组下所有storage的信息
     *
     * @param groupName 组名
     * @return pro.dengyi.fastdfs.core.Storage[]
     * @author 邓艺
     * @date 2019/1/15 14:43
     */
    public List<StorageInfo> getAllStorageInfo(@NotNull String groupName) {
        try {
            return getAllStorageInfo(groupName, null);
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
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
    public List<StorageInfo> getAllStorageInfo(@NotNull String groupName, String storageIpAddr) throws FastdfsException, IOException {
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
            //TODO 处理关流时异常
            trackerSocket.close();
            return ResponseDataUtil.getAllStorageInfo(responseData.getBody());
        }
    }

    /**
     * 以字节数组的形式上传文件
     *
     * @param fileBytes 文件字节数组
     * @return java.lang.String 文件访问地址
     * @author 邓艺
     * @date 2019/1/22 15:39
     */
    public String uploadFile(byte[] fileBytes, String fileName) {
        return fastdfsConfiguration.getAccessHead() + doUploadFile(fileBytes, fileName, null);
    }

    /**
     * 以字节数组的形式上传文件,带metadata
     *
     * @param fileBytes 文件字节数组
     * @param metadata metadata
     * @return java.lang.String
     * @author 邓艺
     * @date 2019/1/22 15:53
     */
    public String uploadFile(byte[] fileBytes, String fileName, List<Object> metadata) {
        return fastdfsConfiguration.getAccessHead() + doUploadFile(fileBytes, fileName, metadata);
    }

    /**
     * 执行上传从文件方法
     * 思考:从文件正常情况都是缩略图，在上传缩略图从何谈起上传元数据呢？所以上传从文件时不考虑伤上传元数据！
     *
     * @param fileBytes 文件字节数组
     * @param masterFileName 父文件名
     * @param fileNameSuffix 子文件名后缀
     * @return java.lang.String
     * @author 邓艺
     * @date 2019/1/22 16:02
     */
    public String doUploadSlaveFile(@NotNull byte[] fileBytes, String groupName, String masterFileName, String fileNameSuffix) {
        try {
            //获取主从文件storage
            BasicStorageInfo uploadMaterAndSlaveFileStorage = getUploadMaterAndSlaveFileStorage(fastdfsConfiguration, groupName, masterFileName);
            Socket storageSocket = new Socket(uploadMaterAndSlaveFileStorage.getIp(), Math.toIntExact(uploadMaterAndSlaveFileStorage.getPort()));

        } catch (IOException e) {
            log.error("创建storagesocket时异常");
        }

        return null;
    }

    /**
     * 执行上传文件
     *
     * @param fileBytes 文件字节数组
     * @param fileName 文件名
     * @param metadata 元数据
     * @return java.lang.String
     * @author 邓艺
     * @date 2019/1/25 22:34
     */
    private String doUploadFile(@NotNull byte[] fileBytes, String fileName, List<Object> metadata) {
        String groupName = null;
        String remoteFilename = null;
        BasicStorageInfo uploadStorage = null;
        try {
            //获取上传文件目的地storage
            uploadStorage = getUploadStorage(fastdfsConfiguration);
            Socket storageSocket = new Socket(uploadStorage.getIp(), Math.toIntExact(uploadStorage.getPort()));
            //上传普通文件
            //1. 第一位为存储路径，后8位长度
            byte[] bodyDataHeader = new byte[1 + 8];
            //TODO 存储index
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
            //如果需要上传metadata启动上传metadata线程
            if (CollectionUtils.isNotEmpty(metadata)) {
                new Thread(new UploadMetadataThread(groupName, remoteFilename, metadata, storageSocket)).start();
            } else {
                storageSocket.close();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return uploadStorage.getIp() + ":" + fastdfsConfiguration.getAccessPort() + "/" + groupName + "/" + remoteFilename;
    }

    public String uploadAppenderFile() {
        return null;
    }

    public Boolean uploadMetadata() {
        return false;
    }

    /**
     * 删除文件
     *
     * @param fileUrl 文件url(如：http://xxx:xx/xxxxx.xx)
     * @return java.lang.Boolean true表示删除成功，false表示删除失败
     * @author 邓艺
     * @date 2019/1/22 11:21
     */
    public Boolean deleteFile(String fileUrl) {
        String[] groupNameAndRemoteFileName = FileNameUtil.getGroupNameAndRemoteFileName(fileUrl);
        return deleteFile(groupNameAndRemoteFileName[0], groupNameAndRemoteFileName[1], fastdfsConfiguration);
    }

    /**
     * 以字节数组的形式下载文件
     *
     * @param fileUrl 文件url
     * @return byte[] 文件字节数组
     * @author 邓艺
     * @date 2019/1/24 11:19
     */
    public byte[] downloadFile(String fileUrl) {
        String[] groupNameAndRemoteFileName = FileNameUtil.getGroupNameAndRemoteFileName(fileUrl);
        BasicStorageInfo downloadStorage = getDownloadStorage(groupNameAndRemoteFileName[0], groupNameAndRemoteFileName[1], fastdfsConfiguration);
        try {
            Socket dowmloadStorageSocket = new Socket(downloadStorage.getIp(), Math.toIntExact(downloadStorage.getPort()));
            return doDownloadFile(groupNameAndRemoteFileName[0], groupNameAndRemoteFileName[1], (long) 0, (long) 0, dowmloadStorageSocket);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

}
