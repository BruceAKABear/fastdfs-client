package pro.dengyi.fastdfs.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5工具类
 *
 * @author 邓艺
 * @version v1.0
 * @date 2018-12-26 16:47
 */
public class Md5Util {

    public static String md5(byte[] source) throws NoSuchAlgorithmException {
        char[] hexDigits= new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(source);
        byte[] digest = messageDigest.digest();

        char[] str = new char[32];
        int k = 0;
        for (int i = 0; i < 16; i++) {
            str[k++] = hexDigits[digest[i] >>> 4 & 0xf];
            str[k++] = hexDigits[digest[i] & 0xf];
        }

        return new String(str);
    }
}
