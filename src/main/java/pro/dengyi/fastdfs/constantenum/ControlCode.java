package pro.dengyi.fastdfs.constantenum;

/**
 * 系统控制编码
 *
 * @author 邓艺
 * @version v1.0
 * @date 2019-01-11 10:55
 */
public enum ControlCode {
    /**
     * 上传文件系统命令码
     */
    UPLOAD((byte) 11),
    /**
     * 删除文件命令码
     */
    DELETE((byte) 12),
    /**
     * 下载文件命令码
     */
    DOWNLOAD((byte) 14),
    /**
     * 断点续传
     */
    APPEND((byte) 24),
    /**
     * 获取服务器响应
     */
    RESPONSE((byte) 100);

    private Byte value;

    ControlCode(Byte value) {
        this.value = value;
    }

    public Byte getValue() {
        return value;
    }
}
