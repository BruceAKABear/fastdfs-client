package pro.dengyi.fastdfs.utils;

import com.sun.istack.internal.NotNull;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 图片工具类，主要作用是将图片生成缩略图或者添加水印
 *
 * @author 邓艺
 * @version v1.0
 * @date 2019-01-09 12:58
 */
public class PictureUtil {

    /**
     * 获取强制比例缩略图
     *
     * @param inputStream 输入流
     * @param width 宽度
     * @param height 高度
     * @return java.io.InputStream 流
     * @author 邓艺
     * @date 2019/1/9 14:03
     */
    public static InputStream getThumbnail(@NotNull InputStream inputStream, @NotNull Integer width, @NotNull Integer height) throws IOException {
        OutputStream outputStream = new FileOutputStream("");
        Thumbnails.of(inputStream).size(width, height).toOutputStream(outputStream);
        //开启智能缩略图
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        int height1 = bufferedImage.getHeight();
        int width1 = bufferedImage.getWidth();
        System.out.println(width1 + "-------" + height1);
        return null;
    }

    /**
     * 获取宽度固定,高度按比例的智能缩略图
     *
     * @param inputStream 输入流
     * @param width 宽度
     * @return java.io.InputStream 流
     * @author 邓艺
     * @date 2019/1/9 14:03
     */
    public static InputStream getThumbnailBasedOnWidth(@NotNull InputStream inputStream, @NotNull Integer width) throws IOException {

        //开启智能缩略图
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        int originalHeight = bufferedImage.getHeight();
        int originalWidth = bufferedImage.getWidth();
        int calculatedHeight = (width * originalHeight) / originalWidth;
        System.out.println(calculatedHeight + "---" + 200);
        return null;
    }

    /**
     * 获取高度固定的,宽度按比例自动的智能缩略图
     *
     * @param inputStream 输入流
     * @param height 高度
     * @return java.io.InputStream 流
     * @author 邓艺
     * @date 2019/1/9 14:03
     */
    public static InputStream getThumbnailBasedOnHeight(@NotNull InputStream inputStream, @NotNull Integer height) throws IOException {
        //开启智能缩略图
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        int originalHeight = bufferedImage.getHeight();
        int originalWidth = bufferedImage.getWidth();
        int calculatedWidth = (height * originalWidth) / originalHeight;
        return null;
    }

}
