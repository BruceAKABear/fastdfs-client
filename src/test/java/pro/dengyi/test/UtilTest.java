package pro.dengyi.test;

import org.junit.Test;
import pro.dengyi.fastdfs.utils.FileNameUtil;

/**
 * 工具类测试类
 *
 * @author 邓艺
 * @version v1.0
 * @date 2019-01-22 13:29
 */
public class UtilTest {
    /**
     * 文件名工具类测试
     */
    @Test
    public void demo1() {
        String[] groupNameAndRemoteFileName = FileNameUtil
                .getGroupNameAndRemoteFileName("http://192.168.0.188:8080/group1/M00/00/1E/wKgAslxBfzqAffAjAASaJQLeUNc070.jpg");
        for (String s : groupNameAndRemoteFileName) {
            System.out.println(s);
        }
    }

}
