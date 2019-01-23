package pro.dengyi.fastdfs.config;

import lombok.Data;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 客户端配置类
 *
 * @author 邓艺
 * @version v1.0
 * @date 2018-12-25 15:41
 */
@Data
public class FastdfsConfiguration {
    /**
     * 网络连接超时时间,单位为毫秒
     */
    private Long networkTimeOut = 30000L;
    /**
     * tracker集合
     */
    private String[] trackers;
    /**
     * tracker连接超时时间，单位为毫秒
     */
    private Long connectTimeout = 5000L;
    /**
     * 编码ISO8859-1 UTF-8
     */
    private Charset charset = StandardCharsets.UTF_8;
    /**
     * 是否开启放到链功能，默认为false
     */
    private Boolean openAntiSteal = false;
    /**
     * 防盗链秘钥
     */
    private String secretKey = "FastDFS1234567890";

    /**
     * 是否开启缩略图
     */
    private Boolean openThumbnail = false;
    /**
     * 缩略图透明度0-1之间
     */
    private Float transparency = 0f;
    /**
     * 水印文件位置一共9种
     */
    private String thumbnailPosition;

    /**
     * tracker心跳检测时间频率，默认30s一次，如果tracker服务器性能越强时间可以设置的越大
     */
    private Integer trackerHeartBeatCheckTime = 30;
    /**
     * 文件访问形式，默认http访问，如果是https访问修改为https://
     */
    private String accessHead = "http://";

}
