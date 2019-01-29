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
     * 上传组从文件系统命令码
     */
    UPLOAD_SLAVE((byte) 21),
    /**
     * tracker查询上传storage
     */
    TRACKER_QUERY_UPLOAD_STORAGE((byte) 101),
    /**
     * tracker查询更新服务器
     */
    TRACKER_QUERY_UPDATE_STORAGE((byte) 103),

    /**
     * 删除文件命令码
     */
    DELETE((byte) 12),
    TRACKER_DELETE_STORAGE((byte) 93),

    /**
     * tracker查询下载storage
     */
    TRACKER_QUERY_DOWNLOAD_STORAGE((byte) 102),
    /**
     * 下载文件命令码
     */
    DOWNLOAD((byte) 14),
    /**
     * 获取metadata
     */
    GET_METADATA((byte) 15),

    /**
     * 断点续传
     */
    APPEND((byte) 24),

    /**
     * 获取服务器响应
     */
    SERVER_RESPONSE((byte) 100),

    /**
     * 查询所有的存储服务器组
     */
    TRACKER_GET_ALL_GROUPINFO((byte) 91),

    /**
     * tracker列举出storage控制字
     */
    TRACKER_GET_ALL_STORAGEINFO((byte) 92),

    QUERY_STORAGE_WITHOUT_GROUP_ALL((byte) 106),

    QUERY_STORE_WITH_GROUP_ALL((byte) 107);

    private Byte value;

    ControlCode(Byte value) {
        this.value = value;
    }

    public Byte getValue() {
        return value;
    }
}
