package cn.wl.android.lib.utils;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by JustBlue on 2019-10-28.
 *
 * @email: bo.li@cdxzhi.com
 * @desc: 通用方法
 */
public final class Actions {

    /**
     * 转换
     *
     * @param source
     * @param mapper
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> List<R> map(@NonNull Collection<T> source, @NonNull Mapper<T, R> mapper) {
        ArrayList<R> list = new ArrayList<>();

        Iterator<T> iterator = source.iterator();
        while (iterator.hasNext()) {
            T next = iterator.next();
            list.add(mapper.map(next));
        }

        return list;
    }

    /**
     * map转换
     *
     * @param <T>
     * @param <R>
     */
    public interface Mapper<T, R> {
        R map(T data);
    }

    /**
     * 过滤器
     *
     * @param <T>
     */
    public interface Filter<T> {

        boolean check(T data);
    }

}
