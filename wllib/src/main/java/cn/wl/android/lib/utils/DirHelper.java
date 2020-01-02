package cn.wl.android.lib.utils;

import android.os.Environment;

import com.blankj.utilcode.util.FileUtils;

import java.io.File;

/**
 * Created by JustBlue on 2019-11-05.
 *
 * @email: bo.li@cdxzhi.com
 * @desc: App文件夹管理工具
 */
public class DirHelper {

    public static final String IMAGE_DIR = "image_dir";

    private static String mImagePath = Environment
            .getExternalStorageDirectory().getAbsolutePath();

    /**
     * 获取当前时间默认图片保存文件夹
     *
     * @return
     */
    public static String getImageDir() {
        String yyMmDd = DateFormat.date2YY_MM_DD(Times.current());

        String dirPath = checkAndFull(mImagePath)
                + IMAGE_DIR // 添加默认图片文件夹
                + File.separator
                + yyMmDd    // 添加日期文件夹
                + File.separator;

        FileUtils.createOrExistsDir(dirPath);

        return dirPath;
    }

    /**
     * 创建图片保存路径
     *
     * @param name
     * @return
     */
    public static String getImageSavePath(String name) {
        return getImageDir() + name;
    }

    private static String checkAndFull(String path) {
        if (path.endsWith(File.separator)) {
            return path;
        }

        return path + File.separator;
    }

}
