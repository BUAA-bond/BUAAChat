package Client;

import Message.*;

import java.awt.*;
import java.io.*;
import java.net.Socket;

/**
 * @author 西西弗
 * @Description:
 * @date 2023/11/9 20:26
 */
@SuppressWarnings({"all"})
public class Client implements Runnable{
    private Socket socket;
    private InputStream is;
    private OutputStream os;
    private boolean isLive = true;
    private User user;

    //程序主入口
    public static void main(String[] args) {

    }

    public Client(Socket socket, User user) {
        this.socket = socket;
        this.user = user;
    }

    //发送消息并显示
    public void send(Message message) {
        try {
            os=socket.getOutputStream();
            ObjectOutputStream oos=new ObjectOutputStream(os);
            oos.writeObject(message);
            oos.flush();
            oos.close();
            //处理信息
            handleMyMessage(message);
        } catch (IOException e) {
            System.out.println("发送消息");
        }
    }
    //发送文本
    public void sendText(String account,String content){
        Text message=createText(account,content);
        send(message);
    }
    //发送图片
    public void sentPhoto(String account,String imagePath,Image image){
        Photo message=createPhoto(account,imagePath,image);
        send(message);
    }
    //创建文本
    public Text createText(String account, String content){
        return new Text(user.getAccount(),account,content);
    }
    //创建图片
    public Photo createPhoto(String account, String imagePath, Image image){
        return new Photo(user.getAccount(),account,imagePath,image);
    }
    //接收消息并显示
    public Message receive(){
        Message message=null;
        try {
            is=socket.getInputStream();
            ObjectInputStream ois=new ObjectInputStream(is);
            Object o=ois.readObject();
            message=(Message) o;
            //处理信息
            handleFriendMessage(message);
        } catch (EOFException e){
            return null;
        }catch (IOException e) {
            System.out.println("发送信息1");
            return null;
        } catch (ClassNotFoundException e){
            System.out.println("发送消息2");
            return null;
        }
        return message;
    }
    //处理朋友信息
    public void handleFriendMessage(Message message){
        if(message==null) return;

    }
    //处理自己信息
    public void handleMyMessage(Message message){
        if(message==null) return;

    }
    //加好友
    public void makeFriends(String Account,String name){//TODO
        user.makeFriends(Account,name);
    }
    //删好友
    public void deleteFriends(String Account,String name){//TODO
        user.deleteFriends(Account,name);
    }
    //建群
    public void buildGroup(){

    }
    //关闭
    public void closeSocket(){
        isLive=false;
        if(socket!=null && !socket.isClosed()){
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("关闭socket");
            }
        }
    }

    @Override
    public void run() {
        while(isLive){
            receive();

            //TODO
        }
    }
}
