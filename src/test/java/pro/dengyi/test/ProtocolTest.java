package pro.dengyi.test;

import org.junit.Test;
import pro.dengyi.fastdfs.connection.ConnectionFactory;
import pro.dengyi.fastdfs.constantenum.EncodingType;
import pro.dengyi.fastdfs.constantenum.SystemCode;
import pro.dengyi.fastdfs.utils.FileNameUtil;
import pro.dengyi.fastdfs.utils.ProtocolUtil;

import javax.swing.plaf.TreeUI;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;

/**
 * 协议测试类
 *
 * @author 邓艺
 * @version v1.0
 * @date 2018-12-26 14:42
 */
public class ProtocolTest {

    @Test
    public void demo1() {
        long n = 10;
        byte[] bs;
        bs = new byte[8];
        bs[0] = (byte) ((n >> 56) & 0xFF);
        bs[1] = (byte) ((n >> 48) & 0xFF);
        bs[2] = (byte) ((n >> 40) & 0xFF);
        bs[3] = (byte) ((n >> 32) & 0xFF);
        bs[4] = (byte) ((n >> 24) & 0xFF);
        bs[5] = (byte) ((n >> 16) & 0xFF);
        bs[6] = (byte) ((n >> 8) & 0xFF);
        bs[7] = (byte) (n & 0xFF);

        for (byte b : bs) {

            System.out.println(b);
        }
    }

    @Test
    public void demo2() {
        byte[] b = new byte[]{0, 0, 0, 0, 0, 0, 0, 10};
        byte[] header = new byte[10];
        System.arraycopy(b, 0, header, 0, 8);
        for (byte b1 : header) {
            System.out.println(b1);
        }
    }

    @Test
    public void demo3() throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update("hello".getBytes());
        byte[] digest = messageDigest.digest();
        for (byte b : digest) {
            System.err.println(b);
        }
    }

    @Test
    public void demo4() {
        LinkedList<String> pool = new LinkedList<>();
        pool.add("aaaaaaaaa");
        pool.add("bbbbbbbbb");
        pool.add("ccccccccc");

        String pop = pool.pop();
        System.err.println(pop);

        for (String s : pool) {
            System.out.println(s);
        }
    }

    @Test
    public void demo5() {
        EncodingType utf8 = EncodingType.UTF8;
        System.out.println(utf8.getValue());

    }

    @Test
    public void demo6() {

        byte[] b = new byte[16];
        Byte bb = new Byte("1");
        String groupName = "group1";
        byte[] bytes = groupName.getBytes();
        System.arraycopy(bytes, 0, b, 0, bytes.length);
        for (byte b1 : b) {
            System.err.print(b1);

        }
    }

    @Test
    public void demo7() {
        Integer n = 10;
        byte[] bs;
        bs = new byte[8];
        bs[0] = (byte) ((n >> 56) & 0xFF);
        bs[1] = (byte) ((n >> 48) & 0xFF);
        bs[2] = (byte) ((n >> 40) & 0xFF);
        bs[3] = (byte) ((n >> 32) & 0xFF);
        bs[4] = (byte) ((n >> 24) & 0xFF);
        bs[5] = (byte) ((n >> 16) & 0xFF);
        bs[6] = (byte) ((n >> 8) & 0xFF);
        bs[7] = (byte) (n & 0xFF);

        for (byte b : bs) {
            System.err.println(b);
        }
    }

    @Test
    public void demo8() {
        String s = FileNameUtil.generateSlaveFilename("202CB962AC59075B964B07152D234B70.jpg", "dddd", "gif");
        System.err.println(s);
    }

    @Test
    public void demo9() {
        String extNameWithDot = FileNameUtil.getExtNameWithDot("1234.jpg");
        System.out.println(extNameWithDot);
    }

    @Test
    public void demo10() {
        String fileName = "202CB962AC59075B964B07152D234B70.jpg";
        String dddd = FileNameUtil.generateSlaveFilename(fileName, "dddd", "jpg");

        System.out.println(dddd);

    }

    @Test
    public void demo11() {
        String fileNae = "..jpg";
        int i = fileNae.indexOf(".");
        int i1 = fileNae.lastIndexOf(".");

        System.out.println("起始索引" + i);
        System.out.println("终止索引" + i1);
    }

    @Test
    public void demo12() throws FileNotFoundException {
        File file = new File("C:\\解密文件\\1.jpg");
        System.out.println(SystemCode.STORAGE_PROTO_CMD_DELETE_FILE.getValue());

    }

    @Test
    public void demo13() throws IOException {
        Socket socket = new Socket("192.168.0.177", 22122);
    }

    @Test
    public void demo14() {
        Charset utf8 = StandardCharsets.UTF_8;
        System.out.println(utf8);
    }

    @Test
    public void demo15() {
        //        ConnectionFactory connection = ConnectionFactory.getConnection();
        //        System.out.println(connection.getConnectTimeout());
    }

    @Test
    public void demo16() {
        String s = "\u0004";
        System.out.println(s);
    }

    @Test
    public void demo17() {
        String s = "http://192.168.0.188:8888/group1/xxx";
        System.out.println(s.indexOf("/"));
        System.out.println(s.indexOf("/", 8));
        System.out.println(s.substring(s.indexOf("/", 8) + 1));
    }

    @Test
    public void demo18() {
        String fid = "group1/M00/00/00/wKgz6lnduTeAMdrcAAEoRmXZPp870.jpeg";
        String[] groupNameAndRemoteFileNameFromFid = FileNameUtil.getGroupNameAndRemoteFileNameFromFid(fid);
        for (String s : groupNameAndRemoteFileNameFromFid) {
            System.out.println(s);
        }

    }

    @Test
    public void demo19() throws IOException {
        //通过tracker获取storage
        byte[] bytes = ProtocolUtil.generateProtoHeader((byte) 104, 16L, (byte) 0);
        Socket socket = new Socket("116.62.195.66", 22122);
        socket.getOutputStream().write(bytes);
        byte[] bytes1 = new byte[16];
        String groupName = "group1";
        byte[] bytes2 = groupName.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(bytes2, 0, bytes1, 0, bytes2.length);
        socket.getOutputStream().write(bytes1);
//        int read = socket.getInputStream().read(new byte[10]);
//        System.out.println(read);
        //0000000401  0001031141111171124900000000004949544654504649575346545400000000892160
      while (true){
          ProtocolUtil.recvHeader(socket.getInputStream(),(byte) 4,40);
      }


    }

     @Test
     public void demo20(){
         byte b = 127;
         System.out.println(b);
     }

}
