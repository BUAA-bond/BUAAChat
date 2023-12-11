package Client;

import Message.Message;
import MyException.ConnectException;
import MyUtil.MyUtil;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.ietf.jgss.GSSName;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static Constant.Constant.host;
import static Constant.Constant.port;
@SuppressWarnings({"all"})
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
        // 创建外层 JsonObject
        JsonObject outerJsonObject = new JsonObject();
        outerJsonObject.addProperty("code", 102);
        JsonObject innerJsonObject = new JsonObject();
        innerJsonObject.addProperty("account", account);
        innerJsonObject.addProperty("name", name);
        innerJsonObject.addProperty("password", password);
        innerJsonObject.addProperty("avatarPath", avatarPath);
        outerJsonObject.add("data", innerJsonObject);
        Gson gson = new Gson();
        String content = gson.toJson(outerJsonObject);
        Message message = new Message(account, "0",content);
        //System.out.println(content);
        try {
            if(socket.isClosed()){
                throw new ConnectException("连接异常中断");
            }
            oos.writeObject(message);
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
        Message msg = null;
        try {
            if(socket.isClosed()){
                throw new ConnectException("连接异常中断");
            }
            msg = (Message) ois.readObject();
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(msg.getData(),JsonObject.class);
            if (jsonObject.get("status").getAsInt()!=9000) return false;
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
        if (!loginReceive()) {
            System.out.println("登录失败");
            return false;
        } else {
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
        jsonObject.addProperty("code",101);
        JsonObject data = new JsonObject();
        data.addProperty("account", account);
        data.addProperty("password", password);
        jsonObject.add("data",data);
        Gson gson = new Gson();
        String content = gson.toJson(jsonObject);
        Message msg = new Message(account,"0",content);
        try {
            if(socket.isClosed()){
                throw new ConnectException("连接异常中断");
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
     * 接收登录请求反馈,就相当于是初始化了
     * @return
     */
    public boolean loginReceive() {
        Message msg = null;
        try {
            if(socket.isClosed()){
                throw new ConnectException("连接异常中断");
            }
            msg = (Message) ois.readObject();
            String json = msg.getData();
            JsonObject jj1 = new Gson().fromJson(json,JsonObject.class);
            if (jj1.get("status").getAsInt()!=9000) return false;
            //TODO
            JsonObject data=jj1.get("data").getAsJsonObject();
            user.setName(data.get("name").getAsString());
            user.setAvatarPath(data.get("avatarPath").getAsString());
            JsonArray friendsJson=data.get("friends").getAsJsonArray();
            JsonArray groupsJson=data.get("groups").getAsJsonArray();
            setFriends(friendsJson);
            setGroups(groupsJson);
            return true;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        } catch (ConnectException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 初始化朋友
     * @param json
     */
    public void setFriends(JsonArray jsonArray){
        for(JsonElement friend:jsonArray){
            JsonObject friendObject = friend.getAsJsonObject();
            String name = friendObject.get("name").getAsString();
            String account = friendObject.get("account").getAsString();
            String avatar =friendObject.get("avatarPath").getAsString();
            this.user.getFriends().add(new UserInfo(account,name,avatar));
            JsonArray messages=friendObject.get("messages").getAsJsonArray();
            setMessages(account,messages);
        }
    }

    /**
     * 初始化群
     * @param json
     */
    public void setGroups(JsonArray jsonArray){
        for(JsonElement group:jsonArray){
            JsonObject groupObject = group.getAsJsonObject();
            String groupName=groupObject.get("name").getAsString();
            String groupAccount=groupObject.get("account").getAsString();
            String groupAvatar=groupObject.get("avatarPath").getAsString();
            GroupInfo groupInfo = new GroupInfo(groupName, groupAccount, groupAvatar);
            JsonArray menbers=groupObject.get("members").getAsJsonArray();
            ArrayList<UserInfo> list=groupInfo.members;
            for(JsonElement member:menbers){
                JsonObject memberObject = member.getAsJsonObject();
                String memberName=memberObject.get("name").getAsString();
                String memberAccount=memberObject.get("account").getAsString();
                String memberAvatar=memberObject.get("avatarPath").getAsString();
                list.add(new UserInfo(memberAccount,memberName,memberAvatar));
            }
            this.user.getGroups().add(groupInfo);//加入
            JsonArray messages=groupObject.get("messages").getAsJsonArray();
            setMessages(groupAccount,messages);
        }
    }

    /**
     * 初始化消息
     * @param account
     * @param jsonArray
     */
    public void setMessages(String account,JsonArray jsonArray){
        ArrayList<ChatInfo> chats=new ArrayList<>();
        for (JsonElement messageElement : jsonArray) {
            // 在这里处理每个 "messages" 数组元素
            JsonObject messageObject = messageElement.getAsJsonObject();
            String sendTime = messageObject.get("sendTime").getAsString();
            String content = messageObject.get("content").getAsString();
            String sender = messageObject.get("sender").getAsString();
            chats.add(new ChatInfo(sender,content,sendTime));
        }
        if(MyUtil.judgeAccount(account)) this.user.getMessagesF().put(account,chats);
        else this.user.getMessagesG().put(account,chats);
    }
    /**
     * 登出
     */
    public void logoutRequest(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code",109);
        JsonObject data=new JsonObject();
        data.addProperty("account", user.getAccount());
        jsonObject.add("data",data);
        String content=new Gson().toJson(jsonObject);
        Message msg = new Message(user.getAccount(),"0",content);
        try{
            oos.writeObject(msg);
            oos.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 登出反馈
     * @param json
     */
    public void logoutFeedback(String json){
        JsonObject jsonObject=new Gson().fromJson(json,JsonObject.class);
        JsonObject data=jsonObject.get("data").getAsJsonObject();
        if(data.get("status").getAsInt()==9000){
            save();
            isLogin=false;
            isLive=false;
            System.out.println("登出成功");
        }else{
            System.out.println("登出失败");
        }
    }
    /**
     * 修改密码
     * @param password
     */
    public void changePasswordRequest(String password){
        JsonObject jsonObject = new JsonObject();
        JsonObject data=new JsonObject();
        jsonObject.addProperty("code",103);
        data.addProperty("account", user.getAccount());
        data.addProperty("password", password);
        jsonObject.add("data",data);
        String content=new Gson().toJson(jsonObject);
        Message msg = new Message(user.getAccount(),"0",content);
        try {
            oos.writeObject(msg);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void changePasswordFeedback(String json){
        JsonObject jsonObject=new Gson().fromJson(json, JsonObject.class);
        JsonObject data=jsonObject.get("data").getAsJsonObject();
        if(data.get("status").getAsInt()==9000){
            user.setPassword(data.get("password").getAsString());
            System.out.println("密码修改成功");
        }else{
            System.out.println("密码修改失败");
        }
    }
    /**
     * 删除好友TODO
     * @param account
     */
    public void removeFriend(String account){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code",204);
        JsonObject data = new JsonObject();
        data.addProperty("from",user.getAccount());
        data.addProperty("account",account);
        jsonObject.add("data",data);
        String content=new Gson().toJson(jsonObject);
        Message message = new Message(user.getAccount(), "0",content);
        try {
            oos.writeObject(message);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 加入群聊申请
     * @param groupAccount
     */
    public void joinGroupRequest(String groupAccount){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code",210);
        JsonObject data = new JsonObject();
        data.addProperty("account",user.getAccount());
        data.addProperty("groupAccount",groupAccount);
        jsonObject.add("data",data);
        String content=new Gson().toJson(jsonObject);
        Message message = new Message(user.getAccount(),"0",content);
        try {
            oos.writeObject(message);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 退群
     * @param groupAccount
     */
    public void quitGroup(String groupAccount){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code",211);
        JsonObject data=new JsonObject();
        data.addProperty("account",user.getAccount());
        data.addProperty("groupAccount",groupAccount);
        jsonObject.add("data",data);
        String content=new Gson().toJson(jsonObject);
        Message message = new Message(user.getAccount(),"0",content);
        try {
            oos.writeObject(message);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 用于给其他用户发送信息
     * @param content
     * @param toUser
     */
    public void sendTextToF(String toUser,String content,int code){//501发文本，502发视频
        code=501;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code",code);
        JsonObject data=new JsonObject();
        data.addProperty("from",user.getAccount());
        data.addProperty("to",toUser);
        data.addProperty("content",content);
        jsonObject.add("data",data);
        String json=new Gson().toJson(jsonObject);
        Message message = new Message(user.getAccount(),toUser,json);
        try{
            if(socket.isClosed()){
                throw new ConnectException("连接异常中断");
            }
            oos.writeObject(message);
            oos.flush();
            HashMap<String, ArrayList<ChatInfo>> map=user.getMessagesF();
            if(map.containsKey(toUser)){
                //将信息记录
                ArrayList<ChatInfo> msgs=map.get(toUser);
                msgs.add(new ChatInfo(user.getAccount(),content));
            }else{
                ArrayList<ChatInfo> msgs=new ArrayList<>();
                msgs.add(new ChatInfo(user.getAccount(),content));
                map.put(toUser,msgs);
            }
        }catch (IOException e){
            e.printStackTrace();
        } catch (ConnectException e) {
            e.printStackTrace();
        }
    }
    public void receiveTextF(String json){
        JsonObject jsonObject=new Gson().fromJson(json,JsonObject.class);
        JsonObject data=jsonObject.get("data").getAsJsonObject();
        String content=data.get("content").getAsString();
        String from=data.get("from").getAsString();
        user.getMessagesF().get(from).add(new ChatInfo(from,content));
    }
    /**
     * 给别人发送好友申请
     * @param toUser
     */
    public void sendFriendRequest(String toUser){
        //this.user向toUser发送好友申请
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code",201);
        JsonObject data=new JsonObject();
        data.addProperty("from",user.getAccount());
        data.addProperty("to",toUser);
        jsonObject.add("data",data);
        Message msg = new Message(user.getAccount(), toUser,gson.toJson(jsonObject));
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
    public boolean receiveFriendFeedback(String json){//TODO
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
        if(jsonObject.get("code").getAsInt()==203) return false;
        JsonObject data=jsonObject.get("data").getAsJsonObject();
        String account=data.get("to").getAsString();
        String name=data.get("name").getAsString();
        String avatar=data.get("avatarPath").getAsString();
        user.getFriends().add(new UserInfo(account,name,avatar));
        return true;
    }
    /**
     * 接收好友申请
     * @param msg
     */
    public UserInfo receiveFriendRequest(String json){
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
        JsonObject data=jsonObject.get("data").getAsJsonObject();
        String account=data.get("from").getAsString();
        String name=data.get("name").getAsString();
        String avatar=data.get("avatar").getAsString();
        return new UserInfo(account,name,avatar);
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
            jsonObject.addProperty("code",202);
            //user.getFriends().put(info.account,info);
        } else jsonObject.addProperty("code",203);
        JsonObject data=new JsonObject();
        data.addProperty("from",toUser);
        data.addProperty("to",user.getAccount());
        jsonObject.add("data",data);
        String content=new Gson().toJson(jsonObject);
        Message msg = new Message(user.getAccount(),toUser,content);
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
    public void handleMessage(Message message){
        JsonParser jsonParser = new JsonParser();
        String json=message.getData();
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
        int code=jsonObject.get("code").getAsInt();
        switch (code){
            case 103://修改密码的回复
                changePasswordFeedback(json);
                break;
            case 109://登出的回复
                logoutFeedback(json);
                break;
            case 201://这里是别人的好友申请
                receiveFriendRequest(json);
                break;
            case 202://这里是我给别人发，然后别人给我的回馈
            case 203:
                receiveFriendRequest(json);
                break;
            case 501://收消息
                receiveTextF(json);
                break;
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

    /**
     * 退出后保存所有信息
     */
    public void save(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code",110);
        JsonObject data=new JsonObject();
        //加入基础信息
        data.addProperty("account",user.getAccount());
        data.addProperty("name",user.getName());
        data.addProperty("password",user.getPassword());
        data.addProperty("avatarPath",user.getAvatarPath());
        //保存朋友
        JsonArray friendsArray = new JsonArray();
        ArrayList<UserInfo> friends=user.getFriends();
        for (int i = 0; i < friends.size(); i++) {
            JsonObject friendObject = new JsonObject();
            UserInfo userInfo=friends.get(i);
            friendObject.addProperty("account",userInfo.account);
            friendObject.addProperty("name",userInfo.name);
            friendObject.addProperty("avatarPath",userInfo.avatarPath);
            //保存聊天记录
            JsonArray messagesArray = new JsonArray();
            ArrayList<ChatInfo> messages=user.getMessagesF().get(userInfo.account);
            for (int j = 0; j < messages.size(); j++) {
                ChatInfo chatInfo=messages.get(j);
                JsonObject chatObject = new JsonObject();
                chatObject.addProperty("sender",chatInfo.fromUser);
                chatObject.addProperty("content",chatInfo.content);
                chatObject.addProperty("sendTime",chatInfo.time);
                messagesArray.add(chatObject);
            }
            friendObject.add("messages",messagesArray);
        }
        data.add("friends",friendsArray);
        //保存群聊
        JsonArray groupArray=new JsonArray();
        ArrayList<GroupInfo> groups=user.getGroups();
        for (int i = 0; i < groups.size(); i++) {
            //基本信息
            JsonObject groupObject=new JsonObject();
            GroupInfo groupInfo = new GroupInfo();
            groupObject.addProperty("account",groupInfo.account);
            groupObject.addProperty("name",groupInfo.name);
            groupObject.addProperty("avatarPath",groupInfo.avatarPath);
            //保存群成员
            ArrayList<UserInfo> members=groupInfo.members;
            JsonArray memberArray=new JsonArray();
            for (int j = 0; j < members.size(); j++) {
                UserInfo userInfo=members.get(j);
                JsonObject memberObject=new JsonObject();
                memberObject.addProperty("account",userInfo.account);
                memberObject.addProperty("name",userInfo.name);
                memberObject.addProperty("avatarPath",userInfo.avatarPath);
                memberArray.add(memberObject);
            }
            groupObject.add("members",memberArray);
            //保存聊天信息
            ArrayList<ChatInfo> messages=user.getMessagesG().get(groupInfo.account);
            JsonArray messagesArray=new JsonArray();
            for (int j = 0; j < messages.size(); j++) {
                ChatInfo chatInfo=messages.get(j);
                JsonObject chatObject=new JsonObject();
                chatObject.addProperty("sender",chatInfo.fromUser);
                chatObject.addProperty("content",chatInfo.content);
                chatObject.addProperty("sendTime",chatInfo.time);
                messagesArray.add(chatObject);
            }
            groupObject.add("messages",messagesArray);
            groupArray.add(groupObject);
        }
        data.add("groups",groupArray);
        jsonObject.add("data",data);
        String content=new Gson().toJson(jsonObject);
        Message message = new Message(user.getAccount(),"0",content);
        try {
            oos.writeObject(message);
            oos.flush();
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
                Message message=receiveMessage();
                handleMessage(message);
            }
        }).start();
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
