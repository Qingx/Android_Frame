package cn.wl.android.lib.ui.common;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.aries.ui.widget.progress.UIProgressDialog;
import com.esay.ffmtool.FfmpegTool;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.wl.android.lib.R;
import cn.wl.android.lib.config.WLConfig;
import cn.wl.android.lib.utils.GlideEngine;
import cn.wl.android.lib.view.CircleButtonView;


/**
 * Created by yufs on 2017/7/5.
 */

public class VideoRecordActivity extends Activity implements MediaRecorder.OnErrorListener, View.OnClickListener {

    public static String NEED_RECORD = "needRecord";
    public static String NEED_SELECT = "needSelect";
    public static String VIDEO_PATH = "videoPath";
    public static String IMAGE_PATH = "imagePath";

    private int REQUEST_CODE_SELECT = 888;
    private ImageView iv_cancel, iv_save;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private MediaRecorder mMediaRecorder; //视屏录制
    private MediaPlayer mMediaPlayer;     //视屏播放器
    private Camera mCamera;

    private CircleButtonView cbv_record;
    private File mRecordFile = null;// 文件
    private String mImgPath = "";
    private int mRotationRecord = 90;//视频输出角度 0横屏  90竖屏  180反横屏
    private boolean isRecording;//正在录制
    private float mWindowW;
    private boolean isBack = true;//是否后置摄像头
    private ImageView iv_change;
    private ImageView iv_finish,
            iv_select;
    private ImageView iv_back;
    private TextView tv_edit;

    private int REQUEST_CODE = 667;
    private int PERMISSION_CODE = 666;

    private boolean canRecord = true;
    private float mWidth;
    private float mHeight;
    private boolean needRecord = true;//是否开启录制功能
    private boolean needSelect = false;//是否开选择图片功能,与录制功能不能同时开启
    private CircleButtonView circleButtonView;

    ExecutorService executorService = Executors.newFixedThreadPool(3);
    FfmpegTool ffmpegTool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //全屏无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_record_activity);
        circleButtonView = findViewById(R.id.cbv_record);
        needRecord = getIntent().getBooleanExtra(NEED_RECORD, true);
        needSelect = getIntent().getBooleanExtra(NEED_SELECT, false);
        circleButtonView.setNeedRecord(needRecord);
        if (!needRecord) {
            ((TextView) findViewById(R.id.tv_tip)).setText("点按拍照");
        }
        ffmpegTool = FfmpegTool.getInstance(this);

