package pro.dengyi.fastdfs.connection;

import lombok.Data;
import org.apache.commons.lang3.ArrayUtils;
import pro.dengyi.fastdfs.exception.FastdfsException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 连接工厂
 *
 * @author 邓艺
 * @version v1.0
 * @date 2019-01-08 10:42
 */
@Data
public class ConnectionFactory {
    private static ConnectionFactory ourInstance = new ConnectionFactory();
    /**
     * 网络连接超时时间,单位为毫秒
     */
    private Long networkTimeOut = 30000L;
    /**
     * tracker集合
     */
    private static String[] trackers;
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
    private Float transparency = 1f;
    /**
     * 水印文件位置一共9种
     */
    private String thumbnailPosition;

    public static Connection getConnection() {
        if (ArrayUtils.isNotEmpty(trackers)) {
            for (String tracker : trackers) {


            }
        } else {
            throw new FastdfsException("tracker不能为空");
        }

        return null;
    }

    private ConnectionFactory() {
    }
}
