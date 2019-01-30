package pro.dengyi.fastdfs.core;

import com.sun.istack.internal.NotNull;
import lombok.extern.slf4j.Slf4j;
import pro.dengyi.fastdfs.config.FastdfsConfiguration;
import pro.dengyi.fastdfs.constantenum.ControlCode;
import pro.dengyi.fastdfs.constantenum.SystemStatus;
import pro.dengyi.fastdfs.entity.ReceiveData;
import pro.dengyi.fastdfs.entity.StorageGroupInfo;
import pro.dengyi.fastdfs.entity.StorageInfo;
import pro.dengyi.fastdfs.utils.FileNameUtil;
import pro.dengyi.fastdfs.utils.ProtocolUtil;
import pro.dengyi.fastdfs.utils.ResponseDataUtil;
import pro.dengyi.fastdfs.utils.TrackerUtil;

import java.io.IOException;
import java.net.Socket;
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
    public List<StorageInfo> getAllStorageInfo(@NotNull String groupName) {
        try {
            return doGetAllStorageInfo(groupName, null, fastdfsConfiguration);
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
            return doGetAllStorageInfo(groupName, storageIpAddr, fastdfsConfiguration).get(0);
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }

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
     * 以字节数组的形式上传文件
     *
     * @param fileBytes 文件字节数组
     * @return string[] 文件访问地址数组 [0]原图访问地址 [1]缩略图访问地址
     * @author 邓艺
     * @date 2019/1/22 15:39
     */
    public String[] uploadFile(byte[] fileBytes, String fileName) {
        return doUploadFile(fileBytes, null, fileName, null, fastdfsConfiguration);

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
    public String[] uploadFile(byte[] fileBytes, String fileName, List<String> metadata) {
        return doUploadFile(fileBytes, null, fileName, metadata, fastdfsConfiguration);
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
