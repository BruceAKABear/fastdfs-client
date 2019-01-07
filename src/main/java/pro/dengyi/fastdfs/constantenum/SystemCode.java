package pro.dengyi.fastdfs.constantenum;

/**
 * 系统代码枚举类
 *
 * @author 邓艺
 * @version v1.0
 * @date 2018-12-26 15:43
 */
public enum SystemCode {
    /**
     * 上传文件系统命令码
     */
    STORAGE_PROTO_CMD_UPLOAD_FILE(11),
    /**
     * 删除文件命令码
     */
    STORAGE_PROTO_CMD_DELETE_FILE(12),
    /**
     * 下载文件命令码
     */
    STORAGE_PROTO_CMD_DOWNLOAD_FILE(14);

    private Integer value;

    private SystemCode(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
