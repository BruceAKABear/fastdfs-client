package pro.dengyi.fastdfs.utils;

import com.sun.istack.internal.NotNull;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import pro.dengyi.fastdfs.config.FastdfsConfiguration;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 缩略图工具类
 *
 * @author 邓艺
 * @version v1.0
 * @date 2019-01-09 12:58
 */
@Slf4j
public class ThumbnailUtil {
    //TODO 存在内存占用过大的情况，具体解决方法需要仔细考虑

    /**
     * 获取强制比例缩略图
     *
     * @param fileBytes 文件字节数组
     * @return 缩略图文件字节数组
     * @author 邓艺
     * @date 2019/1/9 14:03
     */
    public static byte[] getThumbnail(@NotNull byte[] fileBytes, byte[] waterMarkFileBytes, @NotNull FastdfsConfiguration fastdfsConfiguration) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            InputStream inputStream = new ByteArrayInputStream(fileBytes);
            if (ArrayUtils.isNotEmpty(waterMarkFileBytes)) {
                //添加水印
                Thumbnails.of(inputStream).size(fastdfsConfiguration.getThumbnailWidth(), fastdfsConfiguration.getThumbnailHeight())
                        .watermark(EnumUtils.getEnum(Positions.class, fastdfsConfiguration.getWaterMarkPosition()),
                                ImageIO.read(new ByteArrayInputStream(waterMarkFileBytes)), fastdfsConfiguration.getWaterMarktransparency())
                        .toOutputStream(outputStream);
            } else {
                //不添加水印
                Thumbnails.of(inputStream).size(fastdfsConfiguration.getThumbnailWidth(), fastdfsConfiguration.getThumbnailHeight())
                        .toOutputStream(outputStream);
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            log.error("生成缩略图时异常" + e.getMessage());
            return null;
        }
    }

    /**
     * 获取宽度固定,高度按比例的智能缩略图
     *
     * @param fileBytes 源文件字节数组
     * @param waterMarkFileBytes 水印文件字节数组
     * @param fastdfsConfiguration 配置对象
     * @return 缩略图byte数组
     * @author 邓艺
     * @date 2019/1/9 14:03
     */
    public static byte[] getThumbnailBasedOnWidth(@NotNull byte[] fileBytes, byte[] waterMarkFileBytes, @NotNull FastdfsConfiguration fastdfsConfiguration) {

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            InputStream inputStream = new ByteArrayInputStream(fileBytes);
            if (ArrayUtils.isNotEmpty(waterMarkFileBytes)) {
                //添加水印
                Thumbnails.of(inputStream).width(fastdfsConfiguration.getThumbnailWidth())
                        .watermark(EnumUtils.getEnum(Positions.class, fastdfsConfiguration.getWaterMarkPosition()),
                                ImageIO.read(new ByteArrayInputStream(waterMarkFileBytes)), fastdfsConfiguration.getWaterMarktransparency())
                        .toOutputStream(outputStream);
            } else {
                //不添加水印
                Thumbnails.of(inputStream).width(fastdfsConfiguration.getThumbnailWidth()).toOutputStream(outputStream);
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            log.error("生成缩略图时异常" + e.getMessage());
            return null;
        }

    }

    /**
     * 获取高度固定的,宽度按比例自动的智能缩略图
     *
     * @param fileBytes 文件字节数组
     * @param waterMarkFileBytes 水印文件字节数组
     * @return 缩略图文件字节数组
     * @author 邓艺
     * @date 2019/1/9 14:03
     */
    public static byte[] getThumbnailBasedOnHeight(@NotNull byte[] fileBytes, byte[] waterMarkFileBytes, @NotNull FastdfsConfiguration fastdfsConfiguration) {

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            InputStream inputStream = new ByteArrayInputStream(fileBytes);
            if (ArrayUtils.isNotEmpty(waterMarkFileBytes)) {
                //添加水印
                Thumbnails.of(inputStream).height(fastdfsConfiguration.getThumbnailHeight())
                        .watermark(EnumUtils.getEnum(Positions.class, fastdfsConfiguration.getWaterMarkPosition()),
                                ImageIO.read(new ByteArrayInputStream(waterMarkFileBytes)), fastdfsConfiguration.getWaterMarktransparency())
                        .toOutputStream(outputStream);
            } else {
                //不添加水印
                Thumbnails.of(inputStream).height(fastdfsConfiguration.getThumbnailHeight()).toOutputStream(outputStream);
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            log.error("生成缩略图时异常" + e.getMessage());
            return null;
        }
    }

    /**
     * 根据文件名判断文件是不是图片
     *
     * @param fileName 文件名
     * @return boolean 返回true时代表是图片
     * @author 邓艺
     * @date 2019/1/28 16:36
     */
    public static boolean isPicture(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return false;
        }
        // 获得文件后缀名
        String extName = fileName.substring(fileName.lastIndexOf(".") + 1);
        // 声明图片后缀名数组
        String imgeArray[] = {"bmp", "dib", "gif", "jfif", "jpe", "jpeg", "jpg", "png", "tif", "tiff", "ico"};
        // 遍历名称数组
        for (String singleExt : imgeArray) {
            if (singleExt.equals(extName)) {
                return true;
            }
        }
        return false;
    }

}
