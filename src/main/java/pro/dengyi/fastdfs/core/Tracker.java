package pro.dengyi.fastdfs.core;

import com.sun.istack.internal.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import pro.dengyi.fastdfs.constantenum.CommonLength;
import pro.dengyi.fastdfs.constantenum.ControlCode;
import pro.dengyi.fastdfs.constantenum.SystemStatus;
import pro.dengyi.fastdfs.entity.BasicStorageInfo;
import pro.dengyi.fastdfs.entity.ReceiveData;
import pro.dengyi.fastdfs.entity.StorageGroupInfo;
import pro.dengyi.fastdfs.entity.StorageInfo;
import pro.dengyi.fastdfs.exception.FastdfsException;
import pro.dengyi.fastdfs.utils.ProtocolUtil;
import pro.dengyi.fastdfs.utils.ResponseDataUtil;

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
public class Tracker {

    /**
     * 获取所有存储组的信息
     *
     * @return List 存储组信息集合
     * @author 邓艺
     * @date 2019/1/18 20:50
     */
    public static List<StorageGroupInfo> getAllStorageGroupInfo() throws IOException {
        byte[] protoHeader = ProtocolUtil.getProtoHeader(ControlCode.TRACKER_GET_ALL_GROUPINFO.getValue(), 0L, SystemStatus.SUCCESS.getValue());
        Socket socket = new Socket();
        socket.getOutputStream().write(protoHeader);
        //-1 代表读取所有数据
        ReceiveData responseData = ProtocolUtil.getResponseData(socket.getInputStream(), (byte) 100, (long) -1);
        return ResponseDataUtil.getAllStorageGroupInfo(responseData.getBody());
    }

    /**
     * 查询存储组下所有storage的信息
     *
     * @param groupName 组名
     * @return pro.dengyi.fastdfs.core.Storage[]
     * @author 邓艺
     * @date 2019/1/15 14:43
     */
    public static List<StorageInfo> getAllStorageInfo(@NotNull String groupName) throws IOException {
        return getAllStorageInfo(groupName, null);
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
    public static List<StorageInfo> getAllStorageInfo(@NotNull String groupName, String storageIpAddr) throws FastdfsException, IOException {
        if (StringUtils.isBlank(groupName)) {
            throw new FastdfsException("获取storage信息时，groupName不能为空");
        } else {
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
            //获取tracker服务器socket通道
            Socket socket = new Socket();
            socket.getOutputStream().write(wholeMessage);
            ReceiveData responseData = ProtocolUtil.getResponseData(socket.getInputStream(), ControlCode.TRACKER_RESPONSE.getValue(), (long) -1);
            return ResponseDataUtil.getAllStorageInfo(responseData.getBody());
        }
    }

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
     * 获取文件上传目的地服务器
     *
     * @param
     * @return pro.dengyi.fastdfs.entity.BasicStorageInfo
     * @author 邓艺
     * @date 2019/1/22 10:56
     */
    public BasicStorageInfo getUploadStorage() throws IOException {
        //查询上传时不需要封装数据
        byte[] protoHeader = ProtocolUtil.getProtoHeader(ControlCode.TRACKER_QUERY_UPLOAD_STORAGE.getValue(), (long) 0, SystemStatus.SUCCESS.getValue());
        Socket socket = new Socket();
        socket.getOutputStream().write(protoHeader);
        ReceiveData responseData = ProtocolUtil.getResponseData(socket.getInputStream(), (byte) 100, (long) 40);
        return ResponseDataUtil.putDataInToBasicStorageInfo(responseData.getBody(), 0, false);
    }

    //用于下载
    public BasicStorageInfo getDownloadStorage(String groupName, String fid) {
        return null;
    }

    /**
     * 通过tracker获取可更新或删除文件的storage
     * <br>
     * 在删除文件及上传metadata时通过此方法获取storage
     *
     * @param groupName 组名
     * @param remoteFileName 远程文件名
     * @return pro.dengyi.fastdfs.entity.BasicStorageInfo storage基础信息封装类
     * @author 邓艺
     * @date 2019/1/22 13:40
     */
    public BasicStorageInfo getUpdateStorage(String groupName, String remoteFileName) {
        Socket trackerSocket = new Socket();
        //不判断groupname的长度，因为是文件服务器返回的
        //1. 封装组名byte进标准16位长度数组
        byte[] standardGroupNameByteArray = new byte[CommonLength.MAX_GROUPNAME_LENGTH.getLength()];
        byte[] groupNameBytes = groupName.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(groupNameBytes, 0, standardGroupNameByteArray, 0, groupNameBytes.length);
        byte[] standardRemoteFileNameByteArray = remoteFileName.getBytes(StandardCharsets.UTF_8);
        //2. 生成报文头
        byte[] protoHeader = ProtocolUtil.getProtoHeader(ControlCode.TRACKER_QUERY_UPDATE_STORAGE.getValue(),
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
                CommonLength.STANDARD_PROTOCOL_HEAD_LENGTH.getLength() + CommonLength.MAX_GROUPNAME_LENGTH.getLength(), standardRemoteFileNameByteArray.length);
        //4. 获取trackersocket连接并洗出
        Socket socketTracker = new Socket();
        try {
            socketTracker.getOutputStream().write(wholeMessage);
            //5. 获取响应数据
            ReceiveData responseData = ProtocolUtil.getResponseData(socketTracker.getInputStream(), (byte) 100, (long) -1);
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
    public Boolean deleteFile(String groupName, String remoteFileName) {
        //通过tracker获取
        BasicStorageInfo updateStorage = getUpdateStorage(groupName, remoteFileName);
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
