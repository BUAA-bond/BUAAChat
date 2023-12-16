
package com.BUAAChat.Client;

import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;

public class User {
    private String account;
    private String name;
    private String password;
    private Image avatar;
    private String avatarPath;
    private ArrayList<UserInfo> friends = new ArrayList();//主键是好友账号
    private ArrayList<GroupInfo> groups = new ArrayList();//主键是群聊号
    private ArrayList<RequestInfo> requests=new ArrayList<>();
    private HashMap<String, ArrayList<ChatInfo>> messagesF=new HashMap<>();//主键是好友账号，表示与谁谁的聊天记录
    private HashMap<String, ArrayList<ChatInfo>> messagesG=new HashMap<>();//主键是群账号，表示在哪个群的聊天记录
    public User() {
    }

    public User(String account, String password) {
        this.account = account;
        this.password = password;
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

    public User(String account, String name, String password) {
        this.account = account;
        this.name = name;
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

    public Image getAvatar() {
        return this.avatar;
    }

    public String getAvatarPath() {
        return this.avatarPath;
    }

    public HashMap<String, ArrayList<ChatInfo>> getMessagesF() {
        return messagesF;
    }
    public void setAvatar(Image avatar) {
        this.avatar = avatar;
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
