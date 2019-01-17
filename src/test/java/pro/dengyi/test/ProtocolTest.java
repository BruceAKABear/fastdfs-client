package pro.dengyi.test;

import org.junit.Test;
import pro.dengyi.fastdfs.constantenum.SystemStatus;
import pro.dengyi.fastdfs.entity.ReceiveData;
import pro.dengyi.fastdfs.entity.StorageGroupInfo;
import pro.dengyi.fastdfs.utils.DataUtil;
import pro.dengyi.fastdfs.utils.ProtocolUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.TreeSet;

/**
 * 协议测试类
 *
 * @author 邓艺
 * @version v1.0
 * @date 2018-12-26 14:42
 */
public class ProtocolTest {

    /**
     * 测试tracker获取所有存储组
     */
    @Test
    public void demo1() throws IOException {
        Socket socket = new Socket("192.168.199.2", 22122);
        OutputStream outputStream = socket.getOutputStream();
        byte[] protoHeader = ProtocolUtil.getProtoHeader((byte) 91, 0L, SystemStatus.SUCCESS.getValue());
        outputStream.write(protoHeader);
        //接受响应
        ReceiveData responseData = ProtocolUtil.getResponseData(socket.getInputStream(), (byte) 100, (long) -1);
        String string = new String(responseData.getBody(),0,16);
        StorageGroupInfo storageGroupInfo = DataUtil.putDataInToGroupInfo(responseData.getBody(), 0);

        System.out.println(responseData.getBody());
    }

    /**
     * 测试通过tracker获取所有的storage
     */
    @Test
    public void demo2() throws IOException {
        String groupName = "group1";
        byte[] bytes = groupName.getBytes(StandardCharsets.UTF_8);
        Socket socket = new Socket("192.168.199.2", 22122);
        OutputStream outputStream = socket.getOutputStream();
        byte[] protoHeader = ProtocolUtil.getProtoHeader((byte) 91, 0L, SystemStatus.SUCCESS.getValue());
        outputStream.write(protoHeader);
        //byte[] byteArray = new byte[16];
        //System.arraycopy(bytes, 0, byteArray, 0, bytes.length);
        //outputStream.write(byteArray);
        ReceiveData responseData = ProtocolUtil.getResponseData(socket.getInputStream(), (byte) 100, (long) -1);
        int i = responseData.getBody().length - 17;
        //body里面的数据为 组名+(ip+端口）*n
        System.out.println(i);
        int iii = 23;
        int i1 = i / iii;
        System.out.println(i1);
        //255.255.255.255 从16-31
        String groupName11 = new String(responseData.getBody(), 0, 16).trim();
        System.out.println(groupName11);
        String ipAddr = new String(responseData.getBody(), 16, 15).trim();
        Long aLong = ProtocolUtil.byteArray2Long(responseData.getBody(), 31);
        String ipAddr2 = new String(responseData.getBody(), 39, 15).trim();
        Long aLong2 = ProtocolUtil.byteArray2Long(responseData.getBody(), 54);
        System.out.println(ipAddr);
        System.out.println(aLong);
        System.out.println("----------");
        System.out.println(ipAddr2);
        System.out.println(aLong2);
        //TODO 为什么获取到的是group2 下的所有storage，为什么不是获取到group1
    }

     @Test
     public void demo3() throws IOException {
         TreeSet<String> treeSet = new TreeSet<>();
         treeSet.add("192.168.0.188:8080");
         treeSet.add("192.168.0.188:8080");
         treeSet.add("192.168.0.188:8080");
         treeSet.add("192.168.0.188:8081");
         for (String s : treeSet) {
             System.out.println(s);
         }



     }

}
