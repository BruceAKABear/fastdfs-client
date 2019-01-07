package pro.dengyi.fastdfs.config;

import lombok.Data;

/**
 * 客户端配置类
 *
 * @author 邓艺
 * @version v1.0
 * @date 2018-12-25 15:41
 */
@Data
public class ClientProterties {
    /**
     * tracker集合
     */
    private String trackerList;
    /**
     * 连接上tracker服务器的时间，默认为5s
     */
    private Integer connectTimeout = 5;
    /**
     * 网络超时时间
     */
    private Integer networkTimeout;
    /**
     * 编码ISO8859-1 UTF-8
     */
    private String charset;
    private Integer trackerPort;
    /**
     * 是否开启放到链功能，默认为false
     */
    private Boolean openAntiSteal;
    /**
     * 防盗链秘钥
     */
    private String secretKey;

}
