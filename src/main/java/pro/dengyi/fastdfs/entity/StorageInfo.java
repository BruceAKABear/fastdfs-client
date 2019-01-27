package pro.dengyi.fastdfs.entity;

import lombok.Data;

import java.util.Date;

/**
 * 存储服务器信息实体类
 *
 * @author 邓艺
 * @version v1.0
 * @date 2019-01-18 09:38
 */
@Data
public class StorageInfo {

    /**
     * 存储服务器状态
     * 1 初始化，尚未得到同步已有数据的源服务器
     * 2 等待同步，已得到同步已有数据的源服务器
     * 3 同步中
     * 4 已删除，该服务器从本组中摘除
     * 5 离线
     * 6 在线，尚不能提供服务
     * 7 在线，可以提供服务
     */
    private byte status;
    /**
     * 存储服务器ip
     */
    private String id;
    /**
     * 存储服务器ip
     */
    private String ipAddr;
    private String srcIpAddr;
    /**
     * http名
     */
    private String domainName;
    /**
     * 软件版本
     */
    private String version;
    /**
     * 存储服务器容量
     */
    private Long totalMB;
    /**
     * 剩余容量
     */
    private Long freeMB;
    /**
     * 上传优先及 10
     */
    private Long uploadPriority;
    /**
     * 加入存储组时间
     */
    private Date joinTime;
    /**
     * 更新时间
     */
    private Date upTime;
    //store base path count of each storage server
    private Long storePathCount;
    private Long subdirCountPerPath;
    /**
     * 存储服务器端口
     */
    private Long storagePort;
    /**
     * 存储服务器http访问端口
     */
    private Long storageHttpPort;
    /**
     * 当前科协路径
     */
    private Long currentWritePath;
    private int connectionAllocCount;
    private int connectionCurrentCount;
    /**
     *
     */
    private int connectionMaxCount;
    /**
     * 上传文件次数
     */
    private Long totalUploadCount;
    /**
     * 成功上传文件次数
     */
    private Long successUploadCount;
    private Long totalAppendCount;
    private Long successAppendCount;
    private Long totalModifyCount;
    private Long successModifyCount;
    private Long totalTruncateCount;
    private Long successTruncateCount;
    /**
     * 设置metadata次数
     */
    private Long totalSetMetaCount;
    /**
     * 成功设置metadata次数
     */
    private Long successSetMetaCount;
    /**
     * 删除文件次数
     */
    private Long totalDeleteCount;
    /**
     * 成功删除文件次数
     */
    private Long successDeleteCount;
    private Long totalDownloadCount;
    private Long successDownloadCount;
    private Long totalGetMetaCount;
    private Long successGetMetaCount;
    private Long totalCreateLinkCount;
    private Long successCreateLinkCount;
    private Long totalDeleteLinkCount;
    private Long successDeleteLinkCount;
    private Long totalUploadBytes;
    private Long successUploadBytes;
    private Long totalAppendBytes;
    private Long successAppendBytes;
    private Long totalModifyBytes;
    private Long successModifyBytes;
    /**
     * 全部下载的字节数量
     */
    private Long totalDownloadloadBytes;
    /**
     * 全部成功下载字节数量
     */
    private Long successDownloadloadBytes;
    /**
     *
     */
    private Long totalSyncInBytes;
    private Long successSyncInBytes;
    private Long totalSyncOutBytes;
    private Long successSyncOutBytes;
    private Long totalFileOpenCount;
    private Long successFileOpenCount;
    private Long totalFileReadCount;
    private Long successFileReadCount;
    /**
     *
     */
    private Long totalFileWriteCount;
    private Long successFileWriteCount;
    private Date lastSourceUpdate;
    /**
     *
     */
    private Date lastSyncUpdate;
    /**
     * 上一次同步时间
     */
    private Date lastSyncedTimestamp;
    /**
     * 上次心跳时间
     */
    private Date lastHeartBeatTime;
    private boolean ifTrunkServer;

}
