package pro.dengyi.fastdfs.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import pro.dengyi.fastdfs.exception.FastdfsException;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * tracker特有的工具类
 *
 * @author 邓艺
 * @version v1.0
 * @date 2019-01-17 17:56
 */
@Slf4j
public class TrackerUtil {

    /**
     * 随机获取tracker服务器ip和端口
     * <br/>
     * 连接重试次数为tracker数量
     *
     * @param trackers tracker数组
     * @return String[] 单个ip和端口字符串数组
     * @author 邓艺
     * @date 2019/1/17 20:31
     */
    public static Socket getTrackerSocket(String[] trackers) throws FastdfsException {
        if (ArrayUtils.isNotEmpty(trackers)) {
            int trackerNumber = trackers.length;
            for (int i = 0; i < trackerNumber; i++) {
                String tracker = null;
                //如果只有1个tracker不自动生成
                if (trackerNumber == 1) {
                    tracker = trackers[0];
                } else {
                    //如果tracker为组，那么自动生成
                    Random r = new Random();
                    tracker = trackers[r.nextInt(trackerNumber)];
                }
                String[] ipAndPort = IpAddressUtil.getIpAndPort(tracker);
                try {
                    return new Socket(ipAndPort[0], Integer.parseInt(ipAndPort[1]));
                } catch (IOException e) {
                    log.error("连接tracker时异常" + e.getMessage());
                }
            }
            return null;
        } else {
            throw new FastdfsException("tracker集合为空异常");
        }
    }

    /**
     * 获取所有tracker服务器的socket集合
     *
     * @param trackers tracker数组
     * @return list tracker的socket集合
     * @author 邓艺
     * @date 2019/1/17 20:31
     */
    public static List<Socket> getAllTrackerSocket(String[] trackers) throws FastdfsException {
        if (ArrayUtils.isNotEmpty(trackers)) {
            List<Socket> list = new ArrayList<>();
            for (String tracker : trackers) {
                String[] ipAndPort = IpAddressUtil.getIpAndPort(tracker);
                try {
                    Socket socket = new Socket(ipAndPort[0], Integer.parseInt(ipAndPort[1]));
                    list.add(socket);
                } catch (IOException e) {
                    log.error("创建tracker的socket连接时异常,请检查tracker:" + ipAndPort[0] + "的状态");
                }
            }
            return list;
        } else {
            throw new FastdfsException("tracker集合为空异常");
        }
    }
}
