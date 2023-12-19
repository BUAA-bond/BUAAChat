package com.BUAAChat.Server.Server;

import java.util.ArrayList;

import com.BUAAChat.Message;
import com.BUAAChat.Server.Database.DBOperation;
import com.BUAAChat.Server.Message.*;
import com.BUAAChat.Server.Util.JsonUtil;
import com.google.gson.Gson;

/**
 * Chat-related functions.
 * @author WnRock
 * @version 0.1
 */
public class Chat {

    private static Gson gson = new Gson();

    /**
     * Relay chat messages of friends and groups. Used by resender.
     * @param Message m
     * @return boolean
     */
    public static boolean relayPlainText(Message m){
        String to = m.getTo();

        if(Server.isOnline(to)){
            Server.getAllLinks().get(to).msgToClient( m );
            Server.printDebug("[RELAY] Target online now. Resending...");
            return true;
        } else return false;

    }

    /**
     * Relay chat messages of friends and groups.
     * @param Content<ChatMsg>
     * @return Content<Respond>
     */
    public static Content<Respond> relayPlainText(Content<ChatMsg> c){

        ChatMsg m = JsonUtil.jsonToContentData(gson.toJson(c), ChatMsg.class);

        String from = m.getFrom();
        String to = m.getTo();
        String content = m.getContent();

        String code = c.getCode();
        Respond res = new Respond(code, 9999);

        Server.printDebug("[RELAY]: from "+from+", to "+to+", content "+content);

        if(from==null){
            res = new Respond(code,9091, "from");
            return new Content<Respond>(code, res.getStatus(), res);
        } else if(to==null){
            res = new Respond(code,9091, "to");
            return new Content<Respond>(code, res.getStatus(), res);
        } else if(content==null){
            res = new Respond(code,9091, "content");
            return new Content<Respond>(code, res.getStatus(), res);
        }

        Message msg = new Message(
            from, to, 
            gson.toJson(
                new Content<ChatMsg>(code, 0, m)
            )
        );
        
        if(isUserAcc(to)){

            if(!DBOperation.isFriend(from, to)){
                res = new Respond(code,9104, "to");
                return new Content<Respond>(code, res.getStatus(), res);
            }
            if(!DBOperation.isNotBlocked(from, to)){
                res = new Respond(code,9104, "to");
                return new Content<Respond>(code, res.getStatus(), res);
            }

            if(Server.isOnline(to)){
                Server.printDebug("[RELAY]: Recipient online. Sending...");
                Server.getAllLinks().get(to).msgToClient( msg );
                res = new Respond(code, 9000);
            } else {
                Server.printDebug("[RELAY]: Recipient offline. Resend later.");
                Resender.addPending(msg);
                res = new Respond(code, 9000);
            }

            DBOperation.addFriendChatMessage(from,to,content);

        } else if(isGroupAcc(to)){

            if(!DBOperation.isInGroup(from, to)){
                res = new Respond(code,9104, "to");
                return new Content<Respond>(code, res.getStatus(), res);
            }

            ArrayList<AccInfo> mem = DBOperation.getGroupMember(to);
            for (AccInfo accInfo : mem) {
                String uAcc = accInfo.getAccount();
                if(Server.isOnline(uAcc)){
                    Server.getAllLinks().get(uAcc).msgToClient(msg);
                    res = new Respond(code, 9000);
                } else {
                    msg.setTo(uAcc);
                    Resender.addPending(msg);
                    res = new Respond(code, 9000);
                }
            }

            DBOperation.addGroupChatMessage(from, to, content);

        }

        return new Content<Respond>(code, res.getStatus(), res);
    }

    private static boolean isGroupAcc(String to) {
        return to.length() == 5;
    }

    private static boolean isUserAcc(String to) {
        return to.length() >= 6 && to.length() <= 10;
    }

}
