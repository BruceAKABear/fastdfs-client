package pro.dengyi.fastdfs.entity;

import lombok.Data;

/**
 * 存储服务器组信息实体类
 *
 * @author 邓艺
 * @version v1.0
 * @date 2019-01-17 15:29
 */
@Data
public class StorageGroupInfo {
    /**
     * 组名
     */
    private String groupName;
    /**
     * 组所有容量
     */
    private Long totalMB;
    /**
     * 组内空闲容量
     */
    private Long freeMB;
    private Long trunkFreeMB;
    /**
     * 组内存储服务器台数
     */
    private Long storageCount;
    /**
     * 组内存储服务器端口
     */
    private Long storagePort;
    /**
     * 组内存储服务器http端口
     */
    private Long storageHttpPort;
    /**
     * 组内激活状态的存储服务器的数量
     */
    private Long activeCount;
    /**
     * 组内当前可写存储服务器
     */
    private Long currentWriteServer;
    /**
     * 路径
     */
    private Long storePathCount;
    /**
     * 子路径
     */
    private Long subdirCountPerPath;
    /**
     * 分支
     */
    private Long currentTrunkFileId;

}
