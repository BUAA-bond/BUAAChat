import Message.Message;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author 西西弗
 * @Description:
 * @date 2023/12/5 16:31
 */
public class Server {
//    public static void main(String[] args) {
//        try {
//            ServerSocket serverSocket = new ServerSocket(9999);
//            Socket clientSocket = serverSocket.accept();
//            System.out.println("客户端连接成功，地址：" + clientSocket.getInetAddress());
//            OutputStream os=clientSocket.getOutputStream();
//            ObjectOutputStream oos=new ObjectOutputStream(os);
//            InputStream is=clientSocket.getInputStream();
//            ObjectInputStream ois=new ObjectInputStream(is);
//            while(clientSocket!=null){
//                Message msg=(Message) ois.readObject();
//                if(msg instanceof SysMsg) {
//                    SysMsg sysMsg=(SysMsg)msg;
//                    System.out.println(sysMsg.getData());
//                    JsonObject job = new JsonObject();
//                    job.addProperty("code", 0);
//                    String content = new Gson().toJson(job);
//                    SysMsg msg1 = new SysMsg("system", 1, content);
//                    oos.writeObject(msg1);
//                    oos.flush();
//                }else if(msg instanceof Text){
//                    Text text=(Text) msg;
//                    System.out.println(text.getData());
//                }
//            }
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
}
