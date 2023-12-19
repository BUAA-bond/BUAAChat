package com.BUAAChat.Server.Message;

/**
 * Content format class.
 * @author WnRock
 * @version 0.1
 */
public class Content<T> {

    private String code = null;
    private int status = 9999;
    private T data = null;

    /**
     * Constructor.
     * @param String code
     * @param int status
     * @param String data
     */
    public Content(String code,int status,T data){
        this.code = code;
        this.status = status;
        this.data = data;
    }

    /**
     * Getters and setters.
     * @return
     */
    public String getCode() {
        return code;
    }
    public int getStatus() {
        return status;
    }
    public T getData() {
        return data;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public void setData(T data) {
        this.data = data;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    
}
