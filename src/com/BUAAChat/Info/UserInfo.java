package com.BUAAChat.Info;

/**
 * @author 86188
 * @date 2023/12/21
 */
public class UserInfo {
    /**
     *用户名字
     */
    public String name;

    /**
     *用户头像
     */
    public String avatarPath;

    /**
     *用户账号
     */
    public String account;
    public UserInfo(String name, String avatarPath) {
        this.name = name;
        this.avatarPath = avatarPath;
    }

    public UserInfo(String account,String name, String avatarPath) {
        this.name = name;
        this.avatarPath = avatarPath;
        this.account = account;
    }
}
