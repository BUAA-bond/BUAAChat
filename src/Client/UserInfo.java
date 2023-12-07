package Client;

public class UserInfo {
    public String name;
    public String avatarPath;
    public String account;
    public UserInfo(String name, String avatarPath) {
        this.name = name;
        this.avatarPath = avatarPath;
    }

    public UserInfo(String account,String name, String avatarPath) {
        this.name = name;
        this.avatarPath = avatarPath;
        this.account = account;
    }
}
