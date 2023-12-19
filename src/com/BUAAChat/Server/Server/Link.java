package com.BUAAChat.Server.Server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.BUAAChat.Message;
import com.BUAAChat.Server.Config.ServiceException.*;
import com.BUAAChat.Server.Message.*;
import com.BUAAChat.Server.Util.JsonUtil;
import com.google.gson.Gson;

/**
 * Server-Client Link.
 * @author WnRock
 * @version 0.1
 */
@SuppressWarnings(value = {"unchecked", "rawtypes"})
public class Link implements Runnable {
    private Gson gson = new Gson();
    private Socket s = null;
    private String account = "";
    private ObjectInputStream ois = null;
    private ObjectOutputStream oos = null;
    private boolean isActive = false;

    /**
     * Map of System command.
     */
    static Map<String,Function<Content,Content>> sysCmd = new HashMap<>();
    static {
        sysCmd.put("101", param->Account.login(param));
        sysCmd.put("102", param->Account.register(param));
        sysCmd.put("103", param->Account.changePassword(param));
        sysCmd.put("109", param->Account.logout(param));
        sysCmd.put("199", param->Account.deleteAccount(param));

        sysCmd.put("201", param->Friend.sendRequest(param));
        sysCmd.put("202", param->Friend.acceptRequest(param));
        sysCmd.put("203", param->Friend.declineRequest(param));
        sysCmd.put("204", param->Friend.removeFriend(param));
        sysCmd.put("205", param->Friend.blockFriend(param));
        sysCmd.put("209", param->Group.createNewGroup(param));
        sysCmd.put("210", param->Group.joinGroup(param));
        sysCmd.put("211", param->Group.quitGroup(param));

        sysCmd.put("301", param->Account.modifyUserName(param));
        sysCmd.put("302", param->Account.modifyUserAvatar(param));

        sysCmd.put("401", param->Group.getUserInfo(param));
        sysCmd.put("402", param->Group.getAllFriendsInfo(param));
        sysCmd.put("403", param->Group.getGroupInfo(param));
        sysCmd.put("404", param->Group.getAllGroupsInfo(param));
        sysCmd.put("405", param->Friend.getFriendReqHistory(param));

        sysCmd.put("501", param->Chat.relayPlainText(param));
    }

    /**
     * Constructor. Note that first message after connection must be
     * Register or login.
     * @param s
     */
    public Link(Socket s){
        try {
            this.s = s;
            oos = new ObjectOutputStream(this.s.getOutputStream());
            ois = new ObjectInputStream(this.s.getInputStream());
            this.isActive = true;

            Message m = msgFromClient();
            Content<AccInfo> c = JsonUtil.jsonToContent(m.getContent(), AccInfo.class);

            int code = Integer.parseInt(c.getCode());
            Content<AccInfo> res = null;

            if( code != 101 && code != 102 ){
                msgToClient(
                    new Message(
                        "0", "-1", 
                        gson.toJson(
                            new Content<Respond>( c.getCode(), 9104, null )
                        )
                    )
                );
                throw new LinkInitFailException();
            }
            
            res = handleMsg(c);

            if(res.getStatus() != 9000){
                msgToClient(
                    new Message( 
                        "0", "-1",
                        gson.toJson(
                            new Content<Respond>( c.getCode(), res.getStatus(), null )
                        )
                    )
                );
                throw new LinkInitFailException();
            } else if (code==102){
                msgToClient(
                    new Message( 
                        "0", "-1",
                        gson.toJson( res )
                    )
                );
                Server.printDebug("Account "+res.getData().getAccount()+" registered.");
                throw new LinkInitFailException();
            } else {
                this.account = res.getData().getAccount();
                Server.getAllLinks().put(this.account, this);
                msgToClient(
                    new Message(
                        "0", this.account, 
                        gson.toJson(res)
                    )
                );
                this.isActive = true;
                new Thread(this).start();
            }

            Server.printDebug("Client "+this.account+" connected successfully.");

        } catch(LinkInitFailException l) {

            this.isActive = false;
            Server.printDebug(l.getMessage());

        } catch (Exception e) {

            this.isActive = false;
            e.printStackTrace();

        }
    }

    /**
     * Send message to this socket.
     * @param Message m
     */
    public void msgToClient(Message m){
        try {
            oos.writeObject(m);
            oos.flush();
        } catch (Exception e) {
            this.isActive = false;
            e.printStackTrace();
        }
    }

    private Message msgFromClient() throws ConnResetException {
        try {
            Message m = (Message) ois.readObject();
            return m;
        } catch (SocketException e) {
            this.isActive = false;
            throw new ConnResetException();
        } catch (Exception e) {
            this.isActive = false;
            e.printStackTrace();
        }
        return null;
    }

    private Content handleMsg(Content c){
        String code = c.getCode();
        Content res = null;

        Function<Content, Content> f = sysCmd.get(code);
        if( f != null ) res = f.apply(c);
        else res = new Content<Respond>(
            code, 9093,
            new Respond(code,9093,"code")
        );

        return res;
    }

    /**
     * Send message to user.
     * @param Message m
     */
    public static void sendToUser(Message m){
        String targ = m.getTo();
        if( Server.isOnline(targ) ){
            Server.getAllLinks().get(targ).msgToClient(m);
        }
        else Resender.addPending(m);
        return ;
    }

    /**
     * Remove socket from connected users (logout).
     */
    public void logout(){
        this.isActive = false;
    }

    /**
     * Runnable interface functional method.
     */
    public void run(){
        while(isActive){
            try {
                Message m = msgFromClient();
                if(m!=null){
                    Content c = gson.fromJson(m.getContent(), Content.class);
                    Content res = handleMsg(c);
                    msgToClient(
                        new Message( "0", this.getAccount(), gson.toJson(res) )
                    );
                }
            } catch (ConnResetException cre) {
                Server.printDebug("Client "+this.account+" close connection.");
                this.isActive = false;
            } catch (Exception e) {
                e.printStackTrace();
                this.isActive = false;
            }
        }
    }

    /**
     * getters.
     */
    public String getAccount() {
        return account;
    }
}