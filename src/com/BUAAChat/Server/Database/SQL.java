package com.BUAAChat.Server.Database;

import com.BUAAChat.Server.Config.Config;

/**
 * Frequently used SQL statement.
 * @author WnRock
 * @version 0.1
 */
public class SQL {

    private static final String USER = Config.USER_TABLE;
    private static final String FRIEND = Config.FRIEND_TABLE;
    private static final String GROUP_INFO = Config.GROUP_INFO_TABLE;
    private static final String GROUP_MEMBER = Config.GROUP_MEMBER_TABLE;
    private static final String CHAT_MESSAGE = Config.CHAT_MESSAGE_TABLE;
    private static final String FRIREQ_HISTORY = Config.FRIEND_REQUEST_HISTORY_TABLE;

    /**
     * Create user_info table.
     * @return String SQL
     */
    public static String createUserInfoTable(){
        String str = "CREATE TABLE " + USER + " (";
        str += "uID int AUTO_INCREMENT PRIMARY KEY,";
        str += "uName VARCHAR(50) NOT NULL,";
        str += "uAccount VARCHAR(50) NOT NULL,";
        str += "uPassword VARCHAR(100) NOT NULL,";
        str += "uSalt VARCHAR(100) NOT NULL,";
        str += "uAvatarPath VARCHAR(200),";
        str += "uRegisterTime DATETIME DEFAULT CURRENT_TIMESTAMP,";
        str += "uIsValid BOOLEAN DEFAULT TRUE";
        str += ");";
        return str;
    }

    /**
     * Create friend_relation table.
     * @return String SQL
     */
    public static String createFriendRelationTable(){
        String str = "CREATE TABLE " + FRIEND + " (";
        str += "fID int AUTO_INCREMENT PRIMARY KEY,";
        str += "uAccount_A VARCHAR(50) NOT NULL,";
        str += "uAccount_B VARCHAR(50) NOT NULL,";
        str += "uTime DATETIME DEFAULT CURRENT_TIMESTAMP,";
        str += "uIsValid BOOLEAN DEFAULT TRUE";
        str += ");";
        return str;
    }

    /**
     * Create group info table.
     * @return String SQL
     */
    public static String createGroupInfoTable(){
        String str = "CREATE TABLE " + GROUP_INFO + " (";
        str += "gID int AUTO_INCREMENT PRIMARY KEY,";
        str += "gAccount VARCHAR(50) NOT NULL,";
        str += "gName VARCHAR(50) NOT NULL,";
        str += "gCreateTime DATETIME DEFAULT CURRENT_TIMESTAMP,";
        str += "gAvatarPath VARCHAR(200)";
        str += ");";
        return str;
    }

    /**
     * Create friend request history table.
     * @return String SQL
     */
    public static String createFriReqHistoryTable(){
        String str = "CREATE TABLE " + FRIREQ_HISTORY + " (";
        str += "hID int AUTO_INCREMENT PRIMARY KEY,";
        str += "senderAcc VARCHAR(50) NOT NULL,";
        str += "recipientAcc VARCHAR(50) NOT NULL,";
        str += "updateTime DATETIME DEFAULT CURRENT_TIMESTAMP,";
        str += "status VARCHAR(200) NOT NULL";
        str += ");";
        return str;
    }

    /**
     * Create group member table.
     * @return String SQL
     */
    public static String createGroupMemberTable(){
        String str = "CREATE TABLE " + GROUP_MEMBER + " (";
        str += "item int AUTO_INCREMENT PRIMARY KEY,";
        str += "gAccount VARCHAR(50) NOT NULL,";
        str += "uAccount VARCHAR(50) NOT NULL,";
        str += "uJoinTime DATETIME DEFAULT CURRENT_TIMESTAMP,";
        str += "uRole int DEFAULT 0";
        str += ");";
        return str;
    }

    /**
     * Create chat message table.
     * @return String SQL
     */
    public static String createChatMessageTable(){
        String str = "CREATE TABLE " + CHAT_MESSAGE + " (";
        str += "mID int AUTO_INCREMENT PRIMARY KEY,";
        str += "chatAccount VARCHAR(50),";
        str += "sender VARCHAR(50) NOT NULL,";
        str += "recipient VARCHAR(50),";
        str += "content VARCHAR(500) NOT NULL,";
        str += "sendTime DATETIME DEFAULT CURRENT_TIMESTAMP";
        str += ");";
        return str;
    }

