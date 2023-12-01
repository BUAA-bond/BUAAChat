package Server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import Server.ServiceException.*;

/**
 * Server.
 * @author WnRock
 * @version 0.1
 */
public class Server {

    /**
     * Temp. For test.
     * @param args
     */
    public static void main(String[] args) {
        start();
    }

    private static int PORT = 10005;
    private static Map<String,Link> allLinks = new HashMap<>();
    private static ServerSocket ss = null;
    private static Socket s = null;

    /**
     * Start a server (with specified port).
     */
    public static void start(){
        init();
        listen();
    }
    public static void start(int port){
        PORT = port;
        init();
        listen();
    }
    
    private static void init(){
        try {
            ss = new ServerSocket(PORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void listen(){
        while (true) {
            try {
                s = ss.accept();
                Link l = new Link(s);
                allLinks.put(l.getID(), l);
                new Thread(l).start();
            } catch (LinkInitFailException e) {
                System.err.println(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * get all links maintained currently.
     * @return AllLinks
     */
    public static Map<String,Link> getAllLinks(){ return allLinks; }
}

