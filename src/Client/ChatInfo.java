package Client;

import java.util.HashMap;

/**
 * @author 西西弗
 * @Description: 保存的是每一条信息的内容，还有是谁发的
 * @date 2023/12/7 20:57
 */
public class ChatInfo {
    public String fromUser;
    public String content;

    public ChatInfo(String fromUser, String content) {
        this.fromUser = fromUser;
        this.content = content;

    }
}
