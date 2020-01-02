package cn.wl.android.lib.utils;

import android.content.Context;
import android.app.Activity;
import android.text.SpannableStringBuilder;
import android.widget.Toast;

import androidx.annotation.ArrayRes;

import com.aries.ui.widget.action.sheet.UIActionSheetDialog;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.ConvertUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import cn.wl.android.lib.R;
import cn.wl.android.lib.ui.BaseCommonActivity;
import io.reactivex.functions.Consumer;

/**
 * Created by JustBlue on 2019-09-02.
 *
 * @email: bo.li@cdxzhi.com
 * @desc:
 */
public class AlertHelper {


    /**
     * 显示清理底部dialog
     *
     * @param activity
     * @param callback
     */
    public static void showSelectClean(BaseCommonActivity activity, final Consumer<List<String>> callback) {
        showClean(activity, ((dialog, itemView, position) -> {
            switch (position) {
                case 0:
                    Toast.makeText(activity, "清理成功", Toast.LENGTH_SHORT).show();
                    break;
            }
        }));
    }

    /**
     * 显示图片获取方式(拍摄、相册), 并返回选中的图片
     *
     * @param activity
     * @param callback
     */
    public static void showSelectImage(BaseCommonActivity activity, int maxCount, final Consumer<List<String>> callback) {
        showOptions(activity, R.array.select_image_options, (dialog, itemView, position) -> {
            switch (position) {
                case 0:
                    IntentHelper.startPhoto(activity)
                            .compose(activity.bindDestroy())
                            .subscribe(data -> {
                                if (data.isSuccess()) {
                                    String path = data.getData().getPath();
                                    callback.accept(Arrays.asList(path));
                                }
                            });
                    break;
                case 1:
                    IntentHelper.startAlbum(activity, maxCount)
                            .compose(activity.bindDestroy())
                            .subscribe(data -> {
                                if (data.isSuccess()) {
                                    List<String> paths = data.getData();
                                    callback.accept(paths);
                                }
                            });
                    break;
            }
        });
    }

    /**
     * 显示底部弹出, 集合数据框
     *
     * @param activity
     * @param arrayRes
     * @param listener
     */
    public static void showOptions(Context activity, @ArrayRes int arrayRes, UIActionSheetDialog.OnItemClickListener listener) {
        new UIActionSheetDialog.ListIOSBuilder(activity)
                .addItems(arrayRes)
                .setItemsTextColorResource(R.color.text_minor)
                .setTitle(null)
                .setCancel("取消")
                .setCancelMarginTop(ConvertUtils.dp2px(16f))
                .setCancelTextColorResource(R.color.text_minor)
                .setOnItemClickListener(listener)
                .create()
                .setDimAmount(0.6f)
                .setAlpha(1f)
                .show();
    }

    /**
     * 显示底部弹出, 集合数据框
     *
     * @param activity
     * @param list
     * @param listener
     */
    public static void showOptions(Context activity, List<CharSequence> list, UIActionSheetDialog.OnItemClickListener listener) {
//        ArrayList<CharSequence> charSequences = new ArrayList<>();
//
//        for (CharSequence charSequence : list) {
//            charSequences.add(charSequence);
//        }

        new UIActionSheetDialog.ListIOSBuilder(activity)
                .addItems(list)
                .setItemsTextColorResource(R.color.text_minor)
                .setTitle(null)
                .setCancel("取消")
                .setCancelMarginTop(ConvertUtils.dp2px(16f))
                .setCancelTextColorResource(R.color.text_minor)
                .setOnItemClickListener(listener)
                .create()
                .setDimAmount(0.6f)
                .setAlpha(1f)
                .show();
    }

    /**
     * 显示底部弹出, 集合数据框
     *
     * @param activity
     * @param list
     * @param listener
     */
    public static void showOptions(Context activity, String title, List<CharSequence> list, UIActionSheetDialog.OnItemClickListener listener) {
        SpannableStringBuilder spTitle = SpanUtils.getBuilder(title)
                .setBold()
                .setForegroundColor(ColorUtils.getColor(R.color.text_worst))
                .create();

        new UIActionSheetDialog
                .ListIOSBuilder(activity)
                .addItems(list)
                .setItemsTextColorResource(R.color.text_minor)
                .setTitle(spTitle)
                .setTitleTextColor(ColorUtils.getColor(R.color.text_title))
                .setCancel("取消")
                .setCancelMarginTop(ConvertUtils.dp2px(16f))
                .setCancelTextColorResource(R.color.text_minor)
                .setOnItemClickListener(listener)
                .create()
                .setDimAmount(0.6f)
                .setAlpha(1f)
                .show();
    }

    /**
     * 显示底部弹出清理，集合数据框
     *
     * @param activity
     * @param cleanlistener
     */
    public static void showClean(Activity activity, UIActionSheetDialog.OnItemClickListener cleanlistener) {
        String title = "清除缓存将清除本地暂存数据，请确定是否继续？";
        String clean = "立即清理";
        new UIActionSheetDialog.ListIOSBuilder(activity)
                .addItem(clean)
                .setItemsTextColorResource(R.color.delete_red)
                .setTitle(title)
                .setTitleTextSize(14)
                .setCancel(R.string.cancel)
                .setCancelMarginTop(ConvertUtils.dp2px(16f))
                .setCancelMarginTop(ConvertUtils.dp2px(4f))
                .setCancelTextColorResource(R.color.text_minor)
                .setOnItemClickListener(cleanlistener)
                .create()
                .setDimAmount(0.6f)
                .setAlpha(1f)
                .show();
    }

    /**
     * 显示时间选择器
     *
     * @param context
     */
    public static TimePickerView createTimeOptions(Context context, boolean isLimitTime, OnTimeSelectListener listener) {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();

        //正确设置方式 原因：注意事项有说明
        startDate.set(1997, 0, 1);

        if (!isLimitTime) {
            endDate.set(2025, 11, 31);
        }

        TimePickerView picker = new TimePickerBuilder(context, listener)
                .setType(new boolean[]{true, true, true, true, true, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setContentTextSize(16)//滚轮文字大小
                .setTitleSize(18)//标题文字大小
                .setTitleText("选择时间")//标题文字
                .isCyclic(false)//是否循环滚动
                .setTitleColor(ColorUtils.getColor(R.color.text_title))//标题文字颜色
                .setSubmitColor(ColorUtils.getColor(R.color.green_71))//确定按钮文字颜色
                .setCancelColor(ColorUtils.getColor(R.color.text_worst))//取消按钮文字颜色
                .setTitleBgColor(ColorUtils.getColor(R.color.md_white))//标题背景颜色 Night mode
                .setBgColor(ColorUtils.getColor(R.color.md_grey_50))//滚轮背景颜色 Night mode
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("年", "月", "日", "时", "分", "")//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(true) //是否显示为对话框样式
                .build();

        return picker;
    }

}