    /**
     * Drop user info table.
     * @return String SQL
     */
    public static String dropUserInfoTable(){
        String res = "DROP TABLE IF EXISTS " + USER + " ;";
        return res;
    }

    /**
     * Drop Friend request history table.
     * @return String SQL
     */
    public static String dropFriReqHistoryTable(){
        String res = "DROP TABLE IF EXISTS " + FRIREQ_HISTORY + " ;";
        return res;
    }

    /**
     * Drop friend relation table.
     * @return String SQL
     */
    public static String dropFriendRelationTable(){
        String res = "DROP TABLE IF EXISTS " + FRIEND + " ;";
        return res;
    }

    /**
     * Drop group info table.
     * @return String SQL
     */
    public static String dropGroupInfoTable(){
        String res = "DROP TABLE IF EXISTS " + GROUP_INFO + " ;";
        return res;
    }

    /**
     * Drop chat message table.
     * @return String SQL
     */
    public static String dropChatMessageTable(){
        String res = "DROP TABLE IF EXISTS " + CHAT_MESSAGE + " ;";
        return res;
    }

    /**
     * Drop group member table.
     * @return String SQL
     */
    public static String dropGroupMemberTable(){
        String res = "DROP TABLE IF EXISTS " + GROUP_MEMBER + " ;";
        return res;
    }
    
    /**
     * Select from USER_TABLE by user ID.
     * @param String id
     * @param String... column to get
     * @return String SQL
     */
    public static String selectByID(String id, String... column){
        String str = "SELECT " + column[0];
        int len = column.length;
        for(int i=1;i<len;i++) str += ", " + column[i];
        str += " FROM " + USER ;
        str += " WHERE uID = '" + id + "';";
        return str;
    }

    /**
     * Select from USER_TABLE by user account.
     * @param String account
     * @param String... column to get
     * @return String SQL
     */
    public static String selectByAccount(String account, String... column){
        String str = "SELECT " + column[0];
        int len = column.length;
        for(int i=1;i<len;i++) str += ", " + column[i];
        str += " FROM " + USER ;
        str += " WHERE uAccount = '" + account + "';";
        return str;
    }

    /**
     * Insert user info into USER_TABLE.
     * @param String account
     * @param String name
     * @param String password
     * @param String salt
     * @return String SQL
     */
    public static String insertUserInfo(String account,String name,String password,String salt,String avatar){
        String str = "INSERT INTO " + USER;
        str += " (uName,uAccount,uPassword,uSalt,uAvatarPath) VALUES ('" + name;
        str += "', '" + account + "', '" + password + "', '" + salt;
        str += "', '" + avatar + "');";
        return str;
    }

    /**
     * Insert user info into USER_TABLE.
     * @param String account
     * @param String name
     * @param String password
     * @param String salt
     * @return String SQL
     */
    public static String insertFriReqHistory(String account_A,String account_B){
        String str = "INSERT INTO " + FRIREQ_HISTORY;
        str += " (senderAcc, recipientAcc, status) VALUES ('" + account_A;
        str += "', '" + account_B + "', '" + "pending";
        str += "');";
        return str;
    }

    /**
     * Update Friend request status.
     * @param String acc_A
     * @param String acc_B
     * @param String status
     * @return SQL
     */
    public static String updateFriReqStatus(String acc_A,String acc_B,String status){
        String str = "UPDATE " + FRIREQ_HISTORY + " ";
        str += "SET status = '" + status + "' ";
        str += "WHERE senderAcc = '" + acc_A + "' AND ";
        str += "recipientAcc = '" + acc_B + "';";
        return str;
    }

    /**
     * Insert id of both users who become friend.
     * @param String ida
     * @param String idb
     * @return String SQL
     */
    public static String insertFriendRelation(String uacca,String uaccb){
        String str = "INSERT INTO " + FRIEND;
        str += " (uAccount_A, uAccount_B) VALUES ('" + uacca;
        str += "', '" + uaccb + "');";
        return str;
    }

