package pro.dengyi.fastdfs.core;

import com.sun.istack.internal.NotNull;
import lombok.extern.slf4j.Slf4j;
import pro.dengyi.fastdfs.config.FastdfsConfiguration;
import pro.dengyi.fastdfs.constantenum.CommonLength;
import pro.dengyi.fastdfs.constantenum.ControlCode;
import pro.dengyi.fastdfs.constantenum.SystemStatus;
import pro.dengyi.fastdfs.entity.BasicStorageInfo;
import pro.dengyi.fastdfs.entity.ReceiveData;
import pro.dengyi.fastdfs.utils.ProtocolUtil;
import pro.dengyi.fastdfs.utils.ResponseDataUtil;
import pro.dengyi.fastdfs.utils.TrackerUtil;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

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
     * 删除存储服务器
     *
     * @param groupName 组名
     * @param storageIpAddr 存储服务器ip
     * @return boolean
     * @author 邓艺
     * @date 2019/1/20 21:38
     */
    public boolean deleteStorage(String groupName, String storageIpAddr) throws IOException {

        return false;
    }

    /**
     * 上传metadata
     *
     * @param groupName 文件组名
     * @param remoteFileName 远程文件名
     * @param metadata metadata数据
     * @param fastdfsConfiguration 配置
     * @return java.lang.Boolean true上传成功false上传失败
     * @author 邓艺
     * @date 2019/1/24 9:38
     */
    public Boolean uploadMetadeta(String groupName, String remoteFileName, List<Object> metadata, FastdfsConfiguration fastdfsConfiguration) {

        return false;
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
     * 获取下载时storage服务器
     *
     * @param groupName 组名
     * @param remoteFileName 远程文件名
     * @param fastdfsConfiguration 配置对象
     * @return pro.dengyi.fastdfs.entity.BasicStorageInfo 当失败时返回null
     * @author 邓艺
     * @date 2019/1/24 9:50
     */
    public BasicStorageInfo getDownloadStorage(String groupName, String remoteFileName, FastdfsConfiguration fastdfsConfiguration) {
        //组装报文
        byte[] standardGroupNameByteArray = new byte[CommonLength.MAX_GROUPNAME_LENGTH.getLength()];
        byte[] groupNameBytes = groupName.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(groupNameBytes, 0, standardGroupNameByteArray, 0, groupNameBytes.length);
        byte[] remoteFileNameBytes = remoteFileName.getBytes(StandardCharsets.UTF_8);
        byte[] protoHeader = ProtocolUtil.getProtoHeader(ControlCode.TRACKER_QUERY_DOWNLOAD_STORAGE.getValue(),
                (long) (CommonLength.MAX_GROUPNAME_LENGTH.getLength() + remoteFileNameBytes.length), SystemStatus.SUCCESS.getValue());
        //3. 生成完整数据包
        byte[] wholeMessage = new byte[CommonLength.MAX_GROUPNAME_LENGTH.getLength() + remoteFileNameBytes.length + CommonLength.STANDARD_PROTOCOL_HEAD_LENGTH
                .getLength()];
        //头
        System.arraycopy(protoHeader, 0, wholeMessage, 0, CommonLength.STANDARD_PROTOCOL_HEAD_LENGTH.getLength());
        //组名
        System.arraycopy(standardGroupNameByteArray, 0, wholeMessage, CommonLength.STANDARD_PROTOCOL_HEAD_LENGTH.getLength(),
                CommonLength.MAX_GROUPNAME_LENGTH.getLength());
        //文件名
        System.arraycopy(remoteFileNameBytes, 0, wholeMessage,
                CommonLength.STANDARD_PROTOCOL_HEAD_LENGTH.getLength() + CommonLength.MAX_GROUPNAME_LENGTH.getLength(), remoteFileNameBytes.length);
        //获取tracker连接
        try {
            Socket trackerSocket = TrackerUtil.getTrackerSocket(fastdfsConfiguration.getTrackers());
            assert trackerSocket != null;
            trackerSocket.getOutputStream().write(wholeMessage);
            ReceiveData responseData = ProtocolUtil.getResponseData(trackerSocket.getInputStream(), ControlCode.SERVER_RESPONSE.getValue(), (long) -1);
            //TODO 处理关闭时异常
            trackerSocket.close();
            return ResponseDataUtil.putDataInToBasicStorageInfo(responseData.getBody(), 0, false);
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
     * @param groupName 组名
     * @param remoteFileName 远程文件名
     * @param offSet 偏移量
     * @param downloadBytes 下载文件字节数
     * @return byte[] 字节数组
     * @author 邓艺
     * @date 2019/1/24 10:47
     */
    public byte[] doDownloadFile(@NotNull String groupName, @NotNull String remoteFileName, Long offSet, Long downloadBytes, Socket dowmloadStorageSocket) {
        byte[] offSetByteArray = ProtocolUtil.long2ByteArray(offSet);
        byte[] ByteArray = ProtocolUtil.long2ByteArray(downloadBytes);
        byte[] standardGroupNameByteArray = new byte[CommonLength.GROUP_NAME_MAX_LENGTH.getLength()];
        byte[] groupNameBytes = groupName.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(groupNameBytes, 0, standardGroupNameByteArray, 0, groupNameBytes.length);
        byte[] remoteFileNameBytes = remoteFileName.getBytes(StandardCharsets.UTF_8);
        //下载报文头
        byte[] protoHeader = ProtocolUtil.getProtoHeader(ControlCode.DOWNLOAD.getValue(),
                (long) offSetByteArray.length + ByteArray.length + CommonLength.GROUP_NAME_MAX_LENGTH.getLength() + remoteFileNameBytes.length, (byte) 0);
        byte[] wholeMessage = new byte[offSetByteArray.length + ByteArray.length + CommonLength.GROUP_NAME_MAX_LENGTH.getLength() + remoteFileNameBytes.length
                + CommonLength.STANDARD_PROTOCOL_HEAD_LENGTH.getLength()];
        //报文组装
        System.arraycopy(protoHeader, 0, wholeMessage, 0, protoHeader.length);
        System.arraycopy(offSetByteArray, 0, wholeMessage, protoHeader.length, offSetByteArray.length);
        System.arraycopy(ByteArray, 0, wholeMessage, protoHeader.length + offSetByteArray.length, ByteArray.length);
        System.arraycopy(standardGroupNameByteArray, 0, wholeMessage, protoHeader.length + offSetByteArray.length + ByteArray.length,
                standardGroupNameByteArray.length);
        System.arraycopy(remoteFileNameBytes, 0, wholeMessage,
                protoHeader.length + offSetByteArray.length + ByteArray.length + standardGroupNameByteArray.length, remoteFileNameBytes.length);
        //将数据写出
        try {
            dowmloadStorageSocket.getOutputStream().write(wholeMessage);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        //接受数据
        try {
            ReceiveData responseData = ProtocolUtil.getResponseData(dowmloadStorageSocket.getInputStream(), ControlCode.SERVER_RESPONSE.getValue(), (long) -1);
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
    public BasicStorageInfo getUpdateStorage(String groupName, String remoteFileName, FastdfsConfiguration fastdfsConfiguration) {
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
            return ResponseDataUtil.putDataInToBasicStorageInfo(responseData.getBody(), 0, false);

        } catch (IOException e) {
            e.printStackTrace();
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
    public Boolean deleteFile(String groupName, String remoteFileName, FastdfsConfiguration fastdfsConfiguration) {
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