        initView();
        initData();
        setListener();
        requestPermission();
    }

    /**
     * 请求权限
     */
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                canRecord = false;
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                        PERMISSION_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PERMISSION_CODE == requestCode && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            canRecord = true;

        }
    }

    private void initData() {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mWindowW = metric.widthPixels;     // 屏幕宽度（像素）

    }

    @Override
    protected void onResume() {
        super.onResume();
        cbv_record.setVisibility(View.VISIBLE);
        iv_cancel.setVisibility(View.GONE);
        iv_save.setVisibility(View.GONE);
        tv_edit.setVisibility(View.GONE);
//        tv_preview.setVisibility(View.GONE);

        iv_back.setVisibility(View.VISIBLE);
        iv_change.setVisibility(View.VISIBLE);
//        iv_finish.setVisibility(View.VISIBLE);
        if (mRecordFile != null && mRecordFile.getAbsoluteFile() != null) {
            mRecordFile.getAbsoluteFile().delete();
        }
        if (!TextUtils.isEmpty(mImgPath)) {
            File file = new File(mImgPath);
            file.delete();
            mImgPath = "";
        }
    }

    @Override
    protected void onDestroy() {
        freeCamera();
        stopPlay();
        super.onDestroy();
    }

    private void setListener() {
        cbv_record.setOnLongClickListener(new CircleButtonView.OnLongClickListener() {
            @Override
            public void onLongClick() {
                if (!canRecord) {
                    Toast.makeText(VideoRecordActivity.this, "请前往应用设置开启录音权限", Toast.LENGTH_SHORT).show();
                    return;
                }
                isRecording = true;
                startRecord();

                iv_back.setVisibility(View.GONE);
                iv_change.setVisibility(View.GONE);
            }

            @Override
            public void onNoMinRecord(int currentTime) {
                isRecording = false;
                Toast.makeText(VideoRecordActivity.this, "录制视频太短", Toast.LENGTH_SHORT).show();

                iv_back.setVisibility(View.VISIBLE);
                iv_change.setVisibility(View.VISIBLE);
                if (mRecordFile.getAbsoluteFile() != null) {
                    mRecordFile.getAbsoluteFile().delete();
                }
                try {
                    initCamera();
                } catch (IOException e) {
                }
            }

            @Override
            public void onRecordFinishedListener() {
                Log.e("yufs", "视频录制完成:path==" + mRecordFile.getAbsolutePath());
                isRecording = false;
                stopRecord();
                cbv_record.setVisibility(View.GONE);
                iv_change.setVisibility(View.GONE);
                tv_edit.setVisibility(View.GONE);//屏蔽编辑功能

                iv_finish.setVisibility(View.GONE);
                iv_select.setVisibility(View.GONE);
//                tv_preview.setVisibility(View.VISIBLE);
                iv_cancel.setVisibility(View.VISIBLE);//重拍
                iv_save.setVisibility(View.VISIBLE);//使用
                iv_back.setVisibility(View.VISIBLE);


                //直接当前页播放
                if (true) {
                    initMediaPlayer();
                } else {
                    //或者跳转到其他页播放
//                freeCamera();
//                Intent intent = new Intent(VideoRecordActivity.this, PlayVideoActivity.class);
//                intent.putExtra("path", mRecordFile.getAbsolutePath());
//                startActivityForResult(intent, REQUEST_CODE);
                }


            }
        });

        cbv_record.setOnClickListener(new CircleButtonView.OnClickListener() {
            @Override
            public void onClick() {
                takePhoto();
            }
        });

        //手机旋转监听
        OrientationEventListener orientationEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int rotation) {
                //录制的过程不改变
                if (isRecording) {
                    return;
                }
                if (((rotation >= 0) && (rotation <= 30)) || (rotation >= 330)) {
                    // 竖屏拍摄
                    mRotationRecord = 90;
                } else if (((rotation >= 230) && (rotation <= 310))) {
                    // 横屏拍摄
                    mRotationRecord = 0;
                } else if (rotation > 30 && rotation < 95) {
                    // 反横屏拍摄
                    mRotationRecord = 180;
                }
            }
        };
        orientationEventListener.enable();

        tv_edit.setOnClickListener(this);
        iv_cancel.setOnClickListener(this);
        iv_save.setOnClickListener(this);
        iv_change.setOnClickListener(this);
        iv_finish.setOnClickListener(this);
        iv_select.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    /**
     * 初始化视屏播放器
     */
    private void initMediaPlayer() {
        freeCamera();
        try {
            mMediaPlayer = new MediaPlayer();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri uriForFile = FileProvider.getUriForFile(VideoRecordActivity.this,
                        WLConfig.getAuthCode(), mRecordFile);
                mMediaPlayer.setDataSource(this, uriForFile);
            } else {
                mMediaPlayer.setDataSource(this, Uri.parse(mRecordFile.getAbsolutePath()));
            }
            mSurfaceHolder = mSurfaceView.getHolder();
            mSurfaceHolder.addCallback(new MyCallBack());
            mMediaPlayer.setDisplay(mSurfaceHolder);
            mMediaPlayer.prepare();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.start();
                    mMediaPlayer.setLooping(true);
                }
            });
            mMediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                @Override
                public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                    changeVideoSize();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeVideoSize() {
        int videoWidth = mMediaPlayer.getVideoWidth();
        int videoHeight = mMediaPlayer.getVideoHeight();

        //根据视频尺寸去计算->视频可以在sufaceView中放大的最大倍数。
        float max;
        if (getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            //竖屏模式下按视频宽度计算放大倍数值
            max = Math.max((float) videoWidth / (float) mSurfaceView.getWidth(), (float) videoHeight / (float) mSurfaceView.getHeight());
        } else {
            //横屏模式下按视频高度计算放大倍数值
            max = Math.max(((float) videoWidth / (float) mSurfaceView.getHeight()), (float) videoHeight / (float) mSurfaceView.getWidth());
        }

        //视频宽高分别/最大倍数值 计算出放大后的视频尺寸
        videoWidth = (int) Math.ceil((float) videoWidth / max);
        videoHeight = (int) Math.ceil((float) videoHeight / max);

        //无法直接设置视频尺寸，将计算出的视频尺寸设置到surfaceView 让视频自动填充。
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(videoWidth, videoHeight);
        params.topMargin = (int) (mHeight - videoHeight) / 2;
        mSurfaceView.setLayoutParams(params);
    }

    /**
     * 停止视屏播放
     */
    private void stopPlay() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

    }

    @Override
    public void onError(MediaRecorder mr, int what, int extra) {

    }

    private class MyCallBack implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mMediaPlayer.setDisplay(holder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }


    /**
     * 拍照
     */
    private void takePhoto() {
        findViewById(R.id.tv_tip).setVisibility(View.GONE);
        try {
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setFocusMode(Camera.Parameters.ANTIBANDING_AUTO);
            parameters.setPictureFormat(ImageFormat.JPEG);
            mCamera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    //对焦成功
                    if (success) {
                        try {
                            mCamera.takePicture(null, null, cameraCallBack);
                        } catch (Exception e) {
                        }
                    } else if (!isBack) {//前置摄像头会对焦失败
                        mCamera.takePicture(null, null, cameraCallBack);
//                        Toast.makeText(VideoRecordActivity.this, "对焦失败!", Toast.LENGTH_SHORT).show();
                    } else {
                        mCamera.takePicture(null, null, cameraCallBack);
                    }
                }
            });
        } catch (Exception e) {

        }
    }

    /**
     * 拍照成功回调函数
     */
    private Camera.PictureCallback cameraCallBack = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            //先验证手机是否有sdcard
            String status = Environment.getExternalStorageState();
            if (!status.equals(Environment.MEDIA_MOUNTED)) {
                Toast.makeText(getApplicationContext(), "你的sd卡不可用。", Toast.LENGTH_SHORT).show();
                return;
            }
            cbv_record.setVisibility(View.GONE);
            iv_change.setVisibility(View.GONE);
            iv_finish.setVisibility(View.GONE);
            iv_select.setVisibility(View.GONE);
            iv_cancel.setVisibility(View.VISIBLE);
            tv_edit.setVisibility(View.GONE);
