package com.BUAAChat.Info;

/**
 * @author 西西弗
 * @Description: 保存的是每一条信息的内容，发送时间，发送人的信息
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
    public ChatInfo(UserInfo fromUser, String content) {
        this.fromUser = fromUser;
        this.content = content;
    }

    public ChatInfo(UserInfo fromUser, String content, String time) {
        this.fromUser = fromUser;
        this.content = content;
        this.time = time;
    }
}
