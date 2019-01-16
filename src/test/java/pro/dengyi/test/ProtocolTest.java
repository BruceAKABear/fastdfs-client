package pro.dengyi.test;

import org.junit.Test;
import pro.dengyi.fastdfs.constantenum.ControlCode;
import pro.dengyi.fastdfs.constantenum.SystemStatus;
import pro.dengyi.fastdfs.entity.ReceiveData;
import pro.dengyi.fastdfs.utils.ProtocolUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * 协议测试类
 *
 * @author 邓艺
 * @version v1.0
 * @date 2018-12-26 14:42
 */
public class ProtocolTest {

    /**
     * 报文头部工具类测试
     */
    @Test
    public void demo1() {
        byte[] protoHeader = ProtocolUtil.getProtoHeader(ControlCode.UPLOAD.getValue(), 400L, (byte) 0);
        for (byte b : protoHeader) {
            System.out.print(b);
        }
    }

    /**
     * 测试通过tracker获取所有的storage
     */
    @Test
    public void demo2() throws IOException {
        String groupName = "group1";
        byte[] bytes = groupName.getBytes(StandardCharsets.UTF_8);
        Socket socket = new Socket("192.168.0.178", 22122);
        OutputStream outputStream = socket.getOutputStream();
        byte[] protoHeader = ProtocolUtil.getProtoHeader((byte) 106, (long) bytes.length, SystemStatus.SUCCESS.getValue());
        outputStream.write(protoHeader);
        byte[] byteArray = new byte[16];
        System.arraycopy(bytes, 0, byteArray, 0, bytes.length);
        outputStream.write(byteArray);
        ReceiveData responseData = ProtocolUtil.getResponseData(socket.getInputStream(), (byte) 100, (long) -1);
        int i = responseData.getBody().length - 17;
        System.out.println(i);
        int iii = 23;
        int i1 = i / iii;
        System.out.println(i1);
        //255.255.255.255 从16-31
        String ipAddr = new String(responseData.getBody(), 16, 15).trim();
        Long aLong = ProtocolUtil.byteArray2Long(responseData.getBody(), 31);
        System.out.println(ipAddr);
        System.out.println(aLong);
    }

}
