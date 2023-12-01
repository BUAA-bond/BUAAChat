package com.BUAAChat.Server.Client;

import java.io.ObjectInputStream;
import java.net.Socket;

import com.BUAAChat.Server.Message.Message;
import com.BUAAChat.Server.Message.SysMsg;

/**
 * Reader class for client.
 * @author WnRock
 * @version 0.1
 */
public class Reader implements Runnable {
    private Socket s = null;
    private ObjectInputStream ois = null;
    private boolean isActive = false;
    private Message m = null;

    /**
     * Construct a new reader.
     * @param s
     */
    public Reader(Socket s){
        try {
            this.s = s;
            ois = new ObjectInputStream(this.s.getInputStream());
            this.isActive = true;
        } catch (Exception e) {
            this.isActive = false;
            e.printStackTrace();
        }
    }

    /**
     * Receive message from server.
     * @return Message received
     */
    public Message msgFromServer(){
        try {
            Message m = (Message) ois.readObject();
            
            System.out.println(m.getContent());  // Temp. Output to console.
            
            return m;
        } catch (Exception e) {
            this.isActive = false;
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Runnable interface functional method.
     */
    public void run(){
        while(isActive){
            m = msgFromServer();
            if(m instanceof SysMsg&&((SysMsg)m).getType()==-1){
                this.isActive = false;
            }
        }
        try {
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}