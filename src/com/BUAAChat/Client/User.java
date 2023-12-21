
package com.BUAAChat.Client;

import com.BUAAChat.Info.ChatInfo;
import com.BUAAChat.Info.GroupInfo;
import com.BUAAChat.Info.RequestInfo;
import com.BUAAChat.Info.UserInfo;

import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author 西西弗
 * @Description: 用户
 * @date 2023/11/17 16:48
 */
public class User {
    /**
     *用户账号，唯一
     */
    private String account;

    /**
     *用户的名字
     */
    private String name;

    /**
     *用户的密码
     */
    private String password;

    /**
     *用户的头像路径
     */
    private String avatarPath;

    /**
     *用户的所有好友
     */
    private ArrayList<UserInfo> friends = new ArrayList();

    /**
     *用户的所有群
     */
    private ArrayList<GroupInfo> groups = new ArrayList();

    /**
     *与该用户有关的所有好友申请
     */
    private ArrayList<RequestInfo> requests=new ArrayList<>();

    /**
     *该用户与朋友的所有聊天记录，主键是聊天对象的账号，值是聊天集合
     */
    private HashMap<String, ArrayList<ChatInfo>> messagesF=new HashMap<>();

    /**
     *该用户所在群的所有聊天记录，主键是群的账号，值是聊天集合
     */
    private HashMap<String, ArrayList<ChatInfo>> messagesG=new HashMap<>();//主键是群账号，表示在哪个群的聊天记录
    public User() {
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRequests(ArrayList<RequestInfo> requests) {
        this.requests = requests;
    }

    public User(String account, String name, String password, String avatarPath) {
        this.account = account;
        this.name = name;
        this.password = password;
        this.avatarPath = avatarPath;
    }

    public String getName() {
        return this.name;
    }

    public String getAccount() {
        return this.account;
    }

    public String getPassword() {
        return this.password;
    }

    public ArrayList<RequestInfo> getRequests() {
        return requests;
    }

    public String getAvatarPath() {
        return this.avatarPath;
    }

    public HashMap<String, ArrayList<ChatInfo>> getMessagesF() {
        return messagesF;
    }

    public void setFriends(ArrayList<UserInfo> friends) {
        this.friends = friends;
    }

    public void setGroups(ArrayList<GroupInfo> groups) {
        this.groups = groups;
    }

    public ArrayList<UserInfo> getFriends() {
        return friends;
    }

    public ArrayList<GroupInfo> getGroups() {
        return groups;
    }

    public void setMessagesF(HashMap<String, ArrayList<ChatInfo>> messagesF) {
        this.messagesF = messagesF;
    }

    public void setMessagesG(HashMap<String, ArrayList<ChatInfo>> messagesG) {
        this.messagesG = messagesG;
    }

    public HashMap<String, ArrayList<ChatInfo>> getMessagesG() {
        return messagesG;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }
}
