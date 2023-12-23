
package com.BUAAChat.Info;

import java.util.ArrayList;

/**
 * 用于描述群的信息（群号、群名、群头像）的类
 * @author 西西弗
 * @date 2023/12/7 20:57
 */
public class GroupInfo {
    /**
     *群名
     */
    public String name;

    /**
     *群头像
     */
    public String avatarPath;

    /**
     *群账号
     */
    public String account;

    /**
     *群成员
     */
    public ArrayList<UserInfo> members=new ArrayList<>();

    /**
     * 构造器
     * @param account 群号
     * @param name  群名
     * @param avatarPath 群头像路径
     */
    public GroupInfo(String account,String name, String avatarPath) {
        this.name = name;
        this.avatarPath = avatarPath;
        this.account = account;
    }
}
