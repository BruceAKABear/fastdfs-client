package pro.dengyi.fastdfs.utils;

import com.google.common.primitives.Longs;
import com.sun.istack.internal.NotNull;
import lombok.extern.slf4j.Slf4j;
import pro.dengyi.fastdfs.constantenum.CommonLength;
import pro.dengyi.fastdfs.entity.ReceiveData;
import pro.dengyi.fastdfs.entity.ReceiveHeader;
import pro.dengyi.fastdfs.exception.FastdfsException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * 通讯协议工具类
 *
 * @author 邓艺
 * @version v1.0
 * @date 2019-01-04 15:29
 */
@Slf4j
public class ProtocolUtil {

    /**
     * 生成协议头工具
     * 标准报文头部长度为10个字节，前8个字节表示数据包长度,第8个字节表示控制字，最后一个字节表示状态必须为0
     *
     * @param controlCode 系统命令字
     * @param packagelength 数据包长度字节数
     * @param status 状态编码
     * @return byte[] byte数组
     * @author 邓艺
     * @date 2019/1/7 11:11
     */
    public static byte[] getProtoHeader(byte controlCode, Long packagelength, byte status) {
        //标准报文头部长10位
        byte[] protocolHeader = new byte[10];
        //第0-7用字节数组表示包的长度
        byte[] lengthInByteArray = long2ByteArray(packagelength);
        //使用arraycopy效率较高
        System.arraycopy(lengthInByteArray, 0, protocolHeader, 0, 8);
        //第8位表示控制字
        protocolHeader[8] = controlCode;
        //第9位表示命令状态，应该为0
        protocolHeader[9] = status;
        return protocolHeader;
    }

    /**
     * 从输入流中读取标准协议10个字节长度报文头部
     *
     * @param inputStream 输入流
     * @param controlCode 控制字
     * @param dataBodyLength 数据长度
     * @return Map
     * @author 邓艺
     * @date 2019/1/15 15:16
     */
    public static ReceiveHeader getResponseHeader(InputStream inputStream, Byte controlCode, Long dataBodyLength) throws IOException, FastdfsException {
        byte[] responseHeaderBytes = new byte[10];
        //正确性校验
        if (inputStream.read(responseHeaderBytes) != CommonLength.STANDARD_PROTOCOL_HEAD_LENGTH.getLength()) {
            throw new FastdfsException("实际读取到的报文头部长度非法，长度不为10个字节");
        }
        if (responseHeaderBytes[8] != controlCode) {
            throw new FastdfsException("响应报文头部控制字非法，控制字和需要控制字不相同");
        }
        if (responseHeaderBytes[9] != (byte) 0) {
            throw new FastdfsException("服务器响应状态字为异常");
        }
        if (ProtocolUtil.byteArray2Long(responseHeaderBytes) < 0) {
            throw new FastdfsException("数据体中实际数据长度小于0异常");

        }
        if (dataBodyLength >= 0 && !ProtocolUtil.byteArray2Long(responseHeaderBytes).equals(dataBodyLength)) {
            throw new FastdfsException("收取到的数据包长度不等于需要的数据包长度");
        }
        //将数据解析封装返回
        return new ReceiveHeader((byte) 0, ProtocolUtil.byteArray2Long(responseHeaderBytes));
    }

    /**
     * 获取响应的数据内容
     *
     * @param inputStream 输入流
     * @param controlCode 控制字
     * @param dataBodyLength 数据长度
     * @return ReceiveData
     * @author 邓艺
     * @date 2019/1/15 16:02
     */
    public static ReceiveData getResponseData(InputStream inputStream, Byte controlCode, Long dataBodyLength) throws IOException, FastdfsException {
        ReceiveHeader responseHeader = getResponseHeader(inputStream, controlCode, dataBodyLength);
        //如果服务器响应异常
        if (responseHeader.getErrorNo() != 0) {
            return new ReceiveData((byte) 1, null);
        } else {
            //服务器响应正常，读取数据
            //TODO 数据的读取封装是一个很好玩的地方，值得研究一下
            byte[] dataBody = new byte[Math.toIntExact(responseHeader.getDataBodyLength())];
            int totalBytes = 0;
            int remainBytes = Math.toIntExact(responseHeader.getDataBodyLength());
            int bytes;
            while (totalBytes < responseHeader.getDataBodyLength()) {
                if ((bytes = inputStream.read(dataBody, totalBytes, remainBytes)) < 0) {
                    break;
                }
                totalBytes += bytes;
                remainBytes -= bytes;
            }
            if (inputStream.read(dataBody) != Math.toIntExact(responseHeader.getDataBodyLength())) {
                throw new FastdfsException("读取到的字节数和期望数据数不相同");
            }
            return new ReceiveData((byte) 0, dataBody);
        }

    }

    /**
     * 将包长度转换成8个字节长度字节数据
     *
     * @param packagelength 数据包长度
     * @return byte[]
     * @author 邓艺
     * @date 2019/1/7 13:21
     */
    public static byte[] long2ByteArray(Long packagelength) {
        return Longs.toByteArray(packagelength);
    }

    /**
     * 将长度数组转换成long表示长度
     *
     * @param bs 协议头字节数组
     * @return java.lang.Long
     * @author 邓艺
     * @date 2019/1/7 13:39
     */
    public static Long byteArray2Long(byte[] bs) {
        byte[] lengthByte = new byte[8];
        System.arraycopy(bs, 0, lengthByte, 0, 8);
        return Longs.fromByteArray(lengthByte);
    }

    public static Long byteArray2Long(byte[] bs, int offset) {
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
    public static Integer byteArray2Int(byte[] bs, Integer offset) {
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
     * 将long型数据转换为时间
     *
     * @param offSet 偏移量
     * @return Date
     * @author 邓艺
     * @date 2019/1/18 10:24
     */
    public static Date byteArray2Date(@NotNull byte[] bytes, Integer offSet) {
        byte[] timeArray = new byte[8];
        System.arraycopy(bytes, offSet, timeArray, 0, 8);
        return new Date();
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