    /**
     * Insert messages from friend.
     * @param String send
     * @param String recipient
     * @param String content
     * @return SQL
     */
    public static String insertFriendChatMsg(String send,String recipient,String content){
        String str = "INSERT INTO " + CHAT_MESSAGE;
        str += " (sender,recipient,content) VALUES ('" + send;
        str += "', '" + recipient + "', '" + content + "');";
        return str;
    }

    /**
     * Insert messages from group.
     * @param String sender
     * @param String gAccount
     * @param String content
     * @return SQL
     */
    public static String insertGroupChatMsg(String sender,String gAccount,String content){
        String str = "INSERT INTO " + CHAT_MESSAGE;
        str += " (sender,chatAccount,content) VALUES ('" + sender;
        str += "', '" + gAccount + "', '" + content + "');";
        return str;
    }

    /**
     * Insert new group info.
     * @param String group account
     * @param String group name
     * @param String group avatar
     * @return String SQL
     */
    public static String insertGroupInfo(String gAcc,String gName,String gAvatar){
        String str = "INSERT INTO " + GROUP_INFO + " ";
        str += " (gAccount, gName, gAvatarPath) VALUES ('" + gAcc;
        str += "', '" + gName + "', '" + gAvatar + "');";
        return str;
    }

    /**
     * Insert into group member table.
     * @param String group account
     * @param String user account
     * @param String user role
     * @return String SQL
     */
    public static String insertGroupMember(String gAcc,String uAcc,String role){
        String str = "INSERT INTO " + GROUP_MEMBER;
        str += " (gAccount,uAccount,uRole) VALUES ('" + gAcc;
        str += "', '" + uAcc + "', '" + role + "');";
        return str;
    }

    /**
     * delete user info from database
     * @param String user account
     * @return String SQL
     */
    public static String deleteUserInfo(String uAcc){
        String str = "DELETE FROM " + USER;
        str += " WHERE uAccount = '" + uAcc + "';";
        return str;
    }

    /**
     * select statement to get all the members of a group.
     * @param String group account
     * @return String SQL
     */
    public static String selectGroupMemberByGAcc(String gAcc, String... column){
        String str = "SELECT " + column[0];
        int len = column.length;
        for(int i=1;i<len;i++) str += ", " + column[i];
        str += " FROM " + GROUP_MEMBER ;
        str += " WHERE gAccount = '" + gAcc + "';";
        return str;
    }

    /**
     * select statement to get info of group
     * @param String gAcc
     * @param String... column to get
     * @return String SQL
     */
    public static String selectGroupInfoByGAcc(String gAcc,String... column){
        String str = "SELECT " + column[0];
        int len = column.length;
        for(int i=1;i<len;i++) str += ", " + column[i];
        str += " FROM " + GROUP_INFO ;
        str += " WHERE gAccount = '" + gAcc + "';";
        return str;
    }

    /**
     * select all groups the user is in
     * @param String uAcc user account
     * @return String SQL
     */
    public static String selectAllGAccByUAcc(String uAcc){
        String str = "SELECT " + "gAccount";
        str += " FROM " + GROUP_MEMBER ;
        str += " WHERE uAccount = '" + uAcc + "';";
        return str;
    }

    /**
     * select messages from chat channel
     * @param String acc
     * @param String... column
     * @return String SQL
     */
    public static String selectChatMessageByAcc(String acc,String... column){
        String str = "SELECT " + column[0];
        int len = column.length;
        for(int i=1;i<len;i++) str += ", " + column[i];
        str += " FROM " + CHAT_MESSAGE ;
        str += " WHERE chatAccount = '" + acc + "' ";
        str += "OR recipient = '" + acc + "';";
        return str;
    }

    /**
     * Select friend's messages.
     * @param String acc1
     * @param String acc2
     * @param String... column
     * @return SQL
     */
    public static String selectFriendChatMessage(String acc1,String acc2,String... column){
        String str = "SELECT " + column[0];
        int len = column.length;
        for(int i=1;i<len;i++) str += ", " + column[i];
        str += " FROM " + CHAT_MESSAGE ;
        str += " WHERE (sender = '" + acc1 + "' ";
        str += "AND recipient = '" + acc2 + "') OR";
        str += " (sender = '" + acc2 + "' AND recipient = '" + acc1 + "');";
        return str;
    }

