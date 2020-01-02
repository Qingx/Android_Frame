package cn.wl.android.lib.ui.internal;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by JustBlue on 2019-08-26.
 *
 * @email: bo.li@cdxzhi.com
 * @desc: 限制输入类型
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({
        ILifeCycle.CYC_CREATE,
        ILifeCycle.CYC_START_,
        ILifeCycle.CYC_RESUME,
        ILifeCycle.CYC_PASUE_,
        ILifeCycle.CYC_STOP_X,
        ILifeCycle.CYC_DESTROY,
        ILifeCycle.FYC_ATTACH,
        ILifeCycle.FYC_DETTCH
})
public @interface LifecycleAnn {

}
