package com.BUAAChat;

import java.io.Serializable;

/**
 * 用于客户端和服务器的信息传输
 * @author 西西弗
 * @date 2023/11/9 20:12
 */
public class Message implements Serializable {
    /**
     *发送人的账号
     */
    private String from;
    /**
     *接收人的账号
     */
    private String to;
    /**
     *信息的内容，用json保存
     */
    private String content;

    /**
     * 构造器
     * @param from 发送者账号
     * @param to    接收者账号
     * @param content   内容
     */
    public Message(String from, String to, String content) {
        this.from = from;
        this.to = to;
        this.content = content;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getContent() {
        return content;
    }
}
