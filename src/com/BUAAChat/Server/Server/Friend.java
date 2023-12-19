package com.BUAAChat.Server.Server;

import java.util.ArrayList;

import com.BUAAChat.Message;
import com.BUAAChat.Server.Database.DBOperation;
import com.BUAAChat.Server.Message.Content;
import com.BUAAChat.Server.Message.FriendReq;
import com.BUAAChat.Server.Message.Respond;
import com.BUAAChat.Server.Util.JsonUtil;
import com.google.gson.Gson;

/**
 * Friend-relation utils.
 * @author WnRock
 * @version 0.1
 */
public class Friend {

    private static Gson gson = new Gson();

    /**
     * Reject another user's friend request.
     * @param Content<FriendReq> param
     * @return Content<Respond>
     */
    public static Content<Respond> declineRequest(Content<FriendReq> param) {
        FriendReq fr = JsonUtil.jsonToContentData(gson.toJson(param), FriendReq.class);

        String acca = fr.getAccount_A();
        String accb = fr.getAccount_B();
        String code = param.getCode();
        Respond res = null;

        param.setStatus(9000);

        Link.sendToUser(
            new Message(
                accb, acca, gson.toJson(param)
            )
        );
        res = new Respond(code, 9000);

        fr.setStatus("rejected");
        DBOperation.updateFriReqHistory(gson.toJson(fr));

        Server.printDebug("Account "+accb+" reject "+acca+"'s FRIEND REQUEST.");

        return new Content<Respond>(code, res.getStatus(), res);
    }

    /**
     * Accept another user's friend request.
     * @param Content<FriendReq> param
     * @return Content<Respond>
     */
    public static Content<Respond> acceptRequest(Content<FriendReq> param) {
        FriendReq fr = JsonUtil.jsonToContentData(gson.toJson(param), FriendReq.class);

        String acca = fr.getAccount_A();
        String accb = fr.getAccount_B();
        String code = param.getCode();
        Respond res = null;

        DBOperation.putNewFriend(acca, accb);

        param.setStatus(9000);
        
        Link.sendToUser(
            new Message( accb, acca, gson.toJson(param) )
        );
        res = new Respond(code, 9000);

        fr.setStatus("accepted");
        DBOperation.updateFriReqHistory(gson.toJson(fr));

        Server.printDebug("Account "+accb+" accept "+acca+"'s FRIEND REQUEST.");

        return new Content<Respond>(code, res.getStatus(), res);
    }

    /**
     * Send firend request to another user.
     * @param Content<FriendReq> param
     * @return Content<Respond>
     */
    public static Content<Respond> sendRequest(Content<FriendReq> param) {
        FriendReq fr = JsonUtil.jsonToContentData(gson.toJson(param), FriendReq.class);

        String acca = fr.getAccount_A();
        String accb = fr.getAccount_B();
        String code = param.getCode();

        Respond res = null;

        if( DBOperation.isUserExistByAcc(accb) ){
            Link.sendToUser(
                new Message(
                    acca, accb, gson.toJson(param)
                )
            );

            DBOperation.addFriReqHistory(gson.toJson(param));

            res = new Respond(code, 9000);
        }
        else res = new Respond(code, 9201);

        Server.printDebug("Account "+acca+" send FRIEND REQUEST to "+accb+".");

        return new Content<Respond>(
            code, res.getStatus(), res
        );
    }

    /**
     * Get friend request history of a user.
     * @param Content<FriendReq> param
     * @return Content<FriendReq>
     */
    public static Content<FriendReq> getFriendReqHistory(Content<FriendReq> param) {
        FriendReq fr = JsonUtil.jsonToContentData(gson.toJson(param), FriendReq.class);
        String uAcc = fr.getuAccount();

        ArrayList<FriendReq> history = DBOperation.getFriReqHistory(uAcc);
        FriendReq res = new FriendReq();
        res.setHistory(history);

        Content<FriendReq> con = new Content<FriendReq>(
            param.getCode(), 9000, res
        );

        return con;
    }

    /**
     * Remove a friend.
     * @param Content<FriendReq> param
     * @return Content<Respond>
     */
    public static Content<Respond> removeFriend(Content<FriendReq> param) {
        FriendReq fr = JsonUtil.jsonToContentData(gson.toJson(param), FriendReq.class);
        String uAcc_A = fr.getAccount_A();
        String uAcc_B = fr.getAccount_B();

        DBOperation.removeFriend(uAcc_A, uAcc_B);

        Server.printDebug("Account "+uAcc_A+" remove friend "+uAcc_B+".");

        return new Content<Respond>(
            param.getCode(), 9000,
            new Respond(param.getCode(), 9000)
        );
    }

    /**
     * Block a friend.
     * @param Content<FriendReq> param
     * @return Content<Respond>
     */
    public static Content<Respond> blockFriend(Content<FriendReq> param) {
        FriendReq fr = JsonUtil.jsonToContentData(gson.toJson(param), FriendReq.class);
        String uAcc_A = fr.getAccount_A();
        String uAcc_B = fr.getAccount_B();

        DBOperation.blockFriend(uAcc_A, uAcc_B);

        Server.printDebug("Account "+uAcc_A+" block friend "+uAcc_B+".");

        return new Content<Respond>(
            param.getCode(), 9000,
            new Respond(param.getCode(), 9000)
        );
    }
    
}
