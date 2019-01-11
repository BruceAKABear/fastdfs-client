package pro.dengyi.test;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.junit.Test;
import pro.dengyi.fastdfs.utils.PictureUtil;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * @author 邓艺
 * @version v1.0
 * @date 2019-01-09 13:06
 */
@Slf4j
public class PictureTest {

    @Test
    public void demo1() {
        int w = 200;
        int h = 200;
        boolean force = false;
        File imgFile = new File("C:\\解密文件\\1.jpg");
        if (imgFile.exists()) {
            try {
                // ImageIO 支持的图片类型 : [BMP, bmp, jpg, JPG, wbmp, jpeg, png, PNG, JPEG, WBMP, GIF, gif]
                String types = Arrays.toString(ImageIO.getReaderFormatNames());
                String suffix = null;
                // 获取图片后缀
                if (imgFile.getName().contains(".")) {
                    suffix = imgFile.getName().substring(imgFile.getName().lastIndexOf(".") + 1);
                }// 类型和图片后缀所有小写，然后推断后缀是否合法
                if (suffix == null || !types.toLowerCase().contains(suffix.toLowerCase())) {
                    log.error("Sorry, the image suffix is illegal. the standard image suffix is {}." + types);
                    return;
                }
                log.debug("target image's size, width:{}, height:{}.", w, h);
                Image img = ImageIO.read(imgFile);
                if (!force) {
                    // 依据原图与要求的缩略图比例，找到最合适的缩略图比例
                    int width = img.getWidth(null);
                    int height = img.getHeight(null);
                    if ((width * 1.0) / w < (height * 1.0) / h) {
                        if (width > w) {
                            h = Integer.parseInt(new java.text.DecimalFormat("0").format(height * w / (width * 1.0)));
                            log.debug("change image's height, width:{}, height:{}.", w, h);
                        }
                    } else {
                        if (height > h) {
                            w = Integer.parseInt(new java.text.DecimalFormat("0").format(width * h / (height * 1.0)));
                            log.debug("change image's width, width:{}, height:{}.", w, h);
                        }
                    }
                }
                BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                Graphics g = bi.getGraphics();
                g.drawImage(img, 0, 0, w, h, Color.LIGHT_GRAY, null);
                g.dispose();
                String p = imgFile.getPath();
                ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(bi);
                ImageIO.write(bi, suffix, new File(p.substring(0, p.lastIndexOf(File.separator)) + File.separator + "demo" + imgFile.getName()));
                log.debug("缩略图在原路径下生成成功");
            } catch (IOException e) {
                log.error("generate thumbnail image failed.", e);
            }
        } else {
            log.warn("the image is not exist.");
        }
    }

    @Test
    public void demo2() throws IOException {
        File file = new File("c://解密文件//1.jpg");
        FileInputStream fileInputStream = new FileInputStream(file);
        PipedInputStream pipedInputStream = new PipedInputStream();
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        try {
            pipedInputStream.connect(pipedOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        PictureUtil.getThumbnail(fileInputStream, 200, 200);
    }

    @Test
    public void demo3() {
        float f = 1;
        //w*h
        //200 100
        //    30
        System.out.println(f);
        String format = new DecimalFormat("0").format(600 * 300 * 1.0);
        System.err.println(format);
    }

    @Test
    public void demo4() throws IOException {
        File file = new File("c://解密文件//1.jpg");
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStream thumbnailBasedOnHeight = PictureUtil.getThumbnailBasedOnWidth(fileInputStream, 200);

    }

    @Test
    public void demo5() throws IOException {
        //缩略图
        File file = new File("c://解密文件//1.jpg");

        Thumbnails.of(file).height(200).toFile(new File("c://解密文件//11.jpg"));

    }

}