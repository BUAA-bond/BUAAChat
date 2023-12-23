
package com.BUAAChat.Info;

import java.util.ArrayList;

/**
 * @author 西西弗
 * @Description: 保存的是每一条信息的内容,发送时间，发送人的信息
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
