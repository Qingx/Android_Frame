package cn.wl.android.lib.view.holder;

/**
 * Created by JustBlue on 2019-08-27.
 *
 * @email: bo.li@cdxzhi.com
 * @desc: 进度回调
 */
abstract class OPL {

    private boolean isComplete = false;

    final void completeRow() {
        if (!isComplete) {
            isComplete = true;
            onProgress(1);
            onComplete();
        }
    }

    final void progressRow(float progress) {
        if (!isComplete) {
            onProgress(progress);
        }
    }

    /**
     * 进度回调
     *
     * @param progress
     */
    void onProgress(float progress) {

    }

    /**
     * 进度完成
     */
    void onComplete() {

    }

}
