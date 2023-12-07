
package Client;

import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;

public class User {
    private String account;
    private String name;
    private String password;
    private Image avatar;
    private String avatarPath;
    private HashMap<String, UserInfo> friends = new HashMap();//主键是好友账号
    private HashMap<String, GroupInfo> groups = new HashMap();//主键是群聊号
    private HashMap<String, ArrayList<ChatInfo>> messagesF=new HashMap<>();//主键是好友账号，表示与谁谁的聊天记录
    private HashMap<String, ArrayList<ChatInfo>> messagesG=new HashMap<>();//主键是群账号，表示在哪个群的聊天记录
    public User() {
    }

    public User(String account, String password) {
        this.account = account;
        this.password = password;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String account, String name, String password) {
        this.account = account;
        this.name = name;
        this.password = password;
    }



    public User(String account, String name, String password, String avatarPath) {
        this.account = account;
        this.name = name;
        this.password = password;
        this.avatarPath = avatarPath;
    }

    public String getName() {
        return this.name;
    }

    public String getAccount() {
        return this.account;
    }

    public String getPassword() {
        return this.password;
    }

    public Image getAvatar() {
        return this.avatar;
    }

    public String getAvatarPath() {
        return this.avatarPath;
    }

    public HashMap<String, ArrayList<ChatInfo>> getMessagesF() {
        return messagesF;
    }
    public void setAvatar(Image avatar) {
        this.avatar = avatar;
    }

    public void setFriends(HashMap<String, UserInfo> friends) {
        this.friends = friends;
    }

    public void setGroups(HashMap<String, GroupInfo> groups) {
        this.groups = groups;
    }

    public HashMap<String, UserInfo> getFriends() {
        return friends;
    }

    public HashMap<String, GroupInfo> getGroups() {
        return groups;
    }

    public HashMap<String, ArrayList<ChatInfo>> getMessagesG() {
        return messagesG;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }
}
