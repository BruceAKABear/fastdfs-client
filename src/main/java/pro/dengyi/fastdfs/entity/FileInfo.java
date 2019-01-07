package pro.dengyi.fastdfs.entity;

import lombok.Data;

import java.util.Date;

/**
 * 文件信息实体类
 *
 * @author 邓艺
 * @version v1.0
 * @date 2018-12-25 16:35
 */
@Data
public class FileInfo {
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件大小
     */
    private Long fileSize;
    /**
     * 文件创建时间
     */
    private Date createTime;
    /**
     * 校验类型，默认crc32
     */
    private String checkType;
    /**
     * 元数据
     */
    private String[] metaList;

}
