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
import java.util.ArrayList;
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
    private static Message receiveMsg =null;
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
            HashMap<String, ArrayList<ChatInfo>> map=user.getMessages();
            if(map.containsKey(toUser)){
                //将信息记录
                ArrayList<ChatInfo> msgs=map.get(toUser);
                msgs.add(new ChatInfo(user.getAccount(),content));
            }else{
                ArrayList<ChatInfo> msgs=new ArrayList<>();
                msgs.add(new ChatInfo(user.getAccount(),content));
                map.put(toUser,msgs);
            }
            //oos没close
            //TODO
        }catch (IOException e){
            e.printStackTrace();
        } catch (ConnectException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收所有的消息，后续再根据消息类型作不同处理
     * @return
     */
    public Message receiveMessage(){
        try {
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
    public void addFriend(String toUser){

    }

    /**
     * 用于发送好友申请
     * @param toUser
     */
    public void sendFriendRequest(String toUser){
        //this.user向toUser发送好友申请
        SysMsg msg = new SysMsg(user.getAccount(), 4);
        msg.setToUser(toUser);//toUser账号
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("fromUserName",user.getName());
        jsonObject.addProperty("fromUserAvatarPath",user.getAvatarPath());
        msg.setContent(gson.toJson(jsonObject));
        try {
            if(socket.isClosed()){
                throw new ConnectException("连接中断");
            }
            oos.writeObject(msg);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ConnectException e) {
            e.printStackTrace();
        }
    }
    /**
     * 处理好友申请的反馈，通过的话就将好友加入列表
     * @param msg
     * @return
     */
    public boolean receiveFriendFeedback(SysMsg msg){
        String content=msg.getContent();
        String account=msg.getFromUser();//这里fromUser是对方，toUser是自己
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(content).getAsJsonObject();
        if(jsonObject.get("code").getAsInt()!=1) return false;
        //获取朋友的头像和名字
        String avatarPath=jsonObject.get("avatarPath").getAsString();
        String name=jsonObject.get("name").getAsString();
        //加入hashmap
        user.getFriends().put(account,new UserInfo(name,avatarPath));
        return true;
    }
    /**
     * 接收好友申请
     * @param msg
     */
    public UserInfo receiveFriendRequest(SysMsg msg){
        String account= msg.getFromUser();
        String content= msg.getContent();
        JsonParser jsonParser=new JsonParser();
        JsonObject jsonObject=jsonParser.parse(content).getAsJsonObject();
        String name=jsonObject.get("name").getAsString();
        String avatarPath=jsonObject.get("avatarPath").getAsString();
        return new UserInfo(account,name,avatarPath);
    }

    /**
     * 反馈好友申请
     * @param info
     * @param choose
     */
    public void sendRequestFeedback(UserInfo info,boolean choose){
        JsonObject jsonObject = new JsonObject();
        String toUser=info.account;
        if(choose){
            jsonObject.addProperty("code",1);
            user.getFriends().put(info.account,info);
        } else jsonObject.addProperty("code",0);
        String content=new Gson().toJson(jsonObject);
        SysMsg msg = new SysMsg(user.getAccount(),5,content);
        msg.setToUser(toUser);
        try {
            oos.writeObject(msg);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理系统返回的信息，好友申请、好友申请反馈......
     * @param msg
     */
    public void handleSysMsg(SysMsg msg){
        int type=msg.getType();
        switch (type){
            case 4://接收好友申请的反馈信息
                if(receiveFriendFeedback(msg)){
                    //成功，已经将好友集合更新
                    //这里需要处理一些显示的功能
                }else{
                    //失败
                    //这里需要处理显示的功能
                }
                break;
            case 5://接收别人发来的好友请求
                UserInfo info=receiveFriendRequest(msg);
                //根据info显示一些信息
                break;
        }
    }
    public void handleText(Text text){
        String fromUser= text.getFromUser();
        HashMap<String, ArrayList<ChatInfo>> map=user.getMessages();
        if(map.containsKey(fromUser)){
            //将信息记录
            ArrayList<ChatInfo> msgs=map.get(fromUser);
            msgs.add(new ChatInfo(fromUser, text.getContent()));
        }else{
            ArrayList<ChatInfo> msgs=new ArrayList<>();
            msgs.add(new ChatInfo(fromUser, text.getContent()));
            map.put(fromUser,msgs);
        }
    }
    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }
    public void run() {
        new Thread(()->{
            while(isLogin){
                receiveMsg=receiveMessage();
                if(receiveMsg!=null){
                    if(receiveMsg instanceof Text){
                        handleText((Text) receiveMsg);
                    }else if(receiveMsg instanceof SysMsg){
                        handleSysMsg((SysMsg) receiveMsg);
                    }
                }
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

    public User getUser() {
        return user;
    }
}
