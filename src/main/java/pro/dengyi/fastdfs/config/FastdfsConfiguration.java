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
     * 文件上传网络连接超时时间,单位为毫秒(默认超时时间为30秒)
     */
    private Integer networkTimeOut = 30000;
    /**
     * tracker集合
     */
    private String[] trackers;
    /**
     * tracker连接超时时间，单位为毫秒
     */
    private Integer connectTimeout = 5000;
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
     * 缩略图生成策略，0严格按照给定尺寸生成，1宽度优先，2高度优先
     */
    private Integer thumbnailStrategy = 0;
    /**
     * 缩略图宽度
     */
    private Integer thumbnailWidth = 100;
    /**
     * 缩略图高度
     */
    private Integer thumbnailHeight = 100;
    /**
     * 是否开启水印
     */
    private Boolean openWaterMark = false;
    /**
     * 缩略图透明度0-1之间
     */
    private Float waterMarktransparency = 0f;

    /**
     * 水印文件位置一共9种
     * TOP_LEFT:左上      TOP_CENTER:上中     TOP_RIGHT:右上
     * CENTER_LEFT:左中   CENTER:中中         CENTER_RIGHT:右中
     * BOTTOM_LEFT:左下   BOTTOM_CENTER:下中  BOTTOM_RIGHT:右下
     */
    private String waterMarkPosition = "BOTTOM_RIGHT";
    /**
     * 文件访问形式，默认http访问，如果是https访问修改为https://
     */
    private String accessHead = "http://";
    /**
     * 文件访问端口
     */
    private Integer accessPort = 8080;

}
