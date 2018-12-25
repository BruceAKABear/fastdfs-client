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
    private Integer connectTimeout;
    private Integer networkTimeout;
    private String charset;
    private Integer trackerPort;
    private Boolean openAntiSteal;
    private String secretKey;

}
