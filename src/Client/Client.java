package Client;

import Message.Message;
import Message.SysMsg;
import Message.Text;
import MyException.ConnectException;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;

import static Constant.Constant.host;
import static Constant.Constant.port;

public class Client implements Runnable {
    private Socket socket;
    private InputStream is=null;
    private OutputStream os=null;
    private ObjectInputStream ois=null;
    private ObjectOutputStream oos=null;
    private boolean isLive = false;
    private boolean isLogin =false;
    private User user;

    public Client() {
        try {
            this.socket=new Socket(host,port);
            //先oos再ois，有顺序要求，不然会堵塞
            os=socket.getOutputStream();
            oos=new ObjectOutputStream(os);
            is=socket.getInputStream();
            ois=new ObjectInputStream(is);
        } catch (IOException e) {
            System.err.println("连接被拒绝: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 用于注册，只判断用户名有咩有没注册过
     * @param account
     * @param name
     * @param password
     * @param avatarPath
     * @return
     */
    public boolean register(String account, String name, String password, String avatarPath) {
        registerSend(account, name, password, avatarPath);
        return registerReceive();
    }

    /**
     * 向服务器端发送注册信息
     * @param account
     * @param name
     * @param password
     * @param avatarPath
     */
    public void registerSend(String account, String name, String password, String avatarPath) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("account", account);
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("password", password);
        jsonObject.addProperty("avatarPath", avatarPath);
        Gson gson = new Gson();
        String content = gson.toJson(jsonObject);
        SysMsg sysMsg = new SysMsg(account, 2, content);
        try {
            if(socket.isClosed()){
                throw new ConnectException("连接异常中断");
            }
            oos.writeObject(sysMsg);
            oos.flush();
        } catch (IOException | ConnectException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收注册反馈
     * @return
     */
    public boolean registerReceive() {
        SysMsg msg = null;
        try {
            if(socket.isClosed()){
                throw new ConnectException("连接异常中断");
            }
            msg = (SysMsg)ois.readObject();
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = jsonParser.parse(msg.getContent()).getAsJsonObject();
            if (jsonObject.get("code").getAsInt()!=1) return false;
        } catch (ClassNotFoundException | IOException | ConnectException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 登录
     * @param account
     * @param password
     * @return
     */
    public boolean login(String account, String password) {
        loginSend(account, password);
        User tmp = this.loginReceive();
        if (tmp == null) {
            System.out.println("登录失败");
            return false;
        } else {
            this.user = tmp;
            this.user.setAccount(account);
            this.user.setPassword(password);
            return true;
        }
    }

    /**
     * 发送登录请求
     * @param account
     * @param password
     */
    public void loginSend(String account, String password) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("account", account);
        jsonObject.addProperty("password", password);
        Gson gson = new Gson();
        String content = gson.toJson(jsonObject);
        SysMsg msg = new SysMsg(account, 1, content);
        try {
            if(socket.isClosed()){
                throw new ConnectException("连接异常中断");
            }
            oos.writeObject(msg);
            oos.flush();
            //TODO
            //这里的oos没有close，因为oos.close，socket也close了，暂时没想到好的解决方法
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ConnectException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收登录请求反馈
     * @return
     */
    public User loginReceive() {
        SysMsg msg = null;
        try {
            if(socket.isClosed()){
                throw new ConnectException("连接异常中断");
            }
            msg = (SysMsg)ois.readObject();
            String json = msg.getContent();
            JsonParser jsonParser = new JsonParser();
            JsonObject jj = jsonParser.parse(json).getAsJsonObject();
            if (jj.get("code").getAsInt()==0) return new User();
            //TODO
            User user1 = new User();
            user1.setName(jj.get("name").getAsString());
            user1.setAvatarPath(jj.get("avatarPath").getAsString());
            JsonElement friendElement = jj.getAsJsonObject("friends");
            JsonElement groupElement = jj.getAsJsonObject("groups");
            HashMap<String, UserInfo> friendsMap = new Gson().fromJson(friendElement, new TypeToken<HashMap<String, UserInfo>>(){}.getType());
            user1.setFriends(friendsMap);
            HashMap<String, GroupInfo> groupsMap = new Gson().fromJson(groupElement, new TypeToken<HashMap<String, GroupInfo>>(){}.getType());
            user1.setGroups(groupsMap);
            return user1;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        } catch (ConnectException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 用于发送信息，目前只支持文本
     * @param content
     * @param toUser
     */
    public void send(String content,String toUser){
        sendText(content,toUser);
        if(!sendTextFeedback()) System.out.println("发送失败");
    }
    /**
     * 用于给其他用户发送信息
     * @param content
     * @param toUser
     */
    public void sendText(String content,String toUser){
        Text text = new Text(this.user.getAccount(),toUser,content);
        try{
            if(socket.isClosed()){
                throw new ConnectException("连接异常中断");
            }
            oos.writeObject(text);
            oos.flush();
            //oos没close
            //TODO
        }catch (IOException e){
            e.printStackTrace();
        } catch (ConnectException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用于接收发送信息反馈
     * @return
     */
    public boolean sendTextFeedback(){
        try {
            if(socket.isClosed()){
                throw new ConnectException("连接异常中断");
            }
            SysMsg msg=(SysMsg) ois.readObject();
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = jsonParser.parse(msg.getContent()).getAsJsonObject();
            if(jsonObject.get("code").getAsInt()!=1) return false;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (ConnectException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public Message receiveMessage(){
        try {
            if(ois==null){
                throw new IOException("输入流为null");
            }
            if(socket.isClosed()){
                throw new ConnectException("连接中断");
            }
            if(ois.available()>0) {
                Message msg = (Message) ois.readObject();
                System.out.println(msg);
                return msg;
            }else {
                Thread.sleep(1000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        } catch (ConnectException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 请求重新连接，没写完，其实可能没必要写
     */
    public void reConnect(){
        try {
            this.socket=new Socket(host,port);
            os=socket.getOutputStream();
            oos=new ObjectOutputStream(os);
            is=socket.getInputStream();
            ois=new ObjectInputStream(is);
            //TODO
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }
    public void run() {
        new Thread(()->{
            while(isLogin){
                receiveMessage();
            }
        }).start();
        while(isLive) {

        }
        if(!isLive){
            try {
                if (oos != null) oos.close();
                if (ois != null) ois.close();
                if (!socket.isClosed()) socket.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}
