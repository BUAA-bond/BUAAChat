package com.BUAAChat.Server.Server;

import java.util.ArrayList;

import com.BUAAChat.Server.Database.DBOperation;
import com.BUAAChat.Server.Message.AccInfo;
import com.BUAAChat.Server.Message.Content;
import com.BUAAChat.Server.Message.Respond;
import com.BUAAChat.Server.Util.JsonUtil;
import com.google.gson.Gson;

/**
 * Group utils.
 * @author WnRock
 * @version 0.1
 */
public class Group {

    private static Gson gson = new Gson();

    /**
     * get info of group.
     * @param String group account
     * @return GroupInfo group info
     */
    public static Content<AccInfo> getGroupInfo(Content<AccInfo> param){
        AccInfo ai = JsonUtil.jsonToContentData(gson.toJson(param), AccInfo.class);
        String acc = ai.getgAccount();

        return new Content<AccInfo>(
            param.getCode(), 9000, 
            DBOperation.getGroupInfo(acc,false)
        );
    }

    /**
     * get info of all groups.
     * @param String group account
     * @return ArrayList<GroupInfo> info of all groups
     */
    public static Content<ArrayList<AccInfo>> getAllGroupsInfo(Content<AccInfo> param){
        AccInfo ai = JsonUtil.jsonToContentData(gson.toJson(param), AccInfo.class);
        String uacc = ai.getuAccount();

        return new Content<ArrayList<AccInfo>>(
            param.getCode(), 9000, 
            DBOperation.getAllGroups(uacc)
        );
    }

    /**
     * get info of a user.
     * @param String param
     * @return UserInfo info
     */
    public static Content<AccInfo> getUserInfo(Content<AccInfo> param) {
        AccInfo ai = JsonUtil.jsonToContentData(gson.toJson(param), AccInfo.class);
        String acc = ai.getAccount();
        String uacc = ai.getuAccount();

        AccInfo info = DBOperation.getUserInfo(acc,uacc,false);
        if( info==null )
            return new Content<AccInfo>( param.getCode(), 9101, null );
        else
            return new Content<AccInfo>( param.getCode(), 9000, info );
    }

    /**
     * get info of all friends.
     * @param String param
     * @return ArrayList<UserInfo> info
     */
    public static Content<ArrayList<AccInfo>> getAllFriendsInfo(Content<AccInfo> param) {
        AccInfo ai = JsonUtil.jsonToContentData(gson.toJson(param), AccInfo.class);
        String uacc = ai.getuAccount();

        return new Content<ArrayList<AccInfo>>(
            param.getCode(), 9000, 
            DBOperation.getAllFriendInfo(uacc)
        );
    }

    /**
     * Create a new group. Insert group info and group leader info.
     * @param Content<AccInfo> param
     * @return Content<Respond>
     */
    public static Content<Respond> createNewGroup(Content<AccInfo> param){
        AccInfo ai = JsonUtil.jsonToContentData(gson.toJson(param), AccInfo.class);
        String uAcc = ai.getuAccount();
        String gAcc = ai.getgAccount();
        String gName = ai.getName();
        String gAvatar = ai.getAvatar();

        DBOperation.createNewGroup(uAcc, gAcc, gName, gAvatar);

        Server.printDebug(
            "Account "+uAcc+" create new group. Group name "+gName+", account "+gAcc+", avatar "+gAvatar
        );

        return new Content<Respond>(
            param.getCode(), 9000, 
            new Respond(param.getCode(), 9000)
        );
    }

    /**
     * Join a group.
     * @param Content<AccInfo> param
     * @return Content<Respond>
     */
    public static Content<Respond> joinGroup(Content<AccInfo> param) {
        AccInfo ai = JsonUtil.jsonToContentData(gson.toJson(param), AccInfo.class);
        String uAcc = ai.getuAccount();
        String gAcc = ai.getgAccount();

        DBOperation.joinGroup(gAcc, uAcc);
        return new Content<Respond>(
            param.getCode(), 9000, 
            new Respond(param.getCode(), 9000)
        );
    }

    /**
     * Quit from a group.
     * @param Content<AccInfo> param
     * @return Content<Respond>
     */
    public static Content<Respond> quitGroup(Content<AccInfo> param) {
        AccInfo ai = JsonUtil.jsonToContentData(gson.toJson(param), AccInfo.class);
        String uAcc = ai.getuAccount();
        String gAcc = ai.getgAccount();

        DBOperation.quitGroup(gAcc, uAcc);
        return new Content<Respond>(
            param.getCode(), 9000, 
            new Respond(param.getCode(), 9000)
        );
    }

}
