import Message.SysMsg;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author 西西弗
 * @Description:
 * @date 2023/12/5 16:31
 */
public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9999);
            while(true){
                Socket clientSocket = serverSocket.accept();
                System.out.println("客户端连接成功，地址：" + clientSocket.getInetAddress());
                InputStream is=clientSocket.getInputStream();
                OutputStream os=clientSocket.getOutputStream();
                ObjectInputStream ois=new ObjectInputStream(is);
                ObjectOutputStream oos=new ObjectOutputStream(os);
                SysMsg msg=(SysMsg) ois.readObject();
                System.out.println(msg.getContent());
                JsonObject job=new JsonObject();
                job.addProperty("code",0);
                String content=new Gson().toJson(job);
                SysMsg msg1 = new SysMsg("system",1,content);
                oos.writeObject(msg1);
                oos.flush();
                if(oos!=null) oos.close();
                if(ois!=null) ois.close();
                if(!clientSocket.isClosed()){
                    clientSocket.close();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
