package pro.dengyi.fastdfs.core;

import java.io.FileInputStream;

/**
 * 存储服务器核心类 提供上传、下载、下载、修改、断点续传功能
 *
 * @author 邓艺
 * @version v1.0
 * @date 2018-12-25 16:15
 */
public class StorageClient {
  private String pathPrefix;

  /**
   * 以流的形式简单上传文件
   *
   * @param fileInputStream 文件输入流
   * @return java.lang.String
   * @author 邓艺
   * @date 2019/1/9 23:34
   */
  public String uploadFile(FileInputStream fileInputStream) {
    return null;
  }
}
