package com.BUAAChat.Server.Server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.BUAAChat.Server.Config.*;
import com.BUAAChat.Server.Config.ServiceException.IllegalParamException;
import com.BUAAChat.Server.Database.DBOperation;

/**
 * Server.
 * @author WnRock
 * @version 0.1
 */
public class Server {

    private static int PORT = Config.PORT;
    private static Map<String,Link> allLinks = new HashMap<>();
    private static ServerSocket ss = null;
    private static Socket s = null;
    private static boolean debugMode = false;

    /**
     * Main entry point of the server.
     * @param args
     */
    public static void main(String[] args) {
        paramResolve(args);
        start();
    }

    private static boolean strEqual(String str,String... Targ){
        for (String targ : Targ)
            if(str.equals(targ)) return true;
        return false;
    }

    private static void paramResolve(String[] param){
        if(param.length!=0){
            for (int i=0; i<param.length; i++) {
                String str = param[i].toLowerCase();
                if(strEqual(str, "-h", "--help")){
                    printHelp();
                    System.exit(0);
                }
                else if(strEqual(str, "-d", "--debug"))
                    debugMode = true;
                else if(strEqual(str, "-p", "--port")){
                    i++;
                    try {
                        PORT = Integer.parseInt(param[i]);
                        if( PORT < 0 || PORT > 65535 )
                            throw new IllegalParamException();
                    } catch (Exception e) {
                        System.out.println("Invaild PORT.");
                        printHelp();
                        System.exit(0);
                    }
                }
                else {
                    System.out.println("Invaild param \'"+str+"\'.");
                    printHelp();
                    System.exit(0);
                }
            }
        }
        return ;
    }

    private static void printHelp(){
        System.out.println();
        System.out.println("Usage:");
        System.out.println(" ./Server [command] [<options>...]");
        System.out.println();
        System.out.println("Commands:");
        System.out.println(" start                      Start server");
        System.out.println(" stop                       Stop server and quit");
        System.out.println(" reload                     Stop server and restart");
        System.out.println();
        System.out.println("Options:");
        System.out.println(" -h, --help                 Print this page and quit");
        System.out.println(" -d, --debug                Debug mode, print additional messages");
        System.out.println(" -p PORT, --port PORT       Set the port server listens [default: 10005]");
        System.out.println();
        return ;
    }

    /**
     * Start a server (with specified port).
     * @param int [port]
     */
    private static void start(){
        DBOperation.init();
        Resender.init();
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
                new Link(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * get all links maintained currently.
     * @return Map<String,Link> AllLinks
     */
    public static Map<String,Link> getAllLinks(){ return allLinks; }

    /**
     * check if user is online.
     * @param String UserID
     * @return boolean isOnline
     */
    public static boolean isOnline(String UserID){
        return allLinks.containsKey(UserID);
    }

    /**
     * Print debug messages if debugMode is TRUE.
     * @param String msg
     */
    public static void printDebug(String msg){
        if(debugMode) System.out.println("[Server] "+msg);
    }
}

