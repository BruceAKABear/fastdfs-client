package pro.dengyi.fastdfs.core;

import com.sun.istack.internal.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import pro.dengyi.fastdfs.config.FastdfsConfiguration;
import pro.dengyi.fastdfs.constantenum.CommonLength;
import pro.dengyi.fastdfs.constantenum.ControlCode;
import pro.dengyi.fastdfs.constantenum.SystemStatus;
import pro.dengyi.fastdfs.entity.ReceiveData;
import pro.dengyi.fastdfs.entity.StorageGroupInfo;
import pro.dengyi.fastdfs.entity.StorageInfo;
import pro.dengyi.fastdfs.exception.FastdfsException;
import pro.dengyi.fastdfs.utils.FileNameUtil;
import pro.dengyi.fastdfs.utils.ProtocolUtil;
import pro.dengyi.fastdfs.utils.ResponseDataUtil;
import pro.dengyi.fastdfs.utils.TrackerUtil;

import java.io.IOException;
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
     * @return pro.dengyi.fastdfs.core.Storage[] 如果失败则返回null
     * @author 邓艺
     * @date 2019/1/15 14:43
     */
    public List<StorageInfo> doGetAllStorageInfo(@NotNull String groupName) {
        try {
            return doGetAllStorageInfo(groupName, null);
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * 获取特定存储服务器的信息
     *
     * @param groupName 组名
     * @param storageIpAddr 服务器地址
     * @return pro.dengyi.fastdfs.entity.StorageInfo 如果失败则返回null
     * @author 邓艺
     * @date 2019/1/27 22:28
     */
    public StorageInfo getStorageInfo(String groupName, String storageIpAddr) {
        try {
            return doGetAllStorageInfo(groupName, storageIpAddr).get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将存储服务器从集群中删除
     *
     * @param groupName 组名
     * @param storageIpAddr 文件服务器ip
     * @return java.lang.Boolean
     * @author 邓艺
     * @date 2019/1/27 15:45
     */
    public Boolean deleteSotrage(String groupName, String storageIpAddr) {
        return doDeleteStorage(fastdfsConfiguration, groupName, storageIpAddr);
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
    public List<StorageInfo> doGetAllStorageInfo(@NotNull String groupName, String storageIpAddr) throws FastdfsException, IOException {
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
        return fastdfsConfiguration.getAccessHead() + doUploadFile(fileBytes, null, fileName, null, fastdfsConfiguration);
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
    public String uploadFile(byte[] fileBytes, String fileName, List<String> metadata) {
        return fastdfsConfiguration.getAccessHead() + doUploadFile(fileBytes, null, fileName, metadata, fastdfsConfiguration);
    }

    /**
     * 上传断点续传文件
     *
     * @param fileBytes 文件数组
     * @param fileName 文件名
     * @return java.lang.String
     * @author 邓艺
     * @date 2019/1/27 15:49
     */
    public String uploadAppenderFile(byte[] fileBytes, String fileName) {
        return null;
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
        return doDeleteFile(groupNameAndRemoteFileName[0], groupNameAndRemoteFileName[1], fastdfsConfiguration);
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
        return doDownloadFile(fileUrl, (long) 0, (long) 0, fastdfsConfiguration);

    }

}
