package Message;

import java.io.Serializable;

/**
 * @author 西西弗
 * @Description:
 * @date 2023/11/9 20:12
 */
public abstract class Message implements Serializable {
    private String fromUser;//ID
    private String toUser;//ID
    private String content;
    public Message(String fromUser,String toUser,String content){
        this.fromUser=fromUser;
        this.toUser=toUser;
        this.content=content;
    }

    public Message(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getFromUser(){
        return fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public String getContent() {
        return content;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
