package Message;

import java.io.Serializable;

/**
 * @author 西西弗
 * @Description:
 * @date 2023/11/9 20:12
 */
public class Message implements Serializable {
    private String from;//ID
    private String to;//ID
    private String data;

    public Message(String from, String to, String data) {
        this.from = from;
        this.to = to;
        this.data = data;
    }
    public Message(String from, String data) {
        this.from = from;
        this.data = data;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getData() {
        return data;
    }
}
