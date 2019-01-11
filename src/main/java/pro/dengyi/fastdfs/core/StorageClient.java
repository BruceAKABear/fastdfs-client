package pro.dengyi.fastdfs.core;

import lombok.extern.slf4j.Slf4j;
import pro.dengyi.fastdfs.connection.Connection;
import pro.dengyi.fastdfs.connection.ConnectionFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * 存储服务器核心类
 *
 * @author 邓艺
 * @version v1.0
 * @date 2018-12-25 16:15
 */
@Slf4j
public class StorageClient {

    /**
     * 文件删除方法
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
        try {
            connection.sendPackage(cmd, groupName, remoteFileName);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("删除文件时io异常");
            return false;
        }
    }

    public String[] doUploadFile(byte controlCode, String groupName, String masterFileName, String suffixName, String extName, Long fileSize,
            InputStream fileInputStream) {
        return null;
    }

}
