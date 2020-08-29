package com.ql.comm.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;

/**
 * Created by mrqiu on 2017/10/15.
 */

public class JsonUtils {
    public static final Gson sGson = new Gson();

    public static String toJson(Object o) {
        return sGson.toJson(o);
    }

    public static <T> T fromJson(String s, Class<T> clazz) {
        Object o = sGson.fromJson(s, clazz);
        return (T) o;
    }

    public static <T> T fromJson(JsonElement s, Class<T> clazz) {
        Object o = sGson.fromJson(s, clazz);
        return (T) o;
    }


    /**
     * Type type = new TypeToken<List<PayMoney>>() {
     * }.getType();
     *
     * @param s
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T fromTypeJson(JsonElement s, Type type) {
        Object o = sGson.fromJson(s, type);
        return (T) o;
    }

}
