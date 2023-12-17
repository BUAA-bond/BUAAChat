package com.BUAAChat.Client;

import com.BUAAChat.Message;
import com.BUAAChat.MyUtil.MyUtil;
import com.google.gson.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import static com.BUAAChat.Constant.Constant.host;
import static com.BUAAChat.Constant.Constant.port;

public class Client implements Runnable {
    private Socket socket;
    private InputStream is=null;
    private OutputStream os=null;
    private ObjectInputStream ois=null;
    private ObjectOutputStream oos=null;
    private boolean isLogin =false;
    private boolean isLive=false;
    private User user=new User();
    private static Message receiveMsg =null;
    private final Object lock = new Object();
    private boolean isPaused = false;
    public Client() {
    }
    /**
     * client建立连接
     */
    public void init(){
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
     * 初始化用户，应该在登录成功后使用
     * @param account
     */
    public void userInit(String account,String password){
        Message msg=null;
        String json=null;
        //初始化用户
        //user.setName("ccf");
        UserInfo userInfo = getUserInfo(account);
        //System.out.println(userInfo.name);
        user.setName(userInfo.name);
        user.setAvatarPath(userInfo.avatarPath);
        user.setAccount(account);
        user.setPassword(password);
        System.out.println("userInit");
        System.out.println(userInfo.name);
        System.out.println(userInfo.account);
        System.out.println(userInfo.avatarPath);
        try {
            //初始化朋友
            getAllFriendsInfoRequest(account);//发送请求
            msg=(Message)ois.readObject();
            json=msg.getContent();
            System.out.println("friends:"+json);
            user.setFriends(getAllFriendsInfoFeedback(json,0));
            //初始化群
            getAllGroupsInfoRequest(account);//发送请求
            msg=(Message)ois.readObject();
            json=msg.getContent();
            System.out.println("groups:"+json);
            user.setGroups(getAllGroupsInfoFeedback(json,0));
            //初始化请求
            getAllRequestInfoRequest(account);
            msg=(Message)ois.readObject();
            json=msg.getContent();
            System.out.println("requests:"+json);
            user.setRequests(getAllRequestInfoFeedback(json));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
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
        try {
            oos.writeObject(message);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求获取某个用户信息
     * @param account
     */
    public UserInfo getUserInfo(String account){
        pauseThread();
        getInfoRequest(account,"401");
        System.out.println("getUserInfo:"+account);
        //System.out.println(123123123);
        UserInfo userInfo=getUserInfoFeedback();
        resumeThread();
        return userInfo;
    }
    public ArrayList<UserInfo> searchUser(String account){
        ArrayList<UserInfo> tmp=new ArrayList<>();
        tmp.add(getUserInfo(account));
        return tmp;
    }
    /**
     * 获取某个群聊的信息
     * @param account
     * @return
     */
    public GroupInfo getGroupInfo(String account){
        pauseThread();
        getGroupInfoRequest(account);
        GroupInfo groupInfo=getGroupInfoFeedback();
        resumeThread();
        return groupInfo;
    }
    public void getAllRequestInfoRequest(String account){//todo
        getInfoRequest(account,"405");
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
        try {
            oos.writeObject(message);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取某个群聊信息的反馈
     * @return
     */
    public GroupInfo getGroupInfoFeedback(){
        JsonObject jsonObject1=getJsonObjectFromMsg("403");
        if(jsonObject1.get("status").getAsInt()!=9000) return null;
        JsonObject data=jsonObject1.get("data").getAsJsonObject();
        String account=data.get("account").getAsString();
        String name=data.get("name").getAsString();
        String avatar=data.get("avatar").getAsString();
        GroupInfo groupInfo = new GroupInfo(account, name, avatar);
        JsonArray members=data.get("members").getAsJsonArray();
        for (int i = 0; i < members.size(); i++) {
            JsonObject memberObject=(JsonObject) members.get(i);
            UserInfo userInfo = new UserInfo(memberObject.get("account").getAsString(),
                    memberObject.get("name").getAsString(), memberObject.get("avatar").getAsString());
            groupInfo.members.add(userInfo);
        }
        return groupInfo;
    }
    /**
     * 获取用户信息的反馈
     * @return
     */
    public UserInfo getUserInfoFeedback(){
        JsonObject jsonObject = getJsonObjectFromMsg("401");
        if (jsonObject.get("status").getAsInt() != 9000) return null;
        JsonObject data = jsonObject.get("data").getAsJsonObject();
        String name = data.get("name").getAsString();
        String account = data.get("account").getAsString();
        String avatar = data.get("avatar").getAsString();
        UserInfo userInfo = new UserInfo(account,name, avatar);
        return userInfo;
    }

    /**
     * 获取 好友请求信息 反馈
     * @return
     */
    public ArrayList<RequestInfo> getAllRequestInfoFeedback(String json){
        JsonObject jsonObject=new Gson().fromJson(json,JsonObject.class);
        if(jsonObject.get("status").getAsInt()!=9000) return null;
        JsonObject data=jsonObject.get("data").getAsJsonObject();
        JsonArray requests=data.get("history").getAsJsonArray();
        ArrayList<RequestInfo> tmp=new ArrayList<>();
        //System.out.println("getAllRequestInfoFeedback===begin");
        if(requests!=null)
            for(JsonElement requestElement: requests){
                RequestInfo requestInfo = new RequestInfo();
                JsonObject requestObject=(JsonObject) requestElement;
                String account_A=requestObject.get("sender").getAsString();
                String account_B=requestObject.get("recipient").getAsString();
                String type=requestObject.get("status").getAsString();
                if(account_A.equals(user.getAccount())){
                    getInfoRequest(account_B,"401");
                }else{
                    getInfoRequest(account_A,"401");
                }
                UserInfo userInfo=getUserInfoFeedback();
                requestInfo.from=account_A;
                requestInfo.to=account_B;
                requestInfo.name=userInfo.name;
                requestInfo.avatarPath=userInfo.avatarPath;
                if(type.equals("pending")) requestInfo.type=0;
                else if(type.equals("accepted")) requestInfo.type=1;
                else if(type.equals("rejected")) requestInfo.type=-1;
                tmp.add(requestInfo);
                //System.out.println("getAllRequestInfoFeedback===end");
            }
        return tmp;
    }
    /**
     * 用于初始化
     * @param json
     * @return
     */
    public ArrayList<UserInfo> getAllFriendsInfoFeedback(String json,int sign){
        JsonObject jsonObject=new Gson().fromJson(json, JsonObject.class);
        if(jsonObject.get("status").getAsInt()!=9000) return null;
        JsonElement jsonElement=jsonObject.get("data");
        JsonArray friendsJson=null;
        if(jsonElement!=null && jsonElement.isJsonArray()){
            friendsJson=jsonObject.get("data").getAsJsonArray();
        }else if(jsonElement!=null && jsonElement.isJsonObject()){
            JsonObject data=jsonObject.get("data").getAsJsonObject();
            friendsJson=data.get("friends").getAsJsonArray();
        }
        return setFriends(friendsJson,sign);
    }
    /**
     * 用于初始化
     * @param json
     * @return
     */
    public ArrayList<GroupInfo> getAllGroupsInfoFeedback(String json,int sign){
        JsonObject jsonObject=new Gson().fromJson(json, JsonObject.class);
        if(jsonObject.get("status").getAsInt()!=9000) return null;
        JsonElement jsonElement=jsonObject.get("data");
        JsonArray groupsJson=null;
        if(jsonElement!=null && jsonElement.isJsonArray()){
            groupsJson=jsonObject.get("data").getAsJsonArray();
        }else if(jsonElement!=null && jsonElement.isJsonObject()){
            JsonObject data=jsonObject.get("data").getAsJsonObject();
            groupsJson=data.get("groups").getAsJsonArray();
        }
        return setGroups(groupsJson,sign);
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
        try {
            oos.writeObject(message);
            oos.flush();
        } catch (IOException e) {
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

            msg = (Message) ois.readObject();
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(msg.getContent(),JsonObject.class);
            if (jsonObject.get("status").getAsInt()!=9000) return false;
        } catch (ClassNotFoundException | IOException e) {
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
        if (!loginReceive(account)) {
            System.out.println("登录失败");
            return false;
        } else {
            System.out.println("登录成功");
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
        jsonObject.addProperty("code","101");
        JsonObject data = new JsonObject();
        data.addProperty("account", account);
        data.addProperty("password", password);
        jsonObject.add("data",data);
        Gson gson = new Gson();
        String content = gson.toJson(jsonObject);
        Message msg = new Message(account,"0",content);
        try {
            oos.writeObject(msg);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 接收登录请求反馈
     * @return
     */
    public boolean loginReceive(String account) {
        Message msg = null;
        try {
            msg = (Message) ois.readObject();
            String json = msg.getContent();
            JsonObject jj1 = new Gson().fromJson(json,JsonObject.class);
            if (jj1.get("status").getAsInt()!=9000) return false;
            return true;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 初始化朋友
     * @param
     */
    public ArrayList<UserInfo> setFriends(JsonArray jsonArray,int sign){
        ArrayList<UserInfo> tmp=new ArrayList<>();
        if(jsonArray!=null)
            for(JsonElement friend:jsonArray){
                JsonObject friendObject = friend.getAsJsonObject();
                String name = friendObject.get("name").getAsString();
                String account = friendObject.get("account").getAsString();
                String avatar =friendObject.get("avatar").getAsString();
                UserInfo userInfo=new UserInfo(account,name,avatar);
                tmp.add(userInfo);
                if(sign==0) {//初始化获取信息标记
                    JsonArray messages = friendObject.get("messages").getAsJsonArray();
                    setMessages(userInfo, messages);
                }
            }
        return tmp;
    }
    /**
     * 初始化群
     * @param
     */
    public ArrayList<GroupInfo> setGroups(JsonArray jsonArray,int sign){
        ArrayList<GroupInfo> tmp=new ArrayList<>();
        if(jsonArray!=null)
            for(JsonElement group:jsonArray){
                JsonObject groupObject = group.getAsJsonObject();
                String groupName=groupObject.get("name").getAsString();
                String groupAccount=groupObject.get("account").getAsString();
                String groupAvatar=groupObject.get("avatar").getAsString();
                GroupInfo groupInfo = new GroupInfo(groupName, groupAccount, groupAvatar);
                JsonArray members=groupObject.get("members").getAsJsonArray();
                ArrayList<UserInfo> list=groupInfo.members;
                if(members!=null)
                    for(JsonElement member:members){
                        JsonObject memberObject = member.getAsJsonObject();
                        String memberName=memberObject.get("name").getAsString();
                        String memberAccount=memberObject.get("account").getAsString();
                        String memberAvatar=memberObject.get("avatar").getAsString();
                        list.add(new UserInfo(memberAccount,memberName,memberAvatar));
                    }
                tmp.add(groupInfo);//加入
                if(sign==0){//初始化标记
                    JsonArray messages=groupObject.get("messages").getAsJsonArray();
                    setMessages(groupInfo,messages);
                }
            }
        return tmp;
    }
    /**
     * 初始化消息
     * @param
     * @param jsonArray
     */
    public void setMessages(UserInfo userInfo,JsonArray jsonArray){
        ArrayList<ChatInfo> chats=new ArrayList<>();
        for (JsonElement messageElement : jsonArray) {
            // 在这里处理每个 "messages" 数组元素
            JsonObject messageObject = messageElement.getAsJsonObject();
            String sendTime = messageObject.get("sendTime").getAsString();
            String content = messageObject.get("content").getAsString();
            String sender = messageObject.get("sender").getAsString();
            if(sender.equals(userInfo.account))
                chats.add(new ChatInfo(userInfo,content,sendTime));
            else chats.add(new ChatInfo(new UserInfo(user.getAccount(),user.getName(),user.getAvatarPath()),content,sendTime));
        }
        //保存在user里
        this.user.getMessagesF().put(userInfo.account,chats);
    }
    public void setMessages(GroupInfo groupInfo,JsonArray jsonArray){
        ArrayList<ChatInfo> chats=new ArrayList<>();
        ArrayList<UserInfo> members=groupInfo.members;
        for (JsonElement messageElement : jsonArray) {
            // 在这里处理每个 "messages" 数组元素
            JsonObject messageObject = messageElement.getAsJsonObject();
            String content = messageObject.get("content").getAsString();
            String sendTime = messageObject.get("sendTime").getAsString();
            String sender = messageObject.get("sender").getAsString();
            UserInfo userInfo=null;
            for (int i = 0; i < members.size(); i++) {//找到这个发送者
                userInfo=members.get(i);
                if(userInfo.account.equals(sender)) break;
            }
            chats.add(new ChatInfo(userInfo,content,sendTime));
        }
        //保存在user里
        this.user.getMessagesG().put(groupInfo.account,chats);
    }
    /**
     * 根据code读取信息，如果不符合的就转发，然后一直读到符合为止
     * @param code
     * @return
     */
    static Message tmpMsg=null;
    public JsonObject getJsonObjectFromMsg(String code){
        String json=null;
        JsonObject jsonObject=null;
        try {
            while(true){
                tmpMsg=(Message)ois.readObject();
                json=tmpMsg.getContent();
                jsonObject=new Gson().fromJson(json, JsonObject.class);
                if(!jsonObject.get("code").getAsString().equals(code)){//如果不是需要的消息类型，那就开启一个线程去处理
                    new Thread(()->{
                        handleMessage(tmpMsg);
                    }).start();
                }
                else break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
    /**
     * 修改密码
     * @param password
     */
    public boolean changePassword(String password){
        pauseThread();
        changePasswordRequest(password);
        boolean sign=changePasswordFeedback(password);
        resumeThread();
        return sign;
    }
    public void changePasswordRequest(String password){
        JsonObject jsonObject = new JsonObject();
        JsonObject data=new JsonObject();
        jsonObject.addProperty("code","103");
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
    public boolean changePasswordFeedback(String password){
        JsonObject jsonObject=getJsonObjectFromMsg("103");
        if(jsonObject.get("status").getAsInt()==9000){
            user.setPassword(password);
            System.out.println("密码修改成功");
            return true;
        }else{
            System.out.println("密码修改失败");
            return false;
        }
    }
    /**
     * 登出
     */
    public boolean logout(){
        pauseThread();
        logoutRequest();
        boolean sign=logoutFeedback();
        resumeThread();
        return sign;
    }
    public void logoutRequest(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code","109");
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
    public boolean logoutFeedback(){
        JsonObject jsonObject=getJsonObjectFromMsg("109");
        if(jsonObject.get("status").getAsInt()==9000){
            isLogin=false;
            isLive=false;
            if (!isLogin) {
                try {
                    if (oos != null) oos.close();
                    if (ois != null) ois.close();
                    if (!socket.isClosed()) socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("登出成功");
            return true;
        }else{
            System.out.println("登出失败");
            return false;
        }
    }

    /**
     * 删除好友TODO
     * @param account
     */
    public boolean removeFriend(String account){
        pauseThread();
        removeFriendRequest(account);
        boolean sign=removeFriendFeedback(account);
        resumeThread();
        return sign;
    }
    public void removeFriendRequest(String account){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code","204");
        JsonObject data = new JsonObject();
        data.addProperty("account_A",user.getAccount());
        data.addProperty("account_B",account);
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
    public boolean removeFriendFeedback(String account){
        JsonObject jsonObject=getJsonObjectFromMsg("204");
        if(jsonObject.get("status").getAsInt()==9000){
            System.out.println("删除成功");
            ArrayList<UserInfo> friends=user.getFriends();
            for (int i = 0; i < friends.size(); i++) {
                UserInfo userInfo=friends.get(i);
                if(userInfo.account.equals(account)){
                    friends.remove(userInfo);
                    break;
                }
            }
            HashMap<String ,ArrayList<ChatInfo>> messages=user.getMessagesF();
            messages.remove(account);
            return true;
        }else return false;
    }
    public boolean buildGroup(String gAccount,String name,String avatar,ArrayList<UserInfo> members){
        pauseThread();
        buildGroupRequest(gAccount,name,avatar);
        boolean sign=buildGroupFeedback(gAccount,name,avatar);
        if(sign){
            for (int i = 0; i < members.size(); i++) {
                UserInfo userInfo=members.get(i);
                joinGroup(userInfo,gAccount);
            }
        }
        resumeThread();
        return sign;
    }
    public void buildGroupRequest(String gAccount,String name,String avatar){
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("code","209");
        JsonObject data=new JsonObject();
        data.addProperty("uAccount",user.getAccount());
        data.addProperty("gAccount",gAccount);
        data.addProperty("gName",name);
        data.addProperty("gAvatar",avatar);
        jsonObject.add("data",data);
        Message message = new Message(user.getAccount(),"0",new Gson().toJson(jsonObject));
        try {
            oos.writeObject(message);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean buildGroupFeedback(String gAccount,String name,String avatar){
        JsonObject jsonObject=getJsonObjectFromMsg("209");
        if(jsonObject.get("status").getAsInt()==9000){
            GroupInfo groupInfo = new GroupInfo(gAccount, name, avatar);
            groupInfo.members.add(new UserInfo(user.getAccount(),user.getName(),user.getAvatarPath()));
            user.getGroups().add(groupInfo);
            user.getMessagesG().put(gAccount,new ArrayList<ChatInfo>());
            return true;
        } else return false;
    }
    /**
     * 加入群聊申请
     * @param groupAccount
     */
    public boolean joinGroup(UserInfo userInfo,String groupAccount){
        joinGroupRequest(userInfo.account,groupAccount);
        boolean sign=joinGroupFeedback(userInfo,groupAccount);
        return sign;
    }
    public void joinGroupRequest(String uAccount,String groupAccount){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code","210");
        JsonObject data = new JsonObject();
        data.addProperty("uAccount",uAccount);
        data.addProperty("gAccount",groupAccount);
        jsonObject.add("data",data);
        String content=new Gson().toJson(jsonObject);
        Message message = new Message(uAccount,"0",content);
        try {
            oos.writeObject(message);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean joinGroupFeedback(UserInfo userInfo,String groupAccount){
        JsonObject jsonObject=getJsonObjectFromMsg("210");
        if(jsonObject.get("status").getAsInt()!=9000) return false;
        ArrayList<GroupInfo> groups=user.getGroups();
        GroupInfo groupInfo=null;
        for (int i = groups.size()-1; i >= 0; i--) {
            groupInfo=groups.get(i);
            if(groupInfo.account.equals(groupAccount)) break;
        }
        groupInfo.members.add(userInfo);
        return true;
    }

    /**
     * 退群
     * @param groupAccount
     */
    public void quitGroup(String groupAccount){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code","211");
        JsonObject data=new JsonObject();
        data.addProperty("uAccount",user.getAccount());
        data.addProperty("gAccount",groupAccount);
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
     * 修改用户名
     * @param name
     */
    public boolean modifyUserName(String name){
        pauseThread();
        modifyUserNameRequest(name);
        boolean sign=modifyUserNameFeedback(name);
        resumeThread();
        return sign;
    }
    public void modifyUserNameRequest(String name){
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("code","301");
        JsonObject data=new JsonObject();
        data.addProperty("account",user.getAccount());
        data.addProperty("name",name);
        jsonObject.add("data",data);
        String content=new Gson().toJson(jsonObject);
        Message message = new Message(user.getAccount(), "0", content);
        try {
            oos.writeObject(message);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean modifyUserNameFeedback(String name){
        JsonObject jsonObject=getJsonObjectFromMsg("301");
        int status=jsonObject.get("status").getAsInt();
        if(status!=9000){ System.out.println("修改名字失败");return false;}
        user.setName(name);
        System.out.println("修改名字成功");
        return true;
    }
    /**
     * 用于发送信息,给用户或者群聊
     * @param content
     * @param toUser
     */
    public void sendText(String toUser,String content){//501发文本，502发视频
        String code="501";
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
            oos.writeObject(message);
            oos.flush();
            HashMap<String, ArrayList<ChatInfo>> map=null;
            if(MyUtil.judgeAccount(toUser))
                map=user.getMessagesF();
            else if(MyUtil.judgeGroupAccount(toUser))
                map=user.getMessagesG();
            if(map.containsKey(toUser)){
                //将信息记录
                ArrayList<ChatInfo> msgs=map.get(toUser);
                msgs.add(new ChatInfo(new UserInfo(user.getAccount(),user.getName(),user.getAvatarPath()),content));
            }else{
                ArrayList<ChatInfo> msgs=new ArrayList<>();
                msgs.add(new ChatInfo(new UserInfo(user.getAccount(),user.getName(),user.getAvatarPath()),content));
                map.put(toUser,msgs);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void receiveText(String json){
        JsonObject jsonObject=new Gson().fromJson(json,JsonObject.class);
        if(jsonObject.get("status").getAsInt()==9000){
            return;
        }
        JsonObject data=jsonObject.get("data").getAsJsonObject();
        String content=data.get("content").getAsString();
        String from=data.get("from").getAsString();
        String to=data.get("to").getAsString();
        HashMap<String, ArrayList<ChatInfo>> map=null;
        if(MyUtil.judgeAccount(to)){
            map=user.getMessagesF();
            ArrayList<UserInfo> tmpFriends=user.getFriends();
            UserInfo userInfo=null;
            for (int i = 0; i < tmpFriends.size(); i++) {
                userInfo=tmpFriends.get(i);
                if(userInfo.account.equals(from)) break;
            }
            if(map.containsKey(from)){
                //将信息记录
                ArrayList<ChatInfo> msgs=map.get(from);
                msgs.add(new ChatInfo(userInfo,content));
                //map.put(from,msgs);
            }else{
                ArrayList<ChatInfo> msgs=new ArrayList<>();
                msgs.add(new ChatInfo(userInfo,content));
                map.put(from,msgs);
            }
        }else{
            //to是群聊号，from是群里那个发这条信息的人
            map=user.getMessagesG();
            ArrayList<GroupInfo> tmpGroups=user.getGroups();
            GroupInfo groupInfo=null;
            for (int i = 0; i < tmpGroups.size(); i++) {
                groupInfo=tmpGroups.get(i);
                if(groupInfo.account.equals(to)) break;
            }
            ArrayList<UserInfo> tmpMembers=groupInfo.members;
            UserInfo userInfo=null;
            for (int i = 0; i < tmpMembers.size(); i++) {
                userInfo=tmpMembers.get(i);
                if(userInfo.account.equals(from)) break;
            }
            if(!map.containsKey(to)){
                //将信息记录
                ArrayList<ChatInfo> msgs=new ArrayList<>();
                msgs.add(new ChatInfo(userInfo,content));
                map.put(to,msgs);
            }else{
                ArrayList<ChatInfo> msgs=map.get(to);
                msgs.add(new ChatInfo(userInfo,content));
                //map.put(to,msgs);
            }
        }

    }
    /**
     * 给别人发送好友申请
     * @param toUser
     */
    public void addFriendRequest(String toUser,String name,String avatar){//todo
        System.out.println("addFriendRequest:"+name);
        //this.user向toUser发送好友申请
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code","201");
        JsonObject data=new JsonObject();
        data.addProperty("account_A",user.getAccount());
        data.addProperty("account_B",toUser);
        jsonObject.add("data",data);
        Message msg = new Message(user.getAccount(), toUser,gson.toJson(jsonObject));
        try {
            oos.writeObject(msg);
            oos.flush();
            //user.getRequests().add(new RequestInfo(user.getAccount(),toUser,name,avatar,0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 处理好友申请的反馈，通过的话就将好友加入列表
     * @param
     * @return
     */
    public boolean addFriendFeedback(String json){
        System.out.println("addFriendFeedback:"+json);
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
        if(jsonObject.get("status").getAsInt()==9000) return false;
        ArrayList<RequestInfo> requestInfos=user.getRequests();
        JsonObject data=jsonObject.get("data").getAsJsonObject();
        String account=data.get("account_B").getAsString();
        if(jsonObject.get("code").getAsString().equals("203")){
            for (int i = 0; i < requestInfos.size(); i++) {
                RequestInfo requestInfo=requestInfos.get(i);
                if(requestInfo.to.equals(account)){
                    requestInfo.type=-1;
                }
            }
            return false;
        }
        for (int i = 0; i < requestInfos.size(); i++) {
            RequestInfo requestInfo=requestInfos.get(i);
            if(requestInfo.to.equals(account)){
                requestInfo.type=1;
            }
        }
        UserInfo userInfo=getUserInfo(account);
        user.getFriends().add(userInfo);
        return true;
    }
    /**
     * 接收好友申请
     * @param
     */
    public UserInfo receiveAddFriendRequest(String json){
        System.out.println("receiveAddFriendRequest:"+json);
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
        if(jsonObject.get("status").getAsInt()==9000) return null;
        JsonObject data=jsonObject.get("data").getAsJsonObject();
        String account_A=data.get("account_A").getAsString();
        String account_B=data.get("account_B").getAsString();
        ArrayList<RequestInfo> requestInfos=user.getRequests();
        UserInfo userInfo=getUserInfo(account_A);
        requestInfos.add(new RequestInfo(account_A,account_B,userInfo.name,userInfo.avatarPath,0));
        return userInfo;
    }
    /**
     * 反馈好友申请
     * @param
     * @param choose
     */
    public void receiveAddFriendFeedback(String toUser,boolean choose){
        System.out.println("receiveAddFriendFeedback:"+toUser);
        JsonObject jsonObject = new JsonObject();
        ArrayList<RequestInfo> requestInfos=user.getRequests();
        if(choose){
            jsonObject.addProperty("code","202");
            for (int i = 0; i < requestInfos.size(); i++) {
                RequestInfo requestInfo=requestInfos.get(i);
                if(requestInfo.from.equals(toUser)){
                    requestInfo.type=-1;
                }
            }
        } else {
            jsonObject.addProperty("code","203");
            for (int i = 0; i < requestInfos.size(); i++) {
                RequestInfo requestInfo=requestInfos.get(i);
                if(requestInfo.from.equals(toUser)){
                    requestInfo.type=1;
                }
            }
        }
        JsonObject data=new JsonObject();
        data.addProperty("account_A",toUser);
        data.addProperty("account_B",user.getAccount());
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
     * @param
     */
    public void handleMessage(Message message){
        JsonParser jsonParser = new JsonParser();
        String json=message.getContent();
        System.out.println("====="+json);
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
        String  code=jsonObject.get("code").getAsString();
        switch (code){
            case "201"://这里是别人的好友申请
                receiveAddFriendRequest(json);
                break;
            case "202"://这里是我给别人发，然后别人给我的回馈
            case "203":
                addFriendFeedback(json);
                break;
            case "501"://收消息
                receiveText(json);
                break;
        }
    }
    /**
     * 接收所有的消息，后续再根据消息类型作不同处理
     * @return
     */
    public Message receiveMessage(){
        try {
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
        }
        return null;
    }
    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }
    public void run() {
        new Thread(()->{
            synchronized (lock) {
                while (isLogin) {
                    try {
                        while (isPaused) {
                            lock.wait(); // 当 isPaused 为 true 时，线程进入等待状态
                        }
                        Message message = receiveMessage();
                        if(message!=null)
                            handleMessage(message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
//        while(isLive){
//
//        }
    }
    private void pauseThread() {
        synchronized (lock) {
            isPaused = true; // 设置为暂停状态
            // 其他暂停前的操作可以放在这里
        }
    }
    private void resumeThread() {
        synchronized (lock) {
            isPaused = false; // 设置为运行状态
            lock.notify(); // 唤醒等待中的线程
        }
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public User getUser() {
        return user;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setLive(boolean live) {
        isLive = live;
    }
}
