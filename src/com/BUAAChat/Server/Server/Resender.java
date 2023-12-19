package com.BUAAChat.Server.Server;

import java.util.ArrayList;

import com.BUAAChat.Message;
import com.BUAAChat.Server.Config.ServiceException.ResenderException;

/**
 * Resend the pending messages.
 * @author WnRock
 * @version 0.1
 */
public class Resender implements Runnable {

    private static ArrayList<Message> pending = new ArrayList<>();
    private boolean isActive = true;

    private Resender(){ }

    /**
     * Resender init.
     */
    public static void init(){
        Resender resender = new Resender();
        new Thread(resender).start();
    }

    /**
     * resend the pending messages.
     */
    public void resend(){
        if(pending.size()==0) return ;
        
        int cur=0;
        while(cur<pending.size()){
            Message m = pending.get(cur);
            if(Chat.relayPlainText(m)) pending.remove(cur);
            cur++;
        }

        return ;
    }

    /**
     * Add pending messages.
     * @param Message m
     */
    public static void addPending(Message m){
        pending.add(m);
        return ;
    }

    /**
     * Runnable interface.
     */
    public void run() {
        while(this.isActive){
            try {
                resend();
                try{
                    Thread.sleep(100);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                this.isActive = false;
                throw new ResenderException();
            }
        }
    }

    /**
     * Get pending messages list.
     * @return ArrayList<Message>
     */
    public static ArrayList<Message> getPending() {
        return pending;
    }
}