    /**
     * select member account from group
     * @param String user account
     * @param String group account
     * @return String SQL
     */
    public static String selectGroupUser(String uAcc,String gAcc){
        String str = "SELECT uAccount FROM " + GROUP_MEMBER ;
        str += " WHERE uAccount = '" + uAcc ;
        str += "' AND gAccount = '" + gAcc + "';";
        return str;
    }

    /**
     * select friends of user
     * @param String user account
     * @return String SQL
     */
    public static String selectFriends(String uAcc){
        String str = "SELECT uAccount_A AS uAccount FROM " + FRIEND;
        str += " WHERE uAccount_B = '" + uAcc + "' ";
        str += "UNION ";
        str += "SELECT uAccount_B AS uAccount FROM " + FRIEND;
        str += " WHERE uAccount_A = '" + uAcc + "' ";
        return str;
    }

    /**
     * select friend request history from certain table
     * @param String account
     * @return String SQL
     */
    public static String selectFriReqHistoryByAcc(String acc){
        String str = "SELECT * FROM " + FRIREQ_HISTORY + " ";
        str += "WHERE senderAcc = '" + acc + "' ";
        str += "OR recipientAcc = '" + acc + "' ";
        return str;
    }

    /**
     * update user name in database
     * @param String user account
     * @param String newName
     * @return String SQL
     */
    public static String updateUserName(String uAcc,String newName){
        String str = "UPDATE " + USER + " ";
        str += "SET uName = '" + newName + "' ";
        str += "WHERE uAccount = '" + uAcc + "';";
        return str;
    }

    /**
     * update user password in database
     * @param String user account
     * @param String new password (salt)
     * @return String SQL
     */
    public static String updateUserPassword(String uAcc,String newSaltedPwd){
        String str = "UPDATE " + USER + " ";
        str += "SET uPassword = '" + newSaltedPwd + "' ";
        str += "WHERE uAccount = '" + uAcc + "';";
        return str;
    }

    /**
     * update user avatar in database
     * @param String user account
     * @param String new avatar
     * @return String SQL
     */
    public static String updateUserAvatar(String uAcc,String newAvatar){
        String str = "UPDATE " + USER + " ";
        str += "SET uAvatarPath = '" + newAvatar + "' ";
        str += "WHERE uAccount = '" + uAcc + "';";
        return str;
    }

    /**
     * Delete group member.
     * @param String uAcc
     * @param String gAcc
     * @return SQL
     */
    public static String deleteGroupMember(String uAcc,String gAcc){
        String str = "DELETE FROM " + GROUP_MEMBER + " ";
        str += "WHERE uAccount = '" + uAcc + "' ";
        str += "AND gAccount = '" + gAcc + "';";
        return str;
    }

    /**
     * Delete friend.
     * @param String uAcc_A
     * @param String uAcc_B
     * @return SQL
     */
    public static String deleteFriend(String uAcc_A,String uAcc_B){
        String str = "DELETE FROM " + FRIEND + " ";
        str += "WHERE (uAccount_A = '" + uAcc_A + "' ";
        str += "AND uAccount_B = '" + uAcc_B + "') ";
        str += "OR (uAccount_A = '" + uAcc_B + "' AND ";
        str += "uAccount_B = '" + uAcc_A + "');";
        return str;
    }

    /**
     * Update uIsVaild field (block friend).
     * @param String uAcc_A
     * @param String uAcc_B
     * @return SQL
     */
    public static String blockFriend(String uAcc_A,String uAcc_B){
        String str = "UPDATE " + FRIEND + " ";
        str += "SET uIsValid = FALSE ";
        str += "WHERE (uAccount_A = '" + uAcc_A + "' ";
        str += "AND uAccount_B = '" + uAcc_B + "') ";
        str += "OR (uAccount_A = '" + uAcc_B + "' AND ";
        str += "uAccount_B = '" + uAcc_A + "');";
        return str;
    }

    /**
     * Select friend relation users.
     * @param String uAcc_A
     * @param String uAcc_B
     * @return SQL
     */
    public static String seleceFriend(String uAcc_A,String uAcc_B){
        String str = "SELECT * FROM " + FRIEND + " ";
        str += "WHERE (uAccount_A = '" + uAcc_A + "' ";
        str += "AND uAccount_B = '" + uAcc_B + "') ";
        str += "OR (uAccount_A = '" + uAcc_B + "' AND ";
        str += "uAccount_B = '" + uAcc_A + "');";
        return str;
    }

}
