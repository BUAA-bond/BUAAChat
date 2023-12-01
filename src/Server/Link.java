package Server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import Server.ServiceException.*;
import Message.*;

/**
 * Server-Client Link.
 * @author WnRock
 * @version 0.1
 */
public class Link implements Runnable {
    private Socket s = null;
    private String ID = "";
    private ObjectInputStream ois = null;
    private ObjectOutputStream oos = null;
    private boolean isActive = false;

    /**
     * Map of System command.
     */
    Map<String,Function<Message,Boolean>> sysCmd = new HashMap<>();
    {
        sysCmd.put("quit", param->clientQuit(param));
    }

    /**
     * Constructor.
     * @param s
     */
    public Link(Socket s){
        try {
            this.s = s;
            oos = new ObjectOutputStream(this.s.getOutputStream());
            ois = new ObjectInputStream(this.s.getInputStream());
            this.isActive = true;

            Message m = (Message) ois.readObject();
            this.ID = getID(m);
            if( this.ID == null )
                throw new ServiceException.LinkInitFailException();

            msgToClient(new SysMsg("0",200,"Login success. "));
        } catch (LinkInitFailException e){
            throw e;
        } catch (Exception e) {
            this.isActive = false;
            e.printStackTrace();
        }
    }

    private boolean clientQuit(Message m){
        this.isActive = false;
        msgToClient(new SysMsg("0", -1,"Quit success. Bye! "));
        Server.getAllLinks().remove(ID);

        return true;
    }

    private String getID(Message m){
        if( ! ( m instanceof SysMsg ) ) return null;
        if( ((SysMsg)m).getType() != 1 ) return null;
        return m.getFromUser();
    }

    private void msgToClient(Message m){
        try {
            oos.writeObject(m);
            oos.flush();
        } catch (Exception e) {
            this.isActive = false;
            e.printStackTrace();
        }
    }

    private Message msgFromClient(){
        try {
            Message m = (Message) ois.readObject();

            System.out.println( ID + " send: "+m.getContent()); // Temp. Output to console.

            return m;
        } catch (Exception e) {
            this.isActive = false;
            e.printStackTrace();
        }
        return null;
    }

    private void handleSysMsg(Message m){
        String cmd = m.getContent().toLowerCase();
        Function<Message,Boolean> f = sysCmd.get(cmd);
        if( f != null ) f.apply(m);
        else throw new SysCmdNotFoundException();
    }

    private void sendMsg(Message msg,String to){
        if(msg instanceof SysMsg||to.equals("0")){
            try {
                handleSysMsg(msg);
            } catch (SysCmdNotFoundException e) {
                msgToClient(new SysMsg("0", 500, e.getMessage()));
            }
            return ;
        }
        try {
            msg.setContent( ID + " say: " + msg.getContent() );
            Map<String,Link> allLinks = Server.getAllLinks();
            Link l = allLinks.get(to);
            if(l!=null) l.msgToClient(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Runnable interface functional method.
     */
    public void run(){
        while(isActive){
            Message m = msgFromClient();
            if(m!=null) sendMsg(m, m.getToUser());
        }
        try{s.close();}catch(Exception e){e.printStackTrace();}
    }

    /**
     * getters.
     */
    public String getID(){
        return this.ID;
    }
    public ObjectInputStream getOis() {
        return ois;
    }
    public ObjectOutputStream getOos() {
        return oos;
    }
    public Socket getS() {
        return s;
    }
    public Map<String, Function<Message, Boolean>> getSysCmd() {
        return sysCmd;
    }
}