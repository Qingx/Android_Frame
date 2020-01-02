package cn.wl.android.lib.core;

import java.util.Collection;

/**
 * Created by JustBlue on 2019-08-24.
 *
 * @email: bo.li@cdxzhi.com
 * @desc: 数据接口数据处理
 */
public class WLDataHelper {

    /**
     * 接口数据大于0，表示成功
     *
     * @param code
     * @return
     */
    public static boolean isSuccess(int code) {
        return code > 0;
    }

    /**
     * 判断集合是否为空或null
     *
     * @param array
     * @param <T>
     * @return
     */
    public static <T> boolean isEmpty(Collection<T> array) {
        return array == null || array.size() == 0;
    }

}
