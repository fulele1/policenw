package com.xaqb.policenw.Utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by lenovo on 2016/12/1.
 */
public class GsonUtil {
    private static Gson gson = null;

    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    private GsonUtil() {
    }


    /**
     * 转成json
     *
     * @param object
     * @return
     */
    public static String GsonString(Object object) {
        String gsonString = null;
        if (gson != null) {
            gsonString = gson.toJson(object);
        }
        return gsonString;
    }


    /**
     * 转成bean
     *
     * @param gsonString
     * @param cls
     * @return
     */
    public static <T> T GsonToBean(String gsonString, Class<T> cls) {
        T t = null;
        if (gson != null) {
            t = gson.fromJson(gsonString, cls);
        }
        return t;
    }

    /**
     * 转成list
     *
     * @param gsonString
     * @param cls
     * @return
     */
    public static <T> List<T> GsonToList(String gsonString, Class<T> cls) {
        List<T> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString, new TypeToken<List<T>>() {
            }.getType());
        }
        return list;
    }

    /**
     * 转成list中有map的
     *
     * @param gsonString
     * @return
     */
    public static <T> List<Map<String, T>> GsonToListMaps(String gsonString) {
        List<Map<String, T>> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString,
                    new TypeToken<List<Map<String, T>>>() {
                    }.getType());
        }
        return list;
    }

    /**
     * 转成map的
     *
     * @param gsonString
     * @return
     */
    public static <Object> Map<String, Object> GsonToMaps(String gsonString) {
        Map<String, Object> map = null;
        if (gson != null) {
            map = gson.fromJson(gsonString, new TypeToken<Map<String, Object>>() {
            }.getType());
        }
        return map;
    }

    /**
     * json 转 map
     *
     * @param jsonStr
     *            要转换的json字符串
     * @return
     */
    public static Map<String, Object> JsonToMap(String jsonStr) {
        return JsonToMap(jsonStr, null);
    }

    /**
     * json 转 map
     *
     * @param jsonStr
     *            要转换的json字符串
     * @param result
     *            转换的结果放入位置
     * @return
     */
    public static Map<String, Object> JsonToMap(String jsonStr, Map<String, Object> result) {
        if (jsonStr == null) {
            return null;
        }
        if (result == null) {
            result = new HashMap<String, Object>();
        }
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(jsonStr);
        return JsonToMap(result, "▲▼◆", jsonElement);
    }

    /**
     * json 转 map
     *
     * @param result 要转换的json字符串
     * @param key    key
     * @param value  value
     * @return
     */
    public static Map<String, Object> JsonToMap(Map<String, Object> result, String key, JsonElement value) {
        // 如果key为null 直接报错
        if (key == null) {
            throw new RuntimeException("key值不能为null");
        }
        // 如果value为null,则直接put到map中
        if (value == null) {
            result.put(key, value);
        } else {
            // 如果value为基本数据类型，则放入到map中
            if (value.isJsonPrimitive()) {
                result.put(key, value.getAsString());
            } else if (value.isJsonObject()) {
                // 如果value为JsonObject数据类型，则遍历此JSONObject，进行递归调用本方法
                JsonObject jsonObject = value.getAsJsonObject();
                Iterator<Entry<String, JsonElement>> iterator = jsonObject.entrySet().iterator();
                while (iterator.hasNext()) {
                    Entry<String, JsonElement> next = iterator.next();
                    result = JsonToMap(result, next.getKey(), next.getValue());
                }
            } else if (value.isJsonArray()) {
                // 如果value为JsonArray数据类型，则遍历此JsonArray，进行递归调用本方法
                JsonArray jsonArray = value.getAsJsonArray();
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                for (int i = 0, len = jsonArray.size(); i < len; i++) {
                    Map<String, Object> tempMap = new HashMap<String, Object>();
                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                    Iterator<Entry<String, JsonElement>> iterator = jsonObject.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Entry<String, JsonElement> next = iterator.next();
                        tempMap = JsonToMap(tempMap, next.getKey(), next.getValue());
                    }
                    list.add(tempMap);
                }
                result.put(key, list);
            }
        }
        // 返回最终结果
        return result;
    }
}