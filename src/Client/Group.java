package Client;

import java.util.HashMap;

/**
 * @author 西西弗
 * @Description:
 * @date 2023/11/17 16:48
 */
public class Group {
    private String name;
    private String account;
    private HashMap<String,UserInfo> member=new HashMap<>();

    public Group(HashMap<String, UserInfo> member) {

    }

    public Group(String name, HashMap<String, UserInfo> member) {
        this.name = name;
        this.member = member;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, UserInfo> getMember() {
        return member;
    }
}
