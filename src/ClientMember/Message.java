package ClientMember;

import java.io.Serializable;

/**
 * @author 西西弗
 * @Description:
 * @date 2023/11/9 20:12
 */
public abstract class Message implements Serializable {
    private Integer fromUser;//ID
    private Integer toUser;//ID
    private String content;
    public int type;//0表示文本型 1表示是媒体型 2表示文件型
    public Message(Integer fromUser,Integer toUser,String content,int type){
        this.fromUser=fromUser;
        this.toUser=toUser;
        this.content=content;
        this.type=type;
    }
    public Integer getFromUser(){
        return fromUser;
    }

    public Integer getToUser() {
        return toUser;
    }

    public String getContent() {
        return content;
    }
}
