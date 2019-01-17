package pro.dengyi.fastdfs.utils;

import pro.dengyi.fastdfs.entity.StorageGroupInfo;
import pro.dengyi.fastdfs.exception.FastdfsException;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据工具类，主要将数据解析成数据
 *
 * @author 邓艺
 * @version v1.0
 * @date 2019-01-17 15:24
 */
public class DataUtil {
    /**
     * 将服务器返回的数据将存储服务器组信息封装
     *
     * @param bytes 服务器返回数据
     * @return list 存储服务器组信息集合
     * @author 邓艺
     * @date 2019/1/17 16:01
     */
    //00101 000111 00002202 0101 010033 20010000
    public static List<StorageGroupInfo> getAllStorageGroupInfo(byte[] bytes) throws FastdfsException {
        //所有数据的长度
        int allBytesLength = bytes.length;
        //获取一个组信息的长度
        int singleLength = 10;
        if (allBytesLength % singleLength != 0) {
            throw new FastdfsException("数据长度非法");
        }
        List<StorageGroupInfo> list = new ArrayList<>();
        //组的个数
        int groupNumber = allBytesLength / singleLength;
        //起始offset;
        int startOffset = 0;
        for (int i = 0; i < groupNumber; i++) {
            StorageGroupInfo storageGroupInfo = putDataInToGroupInfo(bytes, startOffset);
            list.add(storageGroupInfo);
            startOffset += singleLength;

        }
        return list;
    }

    public static StorageGroupInfo putDataInToGroupInfo(byte[] bytes, Integer startOffSet) {
        int innerOffSet = 0;
        StorageGroupInfo storageGroupInfo = new StorageGroupInfo();
        storageGroupInfo.setGroupName(new String(bytes, startOffSet + innerOffSet, 17));
        innerOffSet += 17;
        storageGroupInfo.setTotalMB(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        return null;
    }

}
