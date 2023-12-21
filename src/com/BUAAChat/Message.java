package com.BUAAChat;

import java.io.Serializable;

/**
 * @author 西西弗
 * @Description: 用于客户端和服务器的信息传输
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
