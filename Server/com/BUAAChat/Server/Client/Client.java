package com.BUAAChat.Server.Client;

import java.net.Socket;

import com.BUAAChat.Server.Config.ServiceException;
import com.BUAAChat.Server.Config.ServiceException.LinkInitFailException;
import com.BUAAChat.Server.Message.*;


/**
 * Client that input from and output to console.
 * @author WnRock
 * @version 0.1
 */
public class Client {
    private String Address = "localhost";
    private int port = 10005;
    private String ID = "3";
    
    /**
     * Temp. For test.
     * @param args
     */
    public static void main(String[] args) {
        new Client().init();
    }

    private Socket s = null;
    private Message m = null;

    /**
     * initiate a new client and connect to Server
     */
    public void init(){
        try {
            s = new Socket(Address,port);

            Sender sender = new Sender(s, ID);
            Reader reader = new Reader(s);
            sender.MsgToServer(new SysMsg(ID,1));
            
            m = reader.msgFromServer();
            if( ! ( m instanceof SysMsg ) || ((SysMsg)m).getType() != 200)
                throw new ServiceException.LinkInitFailException();
            
            new Thread(reader).start();
            new Thread(sender).start();

        } catch (LinkInitFailException e){
            System.err.println(e.getMessage());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}