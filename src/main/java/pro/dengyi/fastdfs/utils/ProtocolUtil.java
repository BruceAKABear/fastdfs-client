package pro.dengyi.fastdfs.utils;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

/**
 * 协议工具类
 *
 * @author 邓艺
 * @version v1.0
 * @date 2019-01-04 15:29
 */
@Slf4j
public class ProtocolUtil {

    /**
     * 生成协议头工具
     *
     * @param cmd 命令编码
     * @param packagelength 数据包长度
     * @param status 状态编码
     * @return byte[] byte数组
     * @author 邓艺
     * @date 2019/1/7 11:11
     */
    public static byte[] generateProtoHead(byte cmd, Long packagelength, byte status) {
        //标准报文头部长10位
        byte[] protocolHeader = new byte[10];
        //用字节数组表示包的长度
        byte[] lengthInByteArray = long2buff(packagelength);
        System.arraycopy(lengthInByteArray, 0, protocolHeader, 0, 8);
        protocolHeader[8] = cmd;
        protocolHeader[9] = status;
        return protocolHeader;
    }

    /**
     * 将包长度转换成8个字节长度字节数据
     *
     * @param packagelength 数据包长度
     * @return byte[]
     * @author 邓艺
     * @date 2019/1/7 13:21
     */
    public static byte[] long2buff(Long packagelength) {
        byte[] bs = new byte[8];
        bs[0] = (byte) ((packagelength >> 56) & 0xFF);
        bs[1] = (byte) ((packagelength >> 48) & 0xFF);
        bs[2] = (byte) ((packagelength >> 40) & 0xFF);
        bs[3] = (byte) ((packagelength >> 32) & 0xFF);
        bs[4] = (byte) ((packagelength >> 24) & 0xFF);
        bs[5] = (byte) ((packagelength >> 16) & 0xFF);
        bs[6] = (byte) ((packagelength >> 8) & 0xFF);
        bs[7] = (byte) (packagelength & 0xFF);
        return bs;
    }

    /**
     * 将长度数组转换成long表示长度
     *
     * @param bs
     * @param offset
     * @return java.lang.Long
     * @author 邓艺
     * @date 2019/1/7 13:39
     */
    public static Long buff2long(byte[] bs, Integer offset) {
        return (((long) (bs[offset] >= 0 ? bs[offset] : 256 + bs[offset])) << 56) | (((long) (bs[offset + 1] >= 0 ? bs[offset + 1] : 256 + bs[offset + 1]))
                << 48) | (((long) (bs[offset + 2] >= 0 ? bs[offset + 2] : 256 + bs[offset + 2])) << 40) | (
                ((long) (bs[offset + 3] >= 0 ? bs[offset + 3] : 256 + bs[offset + 3])) << 32) | (
                ((long) (bs[offset + 4] >= 0 ? bs[offset + 4] : 256 + bs[offset + 4])) << 24) | (
                ((long) (bs[offset + 5] >= 0 ? bs[offset + 5] : 256 + bs[offset + 5])) << 16) | (
                ((long) (bs[offset + 6] >= 0 ? bs[offset + 6] : 256 + bs[offset + 6])) << 8) | ((long) (bs[offset + 7] >= 0 ?
                bs[offset + 7] :
                256 + bs[offset + 7]));
    }

    /**
     * 将长度数据转换成long表示的长度
     *
     * @param bs
     * @param offset
     * @return java.lang.Integer
     * @author 邓艺
     * @date 2019/1/7 13:41
     */
    public static Integer buff2int(byte[] bs, Integer offset) {
        return (((int) (bs[offset] >= 0 ? bs[offset] : 256 + bs[offset])) << 24) | (((int) (bs[offset + 1] >= 0 ? bs[offset + 1] : 256 + bs[offset + 1])) << 16)
                | (((int) (bs[offset + 2] >= 0 ? bs[offset + 2] : 256 + bs[offset + 2])) << 8) | ((int) (bs[offset + 3] >= 0 ?
                bs[offset + 3] :
                256 + bs[offset + 3]));
    }

    /**
     * 将字节数组转换成ip地址
     *
     * @param bs
     * @param offset
     * @return java.lang.String
     * @author 邓艺
     * @date 2019/1/7 13:44
     */
    public static String getIpAddress(byte[] bs, int offset) {
        //storage server ID
        if (bs[0] == 0 || bs[3] == 0) {
            return "";
        }
        int n;
        StringBuilder sbResult = new StringBuilder(16);
        for (int i = offset; i < offset + 4; i++) {
            n = (bs[i] >= 0) ? bs[i] : 256 + bs[i];
            if (sbResult.length() > 0) {
                sbResult.append(".");
            }
            sbResult.append(String.valueOf(n));
        }

        return sbResult.toString();
    }

    /**
     * 获取防盗链token
     *
     * @param remoteFilename 远程存储服务器上文件名
     * @param ts 时间戳 单位秒
     * @param secretKey 防盗链秘钥
     * @return java.lang.String
     * @author 邓艺
     * @date 2019/1/7 14:09
     */
    public static String getToken(String remoteFilename, int ts, String secretKey) throws NoSuchAlgorithmException {
        byte[] bsFilename = remoteFilename.getBytes(StandardCharsets.UTF_8);
        byte[] bsKey = secretKey.getBytes(StandardCharsets.UTF_8);
        byte[] bsTimestamp = (new Integer(ts)).toString().getBytes(StandardCharsets.UTF_8);
        byte[] buff = new byte[bsFilename.length + bsKey.length + bsTimestamp.length];
        System.arraycopy(bsFilename, 0, buff, 0, bsFilename.length);
        System.arraycopy(bsKey, 0, buff, bsFilename.length, bsKey.length);
        System.arraycopy(bsTimestamp, 0, buff, bsFilename.length + bsKey.length, bsTimestamp.length);
        return Md5Util.encode(buff);
    }

}
