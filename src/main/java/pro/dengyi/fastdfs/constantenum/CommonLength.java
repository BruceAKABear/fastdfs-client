package pro.dengyi.fastdfs.constantenum;

/**
 * 系统代码枚举类
 *
 * @author 邓艺
 * @version v1.0
 * @date 2018-12-26 15:43
 */
public enum CommonLength {
    /**
     * 最大组名长度
     */
    GROUP_NAME_MAX_LENGTH(16),
    /**
     * 包长度
     */
    PROTO_PACKAGE_LENGTH(8),
    /**
     * 标准协议头长度10个字节长度
     */
    STANDARD_PROTOCOL_HEAD_LENGTH(10),
    /**
     * 解析tracker返回命令
     */
    TRACKER_RESPONSE(100);

    private Integer value;

    CommonLength(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
