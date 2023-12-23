package com.BUAAChat.Client;

import com.BUAAChat.Info.UserInfo;

import java.util.HashMap;

/**
 * 群
 * @author 西西弗
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

    /**
     * 构造器
     * @param name 群名
     * @param account 群账号
     * @param member  群成员
     */
    public Group(String name, String account, HashMap<String, UserInfo> member) {
        this.name = name;
        this.account = account;
        this.member = member;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, UserInfo> getMember() {
        return member;
    }
}
