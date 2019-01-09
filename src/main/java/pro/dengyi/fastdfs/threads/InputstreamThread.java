package pro.dengyi.fastdfs.threads;

import java.io.IOException;
import java.io.InputStream;

/**
 * 输入流类
 *
 * @author 邓艺
 * @version v1.0
 * @date 2019-01-09 15:30
 */
public class InputstreamThread {
    public static void processDataFromInputStream(InputStream in) {
        byte[] bs = new byte[1024];
        int len;
        // 读取数据，并进行处理
        try {
            while ((len = in.read(bs)) != -1) {
                for (int i = 0; i < len; i++) {
                    System.out.println(bs[i]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
