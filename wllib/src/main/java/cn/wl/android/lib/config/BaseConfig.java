package cn.wl.android.lib.config;

import android.text.TextUtils;

import cn.wl.android.lib.utils.Gsons;
import cn.wl.android.lib.utils.SPUtils;

/**
 * Created by JustBlue on 2019-09-10.
 *
 * @email: bo.li@cdxzhi.com
 * @desc: 偏好设置基类
 */
public class BaseConfig {

    protected final SPUtils sp;

    public BaseConfig(String name) {
        this.sp = SPUtils.getInstance(name);
    }

    /**
     * 保存String类型的数据
     *
     * @param key
     * @param value
     */
    public final void putString(String key, String value) {
        sp.put(key, value);
    }

    /**
     * 获取String类型的值
     *
     * @param key
     * @return
     */
    public final String getString(String key) {
        return sp.getString(key);
    }

    /**
     * 获取String类型的数据
     *
     * @param key
     * @param defValue
     * @return
     */
    public final String getString(String key, String defValue) {
        return sp.getString(key, defValue);
    }

    /**
     * 保存Int类型的数据
     *
     * @param key
     * @param value
     */
    public final void putInt(String key, int value) {
        sp.put(key, value);
    }

    /**
     * 保存Long类型的数据
     *
     * @param key
     * @param value
     */
    public final void putLong(String key, long value) {
        sp.put(key, value);
    }

    /**
     * 获取Int类型的数据
     *
     * @param key
     * @param defValue
     * @return
     */
    public final int getInt(String key, int defValue) {
        return sp.getInt(key, defValue);
    }

    /**
     * 获取Long类型的数据
     *
     * @param key
     * @param defValue
     * @return
     */
    public final long getLong(String key, long defValue) {
        return sp.getLong(key, defValue);
    }

    /**
     * 保存Int类型的数据
     *
     * @param key
     * @param value
     */
    public final void putBoolean(String key, boolean value) {
        sp.put(key, value);
    }

    /**
     * 获取Int类型的数据
     *
     * @param key
     * @param defValue
     * @return
     */
    public final boolean getBoolean(String key, boolean defValue) {
        return sp.getBoolean(key, defValue);
    }

    /**
     * 通过json保存对象
     *
     * @param key
     * @param value
     */
    public final void putObject(String key, Object value) {
        if (value == null) return;

        String json = Gsons.getGson().toJson(value);

        sp.put(key, json);
    }

    /**
     * 获取json保存的对象
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public final <T> T getObject(String key, Class<T> clazz) {
        String json = sp.getString(key, "");

        if (TextUtils.isEmpty(json)) return null;

        try {
            return Gsons.getGson().fromJson(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除所有数据
     */
    public void clear() {
        sp.clear();
    }

}
