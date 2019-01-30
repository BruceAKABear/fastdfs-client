package pro.dengyi.fastdfs.utils;

import com.sun.istack.internal.NotNull;
import org.apache.commons.lang3.StringUtils;
import pro.dengyi.fastdfs.exception.FastdfsException;

/**
 * ip地址工具类
 *
 * @author 邓艺
 * @version v1.0
 * @date 2019-01-08 16:30
 */
public class IpAddressUtil {

    /**
     * 获取ip端口数组
     *
     * @param singleAddress
     * @return java.lang.String[] string[0]ip地址,string[1]端口
     * @author 邓艺
     * @date 2019/1/8 16:36
     */
    public static String[] getIpAndPort(@NotNull String singleAddress) throws FastdfsException {
        if (StringUtils.isNotBlank(singleAddress)) {
            return singleAddress.split(":");
        } else {
            throw new FastdfsException("tracker配置异常");
        }
    }

}
