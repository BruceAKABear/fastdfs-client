package pro.dengyi.test;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import pro.dengyi.fastdfs.config.FastdfsConfiguration;
import pro.dengyi.fastdfs.constantenum.CommonLength;
import pro.dengyi.fastdfs.constantenum.ControlCode;
import pro.dengyi.fastdfs.constantenum.SystemStatus;
import pro.dengyi.fastdfs.core.FastdfsTemplate;
import pro.dengyi.fastdfs.entity.BasicStorageInfo;
import pro.dengyi.fastdfs.entity.ReceiveData;
import pro.dengyi.fastdfs.entity.StorageGroupInfo;
import pro.dengyi.fastdfs.entity.StorageInfo;
import pro.dengyi.fastdfs.utils.FileNameUtil;
import pro.dengyi.fastdfs.utils.ProtocolUtil;
import pro.dengyi.fastdfs.utils.ResponseDataUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 协议测试类
 *
 * @author 邓艺
 * @version v1.0
 * @date 2018-12-26 14:42
 */
public class ProtocolTest {

    /**
     * 测试tracker获取所有存储组
     */
    @Test
    public void demo1() throws IOException {
        Socket socket = new Socket("192.168.0.178", 22122);
        OutputStream outputStream = socket.getOutputStream();
        byte[] protoHeader = ProtocolUtil.getProtoHeader((byte) 91, 0L, SystemStatus.SUCCESS.getValue());
        outputStream.write(protoHeader);
        //接受响应
        ReceiveData responseData = ProtocolUtil.getResponseData(socket.getInputStream(), (byte) 100, (long) -1);
        List<StorageGroupInfo> allStorageGroupInfo = ResponseDataUtil.getAllStorageGroupInfo(responseData.getBody());
        System.out.println("-----");
    }

    /**
     * 获取所有存储服务器信息
     *
     * @throws IOException
     */
    @Test
    public void demo2() throws IOException {
        Socket socket = new Socket("192.168.199.3", 22122);
        String groupName = "group1";
        String storageIpAddr = "192.168.199.4";
        byte[] protoHeader = null;
        byte[] wholeMessage = null;
        byte[] standardGroupNameByteArray = new byte[CommonLength.MAX_GROUPNAME_LENGTH.getLength()];
        byte[] groupNameBytes = groupName.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(groupNameBytes, 0, standardGroupNameByteArray, 0, groupNameBytes.length);
        if (StringUtils.isNotBlank(storageIpAddr)) {
            byte[] storageIpAddrBytes = storageIpAddr.getBytes(StandardCharsets.UTF_8);
            protoHeader = ProtocolUtil
                    .getProtoHeader(ControlCode.TRACKER_GET_ALL_STORAGEINFO.getValue(), (long) 16 + storageIpAddrBytes.length, SystemStatus.SUCCESS.getValue());
            wholeMessage = new byte[42];
            System.arraycopy(protoHeader, 0, wholeMessage, 0, 10);
            System.arraycopy(standardGroupNameByteArray, 0, wholeMessage, 10, 16);
            System.arraycopy(storageIpAddrBytes, 0, wholeMessage, 26, storageIpAddrBytes.length);

        } else {
            protoHeader = ProtocolUtil.getProtoHeader(ControlCode.TRACKER_GET_ALL_STORAGEINFO.getValue(), 16L, SystemStatus.SUCCESS.getValue());
            wholeMessage = new byte[26];
            System.arraycopy(protoHeader, 0, wholeMessage, 0, 10);
            System.arraycopy(standardGroupNameByteArray, 0, wholeMessage, 10, 16);
        }
        socket.getOutputStream().write(wholeMessage);
        ReceiveData responseData = ProtocolUtil.getResponseData(socket.getInputStream(), ControlCode.SERVER_RESPONSE.getValue(), (long) -1);
        socket.close();
        List<StorageInfo> allStorageInfo = ResponseDataUtil.getAllStorageInfo(responseData.getBody());
        System.out.println(allStorageInfo);
    }

