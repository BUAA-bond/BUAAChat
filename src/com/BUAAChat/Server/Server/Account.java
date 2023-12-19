package com.BUAAChat.Server.Server;

import com.BUAAChat.Server.Database.DBOperation;
import com.BUAAChat.Server.Message.AccInfo;
import com.BUAAChat.Server.Message.Content;
import com.BUAAChat.Server.Message.Respond;
import com.BUAAChat.Server.Util.EncryptUtil;
import com.BUAAChat.Server.Util.JsonUtil;
import com.google.gson.Gson;

/**
 * Account-related utils.
 * @author WnRock
 * @version 0.1
 */
public class Account {

    private static Gson gson = new Gson();

    private static boolean accountFormatCheck(String acnt){
        return true;
    }

    private static boolean passwordFormatCheck(String pwd){
        return true;
    }

    private static boolean usernameFormatCheck(String name){
        return true;
    }

    /**
     * 
     * @param String password (input)
     * @param String password (from DB)
     * @param String salt
     * @return boolean Is the passwords match
     */
    public static boolean isPasswordMatch(String pwd, String std, String salt){
        String encrypt = EncryptUtil.encryptString(pwd, salt);
        return encrypt.equals(std);
    }

    /**
     * Check user existence and password
     * 9101 = no user or multiple users
     * 9103 = pass error
     * @param String account
     * @param String password
     * @return int status code
     */
    public static int loginCheck(String account,String pwd){
        boolean flag = DBOperation.isUserExistByAcc(account);
        if(!flag) return 9101;

        AccInfo ui = DBOperation.getUserPassword(account);
        String stdPwd = ui.getPassword();
        ui =  DBOperation.getUserSalt(account);
        String salt = ui.getSalt();

        if( !isPasswordMatch(pwd, stdPwd, salt) ) return 9103;
        return 9000;
    }


    /**
     * User login logic.
     * @param String json containing user login data
     * @return Respond respond
     */
    public static Content<AccInfo> login(Content<AccInfo> param){
        AccInfo ai = JsonUtil.jsonToContentData(gson.toJson(param), AccInfo.class);

        String acnt = ai.getAccount();
        String pwd = ai.getPassword();
        String code = param.getCode();

        Respond res = null;

        if(acnt==null){
            res = new Respond(code,9091, "account");
        } else if(pwd==null){
            res = new Respond(code,9091, "password");
        } else if(!accountFormatCheck(acnt)){
            res = new Respond(code,9092, "account");
        } else if(!passwordFormatCheck(pwd)){
            res = new Respond(code,9092, "password");
        } else {
            int check = loginCheck(acnt, pwd);
            if( check != 9000 ) 
                res = new Respond(code, check);
            else
                res = new Respond(code,9000,acnt);
        }

        AccInfo info = DBOperation.getUserInfo(acnt,acnt, false);

        Server.printDebug("Account "+acnt+" login. Status "+res.getStatus());
        return new Content<AccInfo>( code, res.getStatus(), info );
    }

    /**
     * User register logic.
     * @param String json containing user register data
     * @return Respond respond
     */
    public static Content<AccInfo> register(Content<AccInfo> param){
        AccInfo ai = JsonUtil.jsonToContentData(gson.toJson(param), AccInfo.class);

        String acnt = ai.getAccount();
        String pwd = ai.getPassword();
        String name = ai.getName();

        String code = param.getCode();

        Respond res = null;

        if(acnt==null){
            res = new Respond(code, 9091, "account");
        } else if(pwd==null){
            res = new Respond(code, 9091, "password");
        } else if(name==null){
            res = new Respond(code, 9091, "name");
        } else if(!accountFormatCheck(acnt)){
            res = new Respond(code, 9092, "account");
        } else if(!passwordFormatCheck(pwd)){
            res = new Respond(code, 9092, "password");
        } else if(!usernameFormatCheck(name)){
            res = new Respond(code, 9092, "name");
        } else if( DBOperation.isUserExistByAcc(acnt) ){
            res = new Respond(code, 9102);
        } else {

            DBOperation.insertUser( gson.toJson(ai) );
            res = new Respond(code, 9000, acnt);
        }

        AccInfo info = DBOperation.getUserInfo(acnt,acnt, false);

        Server.printDebug("Account "+acnt+" register. Status "+res.getStatus());
        return new Content<AccInfo>(code, res.getStatus(), info);
    }

