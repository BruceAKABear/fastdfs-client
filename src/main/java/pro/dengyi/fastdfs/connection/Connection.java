package pro.dengyi.fastdfs.connection;

import org.apache.commons.lang3.ArrayUtils;

import java.nio.charset.StandardCharsets;

/**
 * 连接类
 *
 * @author 邓艺
 * @version v1.0
 * @date 2019-01-08 16:45
 */
public class Connection {

    /**
     * 关闭连接
     */
    public void close() {

    }

    public void sendPackage(byte cmd, String groupName, String remoteFileName) {
        //决定封装数据的长度
        byte[] originalBytes = groupName.getBytes(StandardCharsets.UTF_8);
        int groupNameArrayLength = ArrayUtils.getLength(originalBytes);
        byte[] groupNameBytes = new byte[16];
        if (groupNameArrayLength <= 16) {
            System.arraycopy(originalBytes, 0, groupNameBytes, 0, originalBytes.length);
        } else {
            System.arraycopy(originalBytes, 0, groupNameBytes, 0, 16);
        }

    }
}
