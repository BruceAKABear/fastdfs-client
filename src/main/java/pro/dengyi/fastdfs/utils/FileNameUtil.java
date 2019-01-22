package pro.dengyi.fastdfs.utils;

import com.sun.istack.internal.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import pro.dengyi.fastdfs.exception.FastdfsException;

/**
 * 文件名相关工具类
 *
 * @author 邓艺
 * @version v1.0
 * @date 2019-01-04 15:26
 */
@Slf4j
public class FileNameUtil {

    /**
     * 生成从文件的文件名
     *
     * @param masterFilename 主文件名
     * @param sufixName 文件名后缀，用于连接到主文件的后面
     * @param extName 扩展名 可以为null 如果为空将采用主文件相同扩展名
     * @return java.lang.String
     * @author 邓艺
     * @date 2019/1/7 14:13
     */
    public static String generateSlaveFilename(@NotNull String masterFilename, @NotNull String sufixName, String extName) throws FastdfsException {
        //文件名最小为34，md5 32位+.+ 一位扩展名
        if (masterFilename.length() < 34) {
            throw new FastdfsException("主文件名" + masterFilename + "长度非法");
        }
        //扩展名生成
        if (StringUtils.isBlank(extName)) {
            extName = getExtNameWithDot(masterFilename);
        }
        if (StringUtils.isNotBlank(sufixName)) {
            if (sufixName.equals("-m")) {
                throw new FastdfsException("后缀名" + sufixName + "非法,不能包含-m");
            }
        } else {
            throw new FastdfsException("后缀名非法，后缀名不能为空字符串");
        }
        if (extName.indexOf(".") == 0) {
            if (extName.lastIndexOf(".") == 0) {
                return masterFilename.substring(0, masterFilename.indexOf(".")) + sufixName + extName;
            } else {
                throw new FastdfsException("扩展名" + extName + "非法");
            }
        } else {
            throw new FastdfsException("扩展名" + extName + "非法");
        }
    }

    /**
     * 通过文件名获取带点的扩展名
     *
     * @param fileName 文件名 不能为空
     * @return java.lang.String
     * @author 邓艺
     * @date 2019/1/7 14:36
     */
    public static String getExtNameWithDot(@NotNull String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 从文件url中提取出组名和远程文件名
     *
     * @param fileUrl 文件url
     * @return java.lang.String[] [0]为组名，[1]为远程文件名
     * @author 邓艺
     * @date 2019/1/10 13:24
     */
    public static String[] getGroupNameAndRemoteFileName(@NotNull String fileUrl) throws FastdfsException {
        if (StringUtils.isBlank(fileUrl)) {
            throw new FastdfsException("文件url不能为空");
        }
        String[] groupNameAndFid = new String[2];
        String fid = fileUrl.substring(fileUrl.indexOf("/", 8) + 1);
        groupNameAndFid[0] = fid.substring(0, fid.indexOf("/"));
        groupNameAndFid[1] = fid.substring(fid.indexOf("/") + 1);
        return groupNameAndFid;
    }

}
