package pro.dengyi.test;

import org.junit.Test;
import pro.dengyi.fastdfs.connection.Connection;

import java.io.IOException;
import java.net.Socket;
import java.util.Random;

/**
 * @author 邓艺
 * @version v1.0
 * @date 2019-01-14 20:44
 */
public class UseTest {

    /**
     * 上传文件
     */
    @Test
    public void demo1() {
        //1. 设置参数
        //2. 利用连接工厂获取连接
        //3. 进行crud操作
        //4. 关闭资源

    }

    /**
     * 删除文件
     *
     * @throws IOException
     */
    @Test
    public void demo2() throws IOException {
        //直接测试删除
        Socket socket = new Socket("116.62.195.66", 23000);
        Connection.sendPackage((byte) 12, "group1", "M00/00/00/rBCXFVw8rXuAEAAuAAB3uY337jM068.jpg", socket);
        socket.close();

    }

     @Test
     public void demo3(){
         Random r = new Random();
         for (int i = 0; i < 30; i++) {
             int ii = r.nextInt(5);
             System.out.println(ii);
         }

     }


}
