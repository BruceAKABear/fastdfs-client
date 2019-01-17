package pro.dengyi.fastdfs.connection;

import lombok.Data;
import pro.dengyi.fastdfs.config.FastdfsProterties;

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
    private FastdfsProterties fastdfsProterties;

    private static ConnectionFactory ourInstance = new ConnectionFactory();

    public static Connection getConnection() {

        return null;
    }

    private ConnectionFactory() {
    }
}
