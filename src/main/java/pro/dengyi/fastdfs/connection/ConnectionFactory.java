package pro.dengyi.fastdfs.connection;

import lombok.Data;
import pro.dengyi.fastdfs.config.FastdfsConfiguration;

/**
 * 连接工厂
 *
 * @author 邓艺
 * @version v1.0
 * @date 2019-01-08 10:42
 */
@Data
public class ConnectionFactory {
    /**
     * 注入fastdfs配置对象
     */
    private FastdfsConfiguration fastdfsConfiguration;

    public ConnectionFactory(FastdfsConfiguration fastdfsConfiguration) {
        this.fastdfsConfiguration = fastdfsConfiguration;
    }

    public static Connection getConnection() {

        return null;
    }

}
