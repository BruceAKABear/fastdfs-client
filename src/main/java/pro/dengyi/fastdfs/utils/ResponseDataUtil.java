package pro.dengyi.fastdfs.utils;

import pro.dengyi.fastdfs.constantenum.CommonLength;
import pro.dengyi.fastdfs.entity.BasicStorageInfo;
import pro.dengyi.fastdfs.entity.StorageGroupInfo;
import pro.dengyi.fastdfs.entity.StorageInfo;
import pro.dengyi.fastdfs.entity.UploadedFileInfo;
import pro.dengyi.fastdfs.exception.FastdfsException;

import java.util.ArrayList;
import java.util.List;

/**
 * 响应数据工具类，主要用途将tracker响应的数据封装到相应的实体类中
 *
 * @author 邓艺
 * @version v1.0
 * @date 2019-01-17 15:24
 */
public class ResponseDataUtil {
    /**
     * 将服务器返回的数据将存储服务器组信息封装
     *
     * @param bytes 服务器返回数据
     * @return list 存储服务器组信息集合
     * @author 邓艺
     * @date 2019/1/17 16:01
     */
    public static List<StorageGroupInfo> getAllStorageGroupInfo(byte[] bytes) throws FastdfsException {
        //所有数据的长度
        int allBytesLength = bytes.length;
        //获取一整个group信息的长度为105个字节
        int singleLength = 105;
        if (allBytesLength % singleLength != 0) {
            throw new FastdfsException("数据长度非法");
        }
        List<StorageGroupInfo> list = new ArrayList<>();
        //组的个数
        int groupNumber = allBytesLength / singleLength;
        //起始offset;
        int startOffset = 0;
        for (int i = 0; i < groupNumber; i++) {
            StorageGroupInfo storageGroupInfo = putDataInToStorageGroupInfo(bytes, startOffset);
            list.add(storageGroupInfo);
            startOffset += singleLength;
        }
        return list;
    }

    /**
     * 获取所有存储服务器信息集合
     *
     * @param bytes
     * @return List
     * @author 邓艺
     * @date 2019/1/18 21:32
     */
    public static List<StorageInfo> getAllStorageInfo(byte[] bytes) throws FastdfsException {
        //所有数据的长度
        int allBytesLength = bytes.length;
        //获取一整个group信息的长度为105个字节
        int singleLength = 612;
        if (allBytesLength % singleLength != 0) {
            throw new FastdfsException("数据长度非法");
        }
        List<StorageInfo> list = new ArrayList<>();
        //组的个数
        int groupNumber = allBytesLength / singleLength;
        //起始offset;
        int startOffset = 0;
        for (int i = 0; i < groupNumber; i++) {
            StorageInfo storageInfo = putDataInToStorageInfo(bytes, startOffset);
            list.add(storageInfo);
            startOffset += singleLength;
        }
        return list;
    }