    /**
     * change password request handler
     * @param String json
     * @return Respond 
     */
    public static Content<Respond> changePassword(Content<AccInfo> param){
        AccInfo ai = JsonUtil.jsonToContentData(gson.toJson(param), AccInfo.class);

        String acnt = ai.getAccount();
        String pwd = ai.getPassword();

        DBOperation.updateUserPassword(acnt, pwd);

        Server.printDebug("Account "+acnt+" change password.");

        return new Content<Respond>(
            param.getCode(), 9000, 
            new Respond(param.getCode(), 9000)
        );
        
    }

    /**
     * logout request handler
     * @param String json
     * @return Respond 
     */
    public static Content<Respond> logout(Content<AccInfo> param) {
        String json = gson.toJson(param);
        AccInfo ai = JsonUtil.jsonToContentData(json, AccInfo.class);
        String acnt = ai.getAccount();
        int status = 9000;

        if(!Server.isOnline(acnt)){
            status = 9104;
        } else {
            Server.getAllLinks().get(acnt).logout();
            Server.getAllLinks().remove(acnt);
        }

        Server.printDebug("Account "+acnt+" logout. Status "+status);

        return new Content<Respond>(
            param.getCode(), status, 
            new Respond(param.getCode(), status)
        );
    }

    /**
     * delete account request handler, must verify password first
     * @param String json
     * @return Respond 
     */
    public static Content<Respond> deleteAccount(Content<AccInfo> param) {
        AccInfo ai = JsonUtil.jsonToContentData(gson.toJson(param), AccInfo.class);

        String acnt = ai.getAccount();
        String pwd = ai.getPassword();
        String code = param.getCode();

        Respond res = null;

        if( DBOperation.isUserExistByAcc(acnt) ){
            res = new Respond(code, 9102);
        } else if( loginCheck(acnt, pwd) != 9000 ){
            res = new Respond(code, 9103);
        } else {
            DBOperation.deleteUser(acnt);
            res = new Respond(code, 9000);
        }

        Server.printDebug("Account "+acnt+" delete account. Status "+res.getStatus());

        return new Content<Respond>(code, res.getStatus(), res);
    }

    /**
     * change avatar request handler
     * @param String json
     * @return Respond 
     */
    public static Content<Respond> modifyUserAvatar(Content<AccInfo> param) {
        AccInfo ai = JsonUtil.jsonToContentData(gson.toJson(param), AccInfo.class);

        String acnt = ai.getAccount();
        String avatar = ai.getAvatar();

        DBOperation.updateUserAvatar(acnt, avatar);

        Server.printDebug("Account "+acnt+" modify avatar. Status "+9000);

        return new Content<Respond>(
            param.getCode(), 9000, 
            new Respond(param.getCode(), 9000)
        );
    }

    /**
     * change user name request handler
     * @param String json
     * @return Content<Respond> Respond 
     */
    public static Content<Respond> modifyUserName(Content<AccInfo> param) {
        AccInfo ai = JsonUtil.jsonToContentData(gson.toJson(param), AccInfo.class);

        String acnt = ai.getAccount();
        String name = ai.getName();

        DBOperation.updateUserName(acnt, name);

        Server.printDebug("Account "+acnt+" modify name. Status "+9000);

        return new Content<Respond>(
            param.getCode(), 9000, 
            new Respond(param.getCode(), 9000)
        );
    }
}
