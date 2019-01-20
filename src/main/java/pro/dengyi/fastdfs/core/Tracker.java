package pro.dengyi.fastdfs.core;

import com.sun.istack.internal.NotNull;
import org.apache.commons.lang3.StringUtils;
import pro.dengyi.fastdfs.constantenum.CommonLength;
import pro.dengyi.fastdfs.constantenum.ControlCode;
import pro.dengyi.fastdfs.constantenum.SystemStatus;
import pro.dengyi.fastdfs.entity.*;
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

    //用于上传
    public BasicStorageInfo getUploadStorage() {
        return null;
    }

    //用于下载
    public BasicStorageInfo getDownloadStorage(String groupName, String fid) {
        return null;
    }

    //用于删除或者上传metadata
    public BasicStorageInfo getUpdateStorage() {
        return null;
    }

}
