package pro.dengyi.fastdfs.entity;

import lombok.Data;

/**
 * 存储服务器实体（主要封装存储服务器ip,端口,和路径索引）
 *
 * @author 邓艺
 * @version v1.0
 * @date 2019-01-15 16:22
 */
@Data
public class StoragerEntity {

    /**
     * ip
     */
    private String ipAddress;
    /**
     * 端口
     */
    private long port;
    /**
     * 存储索引
     */
    private byte storagePath;

    public StoragerEntity(String ipAddress, long port, byte storagePath) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.storagePath = storagePath;
    }
}
