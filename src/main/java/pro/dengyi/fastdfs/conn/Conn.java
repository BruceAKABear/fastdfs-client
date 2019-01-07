package pro.dengyi.fastdfs.conn;

/**
 * @author 邓艺
 * @version v1.0
 * @date 2019-01-07 10:39
 */
public class Conn {
    private static Conn ourInstance = new Conn();

    public static Conn getConnection() {
        return ourInstance;
    }

    private Conn() {
    }
}
