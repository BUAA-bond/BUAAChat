package com.BUAAChat.Server.Message;

import java.util.ArrayList;

/**
 * Content data format class for friend request.
 * @author WnRock
 * @version 0.1
 */
public class FriendReq {

    private String account_A = null;
    private String account_B = null;
    private String uAccount = null;
    private String gAccount = null;
    private String sender = null;
    private String recipient = null;
    private String updateTime = null;
    private String status = null;
    private ArrayList<FriendReq> history = null;

    /**
     * Getters and setters.
     */
    public String getAccount_A() {
        return account_A;
    }
    public String getAccount_B() {
        return account_B;
    }
    public String getgAccount() {
        return gAccount;
    }
    public String getuAccount() {
        return uAccount;
    }
    public ArrayList<FriendReq> getHistory() {
        return history;
    }
    public String getStatus() {
        return status;
    }
    public String getUpdateTime() {
        return updateTime;
    }
    public String getRecipient() {
        return recipient;
    }
    public String getSender() {
        return sender;
    }
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public void setAccount_A(String account_A) {
        this.account_A = account_A;
    }
    public void setAccount_B(String account_B) {
        this.account_B = account_B;
    }
    public void setHistory(ArrayList<FriendReq> history) {
        this.history = history;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setgAccount(String gAccount) {
        this.gAccount = gAccount;
    }
    public void setuAccount(String uAccount) {
        this.uAccount = uAccount;
    }
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

}
