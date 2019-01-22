package pro.dengyi.fastdfs.entity;

import lombok.Data;

/**
 * 已经上传的文件信息封装实体类
 *
 * @author 邓艺
 * @version v1.0
 */
@Data
public class UploadedFileInfo {
    /**
     * 组名
     */
    private String groupName;
    /**
     * 远程文件名FID
     */
    private String remoteFileName;

}
