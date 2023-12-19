package com.BUAAChat.Server.Util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

import com.BUAAChat.Server.Message.Content;
import com.google.gson.*;
import com.google.gson.reflect.*;


/**
 * Utils that help to parse to and from json.
 * @author WnRock
 * @version 0.1
 */
public class JsonUtil {

    private static Gson gson = new Gson();

    @SuppressWarnings("unchecked")
    private final static <T> T cast(Object o){
        return (T) o;
    }

    /**
     * Get content from json.
     * @param String json
     * @param Class<T> target class
     * @return Content<T>
     */
    public static <T> Content<T> jsonToContent(String json,Class<T> t){
        Type type = new TypeToken<Content<T>>(){}.getType();
        Content<T> ct = gson.fromJson(json, type);
        String jsont = gson.toJson(ct.getData());
        T tt = gson.fromJson(jsont, t);
        ct.setData(tt);
        return ct;
    }

    /**
     * Get Content data from json.
     * @param String json
     * @param Class<T> target class
     * @return T
     */
    public static <T> T jsonToContentData(String json,Class<T> t){
        Content<T> content = jsonToContent(json, t);
        return content.getData();
    }

    /**
     * Get data from json.
     * @param String json
     * @param Class<T> target class
     * @return T 
     */
    public static <T> T jsonToData(String json,Class<T> t){
        T data = gson.fromJson(json, t);
        return data;
    }

    /**
     * Deserialize from json to ArrayList.
     * @param String json
     * @param Class class of elements of ArrayList
     * @return ArrayList containing the elements from json
     */
    public static <T> ArrayList<T> jsonToArrayList(String gsonString,Class<T> cls){
        if(gsonString.equals(null)) return null;

        try {
            Type type = new TypeToken<T>(){}.getType();
            return gson.fromJson(gsonString, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Deserialize from json to ArrayList<Map<String,String>>.
     * @param String json
     * @return ArrayList ArrayList containing the elements from json
     */
    public static ArrayList<Map<String,String>> jsonToList(String gsonString){
        ArrayList<Map<String,String>> res =
            cast(jsonToArrayList(gsonString, Map.class));
        return res;
    }

    /**
     * Deserialize from json to Map<String,String>.
     * @param String json
     * @return Map<String,String> Map containing the elements from json
     */
    public static Map<String,String> jsonToMap(String gsonString){
        if(gsonString.equals(null)) return null;

        try {
            Type type = new TypeToken<Map<String,String>>(){}.getType();
            return gson.fromJson(gsonString, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
