package Client;

import java.awt.*;
import java.util.HashMap;

/**
 * @author 西西弗
 * @Description:
 * @date 2023/11/9 20:50
 */
public class User {
    private String account;
    private String name;
    private String password;
    private Image avatar;
    private String avatarPath;
    private HashMap<String,String> friends=new HashMap<>();//ID+用户对象

    public User(String account,String name,String password,Image avatar){
        this.account=account;
        this.name=name;
        this.password=password;
        this.avatar=avatar;
    }

    public User(String account, String name, String password) {
        this.account = account;
        this.name = name;
        this.password = password;
    }

    public User(String account, String name, String password,String avatarPath) {
        this.account = account;
        this.name = name;
        this.password = password;
        this.avatarPath = avatarPath;
    }

    //加好友
    public void makeFriends(String account,String name){
        friends.put(account,name);
        //TODO
    }
    //删除好友
    public void deleteFriends(String account,String name){
        friends.remove(account,name);
        //TODO
    }

    public String getName() {
        return name;
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }

    public Image getAvatar() {
        return avatar;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatar(Image avatar) {
        this.avatar = avatar;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }
}
