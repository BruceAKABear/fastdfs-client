package pro.dengyi.fastdfs.core;

import com.sun.istack.internal.NotNull;
import org.apache.commons.lang3.StringUtils;
import pro.dengyi.fastdfs.entity.BasicStorageInfo;
import pro.dengyi.fastdfs.entity.ReceiveData;
import pro.dengyi.fastdfs.entity.StorageInfo;
import pro.dengyi.fastdfs.entity.StoragerEntity;
import pro.dengyi.fastdfs.exception.FastdfsException;
import pro.dengyi.fastdfs.utils.ProtocolUtil;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * tracker核心类
 *
 * @author 邓艺
 * @version v1.0
 * @date 2018-12-25 16:15
 */
public class Tracker {

    /**
     * 获取所有存储服务器ip和端口
     *
     * @param socket tracker的socket
     * @param groupName 组名 可以为空
     * @return pro.dengyi.fastdfs.core.Storage[]
     * @author 邓艺
     * @date 2019/1/15 14:43
     */
    public static Set<StoragerEntity> getStorages(@NotNull Socket socket, String groupName) throws IOException {
        //定义控制字
        byte controlCode;
        Long writeLength = null;
        //groupName为空,查询出所有的存储服务器
        if (StringUtils.isNotBlank(groupName)) {
            controlCode = (byte) 107;
            writeLength = 16L;
        } else {
            controlCode = (byte) 106;
            writeLength = 0L;
        }
        //获取报文头部
        byte[] protoHeader = ProtocolUtil.getProtoHeader(controlCode, writeLength, (byte) 0);
        socket.getOutputStream().write(protoHeader);
        //如果组名不为空，将组名写出去
        if (StringUtils.isNotBlank(groupName)) {
            byte[] groupNameBytes = new byte[16];
            byte[] originalBytes = groupName.getBytes(StandardCharsets.UTF_8);
            if (originalBytes.length <= 16) {
                System.arraycopy(originalBytes, 0, groupNameBytes, 0, originalBytes.length);
            } else {
                System.arraycopy(originalBytes, 0, groupNameBytes, 0, 16);
            }
            //将组名写出
            socket.getOutputStream().write(groupNameBytes);
        }
        //3.接受服务器响应的数据,控制字为100
        ReceiveData responseData = ProtocolUtil.getResponseData(socket.getInputStream(), (byte) 100, (long) -1);
        //4.服务器状态校验

        //5.获取存储服务器集合
        Set<StoragerEntity> storagerEntities = new TreeSet<>();
        //存储服务器数量
        int storagerNumber = 10;
        for (int i = 0; i < storagerNumber; i++) {
            storagerEntities.add(new StoragerEntity("192.168.0.188", 23000, (byte) 1));
        }

        return storagerEntities;
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
