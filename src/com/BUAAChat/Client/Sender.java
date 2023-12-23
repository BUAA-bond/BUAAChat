package com.BUAAChat.Client;

import com.BUAAChat.Info.ChatInfo;
import com.BUAAChat.Info.RequestInfo;
import com.BUAAChat.Info.UserInfo;
import com.BUAAChat.Message;
import com.BUAAChat.MyUtil.MyUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import static com.BUAAChat.MyUtil.MyUtil.*;

/**
 * 专门用于发送请求的类
 * @author 西西弗
 * @date 2023/11/17 10:08
 */
public class Sender {
    /**
     *输出流，从socket获取
     */
    private OutputStream os=null;

    /**
     *对象输出流，用于发送Message
     */
    private ObjectOutputStream oos=null;

    /**
     * 构造器
     * @param socket 连接的socket
     */
    public Sender(Socket socket) {
        try {
            //获取输出流实例
            os=socket.getOutputStream();
            oos=new ObjectOutputStream(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用于发送Message，主要是为了减少每次都try-catch的麻烦
     * @param message
     */
    public void send(Message message){
        try {
            oos.writeObject(message);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 发送获取信息请求
     * @param account
     * @param code
     */
    public void getInfoRequest(String account,String code){
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("code",code);
        JsonObject data=new JsonObject();
        data.addProperty("uAccount",account);
        jsonObject.add("data",data);
        Message message = new Message(account,"0",new Gson().toJson(jsonObject));
        send(message);
    }
    /**
     * 请求获取这个用户的所有朋友信息，包括聊天记录，用于初始化
     * @param account
     */
    public void getAllFriendsInfoRequest(String account){
        getInfoRequest(account,"402");
    }
    /**
     * 请求获取这个用户的所有群聊信息，包括聊天记录，用于初始化
     * @param account
     */
    public void getAllGroupsInfoRequest(String account){
        getInfoRequest(account,"404");
    }
    /**
     * 请求获取这个用户有关的 所有加好友申请
     * @param account
     */
    public void getAllRequestInfoRequest(String account){//todo
        getInfoRequest(account,"405");
    }
    /**
     * 发送获取某个群聊的信息请求
     * @param account
     */
    public void getGroupInfoRequest(String account){
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("code","403");
        JsonObject data=new JsonObject();
        data.addProperty("gAccount",account);
        jsonObject.add("data",data);
        Message message = new Message(account,"0",new Gson().toJson(jsonObject));
        send(message);
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
        outerJsonObject.addProperty("code", "102");
        JsonObject innerJsonObject = new JsonObject();
        innerJsonObject.addProperty("account", account);
        innerJsonObject.addProperty("name", name);
        innerJsonObject.addProperty("password", password);
        innerJsonObject.addProperty("avatar", avatarPath);
        outerJsonObject.add("data", innerJsonObject);
        Gson gson = new Gson();
        String content = gson.toJson(outerJsonObject);
        Message message = new Message(account, "0",content);
        send(message);
    }
    /**
     * 发送登录 请求
     * @param account
     * @param password
     */
    public void loginSend(String account, String password) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code","101");
        JsonObject data = new JsonObject();
        data.addProperty("account", account);
        data.addProperty("password", password);
        jsonObject.add("data",data);
        Gson gson = new Gson();
        String content = gson.toJson(jsonObject);
        Message msg = new Message(account,"0",content);
        send(msg);
    }
    /**
     * 发送修改密码 请求
     * @param password
     */
    public void changePasswordRequest(String password){
        User user=User.getUser();
        JsonObject jsonObject = new JsonObject();
        JsonObject data=new JsonObject();
        jsonObject.addProperty("code","103");
        data.addProperty("account", user.getAccount());
        data.addProperty("password", password);
        jsonObject.add("data",data);
        String content=new Gson().toJson(jsonObject);
        Message msg = new Message(user.getAccount(),"0",content);
        send(msg);
    }
    /**
     * 发送登出 请求
     */
    public void logoutRequest(){
        User user=User.getUser();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code","109");
        JsonObject data=new JsonObject();
        data.addProperty("account", user.getAccount());
        jsonObject.add("data",data);
        String content=new Gson().toJson(jsonObject);
        Message msg = new Message(user.getAccount(),"0",content);
        send(msg);
    }
    /**
     * 发送 删除好友请求
     * @param account
     */
    public void removeFriendRequest(String account){
        User user=User.getUser();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code","204");
        JsonObject data = new JsonObject();
        data.addProperty("account_A",user.getAccount());
        data.addProperty("account_B",account);
        jsonObject.add("data",data);
        String content=new Gson().toJson(jsonObject);
        Message message = new Message(user.getAccount(), "0",content);
        send(message);
    }
    /**
     * 发送建群 请求
     * @param gAccount
     * @param name
     * @param avatar
     */
    public void buildGroupRequest(String gAccount,String name,String avatar){
        User user=User.getUser();
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("code","209");
        JsonObject data=new JsonObject();
        data.addProperty("uAccount",user.getAccount());
        data.addProperty("gAccount",gAccount);
        data.addProperty("name",name);
        data.addProperty("avatar",avatar);
        jsonObject.add("data",data);
        Message message = new Message(user.getAccount(),"0",new Gson().toJson(jsonObject));
        send(message);
    }
    /**
     * 发送 加入群聊 请求
     * @param uAccount
     * @param groupAccount
     */
    public void joinGroupRequest(String uAccount,String groupAccount){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code","210");
        JsonObject data = new JsonObject();
        data.addProperty("uAccount",uAccount);
        data.addProperty("gAccount",groupAccount);
        jsonObject.add("data",data);
        String content=new Gson().toJson(jsonObject);
        Message message = new Message(uAccount,"0",content);
        send(message);
    }
    /**
     * 发送修改名字 请求
     * @param name
     */
    public void modifyUserNameRequest(String name){
        User user=User.getUser();
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("code","301");
        JsonObject data=new JsonObject();
        data.addProperty("account",user.getAccount());
        data.addProperty("name",name);
        jsonObject.add("data",data);
        String content=new Gson().toJson(jsonObject);
        Message message = new Message(user.getAccount(), "0", content);
        send(message);
    }
    /**
     * 发送修改头像 请求
     * @param avatarPath
     */
    public void modifyUserAvatarRequest(String avatarPath){
        User user=User.getUser();
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("code","302");
        JsonObject data=new JsonObject();
        data.addProperty("avatar",avatarPath);
        data.addProperty("account",user.getAccount());
        jsonObject.add("data",data);
        Gson gson=new Gson();
        String content=gson.toJson(jsonObject);
        Message message = new Message(user.getAccount(), "0", content);
        send(message);
    }
    /**
     * 用于发送信息,给用户或者群聊
     * @param content
     * @param toUser
     */
    public void sendText(String toUser,String content){
        User user=User.getUser();
        //501发文本，502发视频，但现在只支持发文本
        String code="501";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code",code);
        JsonObject data=new JsonObject();
        data.addProperty("from",User.getUser().getAccount());
        data.addProperty("to",toUser);
        data.addProperty("content",content);
        jsonObject.add("data",data);
        String json=new Gson().toJson(jsonObject);
        Message message = new Message(User.getUser().getAccount(),toUser,json);
        //发送
        send(message);
        HashMap<String, ArrayList<ChatInfo>> map=null;
        //判断是给群发 还是 单个用户发
        if(MyUtil.judgeAccount(toUser))
            map=user.getMessagesF();
        else map=user.getMessagesG();
        //判断是否包含这个键
        if(map.containsKey(toUser)){
            //将信息记录
            ArrayList<ChatInfo> msgs=map.get(toUser);
            msgs.add(new ChatInfo(new UserInfo(user.getAccount(),user.getName(),user.getAvatarPath()),content));
        }else{
            ArrayList<ChatInfo> msgs=new ArrayList<>();
            msgs.add(new ChatInfo(new UserInfo(user.getAccount(),user.getName(),user.getAvatarPath()),content));
            map.put(toUser,msgs);
        }
    }
    /**
     * 给别人发送好友申请
     * @param toUser
     * @param name
     * @param avatar
     */
    public void addFriendRequest(String toUser,String name,String avatar){
        User user=User.getUser();
        System.out.println("addFriendRequest:"+name);
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code","201");
        JsonObject data=new JsonObject();
        data.addProperty("account_A",user.getAccount());
        data.addProperty("account_B",toUser);
        jsonObject.add("data",data);
        Message msg = new Message(user.getAccount(), toUser,gson.toJson(jsonObject));
        send(msg);
    }
    /**
     * 反馈好友申请
     * @param
     * @param choose
     */
    public void sendRequestFeedback(String toUser,String name,String avatar,boolean choose){
        System.out.println("receiveAddFriendFeedback:"+toUser);
        User user=User.getUser();
        JsonObject jsonObject = new JsonObject();
        ArrayList<RequestInfo> requestInfos=user.getRequests();
        if(choose){
            //如果点的是同意
            jsonObject.addProperty("code","202");
            //将请求type改为1
            for (int i = 0; i < requestInfos.size(); i++) {
                RequestInfo requestInfo=requestInfos.get(i);
                if(requestInfo.from.equals(toUser)){
                    requestInfo.type=1;
                }
            }
            user.getFriends().add(new UserInfo(toUser,name,avatar));
            System.out.println("已经将"+toUser+"加入好友集合");
            //同意，更新列表
            updateFriendList();
        } else {
            //拒绝
            jsonObject.addProperty("code","203");
            for (int i = 0; i < requestInfos.size(); i++) {
                RequestInfo requestInfo=requestInfos.get(i);
                if(requestInfo.from.equals(toUser)){
                    requestInfo.type=-1;
                }
            }
        }
        JsonObject data=new JsonObject();
        data.addProperty("account_A",toUser);
        data.addProperty("account_B",user.getAccount());
        jsonObject.add("data",data);
        String content=new Gson().toJson(jsonObject);
        Message msg = new Message(user.getAccount(),toUser,content);
        send(msg);
    }

    /**
     *关闭流
     */
    public void close(){
        try {
            if(os!=null) {os.close();}
            if(oos!=null) oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
