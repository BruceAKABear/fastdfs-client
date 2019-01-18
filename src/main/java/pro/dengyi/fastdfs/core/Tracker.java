package pro.dengyi.fastdfs.core;

import com.sun.istack.internal.NotNull;
import org.apache.commons.lang3.StringUtils;
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
     * @param
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
     * 查询所有storage的信息
     *
     * @param groupName 组名 可以为空
     * @return pro.dengyi.fastdfs.core.Storage[]
     * @author 邓艺
     * @date 2019/1/15 14:43
     */
    public static List<StorageInfo> getAllStorageInfo(@NotNull String groupName) throws IOException {
        Socket socket = new Socket();
        byte[] protoHeader = ProtocolUtil.getProtoHeader(ControlCode.TRACKER_GET_ALL_STORAGEINFO.getValue(), 16L, SystemStatus.SUCCESS.getValue());
        byte[] wholePackeg = new byte[26];
        byte[] groupNameBytes = groupName.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(protoHeader, 0, wholePackeg, 0, 10);
        byte[] StandardgroupNameBytes = new byte[16];
        System.arraycopy(groupNameBytes, 0, StandardgroupNameBytes, 0, groupNameBytes.length);
        System.arraycopy(StandardgroupNameBytes, 0, wholePackeg, 10, 16);
        socket.getOutputStream().write(wholePackeg);
        //接受响应数据
        ReceiveData responseData = ProtocolUtil.getResponseData(socket.getInputStream(), (byte) 100, (long) -1);
        return ResponseDataUtil.getAllStorageInfo(responseData.getBody());
    }

    /**
     * 由tracker获取所有的storage
     *
     * @param socket socket连接
     * @param groupName 组名
     * @return List
     * @author 邓艺
     * @date 2019/1/16 21:17
     */
    public List<String> getAllStorages(Socket socket, String groupName) {
        //如果不能为空，则查询组下的所有存储服务器
        if (StringUtils.isNotBlank(groupName)) {

        } else {

        }
        return null;
    }

    /**
     * 获取组下所有storage的信息
     *
     * @param groupName 组名
     * @param storageIpAddr 存储服务器的IP地址
     * @return List  StorageInfo集合
     * @author 邓艺
     * @date 2019/1/18 12:38
     */
    public List<StorageInfo> getAllStorageInfo(@NotNull String groupName, String storageIpAddr) throws FastdfsException {
        if (StringUtils.isBlank(groupName)) {
            throw new FastdfsException("获取storage信息时，group名不能为空");
        }

        return null;
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
