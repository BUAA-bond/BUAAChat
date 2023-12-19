package com.BUAAChat;

import java.io.Serializable;

/**
 * Message Class, placed outside for compatibility reasons.
 * @author WnRock
 * @version 0.2
 */
public class Message implements Serializable {
    private String from = null;
    private String to = null;
    private String content = null;

    /**
     * Constructor
     * @param String from
     * @param String to
     * @param String content
     */
    public Message(String from,String to,String content){
        this.from = from;
        this.to = to;
        this.content = content;
    }

    /**
     * Getters and setters
     */
    public String getFrom() {
        return from;
    }
    public String getContent() {
        return content;
    }
    public String getTo() {
        return to;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public void setTo(String to) {
        this.to = to;
    }
}