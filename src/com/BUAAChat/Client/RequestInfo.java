package com.BUAAChat.Client;

/**
 * @author 西西弗
 * @Description:
 * @date 2023/12/13 10:05
 */
public class RequestInfo {
    public String from;
    public String to;
    public String name;
    public String avatarPath;
    public int type;//-1表示拒绝 0表示待处理 1表示接受

    public RequestInfo(String from, String to, String name, String avatarPath) {
        this.from = from;
        this.to = to;
        this.name = name;
        this.avatarPath = avatarPath;
    }

    public RequestInfo(String from, String to, String name, String avatarPath, int type) {
        this.from = from;
        this.to = to;
        this.name = name;
        this.avatarPath = avatarPath;
        this.type = type;
    }

    public RequestInfo() {
    }
}
