package pro.dengyi.fastdfs.service;

import pro.dengyi.fastdfs.core.Tracker;
import pro.dengyi.fastdfs.entity.BasicStorageInfo;

/**
 * fastdfs操作模板类
 *
 * @author 邓艺
 * @version v1.0
 * @date 2019-01-20 17:29
 */
public class FastdfsTemplate extends Tracker {

    public String uploadFile() {
        BasicStorageInfo uploadStorage = this.getUploadStorage();
        return null;
    }

    public String uploadAppenderFile() {
        return null;
    }

    public Boolean uploadMetadata() {
        return false;
    }

    public Boolean deleteFile() {
        return false;
    }

    public byte[] downloadFile() {
        return null;
    }

}
