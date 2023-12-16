
package com.BUAAChat.Client;

import java.util.ArrayList;

public class GroupInfo {
    public String name;
    public String avatarPath;
    public String account;
    public ArrayList<UserInfo> members=new ArrayList<>();

    public GroupInfo(String account,String name, String avatarPath) {
        this.name = name;
        this.avatarPath = avatarPath;
        this.account = account;
    }

    public GroupInfo() {
    }

    public GroupInfo(String name, String avatarPath, String account, ArrayList<UserInfo> members) {
        this.name = name;
        this.avatarPath = avatarPath;
        this.account = account;
        this.members = members;
    }
}
