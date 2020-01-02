package cn.wl.android.lib.utils;

import android.content.Intent;
import android.text.TextUtils;

import cn.wl.android.lib.ui.common.VideoRecordActivity;

/**
 * Created by JustBlue on 2019-08-31.
 *
 * @email: bo.li@cdxzhi.com
 * @desc: 自定义拍摄界面返回值封装
 */
public class RecordResult {

    private final String path;
    private final boolean isVideo;

    /**
     * 自动转换拍摄结果并封装
     *
     * @param intent
     * @return
     */
    public static RecordResult auto(Intent intent) {
        if (intent != null) {

            String imagePath = intent.getStringExtra(VideoRecordActivity.IMAGE_PATH);
            String videoPath = intent.getStringExtra(VideoRecordActivity.VIDEO_PATH);

            if (!TextUtils.isEmpty(imagePath)) return photo(imagePath);
            if (!TextUtils.isEmpty(videoPath)) return video(videoPath);

            return empty();
        } else {
            return empty();
        }
    }

    /**
     * 封装图片回调数据
     *
     * @param path
     * @return
     */
    public static RecordResult photo(String path) {
        return new RecordResult(path, false);
    }

    /**
     * 封装视频回调参数
     *
     * @param path
     * @return
     */
    public static RecordResult video(String path) {
        return new RecordResult(path, true);
    }

    /**
     * 封装空数据回调参数
     *
     * @return
     */
    public static RecordResult empty() {
        return new RecordResult("", false);
    }

    private RecordResult(String path, boolean isVideo) {
        this.path = path;
        this.isVideo = isVideo;
    }

    public boolean isSuccess() {
        return !TextUtils.isEmpty(this.path);
    }

    public String getPath() {
        return path;
    }

    public boolean isVidoe() {
        return isVideo;
    }

    @Override
    public String toString() {
        return "RecordResult{" +
                "path='" + path + '\'' +
                ", isVidoe=" + isVideo +
                '}';
    }
}