    /**
     * 测试通过tracker获取所有的storage
     */
    @Test
    public void demo3() throws IOException {
        String groupName = "group1";
        byte[] bytes = groupName.getBytes(StandardCharsets.UTF_8);
        Socket socket = new Socket("61.153.187.80", 22122);
        OutputStream outputStream = socket.getOutputStream();
        byte[] protoHeader = ProtocolUtil.getProtoHeader((byte) 91, 0L, SystemStatus.SUCCESS.getValue());
        outputStream.write(protoHeader);
        //byte[] byteArray = new byte[16];
        //System.arraycopy(bytes, 0, byteArray, 0, bytes.length);
        //outputStream.write(byteArray);
        ReceiveData responseData = ProtocolUtil.getResponseData(socket.getInputStream(), (byte) 100, (long) -1);
        int i = responseData.getBody().length - 17;
        //body里面的数据为 组名+(ip+端口）*n
        System.out.println(i);
        int iii = 23;
        int i1 = i / iii;
        System.out.println(i1);
        //255.255.255.255 从16-31
        String groupName11 = new String(responseData.getBody(), 0, 16).trim();
        System.out.println(groupName11);
        String ipAddr = new String(responseData.getBody(), 16, 15).trim();
        Long aLong = ProtocolUtil.byteArray2Long(responseData.getBody(), 31);
        String ipAddr2 = new String(responseData.getBody(), 39, 15).trim();
        Long aLong2 = ProtocolUtil.byteArray2Long(responseData.getBody(), 54);
        System.out.println(ipAddr);
        System.out.println(aLong);
        System.out.println("----------");
        System.out.println(ipAddr2);
        System.out.println(aLong2);
        //TODO 为什么获取到的是group2 下的所有storage，为什么不是获取到group1
    }

    /**
     * 获取上传目的地服务器(单个)
     *
     * @throws IOException
     */
    @Test
    public void demo4() throws IOException {
        Socket socket = new Socket("61.153.187.80", 22122);
        OutputStream outputStream = socket.getOutputStream();
        //不指定groupname
        byte[] protoHeader = ProtocolUtil.getProtoHeader((byte) 101, 0L, SystemStatus.SUCCESS.getValue());
        socket.getOutputStream().write(protoHeader);
        ReceiveData responseData = ProtocolUtil.getResponseData(socket.getInputStream(), (byte) 100, (long) 40);
        socket.close();
        //返回的数据是 组名+ip+端口 =16+15+8=39
        BasicStorageInfo basicStorageInfo = ResponseDataUtil.putDataInToBasicStorageInfo(responseData.getBody(), 0, true);
    }

    /**
     * 查询下载服务器
     *
     * @throws IOException
     */
    @Test
    public void demo5() throws IOException {
        Socket socket = new Socket("61.153.187.80", 22122);
        OutputStream outputStream = socket.getOutputStream();
    }

    /**
     * 获取删除服务器并删除文件
     */
    @Test
    public void demo6() throws IOException {
        Socket socket = new Socket("192.168.0.178", 22122);
        String groupName = "group1";
        byte[] bytes1 = groupName.getBytes(StandardCharsets.UTF_8);
        String fid = "M00/00/1E/wKgAslxBfzqAffAjAASaJQLeUNc070.jpg";
        byte[] bytes = fid.getBytes(StandardCharsets.UTF_8);
        //数据头
        byte[] protoHeader = ProtocolUtil.getProtoHeader((byte) 103, (long) (16 + bytes.length), SystemStatus.SUCCESS.getValue());
        //数据体
        byte[] whole = new byte[26 + bytes.length];
        System.arraycopy(protoHeader, 0, whole, 0, 10);
        byte[] standard = new byte[16];
        System.arraycopy(bytes1, 0, standard, 0, bytes1.length);
        System.arraycopy(standard, 0, whole, 10, 16);
        System.arraycopy(bytes, 0, whole, 26, bytes.length);
        socket.getOutputStream().write(whole);
        ReceiveData responseData = ProtocolUtil.getResponseData(socket.getInputStream(), (byte) 100, (long) -1);
        BasicStorageInfo basicStorageInfo = ResponseDataUtil.putDataInToBasicStorageInfo(responseData.getBody(), 0, false);
        socket.close();
        //TODO 真正删除开始
        Socket storageSocket = new Socket(basicStorageInfo.getIp(), Math.toIntExact(basicStorageInfo.getPort()));
        byte[] protoHeader1 = ProtocolUtil.getProtoHeader((byte) 12, (long) (16 + bytes.length), SystemStatus.SUCCESS.getValue());
        //数据体
        byte[] whole1 = new byte[26 + bytes.length];
        System.arraycopy(protoHeader1, 0, whole1, 0, 10);
        byte[] standard1 = new byte[16];
        System.arraycopy(bytes1, 0, standard1, 0, bytes1.length);
        System.arraycopy(standard1, 0, whole1, 10, 16);
        System.arraycopy(bytes, 0, whole1, 26, bytes.length);
        storageSocket.getOutputStream().write(whole1);
        ReceiveData responseData1 = ProtocolUtil.getResponseData(storageSocket.getInputStream(), (byte) 100, (long) 0);
        storageSocket.close();
        System.out.println(responseData1.getErrorNo());
    }

