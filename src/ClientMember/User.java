package ClientMember;

import java.awt.*;
import java.util.HashMap;

/**
 * @author 西西弗
 * @Description:
 * @date 2023/11/9 20:50
 */
public class User {
    private Integer ID;
    private String name;
    private String password;
    private Image avatar;
    private String avatarPath;
    private HashMap<Integer,String> friends=new HashMap<>();//ID+用户对象

    public User(int ID,String name,String password,Image avatar){
        this.ID=ID;
        this.name=name;
        this.password=password;
        this.avatar=avatar;
    }

    public User(Integer ID, String name, String password) {
        this.ID = ID;
        this.name = name;
        this.password = password;
    }

    public User(Integer ID, String name, String password,String avatarPath) {
        this.ID = ID;
        this.name = name;
        this.password = password;
        this.avatarPath = avatarPath;
    }

    //加好友
    public void makeFriends(Integer ID,String name){
        friends.put(ID,name);
        //TODO
    }
    //删除好友
    public void deleteFriends(Integer ID,String name){
        friends.remove(ID,name);
        //TODO
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return ID;
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
