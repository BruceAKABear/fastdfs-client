package pro.dengyi.fastdfs.threads;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.net.Socket;
import java.util.List;

/**
 * 上传metadata线程
 *
 * @author 邓艺
 * @version v1.0
 * @date 2019-01-25 21:56
 */
@Data
@Slf4j
public class UploadMetadataThread implements Runnable {
    private String groupName;
    private String remoteFileName;
    private List<Object> metadata;
    /**
     * 存储服务器的socket连接
     */
    private Socket storageScoket;

    public UploadMetadataThread(String groupName, String remoteFileName, List<Object> metadata, Socket storageScoket) {
        this.groupName = groupName;
        this.remoteFileName = remoteFileName;
        this.metadata = metadata;
        this.storageScoket = storageScoket;
    }

    @Override
    public void run() {

    }
}
