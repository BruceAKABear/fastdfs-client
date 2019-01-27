package pro.dengyi.test;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.junit.Test;
import pro.dengyi.fastdfs.utils.PictureUtil;

import javax.imageio.ImageIO;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * @author 邓艺
 * @version v1.0
 * @date 2019-01-09 13:06
 */
@Slf4j
public class PictureTest {
    
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
    public void demo5() throws IOException, ImageReadException {
        //缩略图
        File file = new File("C:\\Users\\dengyi\\Desktop\\IMG_20170521_200051.jpg");
        IImageMetadata metadata = Sanselan.getMetadata(file);

        ArrayList items = metadata.getItems();
        for (Object item : metadata.getItems()) {
            System.out.println(item);
        }
    }

    @Test
    public void demo6() throws IOException {
        File file = new File("C:\\Users\\dengyi\\Desktop\\IMG_20170521_200051.jpg");
        FileInputStream fileInputStream = new FileInputStream(file);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Thumbnails.of(fileInputStream).height(400).toOutputStream(byteArrayOutputStream);
        FileOutputStream fileOutputStream = new FileOutputStream(new File("C:\\Users\\dengyi\\Desktop\\111.jpg"));
        fileOutputStream.write(byteArrayOutputStream.toByteArray());
        fileOutputStream.flush();

    }

    @Test
    public void demo7() throws IOException {
        File file = new File("C:\\Users\\dengyi\\Desktop\\IMG_20170521_200051.jpg");
        InputStream thumbnailBasedOnHeight = PictureUtil.getThumbnailBasedOnHeight(new FileInputStream(file), 200);
        Thumbnails.of(thumbnailBasedOnHeight).height(100).toFile("C:\\Users\\dengyi\\Desktop\\222.jpg");

    }

    @Test
    public void demo8() throws IOException, ImageReadException {
        //缩略图
        File file = new File("C:\\Users\\dengyi\\Desktop\\IMG_20170521_200051.jpg");
        Thumbnails.of(file).size(1944, 2592).watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File("C:\\Users\\dengyi\\Desktop\\logo.jpg")), 0.5f)
                .toFile(new File("C:\\Users\\dengyi\\Desktop\\1111.jpg"));

    }

}
