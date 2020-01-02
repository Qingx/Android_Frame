package cn.wl.android.lib.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import androidx.fragment.app.FragmentActivity;

import com.blankj.utilcode.util.FileUtils;
import com.yalantis.ucrop.UCrop;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.util.EnumSet;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

import cn.wl.android.lib.R;
import cn.wl.android.lib.config.WLConfig;
import cn.wl.android.lib.ui.common.VideoRecordActivity;
import cn.wl.android.lib.ui.internal.IRxOption;
import cn.wl.android.lib.utils.result.DataResult;
import cn.wl.android.lib.utils.result.ResultFragment;
import cn.wl.android.lib.utils.result.RxResult;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;

import static com.zhihu.matisse.MimeType.BMP;
import static com.zhihu.matisse.MimeType.JPEG;
import static com.zhihu.matisse.MimeType.PNG;
import static com.zhihu.matisse.MimeType.WEBP;

/**
 * Created by JustBlue on 2019-08-31.
 *
 * @email: bo.li@cdxzhi.com
 * @desc: 界面跳转辅助
 */
public class IntentHelper {

    // 跳转拍摄界面需要的权限
    private static final String[] PHOTO_PERMISSION = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    // 跳转相册需要的权限
    private static final String[] ALBUM_PERMISSION = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public static final int RC_ALBUM = 301;
    public static final int RC_PHOTO = 302;
    public static final int RC_VIDEO = 303;
    public static final int RC_QR_CODE = 304;

    public static Observable<DataResult<String>> startQR(IRxOption option) {
        return option.getRxPermission().request(ALBUM_PERMISSION)
                .flatMap(success -> {
                    if (success) {
                        return actualQR(option);
                    } else {
                        Toasts.show("获取权限失败, 跳转扫描界面失败!");
                        return Observable.empty();
                    }
                })
                .onErrorReturn(t -> DataResult.cancel());
    }

    /**
     * 实际的扫描二维码
     *
     * @param option
     * @return
     */
    private static ObservableSource<DataResult<String>> actualQR(IRxOption option) {
        RxResult rxResult = option.getRxResult();

        FragmentActivity activity = rxResult.getFragment().getActivity();

        if (activity == null) {
            return Observable.just(DataResult.cancel());
        }

        Intent intent = new Intent(activity, CaptureActivity.class);

        return rxResult.start(intent, RC_QR_CODE)
                .mapBy((data) -> {
                    return data.getStringExtra(Constant.CODED_CONTENT);
                });
    }

    /**
     * 跳转图库并获取界面返回值
     *
     * @param option
     * @return
     */
    public static Observable<DataResult<List<String>>> startAlbum(IRxOption option, int maxCount) {
        return option.getRxPermission().request(ALBUM_PERMISSION)
                .flatMap(success -> {
                    if (success) {
                        return actualAlbum(option, maxCount);
                    } else {
                        Toasts.show("获取权限失败, 跳转相册界面失败!");
                        return Observable.empty();
                    }
                })
                .onErrorReturn(t -> DataResult.cancel()); // 发生异常时返回取消事件
    }

    /**
     * 实际跳转图库方法, 此方法存在权限风险
     * 需申请权限后才能调用
     *
     * @param option
     * @param maxCount
     * @return
     */
    private static Observable<DataResult<List<String>>> actualAlbum(IRxOption option, int maxCount) {
        RxResult rxResult = option.getRxResult();

        CaptureStrategy strategy = new CaptureStrategy(
                true,
                WLConfig.getAuthCode()
        );

        int size = WLConfig.getContext().getResources()
                .getDimensionPixelSize(R.dimen.grid_expected_size);

        Matisse.from(rxResult.getFragment())
                .choose(EnumSet.of(JPEG, PNG))
                .countable(true)
                .gridExpectedSize(size) //一行3列
                .maxSelectable(maxCount)
                .showSingleMediaType(true)
                .captureStrategy(strategy)
                .imageEngine(new GlideEngine())
                .forResult(RC_ALBUM);

        return rxResult.getResultEvent(RC_ALBUM)
                .mapBy(intent -> {
                    //原始图片
                    return Matisse.obtainPathResult(intent);
                });
    }

