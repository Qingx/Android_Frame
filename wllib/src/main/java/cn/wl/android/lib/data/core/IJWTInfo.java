package cn.wl.android.lib.data.core;

import java.util.List;

/**
 * Created by ace on 2017/9/10.
 */
public interface IJWTInfo {
    /**
     * 获取用户名
     *
     * @return
     */
    String getUniqueName();

    /**
     * 获取用户ID
     *
     * @return
     */
    String getId();

    /**
     * 获取名称
     *
     * @return
     */
    String getName();

    /**
     * 获取用户部门ID数组
     * */
    List<String> getDeptIds();

    String getToken();
}
