package pro.dengyi.test;

import org.apache.commons.collections4.CollectionUtils;
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
        fastdfsConfiguration.setTrackers(new String[]{"192.168.199.2:22122", "192.168.199.3:22122"});
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
        List<StorageInfo> allStorageInfo = fastdfsTemplate.getAllStorageInfo(groupName);
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
        FileInputStream fileInputStream = new FileInputStream(new File("C:\\Users\\dengyi\\Desktop\\112.jpg"));
        byte[] fileBytes = new byte[294 * 1024];
        fileInputStream.read(fileBytes);
        //1. 设置参数(必须参数为tracker地址)
        FastdfsConfiguration fastdfsConfiguration = new FastdfsConfiguration();
        fastdfsConfiguration.setTrackers(new String[]{"61.153.187.80:22122"});
        //创建模板对象
        FastdfsTemplate fastdfsTemplate = new FastdfsTemplate(fastdfsConfiguration);
        String s = fastdfsTemplate.uploadFile(fileBytes, "112.jpg");
        System.out.println(s);

    }

    @Test
    public void demo4() {

    }

}
