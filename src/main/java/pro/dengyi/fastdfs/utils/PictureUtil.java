package pro.dengyi.fastdfs.utils;

import com.sun.istack.internal.NotNull;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import net.coobird.thumbnailator.Thumbnails;

/**
 * 图片工具类，主要作用是将图片生成缩略图或者添加水印
 *
 * @author 邓艺
 * @version v1.0
 * @date 2019-01-09 12:58
 */
public class PictureUtil {
  //TODO 存在内存占用过大的情况，具体解决方法需要仔细考虑

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
  public static InputStream getThumbnail(@NotNull InputStream inputStream, @NotNull Integer width,
      @NotNull Integer height) throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    Thumbnails.of(inputStream).size(width, height).toOutputStream(outputStream);
    return new ByteArrayInputStream(outputStream.toByteArray());
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
  public static InputStream getThumbnailBasedOnWidth(@NotNull InputStream inputStream,
      @NotNull Integer width) throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    Thumbnails.of(inputStream).width(width).toOutputStream(outputStream);
    return new ByteArrayInputStream(outputStream.toByteArray());
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
  public static InputStream getThumbnailBasedOnHeight(@NotNull InputStream inputStream,
      @NotNull Integer height) throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    Thumbnails.of(inputStream).height(height).toOutputStream(outputStream);
    return new ByteArrayInputStream(outputStream.toByteArray());
  }

}
