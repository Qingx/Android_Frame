package cn.wl.android.lib.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by JustBlue on 2019-08-24.
 *
 * @email: bo.li@cdxzhi.com
 * @desc: Gson对象
 */
public class Gsons {

    private volatile static Gson mInstance;

    /**
     * 获取全局{@link Gson}
     *
     * @return
     */
    public static Gson getGson() {
        if (mInstance == null) {
            synchronized (Gsons.class) {
                if (mInstance == null) {
                    mInstance = new GsonBuilder()
                            .registerTypeAdapter(int.class, new IntegerDefault0Adapter())
                            .registerTypeAdapter(Integer.class, new IntegerDefault0Adapter())
                            .registerTypeAdapter(double.class, new DoubleDefault0Adapter())
                            .registerTypeAdapter(Double.class, new DoubleDefault0Adapter())
                            .registerTypeAdapter(Long.class, new LongDefault0Adapter())
                            .registerTypeAdapter(long.class, new LongDefault0Adapter())
                            .registerTypeAdapter(Float.class, new LongDefault0Adapter())
                            .registerTypeAdapter(float.class, new LongDefault0Adapter())
                            .create();
                }
            }
        }

        return mInstance;
    }

    static class IntegerDefault0Adapter implements JsonSerializer<Integer>, JsonDeserializer<Integer> {

        @Override
        public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            try {
                if (json.getAsString().equals("") || json.getAsString().equals("null")) {
                    //定义为int类型,如果后台返回""或者null,则返回0
                    return 0;
                }
            } catch (Exception ignore) {
            }
            try {
                return json.getAsInt();
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }

        @Override
        public JsonElement serialize(Integer src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src);
        }
    }

    static class LongDefault0Adapter implements JsonSerializer<Long>, JsonDeserializer<Long> {

        @Override
        public Long deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            try {
                if (json.getAsString().equals("") || json.getAsString().equals("null")) {
                    return 0L;
                }
            } catch (Exception ignore) {
            }
            try {
                return json.getAsLong();
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }

        @Override
        public JsonElement serialize(Long src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src);
        }
    }

    static class DoubleDefault0Adapter implements JsonSerializer<Double>, JsonDeserializer<Double> {

        @Override
        public Double deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            try {
                if (json.getAsString().equals("") || json.getAsString().equals("null")) {
                    return 0.00;
                }
            } catch (Exception ignore) {
            }
            try {
                return json.getAsDouble();
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }

        @Override
        public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src);
        }
    }

    static class FloatDefault0Adapter implements JsonSerializer<Float>, JsonDeserializer<Float> {

        @Override
        public Float deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            try {
                if (json.getAsString().equals("") || json.getAsString().equals("null")) {
                    return 0F;
                }
            } catch (Exception ignore) {
            }
            try {
                return json.getAsFloat();
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }

        @Override
        public JsonElement serialize(Float src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src);
        }
    }

}
