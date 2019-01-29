package pro.dengyi.test;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import pro.dengyi.fastdfs.config.FastdfsConfiguration;
import pro.dengyi.fastdfs.core.FastdfsTemplate;
import pro.dengyi.fastdfs.entity.StorageGroupInfo;
import pro.dengyi.fastdfs.entity.StorageInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author 邓艺
 * @version v1.0
 * @date 2019-01-14 20:44
 */
public class UseTest {

    /**
     * 获取存储组所有信息
     */
    @Test
    public void demo1() {
        //1. 设置参数(必须参数为tracker地址)
        FastdfsConfiguration fastdfsConfiguration = new FastdfsConfiguration();
        fastdfsConfiguration.setTrackers(new String[]{"61.153.187.80:22122"});
        //创建模板对象
        FastdfsTemplate fastdfsTemplate = new FastdfsTemplate(fastdfsConfiguration);
        //进行操作
        List<StorageGroupInfo> allStorageGroupInfo = fastdfsTemplate.getAllStorageGroupInfo();
        if (CollectionUtils.isNotEmpty(allStorageGroupInfo)) {
            for (StorageGroupInfo storageGroupInfo : allStorageGroupInfo) {
                System.out.println(storageGroupInfo);
            }
        }
    }

    /**
     * 获取存组下所有服务器信息
     */
    @Test
    public void demo2() throws IOException {
        //1. 设置参数(必须参数为tracker地址)
        FastdfsConfiguration fastdfsConfiguration = new FastdfsConfiguration();
        fastdfsConfiguration.setTrackers(new String[]{"192.168.199.2:22122", "192.168.199.3:22122"});
        //创建模板对象
        FastdfsTemplate fastdfsTemplate = new FastdfsTemplate(fastdfsConfiguration);
        //进行操作
        String groupName = "group1";
        //        String ipaddr = "192.168.199.7";
        List<StorageInfo> allStorageInfo = fastdfsTemplate.doGetAllStorageInfo(groupName);
        if (CollectionUtils.isNotEmpty(allStorageInfo)) {
            for (StorageInfo storageInfo : allStorageInfo) {
                System.out.println(storageInfo);
            }
        }

    }

    /**
     * 文件上传字节数组的方式
     */
    @Test
    public void demo3() throws IOException {
        File file = new File("C:\\Users\\dengyi\\Desktop\\1111.jpg");
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] fileBytes = new byte[(int) file.length()];
        fileInputStream.read(fileBytes);
        //1. 设置参数(必须参数为tracker地址)
        FastdfsConfiguration fastdfsConfiguration = new FastdfsConfiguration();
        fastdfsConfiguration.setTrackers(new String[]{"61.153.187.80:22122"});
        //创建模板对象
        FastdfsTemplate fastdfsTemplate = new FastdfsTemplate(fastdfsConfiguration);
        fastdfsTemplate.uploadFile(fileBytes, file.getName());

    }

    /**
     * 字节数据的形式下载文件
     */
    @Test
    public void demo4() throws IOException {
        FastdfsConfiguration fastdfsConfiguration = new FastdfsConfiguration();
        fastdfsConfiguration.setTrackers(new String[]{"61.153.187.80:22122"});
        //创建模板对象
        FastdfsTemplate fastdfsTemplate = new FastdfsTemplate(fastdfsConfiguration);
        byte[] bytes = fastdfsTemplate.downloadFile("http://61.153.187.80:9000/group1/M00/00/1D/wKgBDFxNqL-AJMAxAAgq1ZfG2-8804.jpg");
        fastdfsTemplate.uploadFile(bytes, "2222.jpg");

    }

    /**
     * 测试上传缩略图
     */
    @Test
    public void demo5() throws IOException {
        File file = new File("C:\\Users\\dengyi\\Desktop\\112.jpg");
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] fileBytes = new byte[(int) file.length()];
        fileInputStream.read(fileBytes);
        FastdfsConfiguration fastdfsConfiguration = new FastdfsConfiguration();
        fastdfsConfiguration.setTrackers(new String[]{"192.168.0.178:22122"});
        fastdfsConfiguration.setOpenThumbnail(true);
        fastdfsConfiguration.setAccessPort(9000);
        fastdfsConfiguration.setThumbnailWidth(100);
        fastdfsConfiguration.setThumbnailHeight(200);
        //创建模板对象
        FastdfsTemplate fastdfsTemplate = new FastdfsTemplate(fastdfsConfiguration);
        String[] uploadFile = fastdfsTemplate.uploadFile(fileBytes, file.getName());
        if (ArrayUtils.isNotEmpty(uploadFile)) {
            for (String s : uploadFile) {
                System.out.println(s);
            }
        }

    }

}
