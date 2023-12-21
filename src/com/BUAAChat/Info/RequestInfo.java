package com.BUAAChat.Info;

/**
 * @author 西西弗
 * @Description: 保存加好友申请的相关信息
 * @date 2023/12/13 10:05
 */
public class RequestInfo {
    /**
     *申请人账号
     */
    public String from;

    /**
     *被申请人账号
     */
    public String to;

    /**
     *申请人名字
     */
    public String name;

    /**
     *申请人头像
     */
    public String avatarPath;

    /**
     *申请的状态，-1表示拒绝 0表示待处理 1表示接受
     */
    public int type;

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
