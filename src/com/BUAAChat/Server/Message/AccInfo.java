package com.BUAAChat.Server.Message;

import java.util.ArrayList;

/**
 * Content data format class for Account info (user and group).
 * @author WnRock
 * @version 0.1
 */
public class AccInfo {
    private String account = null;
    private String name = null;
    private String avatar = null;
    private String password = null;
    private String salt = null;
    private ArrayList<ChatMsg> messages = null;
    private ArrayList<AccInfo> members = null;

    private String uAccount = null;
    private String gAccount = null;

    /**
     * Constructor.
     */
    public AccInfo(){ }
    public AccInfo(String acc,String pwd){
        this.account = acc;
        this.password = pwd;
    }
    public AccInfo(String name,String acc,String avatar){
        this.name = name;
        this.account = acc;
        this.avatar = avatar;
    }
    public AccInfo(String name,String acc,String password,String avatar){
        this.name = name;
        this.account = acc;
        this.password = password;
        this.avatar = avatar;
    }
    public AccInfo(String name,String acc,String avatar,ArrayList<ChatMsg> msgs){
        this.name = name;
        this.account = acc;
        this.avatar = avatar;
        this.messages = msgs;
    }
    public AccInfo(String acc,String name,ArrayList<AccInfo> mem){
        this.name = name;
        this.account = acc;
        this.members = mem;
    }
    public AccInfo(String acc,String name,ArrayList<AccInfo> mem,ArrayList<ChatMsg> msgs){
        this.name = name;
        this.account = acc;
        this.members = mem;
        this.messages = msgs;
    }
    
    /**
     * Getters and setters.
     */
    public String getAccount() {
        return account;
    }
    public String getAvatar() {
        return avatar;
    }
    public ArrayList<AccInfo> getMembers() {
        return members;
    }
    public ArrayList<ChatMsg> getMessages() {
        return messages;
    }
    public String getName() {
        return name;
    }
    public String getPassword() {
        return password;
    }
    public String getSalt() {
        return salt;
    }
    public String getgAccount() {
        return gAccount;
    }
    public String getuAccount() {
        return uAccount;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public void setMembers(ArrayList<AccInfo> members) {
        this.members = members;
    }
    public void setMessages(ArrayList<ChatMsg> messages) {
        this.messages = messages;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setSalt(String salt) {
        this.salt = salt;
    }
    public void setgAccount(String gAccount) {
        this.gAccount = gAccount;
    }
    public void setuAccount(String uAccount) {
        this.uAccount = uAccount;
    }
}
