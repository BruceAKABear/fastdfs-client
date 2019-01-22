package pro.dengyi.fastdfs.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import pro.dengyi.fastdfs.core.Tracker;
import pro.dengyi.fastdfs.entity.BasicStorageInfo;
import pro.dengyi.fastdfs.utils.FileNameUtil;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

/**
 * fastdfs操作模板类
 *
 * @author 邓艺
 * @version v1.0
 * @date 2019-01-20 17:29
 */
@Slf4j
public class FastdfsTemplate extends Tracker {

    /**
     * 以字节数组的形式上传文件
     *
     * @param fileBytes 文件字节数组
     * @return java.lang.String 文件访问地址
     * @author 邓艺
     * @date 2019/1/22 15:39
     */
    public String uploadFile(byte[] fileBytes) {
        return doUploadFile(fileBytes, null, null, null, null);
    }

    /**
     * 以字节数组的形式上传文件,带metadata
     *
     * @param fileBytes 文件字节数组
     * @param metadata metadata
     * @return java.lang.String
     * @author 邓艺
     * @date 2019/1/22 15:53
     */
    public String uploadFile(byte[] fileBytes, List<Object> metadata) {
        return doUploadFile(fileBytes, null, null, null, metadata);
    }

    /**
     * 执行上传文件方法
     *
     * @param fileBytes 文件字节数组
     * @param groupName 组名
     * @param masterFileName 父文件名
     * @param fileNameSuffix 子文件名后缀
     * @param metadata metadata
     * @return java.lang.String
     * @author 邓艺
     * @date 2019/1/22 16:02
     */
    public String doUploadFile(byte[] fileBytes, String groupName, String masterFileName, String fileNameSuffix, List<Object> metadata) {
        try {
            //获取上传文件目的地storage
            BasicStorageInfo uploadStorage = getUploadStorage();
            Socket storageSocket = new Socket(uploadStorage.getIp(), Math.toIntExact(uploadStorage.getPort()));
            //1. 判断上传文件类型 普通上传或者是上传主从文件
            if (StringUtils.isNotBlank(masterFileName) && StringUtils.isNotBlank(fileNameSuffix)) {
                //上传主从文件
                if (CollectionUtils.isNotEmpty(metadata)) {
                    //需要上传metadata

                } else {

                }

            } else {
                //上传普通文件
                if (CollectionUtils.isNotEmpty(metadata)) {
                    //需要上传metadata

                } else {
                    //不需要上传metadata

                }

            }
        } catch (IOException e) {
            log.error("创建storagesocket时异常");
        }

        return null;
    }

    public String uploadAppenderFile() {
        return null;
    }

    public Boolean uploadMetadata() {
        return false;
    }

    /**
     * 删除文件
     *
     * @param fileUrl 文件url(如：http://xxx:xx/xxxxx.xx)
     * @return java.lang.Boolean true表示删除成功，false表示删除失败
     * @author 邓艺
     * @date 2019/1/22 11:21
     */
    public Boolean deleteFile(String fileUrl) {
        String[] groupNameAndRemoteFileName = FileNameUtil.getGroupNameAndRemoteFileName(fileUrl);
        return deleteFile(groupNameAndRemoteFileName[0], groupNameAndRemoteFileName[1]);
    }

    public byte[] downloadFile() {
        return null;
    }

}