//            tv_preview.setVisibility(View.VISIBLE);
            iv_back.setVisibility(View.VISIBLE);
            iv_save.setVisibility(View.VISIBLE);
            //大部分手机拍照都是存到这个路径
            String filePath = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/";
            String picturePath = System.currentTimeMillis() + ".jpg";
            File file = new File(filePath, picturePath);
            try {
                //存到本地相册
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(data);
                fileOutputStream.close();
                mImgPath = file.getAbsolutePath();

//                //显示图片
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inSampleSize = 2;
//                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };


    private void startRecord() {
        //初始录制视频保存路径
        findViewById(R.id.tv_tip).setVisibility(View.GONE);
        createRecordDir();
        try {
//            initCamera();
            mCamera.unlock();

            initRecord();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        iv_cancel = findViewById(R.id.iv_cancel);
        iv_save = findViewById(R.id.iv_save);
        tv_edit = findViewById(R.id.tv_edit);

        iv_back = findViewById(R.id.iv_back);
        iv_change = findViewById(R.id.iv_change);
        iv_finish = findViewById(R.id.iv_finish);
        iv_select = findViewById(R.id.iv_select);
        mSurfaceView = findViewById(R.id.sv_video);
        cbv_record = findViewById(R.id.cbv_record);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new CustomCallBack());
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        if (needSelect) {
            iv_select.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    /**
     * 选择照片
     *
     * @param
     */
    private void selectPic() {
        Matisse.from(this)
                .choose(MimeType.ofImage())
                .capture(false)
                .captureStrategy(new CaptureStrategy(true, WLConfig.getAuthCode()))
                .countable(false)
                .gridExpectedSize(3)//一行3列
                .maxSelectable(1)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_SELECT);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back || v.getId() == R.id.iv_finish) {
            finish();
            overridePendingTransition(R.anim.anim_slide_down_in, R.anim.anim_slide_down_out);
        } else if (R.id.iv_select == v.getId()) {//选择图库
            selectPic();
        } else if (v.getId() == R.id.iv_change) {
            try {
                isBack = !isBack;
                initCamera();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (v.getId() == R.id.iv_cancel) {
            cbv_record.setVisibility(View.VISIBLE);
            iv_change.setVisibility(View.VISIBLE);
            iv_finish.setVisibility(View.VISIBLE);
            if (needSelect) {
                iv_select.setVisibility(View.VISIBLE);
            }
            tv_edit.setVisibility(View.GONE);
//            iv_back.setVisibility(View.GONE);
//            tv_preview.setVisibility(View.GONE);
            if (mRecordFile != null) {
                stopPlay();
                mRecordFile.getAbsoluteFile().delete();
            }
            if (!TextUtils.isEmpty(mImgPath)) {
                File file = new File(mImgPath);
                file.getAbsoluteFile().delete();
                file.delete();
                mImgPath = "";
            }
            iv_cancel.setVisibility(View.GONE);
            iv_save.setVisibility(View.GONE);
            try {
                mSurfaceView.setLayoutParams(new RelativeLayout.LayoutParams((int) mWidth, (int) mHeight));
                initCamera();

            } catch (IOException e) {
            }
        } else if (v.getId() == R.id.iv_save) {//直接使用
            if (!mImgPath.isEmpty()) {
                Intent data = new Intent()
                        .putExtra(IMAGE_PATH, mImgPath);
                setResult(RESULT_OK, data);
                finish();
            } else if (mRecordFile != null && !mRecordFile.getAbsolutePath().isEmpty()) {
                //压缩视频然后返回
                String path = mRecordFile.getAbsolutePath();
//                compressVideo(path);


//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    Uri uriForFile = FileProvider.getUriForFile(VideoRecordActivity.this,
//                            "com.shiwaixiangcun.customer.fileprovider", new File(path));
//                    path = FileUtil.getRealPathFromURIs(VideoRecordActivity.this, uriForFile);
////                    path = fileByUri.getAbsolutePath();
//                }
//
                setResult(RESULT_OK, new Intent().putExtra(VIDEO_PATH, path));
                finish();
            }
        }

    }


    private class CustomCallBack implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {

            try {
                mWidth = mSurfaceView.getWidth();
                mHeight = mSurfaceView.getHeight();
                initCamera();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//            if (mSurfaceHolder.getSurface() == null) {
//                // preview surface does not exist
//                return;
//            }
//
//            // stop preview before making changes
//            try {
//                mCamera.stopPreview();
//            } catch (Exception e) {
//                // ignore: tried to stop a non-existent preview
//            }
//
//            // set preview size and make any resize, rotate or
//            // reformatting changes here
//
//            // start preview with new settings
//            cameraFocus();
            //不聚焦直接调用以下方法
            afterFocus();


        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            freeCamera();
        }

    }

    private void cameraFocus() {
        // 实现自动对焦
        mCamera.autoFocus(new Camera.AutoFocusCallback() {

            public void onAutoFocus(boolean success, Camera camera) {
                if (success) {
                    afterFocus();
                    camera.cancelAutoFocus();// 只有加上了这一句，才会自动对焦。
                }
            }

        });

    }

    private void afterFocus() {
        try {
            mCamera.stopPreview();
            startPreview();
        } catch (Exception e) {
        }
        try {
            Camera.Parameters params = mCamera.getParameters();
            List<String> focusModes = params.getSupportedFocusModes();
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }
            //适配屏幕
            List<Camera.Size> sizeList = params.getSupportedPreviewSizes();//获取所有支持的camera尺寸
            Camera.Size optionSize = getOptimalPreviewSize(sizeList, mSurfaceView.getWidth(), mSurfaceView.getHeight());//获取一个最为适配的camera.size
            params.setPreviewSize(optionSize.width, optionSize.height);//把camera.size赋值到parameters

            mCamera.setParameters(params);
        } catch (Exception e) {

        }
    }

    /**
     * 初始化,设置输出角度
     *
     * @throws IOException
     */
    @SuppressLint("NewApi")
    private void initRecord() throws IOException {
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.reset();
        if (mCamera != null)
            mMediaRecorder.setCamera(mCamera);
        mMediaRecorder.setOnErrorListener(this);
        mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);// 视频源
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 音频源

        //注释掉的代码官方说这样的配置方法是Android2.2以下使用
//		mMediaRecorder.setOutputFormat(OutputFormat.MPEG_4);// 视频输出格式
//		mMediaRecorder.setAudioEncoder(AudioEncoder.AAC);// 音频格式:AAC兼容会高点
//      mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);//设置视频编码：h264在常见的网页上都可播放
//		mMediaRecorder.setVideoSize(mWidth, mHeight);// 设置分辨率：
//		mMediaRecorder.setVideoEncodingBitRate(1 * 1024 * 1024*100);// 设置帧频率

        //Android2.2以上直接用MediaRecorder.setProfile得到统一的配置
        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_720P));
        if (isBack) {
            mMediaRecorder.setOrientationHint(mRotationRecord);// 输出旋转90度，保持竖屏录制
//            mMediaRecorder.setOrientationHint(90);// 输出旋转90度，保持竖屏录制
        } else {
            if (mRotationRecord == 90) {//竖屏
                mMediaRecorder.setOrientationHint(mRotationRecord + 180);// 输出旋转90度，保持竖屏录制
            } else {
//                mMediaRecorder.setOrientationHint(90);// 输出旋转90度，保持竖屏录制
                mMediaRecorder.setOrientationHint(0);// 输出旋转90度，保持竖屏录制
            }
        }
        mMediaRecorder.setOutputFile(mRecordFile.getAbsolutePath());
        try {
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 相机预览
     */
    private void startPreview() {
        try {
            //相机与SurfaceView进行绑定
            mCamera.setPreviewDisplay(mSurfaceHolder);
            //预览的图片旋转
            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 初始化摄像头
     */
    private void initCamera() throws IOException {
        if (mCamera != null) {
            freeCamera();
        }
        try {
            mCamera = Camera.open(isBack ? 0 : 1);
        } catch (Exception e) {
            e.printStackTrace();
            freeCamera();
            Toast.makeText(VideoRecordActivity.this, "启动摄像头失败,请退出重试", Toast.LENGTH_SHORT).show();
        }
        if (mCamera == null)
            return;
        Camera.Parameters parameters = mCamera.getParameters();
//        parameters.set("orientation", "landscape");
        if (isBack) {
            parameters.set("orientation", "portrait");
            parameters.set("rotation", 90);
        } else {
            parameters.set("orientation", "portrait");
            parameters.set("rotation", 270);
        }
//        适配屏幕
        List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();//获取所有支持的camera尺寸
        Camera.Size optionSize = getOptimalPreviewSize(sizeList, mSurfaceView.getWidth(), mSurfaceView.getHeight());//获取一个最为适配的camera.size
        parameters.setPreviewSize(optionSize.width, optionSize.height);//把camera.size赋值到parameters


        mCamera.setParameters(parameters);
        mCamera.setDisplayOrientation(90);
        mCamera.setPreviewDisplay(mSurfaceHolder);
        mCamera.startPreview();
//        mCamera.unlock();
    }


    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) h / w;
        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Camera.Size size : sizes) {//数据默认横屏
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    private void createRecordDir() {
        //录制的视频保存文件夹
        File videoFolder = new File(Environment.getExternalStorageDirectory()
                + "/DCIM/Camera/");//录制视频的保存地址
//        String path = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/视频/" ;
        if (!videoFolder.exists()) {
            videoFolder.mkdirs();
        }
        try {
            // mp4格式的录制的视频文件
            mRecordFile = File.createTempFile("VID_", ".mp4", videoFolder);
            mRecordFile.setWritable(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 压缩视频
     *
     * @param videoPath
     */
    public void compressVideo(String videoPath) {
        File file = new File(videoPath);
        if (file.exists()) {
            UIProgressDialog dialog = showLoading();

            executorService.execute(() -> {
                File videoFolder = new File(Environment.getExternalStorageDirectory()
                        + "/DCIM/Camera/");//录制视频的保存地址
                if (!videoFolder.exists()) {
                    videoFolder.mkdirs();
                }
                try {
                    // mp4格式的录制的视频文件
                    File newVideo = File.createTempFile("VIDEO_", ".mp4", videoFolder);
                    newVideo.setWritable(true);
                    ffmpegTool.compressVideo(videoPath, newVideo.getAbsolutePath(), 3, (i, oldPath, newPath, b, i1) -> {
                        dialog.dismiss();
                        if (b) {//压缩成功
                            if (mRecordFile != null && mRecordFile.exists()) {//删除老视频
                                mRecordFile.delete();
                            }
                            setResult(RESULT_OK, new Intent().putExtra(VIDEO_PATH, newPath));
                            finish();
                        } else {//压缩失败返回原视频
                            setResult(RESULT_OK, new Intent().putExtra(VIDEO_PATH, videoPath));
                            finish();
                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(this, "处理视频失败", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            });
        } else {
            Toast.makeText(this, "未找该视频", Toast.LENGTH_SHORT).show();
        }
    }

    private UIProgressDialog showLoading() {
        UIProgressDialog uiProgressDialog = new UIProgressDialog.NormalBuilder(this)
                .setMessage(R.string.handle_video)
                .setIndeterminateDrawable(0)
                .setBackgroundRadiusResource(R.dimen.dp_radius_loading)
                .setCancelable(true)
                .setCanceledOnTouchOutside(false)
                .create()
                .setDimAmount(0.6f);
        uiProgressDialog.show();
        return uiProgressDialog;
    }


    /**
     * 释放摄像头资源
     */
    private void freeCamera() {
        try {
            if (mCamera != null) {
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
                mCamera.lock();
                mCamera.release();
                mCamera = null;
            }
        } catch (Exception e) {
            //视频成功录制了，资源回收的时候偶尔会崩溃
        }
    }

    /**
     * 停止录制
     */
    public void stopRecord() {
        if (mMediaRecorder != null) {
            // 设置后不会崩
            mMediaRecorder.setOnErrorListener(null);
            mMediaRecorder.setPreviewDisplay(null);
            try {
                mMediaRecorder.stop();
                mMediaRecorder.release();
//                mCamera.stopPreview();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (RuntimeException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * @param v
     */
    private void startAnimator(View v) {

        AnimatorSet animatorSet = new AnimatorSet();//组合动画
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, "scaleX", 0, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, "scaleY", 0, 1f);

        animatorSet.setDuration(1000);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.play(scaleX).with(scaleY);//两个动画同时开始
        animatorSet.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT && resultCode == RESULT_OK) {
            ArrayList<String> originalList = (ArrayList<String>) Matisse.obtainPathResult(data);
            if (originalList != null && originalList.size() > 0) {
                Intent extra = new Intent()
                        .putExtra(IMAGE_PATH, originalList.get(1));
                setResult(RESULT_OK, extra);
                finish();
            }
        }
    }
}
