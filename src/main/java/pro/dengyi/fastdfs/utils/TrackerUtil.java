package pro.dengyi.fastdfs.utils;

import org.apache.commons.lang3.ArrayUtils;
import pro.dengyi.fastdfs.exception.FastdfsException;

import java.util.Random;
import java.util.TreeSet;

/**
 * tracker特有的工具类
 *
 * @author 邓艺
 * @version v1.0
 * @date 2019-01-17 17:56
 */
public class TrackerUtil {

    /**
     * 随机获取tracker服务器ip和端口
     *
     * @param trackers tracker数组
     * @param treeSet
     * @return String[] 单个ip和端口字符串数组
     * @author 邓艺
     * @date 2019/1/17 20:31
     */
    public static void getRandomTrackerIpAndPort(String[] trackers, TreeSet<String> treeSet) throws FastdfsException {
        if (ArrayUtils.isNotEmpty(trackers)) {
            int trackerNumber = trackers.length;
            Random r = new Random();
            String tracker = trackers[r.nextInt(trackerNumber + 1)];
            treeSet.add(tracker);
        } else {
            throw new FastdfsException("tracker集合为空异常");
        }

    }

}
