package pro.dengyi.fastdfs.entity;

import lombok.Data;

/**
 * 接收到数据据封装实体类
 *
 * @author 邓艺
 * @version v1.0
 * @date 2019-01-08 09:55
 */
@Data
public class ReceiveData {
    /**
     * 异常编码 0为正常，非0为不正常
     */
    private byte errorNo;
    /**
     * 数据
     */
    private byte[] body;

    public ReceiveData(byte errorNo, byte[] body) {
        this.errorNo = errorNo;
        this.body = body;
    }
}
