package pro.dengyi.fastdfs.constantenum;

/**
 * 协议枚举类
 *
 * @author 邓艺
 * @version v1.0
 * @date 2019-01-04 13:29
 */
public enum ProtocolEnum {
    /**
     * 协议报文头部长度，一共10位
     */
    PROTOCOL_HEAD_LENGTH(10),
    /**
     * 标准组名字节数组长度
     */
    STANDARD_GROUPNAMEBYTES_LENGTH(16);

    ProtocolEnum(Integer value) {
        this.value = value;
    }

    private Integer value;

    public Integer getValue() {
        return value;
    }
}
