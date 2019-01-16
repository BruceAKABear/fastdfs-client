package pro.dengyi.fastdfs.constantenum;

/**
 * 系统状态枚举类
 *
 * @author 邓艺
 * @version v1.0
 * @date 2019-01-16 08:41
 */
public enum SystemStatus {
    /**
     * 成功状态
     */
    SUCCESS((byte) 0);
    private byte value;

    SystemStatus(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
