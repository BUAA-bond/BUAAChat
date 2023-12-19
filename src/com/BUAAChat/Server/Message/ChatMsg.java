package com.BUAAChat.Server.Message;

/**
 * Content data format class for chat message info (friend and group).
 * @author WnRock
 * @version 0.1
 */
public class ChatMsg {
    private String from = null;
    private String to = null;
    private String sender = null;
    private String sendTime = null;
    private String content = null;

    /**
     * Constructor.
     * @param String sender
     * @param String sendTime
     * @param String content
     */
    public ChatMsg(String sender,String sendTime,String content){
        this.content = content;
        this.sendTime = sendTime;
        this.sender = sender;
    }

    /**
     * Getters and setters.
     */
    public String getContent() {
        return content;
    }
    public String getSendTime() {
        return sendTime;
    }
    public String getSender() {
        return sender;
    }
    public String getFrom() {
        return from;
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
    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public void setTo(String to) {
        this.to = to;
    }
}
