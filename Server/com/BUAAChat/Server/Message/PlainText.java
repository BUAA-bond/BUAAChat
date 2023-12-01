package com.BUAAChat.Server.Message;

/**
 * @author WnRock
 * @version 0.1
 */
public class PlainText extends Message{

    /**
     * Constructor.
     * @param fromUser
     * @param toUser
     * @param content
     */
    public PlainText(String fromUser,String toUser,String content){
        super(fromUser,toUser,content);
    }
}
