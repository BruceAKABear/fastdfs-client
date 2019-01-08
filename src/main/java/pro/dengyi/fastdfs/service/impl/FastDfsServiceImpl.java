package pro.dengyi.fastdfs.service.impl;

import com.sun.istack.internal.NotNull;
import pro.dengyi.fastdfs.service.FastDfsService;

/**
 * 文件上传服务了接口实现类
 *
 * @author 邓艺
 * @version v1.0
 * @date 2018-12-26 14:25
 */
public class FastDfsServiceImpl implements FastDfsService {

    @Override
    public String uploadFile(String groupName, Byte[] fileBuff, String fileExtName, String[] metaList) {
        return null;
    }

    @Override
    public String uploadAppenderFile() {
        return null;
    }

    @Override
    public Boolean deleteFile(String groupName, String remoteFileName) {
        return null;
    }

    @Override
    public Boolean updateFile() {
        return null;
    }

    @Override
    public Byte[] downloadFile(String groupName, String remoteFileName) {
        return new Byte[0];
    }

    @Override
    public Byte[] downloadFile(String groupName, String remoteFileName, Long fileOffset, Long downloadBytes) {
        return new Byte[0];
    }

    private String doUploadFile(byte cmd, String groupName, String masterFilename, String prefixName, String fileExtName, long fileSize, String[] metaList) {

        return null;
    }

    /**
     * 发送数据包
     *
     * @param cmd
     * @param groupName
     * @param remoteFilename
     */
    private void doSendPackage(@NotNull byte cmd, @NotNull String groupName, @NotNull String remoteFilename) {

    }

    private String[] doUploadFile(Byte cmd, @NotNull String groupName, String masterFilename, String suffixName, String extName, Long fileSize) {
        String[] results = new String[2];
        return results;
    }

}
