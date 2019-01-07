package pro.dengyi.fastdfs.constantenum;

/**
 * 编码类型枚举类
 *
 * @author 邓艺
 * @version v1.0
 * @date 2018-12-26 13:24
 */
public enum EncodingType {
    /**
     * 8859-1编码
     */
    ISO8859("ISO8859-1"),
    /**
     * u8编码
     */
    UTF8("UTF-8");
    private String value;

    private EncodingType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
