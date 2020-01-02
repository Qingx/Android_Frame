package cn.wl.android.lib.utils.result;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by JustBlue on 2019-08-31.
 *
 * @email: bo.li@cdxzhi.com
 * @desc: {@link androidx.fragment.app.Fragment#onActivityResult(int, int, Intent)}
 * 结果封装
 */
public class ActivityResult {

    private final Intent data;
    private final int resultCode;
    private final int requestCode;

    /**
     * 获取默认取消事件
     *
     * @param requestCode
     * @return
     */
    public static ActivityResult getCancel(int requestCode) {
        return new ActivityResult(null,
                Activity.RESULT_CANCELED, requestCode);
    }

    public ActivityResult(Intent data, int resultCode, int requestCode) {
        this.data = data;
        this.resultCode = resultCode;
        this.requestCode = requestCode;
    }

    public boolean isSuccess() {
        return resultCode == Activity.RESULT_OK;
    }

    public Intent getData() {
        return data;
    }

    public int getResultCode() {
        return resultCode;
    }

    public int getRequestCode() {
        return requestCode;
    }

    @Override
    public String toString() {
        return "ActivityResult{" +
                "data=" + data +
                ", resultCode=" + resultCode +
                ", requestCode=" + requestCode +
                '}';
    }
}
