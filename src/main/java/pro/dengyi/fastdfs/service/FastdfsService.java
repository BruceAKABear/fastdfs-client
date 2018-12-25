package pro.dengyi.fastdfs.service;

/**
 * fastdfs文件服务接口
 *
 * @author 邓艺
 * @version v1.0
 * @date 2018-12-25 16:53
 */
public interface FastdfsService {
    String uploadFile();
    Boolean deleteFile();
    Boolean updateFile();
}
