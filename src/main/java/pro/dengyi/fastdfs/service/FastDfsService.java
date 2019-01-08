package pro.dengyi.fastdfs.service;

/**
 * 文件上传服务接口
 *
 * @author 邓艺
 * @version v1.0
 * @date 2018-12-26 14:22
 */
public interface FastDfsService {
    String uploadFile(String groupName, Byte[] fileBuff, String fileExtName, String[] metaList);

    String uploadAppenderFile();

    /**
     * 删除文件
     *
     * @param groupName 组名
     * @param remoteFileName 远程文件名
     * @return java.lang.Boolean
     * @author 邓艺
     * @date 2019/1/8 9:46
     */
    Boolean deleteFile(String groupName, String remoteFileName);

    Boolean updateFile();

    /**
     * 下载文件(下载完整文件)
     *
     * @param groupName 组名
     * @param remoteFileName 远程文件名
     * @return byte[] 字节数组
     */
    Byte[] downloadFile(String groupName, String remoteFileName);

    /**
     * 下载文件(指定文件起始点和下载大小)
     *
     * @param groupName 组名
     * @param remoteFileName 远程文件名
     * @param fileOffset 起始偏移量
     * @param downloadBytes 下载大小
     * @return java.lang.Byte[] 字节数组
     * @author 邓艺
     * @date 2019/1/8 9:41
     */
    Byte[] downloadFile(String groupName, String remoteFileName, Long fileOffset, Long downloadBytes);

}