    /**
     * 根据便宜量封装数据进存储组信息实体类
     *
     * @param bytes
     * @param startOffSet
     * @return pro.dengyi.fastdfs.entity.StorageGroupInfo
     * @author 邓艺
     * @date 2019/1/18 9:47
     */
    private static StorageGroupInfo putDataInToStorageGroupInfo(byte[] bytes, Integer startOffSet) {
        int innerOffSet = 0;
        StorageGroupInfo storageGroupInfo = new StorageGroupInfo();
        //组名,为什么读取17个字节，应该是有一位作为状态字节但是没有使用
        storageGroupInfo.setGroupName(new String(bytes, startOffSet + innerOffSet, 17).trim());
        innerOffSet += 17;
        storageGroupInfo.setTotalMB(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageGroupInfo.setFreeMB(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageGroupInfo.setTrunkFreeMB(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageGroupInfo.setStorageCount(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageGroupInfo.setStoragePort(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageGroupInfo.setStorageHttpPort(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageGroupInfo.setActiveCount(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageGroupInfo.setCurrentWriteServer(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageGroupInfo.setStorePathCount(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageGroupInfo.setSubdirCountPerPath(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageGroupInfo.setCurrentTrunkFileId(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        return storageGroupInfo;
    }

    /**
     * 封装单个存储服务器信息工具类
     *
     * @param bytes
     * @param startOffSet
     * @return pro.dengyi.fastdfs.entity.StorageInfo
     * @author 邓艺
     * @date 2019/1/18 11:01
     */
    public static StorageInfo putDataInToStorageInfo(byte[] bytes, Integer startOffSet) {
        int innerOffSet = 0;
        StorageInfo storageInfo = new StorageInfo();
        storageInfo.setStatus((byte) 1);
        innerOffSet += 1;
        storageInfo.setId(new String(bytes, startOffSet + innerOffSet, CommonLength.STORAGE_ID_LENGTH.getLength()));
        innerOffSet += CommonLength.STORAGE_ID_LENGTH.getLength();
        storageInfo.setIpAddr(new String(bytes, startOffSet + innerOffSet, 16));
        innerOffSet += 16;
        storageInfo.setDomainName(new String(bytes, startOffSet + innerOffSet, 128));
        innerOffSet += 128;
        storageInfo.setSrcIpAddr(new String(bytes, startOffSet + innerOffSet, 16));
        innerOffSet += 16;
        storageInfo.setVersion(new String(bytes, startOffSet + innerOffSet, 6));
        innerOffSet += 6;
        storageInfo.setTotalMB(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setFreeMB(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setUploadPriority(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setJoinTime(ProtocolUtil.byteArray2Date(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setUpTime(ProtocolUtil.byteArray2Date(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setStorePathCount(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setSubdirCountPerPath(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setStoragePort(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setStorageHttpPort(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setCurrentWritePath(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        //TODO 差一个
        innerOffSet += 4;
        innerOffSet += 4;
        innerOffSet += 4;
        storageInfo.setTotalUploadCount(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setSuccessUploadCount(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setTotalAppendCount(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setSuccessAppendCount(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setTotalModifyCount(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setSuccessModifyCount(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setTotalTruncateCount(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setSuccessTruncateCount(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setTotalSetMetaCount(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setSuccessSetMetaCount(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setTotalDeleteCount(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setSuccessDeleteCount(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setTotalDownloadCount(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setSuccessDownloadCount(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setTotalGetMetaCount(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setSuccessGetMetaCount(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setTotalCreateLinkCount(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setSuccessCreateLinkCount(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setTotalDeleteLinkCount(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setSuccessDeleteLinkCount(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setTotalUploadBytes(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setSuccessUploadBytes(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setTotalAppendBytes(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setSuccessAppendBytes(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setTotalModifyBytes(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setSuccessModifyBytes(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setTotalDownloadloadBytes(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setSuccessDownloadloadBytes(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setTotalSyncInBytes(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setSuccessSyncInBytes(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setTotalSyncOutBytes(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setSuccessSyncOutBytes(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setTotalFileOpenCount(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setSuccessFileOpenCount(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setTotalFileReadCount(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setSuccessFileReadCount(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setTotalFileWriteCount(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setSuccessFileWriteCount(ProtocolUtil.byteArray2Long(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setLastSourceUpdate(ProtocolUtil.byteArray2Date(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setLastSyncUpdate(ProtocolUtil.byteArray2Date(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setLastSyncedTimestamp(ProtocolUtil.byteArray2Date(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setLastHeartBeatTime(ProtocolUtil.byteArray2Date(bytes, startOffSet + innerOffSet));
        innerOffSet += 8;
        storageInfo.setIfTrunkServer(false);
        innerOffSet += 1;
        return storageInfo;
    }

    /**
     * 将tracker返回的数据封装到基本存储服务器实体类中
     *
     * @param bytes 数据
     * @param offSet 偏移量
     * @param needStorePath 是否需要封装storepath
     * @return pro.dengyi.fastdfs.entity.BasicStorageInfo
     * @author 邓艺
     * @date 2019/1/18 13:38
     */
    public static BasicStorageInfo putDataInToBasicStorageInfo(byte[] bytes, Integer offSet, Boolean needStorePath) {
        int innerOffSet = 0;
        BasicStorageInfo basicStorageInfo = new BasicStorageInfo();
        basicStorageInfo.setGroupName(new String(bytes, offSet + innerOffSet, CommonLength.GROUP_NAME_MAX_LENGTH.getLength()).trim());
        innerOffSet += CommonLength.GROUP_NAME_MAX_LENGTH.getLength();
        basicStorageInfo.setIp(new String(bytes, offSet + innerOffSet, 15).trim());
        innerOffSet += 15;
        basicStorageInfo.setPort(ProtocolUtil.byteArray2Long(bytes, offSet + innerOffSet));
        if (needStorePath) {
            basicStorageInfo.setStorePath(bytes[offSet + 31]);
        }
        return basicStorageInfo;
    }

    /**
     * 将返回的数据封装进文件上传信息封装实体
     *
     * @param bytes
     * @param offSet
     * @return UploadedFileInfo
     * @author 邓艺
     * @date 2019/1/22 9:49
     */
    public static UploadedFileInfo putDataInToUploadedFileInfo(byte[] bytes, Integer offSet) {
        UploadedFileInfo uploadedFileInfo = new UploadedFileInfo();
        uploadedFileInfo.setGroupName(new String(bytes, 0, CommonLength.GROUP_NAME_MAX_LENGTH.getLength()).trim());
        uploadedFileInfo.setRemoteFileName(new String(bytes, 16, bytes.length - 16).trim());
        return uploadedFileInfo;
    }

}
