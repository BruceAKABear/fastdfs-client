package pro.dengyi.fastdfs.threads;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 输出流类
 *
 * @author 邓艺
 * @version v1.0
 * @date 2019-01-09 15:30
 */
public class OutputStreamThread {
    /**
     * 将数据放入输出流中
     *
     * @param out 输出流
     * @return void
     * @author 邓艺
     * @date 2019/1/9 15:34
     */
    public static void putDataOnOutputStream(OutputStream out) throws IOException {
        byte[] bs = new byte[2];
        for (int i = 0; i <= 100; i++) {
            bs[0] = (byte) i;
            bs[1] = (byte) (i + 1);
            // 测试写入字节数组
            out.write(bs);
            out.flush();
            try {
                // 等待0.1秒
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
