package com.BUAAChat;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * @author 西西弗
 * @Description:
 * @date 2023/12/16 11:36
 */
public class test {
    public static void main(String[] args) {
        int code=401;
        String account="123456";
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("code",code);
        JsonObject data=new JsonObject();
        data.addProperty("uAccount",account);
        jsonObject.add("data",data);
        System.out.println(new Gson().toJson(jsonObject));
    }
}
