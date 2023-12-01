package com.BUAAChat.Server.Message;

import java.io.Serializable;

/**
 * Message Class
 * @author WnRock
 * @version 0.1
 */
public class Message implements Serializable {
    private String fromUser = null;
    private String toUser = null;
    private String content;

    /**
     * Default constructor
     */
    public Message(){ }

    /**
     * Constructor
     * @param fromUser
     */
    public Message(String fromUser){
        this.fromUser = fromUser;
    }

    /**
     * Another constructor
     * @param fromUser
     * @param toUser
     */
    public Message(String fromUser,String toUser){
        this.fromUser = fromUser;
        this.toUser = toUser;
    }

    /**
     * Another constructor again.
     * @param fromUser
     * @param toUser
     * @param content
     */
    public Message(String fromUser,String toUser,String content){
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.content = content;
    }

    /**
     * get USER that send the Message.
     * @return user
     */
    public String getFromUser(){
        return this.fromUser;
    }

    /**
     * get content.
     * @return
     */
    public String getContent(){
        return this.content;
    }

    /**
     * get USER that message sent to.
     * @return
     */
    public String getToUser(){
        return this.toUser;
    }

    /**
     * set Content to str.
     * @param str
     */
    public void setContent(String str){
        this.content = str;
    }
}