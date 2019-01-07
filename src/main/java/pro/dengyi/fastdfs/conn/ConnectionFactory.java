package pro.dengyi.fastdfs.conn;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import pro.dengyi.fastdfs.entity.FileInfo;

/**
 * 连接工厂
 *
 * @author 邓艺
 * @version v1.0
 * @date 2018-12-27 10:49
 */
public class ConnectionFactory extends BasePooledObjectFactory<FileInfo> {

    @Override
    public FileInfo create() throws Exception {
        return new FileInfo();
    }

    @Override
    public PooledObject<FileInfo> wrap(FileInfo fileInfo) {
        return null;
    }
}
