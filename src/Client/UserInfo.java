//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package Client;

public class UserInfo {
    public String account;
    public String name;
    public String avatarPath;

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
