package pro.dengyi.fastdfs.connection;

import org.apache.commons.lang3.ArrayUtils;
import pro.dengyi.fastdfs.config.FastdfsProterties;
import pro.dengyi.fastdfs.exception.FastdfsException;
import pro.dengyi.fastdfs.utils.ProtocolUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.TreeSet;

/**
 * 连接类
 *
 * @author 邓艺
 * @version v1.0
 * @date 2019-01-08 16:45
 */
public class Connection {

    /**
     * 发送基础数据包方法
     *
     * @param controlCode 控制码
     * @param groupName 组名
     * @param remoteFileName 远程文件名
     * @return void
     * @author 邓艺
     * @date 2019/1/11 10:52
     */
    public static void sendPackage(byte controlCode, String groupName, String remoteFileName, Socket socket) throws IOException {
        //获取远程文件名字节数组
        byte[] remoteFileNameBytes = remoteFileName.getBytes(StandardCharsets.UTF_8);
        //产生报文头16为组名标准长度
        byte[] headerBytes = ProtocolUtil.getProtoHeader(controlCode, (long) (16 + remoteFileNameBytes.length), (byte) 0);
        //决定封装组名的数据及长度
        byte[] originalBytes = groupName.getBytes(StandardCharsets.UTF_8);
        byte[] groupNameBytes = new byte[16];
        if (ArrayUtils.getLength(originalBytes) <= 16) {
            System.arraycopy(originalBytes, 0, groupNameBytes, 0, originalBytes.length);
        } else {
            System.arraycopy(originalBytes, 0, groupNameBytes, 0, 16);
        }
        //完整数据包拼接,26为报文头的长度加上组名字节数组的长度
        byte[] wholePackage = new byte[26 + remoteFileNameBytes.length];
        System.arraycopy(headerBytes, 0, wholePackage, 0, 10);
        System.arraycopy(groupNameBytes, 0, wholePackage, 10, 16);
        System.arraycopy(remoteFileNameBytes, 0, wholePackage, 26, remoteFileNameBytes.length);
        //发送包
        socket.getOutputStream().write(wholePackage);
    }

    //TODO 确定重试次数
    public Socket getTrackerConnection() {
        FastdfsProterties fastdfsProterties = new FastdfsProterties();
        TreeSet<String> treeSet = new TreeSet<>();
        String[] randomTrackerIpAndPort = null;
        InetSocketAddress inetSocketAddress = new InetSocketAddress(randomTrackerIpAndPort[0], Integer.parseInt(randomTrackerIpAndPort[1]));
        Socket socket = new Socket();
        try {
            socket.connect(inetSocketAddress, 10);
            return socket;
        } catch (IOException e) {
            throw new FastdfsException("连接tracker" + randomTrackerIpAndPort[0] + "时异常，请检查服务器");
        }

    }
}
