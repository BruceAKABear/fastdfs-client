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
     * 状态
     */
    private byte status;
    private String id;
    private String ipAddr;
    private String srcIpAddr;
    private String domainName; //http domain name
    /**
     * 软件版本
     */
    private String version;
    /**
     * 存储服务器容量
     */
    private long totalMB;
    /**
     * 剩余容量
     */
    private long freeMB;
    /**
     * 上传优先
     */
    private long uploadPriority;
    /**
     * 加入存储组时间
     */
    private Date joinTime;
    /**
     * 开始时间
     */
    private Date upTime;
    private Long storePathCount;  //store base path count of each storage server
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
    private Long connectionCurrentCount;
    private int connectionMaxCount;
    private long totalUploadCount;
    private long successUploadCount;
    private long totalAppendCount;
    private long successAppendCount;
    private long totalModifyCount;
    private long successModifyCount;
    private long totalTruncateCount;
    private long successTruncateCount;
    private long totalSetMetaCount;
    private long successSetMetaCount;
    private long totalDeleteCount;
    private long successDeleteCount;
    private long totalDownloadCount;
    private long successDownloadCount;
    private long totalGetMetaCount;
    private long successGetMetaCount;
    private long totalCreateLinkCount;
    private long successCreateLinkCount;
    private long totalDeleteLinkCount;
    private long successDeleteLinkCount;
    private long totalUploadBytes;
    private long successUploadBytes;
    private long totalAppendBytes;
    private long successAppendBytes;
    private long totalModifyBytes;
    private long successModifyBytes;
    private long totalDownloadloadBytes;
    private long successDownloadloadBytes;
    private long totalSyncInBytes;
    private long successSyncInBytes;
    private long totalSyncOutBytes;
    private long successSyncOutBytes;
    private long totalFileOpenCount;
    private long successFileOpenCount;
    private long totalFileReadCount;
    private long successFileReadCount;
    private long totalFileWriteCount;
    private long successFileWriteCount;
    private Date lastSourceUpdate;
    private Date lastSyncUpdate;
    private Date lastSyncedTimestamp;
    private Date lastHeartBeatTime;
    private boolean ifTrunkServer;

}
