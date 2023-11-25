package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author 西西弗
 * @Description:
 * @date 2023/11/9 20:08
 */
public class Server implements Runnable{
    private ServerSocket serverSocket;
    private ArrayList<Socket> sockets=new ArrayList<>();
    public static void main(String[] args) {

    }
    public Server(){
        try {
            serverSocket=new ServerSocket(15000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        while (true){
            try {
                Socket socket=serverSocket.accept();
                sockets.add(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