    /**
     * 跳转自定义拍照界面
     *
     * @param option
     * @return
     */
    public static Observable<DataResult<RecordResult>> startPhoto(IRxOption option) {
        return option.getRxPermission().request(ALBUM_PERMISSION)
                .flatMap(success -> {
                    if (success) {
                        return actualRecord(option, false);
                    } else {
                        Toasts.show("获取权限失败, 跳转拍摄界面失败!");
                        return Observable.empty();
                    }
                })
                .onErrorReturn(t -> DataResult.cancel()); // 发生异常时返回取消事件
    }

    /**
     * 跳转自定义拍照界面
     *
     * @param option
     * @return
     */
    public static Observable<DataResult<RecordResult>> startVideo(IRxOption option) {
        return option.getRxPermission().request(ALBUM_PERMISSION)
                .flatMap(success -> {
                    if (success) {
                        return actualRecord(option, true);
                    } else {
                        Toasts.show("获取权限失败, 跳转拍摄界面失败!");
                        return Observable.empty();
                    }
                });
    }

    /**
     * 跳转自定义图片\视频拍摄界面
     *
     * @param option
     * @param needRecord 是否需要长按拍摄
     * @return
     */
    private static Observable<DataResult<RecordResult>> actualRecord(IRxOption option, boolean needRecord) {
        RxResult rxResult = option.getRxResult();

        Activity activity = rxResult.getFragment().getActivity();

        // 跳转自定义拍照界面
        Intent intent = new Intent(activity, VideoRecordActivity.class);
        intent.putExtra(VideoRecordActivity.NEED_RECORD, needRecord);

        return rxResult.start(intent, needRecord ? RC_VIDEO : RC_PHOTO)
                .mapBy(data -> RecordResult.auto(data));
    }

    /**
     * 跳转剪切界面
     *
     * @param option
     * @param path
     * @return
     */
    public static Observable<DataResult<String>> startCrop(IRxOption option, String path) {
        return startCrop(option, path, 600, 1);
    }

    /**
     * 跳转剪切界面
     *
     * @param option
     * @param path
     * @param whRatio
     * @return
     */
    public static Observable<DataResult<String>> startCrop(IRxOption option, String path, float whRatio) {
        return startCrop(option, path, 600, whRatio);
    }

    /**
     * 跳转剪切界面
     *
     * @param option
     * @param path
     * @param size
     * @param whRatio
     * @return
     */
    public static Observable<DataResult<String>> startCrop(IRxOption option, String path, int size, float whRatio) {
        return option.getRxPermission().request(ALBUM_PERMISSION)
                .flatMap(success -> {
                    if (success) {
                        return actualCrop(option, path, size, whRatio);
                    } else {
                        Toasts.show("获取权限失败, 跳转相册界面失败!");
                        return Observable.empty();
                    }
                })
                .onErrorReturn(t -> DataResult.cancel()); // 发生异常时返回取消事件
    }

    /**
     * 跳转实际剪切界面
     *
     * @param option
     * @param path
     * @param size
     * @return
     */
    private static Observable<DataResult<String>> actualCrop(IRxOption option, String path, int size, float whRatio) {
        RxResult rxResult = option.getRxResult();

        Uri sUri = UriUtil.getUri(path);

        String dir = Environment.getExternalStorageDirectory()
                .getAbsoluteFile() + "/crop-image/";

        FileUtils.createOrExistsDir(dir);

        Uri dUri = Uri.fromFile(new File(dir,
                System.currentTimeMillis() + "_crop_temp.jpg"));

        UCrop.Options options = new UCrop.Options();
        options.setHideBottomControls(true);

        ResultFragment fragment = rxResult.getFragment();
        UCrop.of(sUri, dUri)
                .withOptions(options)
                .withAspectRatio(1F, whRatio)
                .withMaxResultSize(size, size)
                .start(fragment.getActivity(), fragment);

        return rxResult.getResultEvent(UCrop.REQUEST_CROP)
                .mapBy(intent -> {
                    Uri resultUri = UCrop.getOutput(intent);
                    String actualUrl = UriUtil.getFilePathByUri(resultUri);
                    return actualUrl;
                });
    }

}
