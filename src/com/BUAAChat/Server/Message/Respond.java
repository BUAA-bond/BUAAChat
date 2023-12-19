package com.BUAAChat.Server.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * Server respond. Read README.md for meanings of the code.
 * @author WnRock
 * @version 0.1
 */
public class Respond {
    private static transient Map<Integer,String> msgs = new HashMap<>();
    static {
        msgs.put(9000, "OK.");

        msgs.put(9101, "User not exist.");
        msgs.put(9102, "Account already exist.");
        msgs.put(9103, "Wrong password.");
        msgs.put(9104, "Invalid user status.");

        msgs.put(9091, "Parameter missing.");
        msgs.put(9092, "Parameter format error.");
        msgs.put(9093, "Parameter error.");

        msgs.put(9501, "User not exist.");

        msgs.put(9999, "Server error.");
    }

    private String code = null;
    private int status = 9999;
    private String msg = null;
    private String param = null;

    /**
     * Constructor.
     * @param String code
     * @param int status
     */
    public Respond(String code,int status){
        this.code = code;
        this.status = status;
        if( ! msgs.containsKey(status) )
            this.status = 9999;
        this.msg = msgs.get(status);

    }

    /**
     * Constructor.
     * @param String code
     * @param int status
     * @param String param
     */
    public Respond(String code,int status,String param){
        this.code = code;
        this.status = status;
        if( ! msgs.containsKey(status) )
            this.status = 9999;
        this.msg = msgs.get(status);
        this.param = param;
    }

    /**
     * Getters and setters.
     */
    public String getCode() {
        return code;
    }
    public int getStatus() {
        return status;
    }
    public String getMsg() {
        return msg;
    }
    public String getParam() {
        return param;
    }
    public static Map<Integer, String> getMsgs() {
        return msgs;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public static void setMsgs(Map<Integer, String> msgs) {
        Respond.msgs = msgs;
    }
    public void setParam(String param) {
        this.param = param;
    }
    public void setStatus(int status) {
        this.status = status;
    }
}