    /**
     * 测试上传
     */
    @Test
    public void demo7() throws IOException {
        /**
         * 几个问题：
         * 1.将文件上传至指定group是否有必要，如果将文件上传至指定group破坏了负载均衡如果是集群必然会导致有的group中不能有group
         * 2.metadata是否有上传的必要，就接触的以及可能会接触的，metadata不存在用途
         */
        //1.获取上传storage
        Socket trackerSocket = new Socket("192.168.199.2", 22122);
        byte[] protoHeader = ProtocolUtil.getProtoHeader((byte) 101, (long) 0, SystemStatus.SUCCESS.getValue());
        trackerSocket.getOutputStream().write(protoHeader);
        ReceiveData responseData = ProtocolUtil.getResponseData(trackerSocket.getInputStream(), (byte) 100, (long) 40);
        BasicStorageInfo basicStorageInfo = ResponseDataUtil.putDataInToBasicStorageInfo(responseData.getBody(), 0, false);
        //上传
        byte[] bytes = new byte[522 * 1024];
        File file = new File("C:\\Users\\dengyi\\Desktop\\1111.jpg");
        FileInputStream fileInputStream = new FileInputStream(file);
        fileInputStream.read(bytes);
        Socket storageSocket = new Socket(basicStorageInfo.getIp(), Math.toIntExact(basicStorageInfo.getPort()));
        //---------
        byte[] protoHeader1 = ProtocolUtil.getProtoHeader((byte) 11, (long) 522 * 1024 + 15, SystemStatus.SUCCESS.getValue());

        //组装报文内容
        byte[] wholePackage = new byte[25];
        System.arraycopy(protoHeader1, 0, wholePackage, 0, 10);
        //ti
        byte[] bytes2 = ProtocolUtil.long2ByteArray((long) 522 * 1024);
        byte[] dataBody = new byte[9];
        dataBody[0] = 0;
        System.arraycopy(bytes2, 0, dataBody, 1, 8);
        System.arraycopy(dataBody, 0, wholePackage, 10, 9);
        String extNameWithOutDot = FileNameUtil.getExtNameWithDot("1111.jpg").substring(1);
        byte[] standardExtNameByteArray = new byte[6];
        byte[] bytes3 = extNameWithOutDot.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(bytes3, 0, standardExtNameByteArray, 0, bytes3.length);
        //
        System.arraycopy(standardExtNameByteArray, 0, wholePackage, 19, 6);
        storageSocket.getOutputStream().write(wholePackage);
        storageSocket.getOutputStream().write(bytes);
        ReceiveData responseData1 = ProtocolUtil.getResponseData(storageSocket.getInputStream(), (byte) 100, (long) -1);
        //49 6+43
        String groupName = new String(responseData1.getBody(), 0, 16).trim();
        String remoteFilename = new String(responseData1.getBody(), 16, responseData1.getBody().length - 16);

        System.out.println("11111");

    }

    /**
     * 获取从文件存储服务器（随机）
     */
     @Test
     public void demo8() throws IOException {
         //1. 设置参数(必须参数为tracker地址)
         FastdfsConfiguration fastdfsConfiguration = new FastdfsConfiguration();
         fastdfsConfiguration.setTrackers(new String[]{"192.168.199.2:22122", "192.168.199.3:22122"});
         //创建模板对象
         FastdfsTemplate fastdfsTemplate = new FastdfsTemplate(fastdfsConfiguration);
         BasicStorageInfo uploadMaterAndSlaveFileStorage = fastdfsTemplate.getUploadSlaveFileStorage(fastdfsConfiguration, "group1", "M00/00/00/wKjHBVxLnpiANuy-AAgoAP6sYQ8896.jpg");
         System.out.println(uploadMaterAndSlaveFileStorage.getIp());
     }

}
