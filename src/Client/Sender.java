package Client;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import Message.*;
import Message.PlainText;

/**
 * Sender class for client.
 * @author WnRock
 * @version 0.1
 */
public class Sender implements Runnable {
    private Scanner in = null;
    private Socket s = null;
    private String ID = "";
    private ObjectOutputStream oos = null;
    private Message m = null;
    private boolean isActive = false;

    /**
     * Construct a new Sender.
     * @param s
     * @param id
     */
    public Sender(Socket s,String id){
        try {
            this.in = new Scanner(System.in);
            this.s = s;
            this.ID = id;
            oos = new ObjectOutputStream(this.s.getOutputStream());
            this.isActive = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Message getMessage(){
        m = null;

        String input = in.nextLine();
        if(input.charAt(0)=='@'){
            String[] str = input.substring(1).split(":");
            if(str.length == 1) return null;
            else m = new PlainText(ID, str[0], str[1]);
        } else if(input.charAt(0)=='$'){
            input = input.substring(1);
            m = new SysMsg(ID, 3,input);
            if(input.toLowerCase().equals("quit"))
                this.isActive = false;
        } 
        
        return m;
    }

    /**
     * Send message to Server.
     * @param m
     */
    public void MsgToServer(Message m){
        if( m == null ) return ;
        try {
            oos.writeObject(m);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        m = null;
    }

    /**
     * Runnable interface functional method.
     */
    public void run(){
        while (isActive) {
            MsgToServer(getMessage());
        }
    }
}