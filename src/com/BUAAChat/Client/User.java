
package com.BUAAChat.Client;

import com.BUAAChat.Info.ChatInfo;
import com.BUAAChat.Info.GroupInfo;
import com.BUAAChat.Info.RequestInfo;
import com.BUAAChat.Info.UserInfo;

import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 用户
 * @author 西西弗
 * @date 2023/11/17 16:48
 */
public class User {
    private static User user=null;
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
    private User() {
    }

    /**
     * 单例模式，获取User实例
     * @return  User
     */
    public static User getUser() {
        if(user==null) user=new User();
        return user;
    }

    /**
     * 设置账号
     * @param account
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * 设置名字
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 设置密码
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 设置 好友申请记录的数组
     * @param requests
     */
    public void setRequests(ArrayList<RequestInfo> requests) {
        this.requests = requests;
    }

    /**
     * 获取名字
     * @return String
     */
    public String getName() {
        return this.name;
    }

    /**
     * 获取账号
     * @return String
     */
    public String getAccount() {
        return this.account;
    }

    /**
     * 获取 好友申请记录的数组
     * @return {@link ArrayList}<{@link RequestInfo}>
     */
    public ArrayList<RequestInfo> getRequests() {
        return requests;
    }

    /**
     * 获取 用户头像的路径
     * @return  String
     */
    public String getAvatarPath() {
        return this.avatarPath;
    }

    /**
     * 获取用于记录用户与好友聊天的集合
     * @return {@link HashMap}<{@link String}, {@link ArrayList}<{@link ChatInfo}>>
     */
    public HashMap<String, ArrayList<ChatInfo>> getMessagesF() {
        return messagesF;
    }

    /**
     * 设置 朋友
     * @param friends
     */
    public void setFriends(ArrayList<UserInfo> friends) {
        this.friends = friends;
    }

    /**
     * 设置 群
     * @param groups
     */
    public void setGroups(ArrayList<GroupInfo> groups) {
        this.groups = groups;
    }

    /**
     * 获取 朋友集合
     *
     * @return {@link ArrayList}<{@link UserInfo}>
     * @retu
     */
    public ArrayList<UserInfo> getFriends() {
        return friends;
    }

    /**
     * 获取 群集合
     *
     * @return {@link ArrayList}<{@link GroupInfo}>
     */
    public ArrayList<GroupInfo> getGroups() {
        return groups;
    }

    /**
     * 获取用于记录 用户在群中聊天信息的集合
     *
     * @return {@link HashMap}<{@link String}, {@link ArrayList}<{@link ChatInfo}>>
     */
    public HashMap<String, ArrayList<ChatInfo>> getMessagesG() {
        return messagesG;
    }

    /**
     * 设置头像路径
     * @param avatarPath
     */
    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }
}
