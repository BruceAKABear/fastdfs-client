package pro.dengyi.fastdfs.entity;

import lombok.Data;

/**
 * 存储服务器基础信息服务类
 *
 * @author 邓艺
 * @version v1.0
 * @date 2019-01-18 13:32
 */
@Data
public class BasicStorageInfo {

    /**
     * 组名
     */
    private String groupName;

    /**
     * ip
     */
    private String ip;
    /**
     * 端口
     */
    private Long port;
    /**
     * 存储序列,默认值为0
     */
    private Byte storePath = (byte) 0;

}
