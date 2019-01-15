package pro.dengyi.test;

import org.junit.Test;
import pro.dengyi.fastdfs.constantenum.ControlCode;
import pro.dengyi.fastdfs.utils.ProtocolUtil;

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

}
