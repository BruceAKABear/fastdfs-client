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
     * 存储路径索引，基于0
     * 什么是存储路径？一台存储服务器可以设置多个存储路径，通过索引来区分上传到了什么路径
     */
    private Byte storePath = (byte) 0;

}
