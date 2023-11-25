package ClientMember;

import java.util.HashMap;

/**
 * @author 西西弗
 * @Description:
 * @date 2023/11/17 16:48
 */
public class Group {
    private String name;
    private HashMap<Integer,User> member=new HashMap<>();

    public Group(HashMap<Integer, User> member) {
        this.member = member;
        for (int i = 0; i < member.size(); i++) {
            if(i== member.size()-1)
                name+=member.get(i).getName();
            else
                name+=member.get(i).getName()+"、";
        }
    }

    public Group(String name, HashMap<Integer, User> member) {
        this.name = name;
        this.member = member;
    }

    public String getName() {
        return name;
    }

    public HashMap<Integer, User> getMember() {
        return member;
    }
}
