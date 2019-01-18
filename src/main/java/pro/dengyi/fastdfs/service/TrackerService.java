package pro.dengyi.fastdfs.service;

import pro.dengyi.fastdfs.entity.StorageGroupInfo;
import pro.dengyi.fastdfs.entity.StorageInfo;

import java.util.List;

/**
 * tracker服务器服务类接口
 *
 * @author 邓艺
 * @version v1.0
 * @date 2019-01-18 12:48
 */
public interface TrackerService {

    List<StorageGroupInfo> queryAllStorageGroupInfo();

    List<StorageInfo> queryAllStorageInfo();
}
