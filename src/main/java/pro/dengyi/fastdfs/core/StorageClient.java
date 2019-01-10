package pro.dengyi.fastdfs.core;

import pro.dengyi.fastdfs.connection.Connection;
import pro.dengyi.fastdfs.connection.ConnectionFactory;

/**
 * 存储服务器核心类
 *
 * @author 邓艺
 * @version v1.0
 * @date 2018-12-25 16:15
 */
public class StorageClient {

    /**
     * 删除文件
     *
     * @param groupName 组名
     * @param remoteFileName 远程文件名
     * @return java.lang.Boolean
     * @author 邓艺
     * @date 2019/1/10 16:25
     */
    public Boolean deleteFile(String groupName, String remoteFileName) {
        Connection connection = ConnectionFactory.getConnection();
        byte cmd = 111;
        connection.sendPackage(cmd, groupName, remoteFileName);
        return false;
    }

}
