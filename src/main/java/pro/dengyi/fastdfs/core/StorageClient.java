package pro.dengyi.fastdfs.core;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import pro.dengyi.fastdfs.connection.Connection;
import pro.dengyi.fastdfs.connection.ConnectionFactory;
import pro.dengyi.fastdfs.constantenum.ControlCode;
import pro.dengyi.fastdfs.utils.ProtocolUtil;

import java.io.*;
import java.util.ArrayList;

/**
 * 存储服务器核心类
 *
 * @author 邓艺
 * @version v1.0
 * @date 2018-12-25 16:15
 */
@Slf4j
public class StorageClient {

    /**
     * 文件删除方法
     *
     * @param groupName 组名
     * @param remoteFileName 远程文件名
     * @return java.lang.Boolean
     * @author 邓艺
     * @date 2019/1/10 16:25
     */
    public Boolean deleteFile(String groupName, String remoteFileName) {
        Connection connection = ConnectionFactory.getConnection();
        return null;
    }

    /**
     * 标准文件上传类
     *
     * @param fileInputStream 文件输入流
     * @param extName 扩展名
     * @param fileSize 文件大小
     * @param metadata 元数据
     * @return java.lang.String[]
     * @author 邓艺
     * @date 2019/1/11 16:00
     */
    private String[] doUploadFile(InputStream fileInputStream, String extName, Long fileSize, ArrayList<Object> metadata) {
        //获取连接
        Connection connection = ConnectionFactory.getConnection();
        //产生报文头部字节数组
        //TODO 为什么是15？
        byte[] protoHeaderBytes = ProtocolUtil.generateProtoHeader(ControlCode.UPLOAD.getValue(), 15 + fileSize, (byte) 0);
        //整体包长度
        byte[] wholePackage = new byte[25];

        OutputStream outPutStream = new ByteArrayOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outPutStream, 1024);
        //将包写出
        return null;
    }

}
