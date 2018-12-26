package pro.dengyi.fastdfs.utils;

/**
 * 文件系统核心工具类
 *
 * @author 邓艺
 * @version v1.0
 * @date 2018-12-26 12:54
 */
public class CoreUtil {

    /**
     * 协议头部生成工具
     *
     * @param pkgLength 协议长度
     * @param errorNo 异常码
     * @param cmd 命令码
     * @return byte[]
     * @author 邓艺
     * @date 2018/12/26 15:44
     */
    public static byte[] buildProtocolHeader(Long pkgLength, byte errorNo, byte cmd) {
        byte[] header = new byte[10];
        byte[] bytes = long2buff(pkgLength);
        System.arraycopy(bytes, 0, header, 0, 8);
        header[8] = cmd;
        header[9] = errorNo;
        return header;
    }

    public static byte[] reciveHeader() {
        byte[] header = new byte[10];
        return header;
    }


    public static String generateAntiToken(String secretKey,Integer times,String remoteFileName) {
        return "";
    }

    /**
     * buff转换工具类
     *
     * @param pkgLength
     * @return byte[] 8位字节数组
     * @author 邓艺
     * @date 2018/12/26 15:28
     */
    public static byte[] long2buff(long pkgLength) {
        byte[] bs = new byte[8];
        bs[0] = (byte) ((pkgLength >> 56) & 0xFF);
        bs[1] = (byte) ((pkgLength >> 48) & 0xFF);
        bs[2] = (byte) ((pkgLength >> 40) & 0xFF);
        bs[3] = (byte) ((pkgLength >> 32) & 0xFF);
        bs[4] = (byte) ((pkgLength >> 24) & 0xFF);
        bs[5] = (byte) ((pkgLength >> 16) & 0xFF);
        bs[6] = (byte) ((pkgLength >> 8) & 0xFF);
        bs[7] = (byte) (pkgLength & 0xFF);
        return bs;
    }

}
