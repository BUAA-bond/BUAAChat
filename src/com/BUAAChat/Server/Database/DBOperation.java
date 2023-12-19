package com.BUAAChat.Server.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.BUAAChat.Server.Config.Config;
import com.BUAAChat.Server.Message.AccInfo;
import com.BUAAChat.Server.Message.ChatMsg;
import com.BUAAChat.Server.Message.FriendReq;
import com.BUAAChat.Server.Server.Server;
import com.BUAAChat.Server.Util.EncryptUtil;
import com.BUAAChat.Server.Util.JsonUtil;
import com.google.gson.Gson;

/**
 * Database operations.
 * @author WnRock
 * @version 0.1
 */
public class DBOperation {

    private static final String JDBC_DRIVER = Config.JDBC_DRIVER;
    private static final String DB_URL = Config.DB_URL;
    private static final String DB_USER = Config.DB_USER;
    private static final String DB_PASS = Config.DB_PASS;

    private static Connection conn = null;
    private static Statement stmt = null;
    private static String sql = null;
    private static ResultSet rs = null;

    /**
     * Database connection init.
     */
    public static void init(){
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            stmt = conn.createStatement();
        } catch (Exception e) {
            System.err.println("DB CONN FAIL.");
            e.printStackTrace();
        }
    }

    private static void exeNoRet(String SQL){
        try {
            sql = SQL;
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ;
    }

    /**
     * Get user password and salt from database.
     * @param String account
     * @return String json
     */
    public static AccInfo getUserPassword(String account){
        AccInfo ai = new AccInfo();
        sql = SQL.selectByAccount(account, "uID", "uPassword", "uSalt");

        try {
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                ai.setPassword(rs.getString("uPassword"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ai;
    }

    /**
     * Get user password and salt from database.
     * @param String account
     * @return String json
     */
    public static AccInfo getUserSalt(String account){
        AccInfo ai = new AccInfo();
        sql = SQL.selectByAccount(account, "uID", "uPassword", "uSalt");

        try {
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                ai.setSalt(rs.getString("uSalt"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ai;
    }

    /**
     * Get all the info of a user.
     * @param String account
     * @return UserInfo info
     */
    public static AccInfo getUserInfo(String self,String targ,boolean msg){
        sql = SQL.selectByAccount(targ, "*");
        AccInfo ai = null;

        try {
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                if(!msg){
                    ai = new AccInfo(
                        rs.getString("uName"),
                        rs.getString("uAccount"),
                        rs.getString("uAvatarPath")
                    );
                } else {
                    ai = new AccInfo(
                        rs.getString("uName"),
                        rs.getString("uAccount"),
                        rs.getString("uAvatarPath"),
                        getAllMessageByUser(self,targ)
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ai;
    }

    /**
     * Insert user info into database.
     * @param String json containing info
     */
    public static void insertUser(String json){
        Map<String, String> info = JsonUtil.jsonToMap(json);
        String name = info.get("name");
        String acnt = info.get("account");
        String pwd = info.get("password");
        String avatar = info.get("avatar");
        String salt = EncryptUtil.randomSalt(Config.SALT_LEN);
        pwd = EncryptUtil.encryptString(pwd, salt);

        String s = SQL.insertUserInfo(acnt, name, pwd, salt, avatar);
        exeNoRet(s);

        return ;
    }

    /**
     * Get user ID from database by account.
     * @param String account
     * @return String ID
     */
    public static String getIDByAccount(String account){
        Gson gson = new Gson();
        Map<String, String> user = new LinkedHashMap<>();
        ArrayList<Map<String, String>> info = new ArrayList<>();
        String s = SQL.selectByAccount(account, "uID");

        try {
            rs = stmt.executeQuery(s);
            while(rs.next()){
                user.clear();
                user.put("uID", rs.getString("uID"));
                info.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(info.size()!=1) return null;
        return gson.toJson(info);
    }

    /**
     * check if a user account has been used (user exist).
     * @param String account
     * @return boolean result
     */
    public static boolean isUserExistByAcc(String uAcc){
        Map<String, String> user = new LinkedHashMap<>();
        ArrayList<Map<String, String>> info = new ArrayList<>();
        String s = SQL.selectByAccount(uAcc, "uID");

        try {
            rs = stmt.executeQuery(s);
            while(rs.next()){
                user.clear();
                user.put("uID", rs.getString("uID"));
                info.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return info.size() == 1 ;
    }

    /**
     * Add user info to database.
     * @param String groupID
     * @param String userID
     */
    public static void joinGroup(String gAcc,String uAcc){
        String s = SQL.insertGroupMember(gAcc, uAcc, "0");
        exeNoRet(s);
        return ;
    }

    /**
     * Delete user info from database.
     * @param String user account
     */
    public static void deleteUser(String uAcc){
        String s = SQL.deleteUserInfo(uAcc);
        exeNoRet(s);
        return ;
    }

    /**
     * get group name by account
     * @param String group account
     * @return String group name
     */
    public static String getGroupName(String gAcc){
        sql = SQL.selectGroupInfoByGAcc(gAcc, "gName");
        String name = null;

        try {
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                name = rs.getString("gName");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return name;
    }

    /**
     * Get the info of members of a group.
     * @param String group account
     * @return ArrayList<UserInfo> members
     */
    public static ArrayList<AccInfo> getGroupMember(String gAcc){
        ArrayList<AccInfo> members = new ArrayList<>();
        ArrayList<String> uAcc = new ArrayList<>();
        sql = SQL.selectGroupMemberByGAcc(gAcc, "uAccount");

        try {
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                uAcc.add(rs.getString("uAccount"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String u : uAcc) {
            members.add(getUserInfo(u, u, false));
        }

        return members;
    }

    /**
     * check if user is in the given group
     * @param String user account
     * @param String group account
     * @return boolean result
     */
    public static boolean isInGroup(String uAcc,String gAcc){
        sql = SQL.selectGroupUser(uAcc, gAcc);
        ArrayList<Map<String,String>> tmp = new ArrayList<>();
        Map<String,String> info = new HashMap<>();

        try {
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                info.clear();
                info.put("uAccount", rs.getString("uAccount"));
                tmp.add(info);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tmp.size() >= 1;
    }

    /**
     * get info of group.
     * @param String group account
     * @return GroupInfo group info
     */
    public static AccInfo getGroupInfo(String gAccount,boolean msg){
        AccInfo g = new AccInfo();
        if(!msg){
            g.setAccount(gAccount);
            g.setName(getGroupName(gAccount));
            g.setAvatar(getGroupAvatar(gAccount));
            g.setMembers(getGroupMember(gAccount));
        } else {
            g.setAccount(gAccount);
            g.setName(getGroupName(gAccount));
            g.setAvatar(getGroupAvatar(gAccount));
            g.setMembers(getGroupMember(gAccount));
            g.setMessages(getAllMessageByChatAcc(gAccount));
        }
        
        return g;
    }

    /**
     * Get avatar of a group.
     * @param String gAccount
     * @return String
     */
    public static String getGroupAvatar(String gAccount) {
        sql = SQL.selectGroupInfoByGAcc(gAccount, "gAvatarPath");
        String res = null;
        try {
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                res = rs.getString("gAvatarPath");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * get all groups of user
     * @param String user account
     * @return ArrayList<GroupInfo>
     */
    public static ArrayList<AccInfo> getAllGroups(String uAcc){
        ArrayList<String> allgAcc = new ArrayList<>();
        ArrayList<AccInfo> allGroup = new ArrayList<>();
        sql = SQL.selectAllGAccByUAcc(uAcc);
        try {
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                allgAcc.add(rs.getString("gAccount"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String gAcc : allgAcc) {
            allGroup.add(getGroupInfo(gAcc,true));
        }
        return allGroup;
    }

    /**
     * Add friend relation to database.
     * @param String uAcc_A
     * @param String uAcc_B
     */
    public static void putNewFriend(String uAcc_A, String uAcc_B){
        sql = SQL.insertFriendRelation(uAcc_A, uAcc_B);
        exeNoRet(sql);
        return ;
    }

    /**
     * get messages of given chat account ( single or group )
     * @param String account
     * @return ArrayList<ChatMsg>
     */
    public static ArrayList<ChatMsg> getAllMessageByChatAcc(String chat){
        ArrayList<ChatMsg> all = new ArrayList<>();
        sql = SQL.selectChatMessageByAcc(chat, "*");
        Map<String,String> info = new HashMap<>();

        try {
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                info.clear();
                ChatMsg m = new ChatMsg(
                    rs.getString("sender"),
                    rs.getString("sendTime"),
                    rs.getString("content")
                );
                all.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return all;
    }

    /**
     * Get messages of a user.
     * @param String user1
     * @param String user2
     * @return ArrayList<ChatMsg>
     */
    public static ArrayList<ChatMsg> getAllMessageByUser(String u1,String u2){
        ArrayList<ChatMsg> all = new ArrayList<>();
        sql = SQL.selectFriendChatMessage(u1, u2, "*");
        Map<String,String> info = new HashMap<>();

        try {
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                info.clear();
                ChatMsg m = new ChatMsg(
                    rs.getString("sender"),
                    rs.getString("sendTime"),
                    rs.getString("content")
                );
                all.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return all;
    }

    /**
     * get info of all friends from database.
     * @param String user account
     * @return ArrayList<UserInfo>
     */
    public static ArrayList<AccInfo> getAllFriendInfo(String uAcc){
        ArrayList<AccInfo> allFriends = new ArrayList<>();
        ArrayList<String> alluAcc = new ArrayList<>();
        sql = SQL.selectFriends(uAcc);
        try {
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                alluAcc.add(rs.getString("uAccount"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String uacc : alluAcc) {
            allFriends.add(getUserInfo(uacc,uacc, true));
        }
        return allFriends;
    }

    /**
     * update user avatar in database.
     * @param String user account
     * @param String new avatar
     */
    public static void updateUserAvatar(String uAcc,String avatar){
        sql = SQL.updateUserAvatar(uAcc, avatar);
        exeNoRet(sql);
        return ;
    }

    /**
     * update user name in database.
     * @param String user account
     * @param String new name
     */
    public static void updateUserName(String uAcc,String name){
        sql = SQL.updateUserName(uAcc, name);
        exeNoRet(sql);
        return ;
    }

    /**
     * update user password in database.
     * @param String user account
     * @param String new password
     */
    public static void updateUserPassword(String uAcc,String pwd){
        String salt = getUserSalt(uAcc).getSalt();
        String saltPwd = EncryptUtil.encryptString(pwd, salt);

        sql = SQL.updateUserPassword(uAcc, saltPwd);
        exeNoRet(sql);
        return ;
    }

    /**
     * Add friend request history.
     * @param String json
     */
    public static void addFriReqHistory(String json){
        FriendReq fr = JsonUtil.jsonToContentData(json, FriendReq.class);
        String sender = fr.getAccount_A();
        String recipient = fr.getAccount_B();

        sql = SQL.insertFriReqHistory(sender, recipient);
        exeNoRet(sql);
        
        return ;
    }

    /**
     * Update friend request history (to 'accepted' or 'rejected').
     * @param String json
     */
    public static void updateFriReqHistory(String json){
        FriendReq fr = JsonUtil.jsonToData(json, FriendReq.class);
        String sender = fr.getAccount_A();
        String recipient = fr.getAccount_B();
        String status = fr.getStatus();

        sql = SQL.updateFriReqStatus(sender, recipient, status);
        exeNoRet(sql);

        return ;
    }

    /**
     * Get friend request history.
     * @param String acc
     * @return ArrayList<FriendReq>
     */
    public static ArrayList<FriendReq> getFriReqHistory(String acc){
        FriendReq fr = null;
        ArrayList<FriendReq> history = new ArrayList<>();
        sql = SQL.selectFriReqHistoryByAcc(acc);

        try {
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                fr = new FriendReq();
                fr.setSender(rs.getString("senderAcc"));
                fr.setRecipient(rs.getString("recipientAcc"));
                fr.setUpdateTime(rs.getString("updateTime"));
                fr.setStatus(rs.getString("status"));
                history.add(fr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return history;
    }

    /**
     * Create a new group.
     * @param String uAcc
     * @param String gAcc
     * @param String gName
     * @param String gAvatar
     */
    public static void createNewGroup(String uAcc,String gAcc,String gName,String gAvatar){
        sql = SQL.insertGroupInfo(gAcc, gName, gAvatar);
        exeNoRet(sql);

        sql = SQL.insertGroupMember(gAcc, uAcc, "2");
        exeNoRet(sql);
    }

    /**
     * Add friend chat messages.
     * @param String sender
     * @param String recipient
     * @param String content
     */
    public static void addFriendChatMessage(String sender,String recipient,String content){
        sql = SQL.insertFriendChatMsg(sender, recipient, content);
        exeNoRet(sql);
        Server.printDebug("[DEBUG] Adding chat message (friend).");
        return ;
    }

    /**
     * Add group chat messages.
     * @param String sender
     * @param String gAcc
     * @param String content
     */
    public static void addGroupChatMessage(String sender,String gAcc,String content){
        sql = SQL.insertGroupChatMsg(sender, gAcc, content);
        exeNoRet(sql);
        return ;
    }

    /**
     * Quit from a group.
     * @param String gAcc
     * @param String uAcc
     */
    public static void quitGroup(String gAcc, String uAcc) {
        sql = SQL.deleteGroupMember(uAcc, gAcc);
        exeNoRet(sql);
        return ;
    }

    /**
     * Remove a friend.
     * @param String uAcc_A
     * @param String uAcc_B
     */
    public static void removeFriend(String uAcc_A,String uAcc_B){
        sql = SQL.deleteFriend(uAcc_A, uAcc_B);
        exeNoRet(sql);
        return ;
    }

    /**
     * Block a user.
     * @param String uAcc_A
     * @param String uAcc_B
     */
    public static void blockFriend(String uAcc_A,String uAcc_B){
        sql = SQL.blockFriend(uAcc_A, uAcc_B);
        exeNoRet(sql);
        return ;
    }

    /**
     * If two user have friend relation
     * @param String uAcc_A
     * @param String uAcc_B
     * @return boolean
     */
    public static boolean isFriend(String uAcc_A,String uAcc_B){
        sql = SQL.seleceFriend(uAcc_A, uAcc_B);
        int cnt = 0;

        try {
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                cnt++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cnt==1;
    }

    /**
     * If one has blocked his friend
     * @param String uAcc_A
     * @param String uAcc_B
     * @return boolean 
     */
    public static boolean isNotBlocked(String uAcc_A,String uAcc_B){
        sql = SQL.seleceFriend(uAcc_A, uAcc_B);
        boolean valid = false;

        try {
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                valid = rs.getBoolean("uIsValid");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return valid;
    }

    /**
     * DELETE ALL THE TABLES FROM DATABASE. Good luck!  :(
     * @param void NO PARAM
     * @return NOTHING LEFT
     */
    public static void destroy(){
        exeNoRet(SQL.dropChatMessageTable());
        exeNoRet(SQL.dropFriReqHistoryTable());
        exeNoRet(SQL.dropFriendRelationTable());
        exeNoRet(SQL.dropGroupInfoTable());
        exeNoRet(SQL.dropGroupMemberTable());
        exeNoRet(SQL.dropUserInfoTable());
    }

    /**
     * RE-CREATE ALL THE TABLES FROM DATABASE.  :)
     * @param void NO PARAM
     * @return EVERYTHING
     */
    public static void rebuild(){
        exeNoRet(SQL.createChatMessageTable());
        exeNoRet(SQL.createFriReqHistoryTable());
        exeNoRet(SQL.createFriendRelationTable());
        exeNoRet(SQL.createGroupInfoTable());
        exeNoRet(SQL.createGroupMemberTable());
        exeNoRet(SQL.createUserInfoTable());
    }
    
}
