package com.BUAAChat.Client;

import com.BUAAChat.Info.UserInfo;

import java.util.HashMap;

/**
 * @author 西西弗
 * @Description: 群
 * @date 2023/11/17 16:48
 */
public class Group {
    /**
     *群的名字
     */
    private String name;
    /**
     *群账号
     */
    private String account;
    /**
     *群的群成员
     */
    private HashMap<String, UserInfo> member=new HashMap<>();

    public Group(HashMap<String, UserInfo> member) {

    }

    public Group(String name, HashMap<String, UserInfo> member) {
        this.name = name;
        this.member = member;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, UserInfo> getMember() {
        return member;
    }
}
