package com.BUAAChat;

import java.io.Serializable;

/**
 * @author 西西弗
 * @Description:
 * @date 2023/11/9 20:12
 */
public class Message implements Serializable {
    private String from;//Account
    private String to;//
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
