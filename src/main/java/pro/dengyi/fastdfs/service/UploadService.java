package pro.dengyi.fastdfs.service;

/**
 * 文件上传服务接口
 *
 * @author 邓艺
 * @version v1.0
 * @date 2018-12-26 14:22
 */
public interface UploadService {
    String uploadFile();

    Boolean deleteFile();

    Boolean updateFile();

}
