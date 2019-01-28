package pro.dengyi.fastdfs.threads;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

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
    private List<String> metadata;
    /**
     * 存储服务器的socket连接
     */
    private Socket storageScoket;

    public UploadMetadataThread(String groupName, String remoteFileName, List<String> metadata, Socket storageScoket) {
        this.groupName = groupName;
        this.remoteFileName = remoteFileName;
        this.metadata = metadata;
        this.storageScoket = storageScoket;
    }

    @Override
    public void run() {
        if (CollectionUtils.isNotEmpty(metadata)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(metadata.get(0));
            for (int i = 1; i < metadata.size(); i++) {

            }
        }

    }
}
