package pro.dengyi.fastdfs.service.impl;

import pro.dengyi.fastdfs.service.UploadService;

/**
 * 文件上传服务了接口实现类
 *
 * @author 邓艺
 * @version v1.0
 * @date 2018-12-26 14:25
 */
public class UploadServiceImpl implements UploadService {

    @Override
    public String uploadFile(String groupName, byte[] fileBuff, String fileExtName, String[] metaList) {
        return null;
    }

    @Override
    public String uploadAppenderFile() {
        return null;
    }

    @Override
    public Boolean deleteFile() {
        return null;
    }

    @Override
    public Boolean updateFile() {
        return null;
    }

    private String doUploadFile(byte cmd, String groupName, String masterFilename, String prefixName, String fileExtName, long fileSize, String[] metaList) {

        return null;
    }

}
