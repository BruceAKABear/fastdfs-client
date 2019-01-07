package pro.dengyi.fastdfs.entity;

import java.util.TreeMap;

/**
 * 文件元数据类
 *
 * @author 邓艺
 * @version v1.0
 * @date 2019-01-07 16:12
 */
public class MetaData {

    private TreeMap<String, Object> params = new TreeMap<>();

    public void set(String key, Object value) {
        params.put(key, value);
    }

    public Object get(String key) {
        return params.get(key);
    }
}
