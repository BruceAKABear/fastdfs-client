package pro.dengyi.fastdfs.core;

/**
 * 报文工厂类
 * 用途：将报文组装起来
 *
 * @author 邓艺
 * @version v1.0
 * @date 2019-01-20 17:12
 */
public class MessageFactory {

    public static byte[] build(byte[] protocolHeader, byte[]... bytes) {
        byte[] wholePackageMessage = new byte[10];
        return wholePackageMessage;
    }

}
