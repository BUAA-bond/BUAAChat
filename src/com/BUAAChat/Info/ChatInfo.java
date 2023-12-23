package com.BUAAChat.Info;

/**
 * 用于描述聊天信息的类
 * @author 西西弗
 * @date 2023/12/7 20:57
 */
public class ChatInfo {
    /**
     *信息的发送者
     */
    public UserInfo fromUser;

    /**
     *信息的内容
     */
    public String content;

    /**
     *信息的发送时间
     */
    public String time;

    /**
     * 构造器
     * @param fromUser 发送者的信息
     * @param content   发送的内容
     */
    public ChatInfo(UserInfo fromUser, String content) {
        this.fromUser = fromUser;
        this.content = content;
    }

    /**
     * 构造器
     * @param fromUser 发送者
     * @param content   内容
     * @param time  发送时间
     */
    public ChatInfo(UserInfo fromUser, String content, String time) {
        this.fromUser = fromUser;
        this.content = content;
        this.time = time;
    }
}
