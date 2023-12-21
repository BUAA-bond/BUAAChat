package com.BUAAChat.Client;

/**
 * @author 西西弗
 * @Description: 客户端类，用于处理各种交互
 * @date 2023/11/17 10:05
 */

import com.BUAAChat.Info.ChatInfo;
import com.BUAAChat.Info.GroupInfo;
import com.BUAAChat.Info.RequestInfo;
import com.BUAAChat.Info.UserInfo;
import com.BUAAChat.Message;
import com.BUAAChat.MyUtil.MyUtil;
import com.google.gson.*;
import javafx.application.Platform;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import static com.BUAAChat.Constant.Constant.*;

public class Client implements Runnable {
    /**
     *用于建立连接的socket
     */
    private Socket socket;
    private Sender sender;
    /**
     *输入流，从socket获取
     */
    private InputStream is=null;

    /**
     *对象输入流，用于读取Message
     */
    private ObjectInputStream ois=null;

    /**
     *记录用户是否处于登录状态
     */
    private boolean isLogin =false;

    /**
     *与client绑定的当前用户
     */
    private User user=new User();

    /**
     *每个操作规定的统一等待时间
     */
    private static int sleepTime=200;
    public Client() {
    }

    /**
     *与服务器建立连接
     */
    public void connect(){
        try {
            this.socket=new Socket(host,port);
            //有顺序要求，不然会堵塞，先os再is
            sender=new Sender(socket,user);
            is=socket.getInputStream();
            ois=new ObjectInputStream(is);
        } catch (IOException e) {
            System.err.println("连接被拒绝: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 登录成功后初始化用户
     * @param account
     * @param password
     */
    public void userInit(String account,String password){
        //获取用户的个人信息
        UserInfo userInfo = getUserInfo(account);
        user.setName(userInfo.name);
        user.setAvatarPath(userInfo.avatarPath);
        user.setAccount(account);
        user.setPassword(password);
        System.out.println("userInit");
        System.out.println(userInfo.name);
        System.out.println(userInfo.account);
        //发送获取 好友信息 的请求，包含聊天记录
        sender.getAllFriendsInfoRequest(account);
        sleep(sleepTime);
        //发送获取 群信息 的请求，包含聊天记录
        sender.getAllGroupsInfoRequest(account);
        sleep(sleepTime);
        //发送获取 与加好友申请有关 的请求
        sender.getAllRequestInfoRequest(account);
        sleep(sleepTime);
    }


    /**
     * 获取指定用户的信息
     * @param account
     * @return {@link UserInfo}
     */
    UserInfo tmpGetUserInfo=null;
    public UserInfo getUserInfo(String account){
        sender.getInfoRequest(account,"401");
        System.out.println("getUserInfo:"+account);
        sleep(sleepTime);
        UserInfo userInfo=tmpGetUserInfo;
        return userInfo;
    }

    /**
     * 搜索用户
     * @param account
     * @return {@link ArrayList}<{@link UserInfo}>
     */
    public ArrayList<UserInfo> searchUser(String account){
        ArrayList<UserInfo> tmp=new ArrayList<>();
        tmp.add(getUserInfo(account));
        return tmp;
    }
    /**
     * 获取指定群的信息
     * @param account
     * @return {@link GroupInfo}
     */
    public GroupInfo getGroupInfo(String account){
        sender.getGroupInfoRequest(account);
        GroupInfo groupInfo=getGroupInfoFeedback();
        return groupInfo;
    }



    /**
     * 获取某个群聊信息的反馈
     * @return {@link GroupInfo}
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
        //将每个member里的信息转化为UserInfo，放到groupInfo中
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
     * @param json
     * @return {@link UserInfo}
     */
    public UserInfo getUserInfoFeedback(String json){
        System.out.println("getUserInfoFeedback"+json);
        JsonObject jsonObject=new Gson().fromJson(json, JsonObject.class);
        if (jsonObject.get("status").getAsInt() != 9000) return null;
        JsonObject data = jsonObject.get("data").getAsJsonObject();
        String name = data.get("name").getAsString();
        String account = data.get("account").getAsString();
        String avatar = data.get("avatar").getAsString();
        UserInfo userInfo = new UserInfo(account,name, avatar);
        tmpGetUserInfo=userInfo;
        return userInfo;
    }

    /**
     * 获取用户信息的反馈，重载
     * @return {@link UserInfo}
     */
    public UserInfo getUserInfoFeedback(){
        JsonObject jsonObject = getJsonObjectFromMsg("401");
        if (jsonObject.get("status").getAsInt() != 9000) return null;
        JsonObject data = jsonObject.get("data").getAsJsonObject();
        String avatar = data.get("avatar").getAsString();
        String account = data.get("account").getAsString();
        String name = data.get("name").getAsString();
        UserInfo userInfo = new UserInfo(account,name, avatar);
        return userInfo;
    }
    /**
     * 获取 好友请求信息 反馈
     * @param json
     * @return {@link ArrayList}<{@link RequestInfo}>
     */
    public ArrayList<RequestInfo> getAllRequestInfoFeedback(String json){
        System.out.println("getAllRequests:");
        System.out.println(json);
        JsonObject jsonObject=new Gson().fromJson(json,JsonObject.class);
        if(jsonObject.get("status").getAsInt()!=9000) return null;
        JsonObject data=jsonObject.get("data").getAsJsonObject();
        JsonArray requests=data.get("history").getAsJsonArray();
        ArrayList<RequestInfo> tmp=new ArrayList<>();
        //将requests里的信息转化为RequestInfo
        if(requests!=null)
            for(JsonElement requestElement: requests){
                RequestInfo requestInfo = new RequestInfo();
                JsonObject requestObject=(JsonObject) requestElement;
                String account_A=requestObject.get("sender").getAsString();
                String account_B=requestObject.get("recipient").getAsString();
                String type=requestObject.get("status").getAsString();
                //获取对话双方中，另一个人的信息
                if(account_A.equals(user.getAccount())){
                    sender.getInfoRequest(account_B,"401");
                }else{
                    sender.getInfoRequest(account_A,"401");
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
            }
        return tmp;
    }

    /**
     * 获取当前用户 所有朋友信息 的反馈
     * @param json
     * @param sign
     * @return {@link ArrayList}<{@link UserInfo}>
     */
    public ArrayList<UserInfo> getAllFriendsInfoFeedback(String json,int sign){
        System.out.println("getAllFriends:");
        System.out.println(json);
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
     * 获取当前用户 所有群信息 的反馈
     * @param json
     * @param sign
     * @return {@link ArrayList}<{@link GroupInfo}>
     */
    public ArrayList<GroupInfo> getAllGroupsInfoFeedback(String json,int sign){
        System.out.println("getAllGroups:");
        System.out.println(json);
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
        sender.registerSend(account, name, password, avatarPath);
        return registerReceive();
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
     * @return boolean
     */
    public boolean login(String account, String password) {
        sender.loginSend(account, password);
        if (!loginReceive(account)) {
            System.out.println("登录失败");
            return false;
        } else {
            System.out.println("登录成功");
            return true;
        }
    }

    /**
     * 接收登录请求 反馈
     * @param account
     * @return boolean
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
     * @param jsonArray
     * @param sign
     * @return {@link ArrayList}<{@link UserInfo}>
     */
    public ArrayList<UserInfo> setFriends(JsonArray jsonArray,int sign){
        ArrayList<UserInfo> tmp=new ArrayList<>();
        if(jsonArray!=null)
            for(JsonElement friend:jsonArray){
                if(friend==null || friend.isJsonNull()) continue;
                //解析每个数组元素
                JsonObject friendObject = friend.getAsJsonObject();
                String name = friendObject.get("name").getAsString();
                String account = friendObject.get("account").getAsString();
                String avatar =friendObject.get("avatar").getAsString();
                //转化为UserInfo
                UserInfo userInfo=new UserInfo(account,name,avatar);
                tmp.add(userInfo);
                //初始化 聊天记录
                if(sign==0) {
                    JsonArray messages = friendObject.get("messages").getAsJsonArray();
                    setMessages(userInfo, messages);
                }
            }
        return tmp;
    }
    /**
     * 初始化群
     * @param jsonArray
     * @param sign
     * @return {@link ArrayList}<{@link GroupInfo}>
     */
    public ArrayList<GroupInfo> setGroups(JsonArray jsonArray,int sign){
        ArrayList<GroupInfo> tmp=new ArrayList<>();
        if(jsonArray!=null)
            for(JsonElement group:jsonArray){
                if(group==null || group.isJsonNull()) continue;
                //解析，获取每个群的name account avatar
                JsonObject groupObject = group.getAsJsonObject();
                String groupName=groupObject.get("name").getAsString();
                String groupAccount=groupObject.get("account").getAsString();
                String groupAvatar=groupObject.get("avatar").getAsString();
                GroupInfo groupInfo = new GroupInfo(groupAccount,groupName, groupAvatar);
                //解析群成员的信息
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
                //加入
                tmp.add(groupInfo);
                //初始化聊天记录
                if(sign==0){
                    JsonArray messages=groupObject.get("messages").getAsJsonArray();
                    setMessages(groupInfo,messages);
                }
            }
        return tmp;
    }

    /**
     * 保存与朋友的消息
     * @param userInfo
     * @param jsonArray
     */
    public void setMessages(UserInfo userInfo,JsonArray jsonArray){
        ArrayList<ChatInfo> chats=new ArrayList<>();
        for (JsonElement messageElement : jsonArray) {
            if(messageElement==null && messageElement.isJsonNull()) continue;
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
    /**
     * 保存与群的消息
     * @param groupInfo
     * @param jsonArray
     */
    public void setMessages(GroupInfo groupInfo,JsonArray jsonArray){
        ArrayList<ChatInfo> chats=new ArrayList<>();
        ArrayList<UserInfo> members=groupInfo.members;
        for (JsonElement messageElement : jsonArray) {
            if(messageElement==null || messageElement.isJsonNull())continue;
            // 在这里处理每个 "messages" 数组元素
            JsonObject messageObject = messageElement.getAsJsonObject();
            String content = messageObject.get("content").getAsString();
            String sendTime = messageObject.get("sendTime").getAsString();
            String sender = messageObject.get("sender").getAsString();
            UserInfo userInfo=null;
            //找到这个发送者
            for (int i = 0; i < members.size(); i++) {
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
     * @return {@link JsonObject}
     */
    public JsonObject getJsonObjectFromMsg(String code){
        JsonObject jsonObject=null;
        try {
            while(true){
                Message tmpMsg=(Message)ois.readObject();
                String json=tmpMsg.getContent();
                jsonObject=new Gson().fromJson(json, JsonObject.class);
                //如果不是需要的消息类型，那就开启一个线程去处理
                if(!jsonObject.get("code").getAsString().equals(code)){
                    new Thread(()->{
                        handleMessage(tmpMsg);
                    }).start();
                } else break;
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
     * @return boolean
     */
    boolean tmpChangePassword=false;
    public boolean changePassword(String password){
        //发送请求
        sender.changePasswordRequest(password);
        sleep(sleepTime);
        boolean sign=tmpChangePassword;
        tmpChangePassword=false;
        if(sign) user.setPassword(password);
        return sign;
    }

    /**
     * 修改密码 反馈
     * @param json
     * @return boolean
     */
    public boolean changePasswordFeedback(String json){
        JsonObject jsonObject=new Gson().fromJson(json, JsonObject.class);
        if(jsonObject.get("status").getAsInt()==9000){
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
    boolean tmpLogout=false;
    public boolean logout(){
        //发送登出请求
        sender.logoutRequest();
        sleep(sleepTime);
        boolean sign=tmpLogout;
        tmpLogout=false;
        return sign;
    }

    /**
     * 登出 反馈
     * @param json
     * @return boolean
     */
    public boolean logoutFeedback(String json){
        System.out.println("logoutFeedback:"+json);
        JsonObject jsonObject=new Gson().fromJson(json, JsonObject.class);
        if(jsonObject.get("status").getAsInt()==9000){
            isLogin=false;
            if (!isLogin) {
                try {
                    if(sender !=null) sender.close();
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
     * 删除好友
     * @param account
     * @return boolean
     */
    boolean tmpRemoveFriend=false;
    public boolean removeFriend(String account){
        sender.removeFriendRequest(account);
        sleep(sleepTime);
        boolean sign=tmpRemoveFriend;
        if(sign){
            //找到那个好友，把他从列表删掉
            ArrayList<UserInfo> friends=user.getFriends();
            for (int i = 0; i < friends.size(); i++) {
                UserInfo userInfo=friends.get(i);
                if(userInfo.account.equals(account)){
                    friends.remove(userInfo);
                    break;
                }
            }
            //删掉与好友的聊天信息
            HashMap<String ,ArrayList<ChatInfo>> messages=user.getMessagesF();
            messages.remove(account);
        }
        return sign;
    }

    /**
     * 删除好友 反馈
     * @param json
     * @return boolean
     */
    public boolean removeFriendFeedback(String json){
        System.out.println("removeFriendFeedback:"+json);
        JsonObject jsonObject=new Gson().fromJson(json, JsonObject.class);
        if(jsonObject.get("status").getAsInt()==9000){
            tmpRemoveFriend=true;
            System.out.println("删除成功");
            return true;
        }
        System.out.println("删除失败");
        tmpRemoveFriend=false;
        return false;
    }

    /**
     * @param gAccount
     * @param name
     * @param avatar
     * @param members
     * @return boolean
     */
    boolean tmpBuildGroup=false;
    public boolean buildGroup(String gAccount,String name,String avatar,ArrayList<UserInfo> members){
        System.out.println("buildGroup:"+gAccount+" name:"+name);
        //发送请求
        sender.buildGroupRequest(gAccount,name,avatar);
        sleep(sleepTime);
        boolean sign=tmpBuildGroup;
        if(sign){
            //建群
            GroupInfo groupInfo = new GroupInfo(gAccount, name, avatar);
            //先把自己加入群members
            groupInfo.members.add(new UserInfo(user.getAccount(),user.getName(),user.getAvatarPath()));
            user.getGroups().add(groupInfo);
            user.getMessagesG().put(gAccount,new ArrayList<ChatInfo>());
            //将所有成员加进去
            for (int i = 0; i < members.size(); i++) {
                UserInfo userInfo=members.get(i);
                joinGroup(userInfo,gAccount);
            }
        }
        tmpBuildGroup=false;
        updateGroupList();
        return sign;
    }


    /**
     * 建群 反馈
     * @param json
     * @return boolean
     */
    public boolean buildGroupFeedback(String json){
        JsonObject jsonObject=new Gson().fromJson(json, JsonObject.class);
        if(jsonObject.get("status").getAsInt()==9000){
            tmpBuildGroup=true;
            return true;
        }
        tmpBuildGroup=false;
        return false;
    }

    /**
     * 加入群聊
     * @param userInfo
     * @param groupAccount
     * @return boolean
     */
    boolean tmpJoinGroup=false;
    public boolean joinGroup(UserInfo userInfo,String groupAccount){
        System.out.println(userInfo.account+" joinGroup:"+groupAccount);
        sender.joinGroupRequest(userInfo.account,groupAccount);
        sleep(sleepTime);
        boolean sign=tmpJoinGroup;
        if(sign){
            ArrayList<GroupInfo> groups=user.getGroups();
            GroupInfo groupInfo=null;
            //找到指定群聊
            for (int i = groups.size()-1; i >= 0; i--) {
                groupInfo=groups.get(i);
                if(groupInfo.account.equals(groupAccount)) break;
            }
            groupInfo.members.add(userInfo);
        }
        tmpJoinGroup=false;
        return sign;
    }

    /**
     * 加入群聊 反馈
     * @param json
     * @return boolean
     */
    public boolean joinGroupFeedback(String json){
        System.out.println("joinGroupFeedback"+json);
        JsonObject jsonObject=new Gson().fromJson(json, JsonObject.class);
        if(jsonObject.get("status").getAsInt()!=9000){
            tmpJoinGroup=false;
            return false;
        }
        tmpJoinGroup=true;
        return true;
    }

    /**
     * 修改用户名
     * @param name
     */
    boolean tmpModifyUserName=false;
    public boolean modifyUserName(String name){
        //发送请求
        sender.modifyUserNameRequest(name);
        sleep(sleepTime);
        boolean sign=tmpModifyUserName;
        if(sign) user.setName(name);
        tmpModifyUserName=false;
        return sign;
    }

    /**
     * 修改名字 反馈
     * @param json
     * @return boolean
     */
    public boolean modifyUserNameFeedback(String json){
        System.out.println("modifyUserNameFeedback:"+json);
        JsonObject jsonObject=new Gson().fromJson(json, JsonObject.class);
        int status=jsonObject.get("status").getAsInt();
        if(status!=9000){ tmpModifyUserName=false;System.out.println("修改名字失败");return false;}
        tmpModifyUserName=true;
        System.out.println("修改名字成功");
        return true;
    }

    /**
     * 修改头像
     * @return
     */
    boolean tmpModifyUserAvatar=false;
    public boolean modifyUserAvatar(String avatarPath){
        //发送请求
        sender.modifyUserAvatarRequest(avatarPath);
        sleep(sleepTime);
        boolean sign=tmpModifyUserAvatar;
        if(sign){
            user.setAvatarPath(avatarPath);
        }
        tmpModifyUserAvatar=false;
        return sign;
    }

    /**
     * 修改头像 反馈
     * @param json
     * @return boolean
     */
    public boolean modifyUserAvatarFeedback(String json){
        System.out.println("modifyUserAvatarFeedback:"+json);
        JsonObject jsonObject=new Gson().fromJson(json, JsonObject.class);
        if(jsonObject.get("status").getAsInt()!=9000){
            tmpModifyUserAvatar=false;
            System.out.println("修改头像失败");
            return false;
        }else{
            tmpModifyUserAvatar=true;
            System.out.println("修改头像成功");
            return true;
        }
    }


    /**
     * 接收 消息
     * @param json
     */
    public void receiveText(String json){
        System.out.println("receiveText:"+json);
        JsonObject jsonObject=new Gson().fromJson(json,JsonObject.class);
        if(jsonObject.get("status").getAsInt()==9000){
            //服务器返回的 发送信息成功与否反馈 不是好友消息
            return;
        }else{
            //服务器返回的 发送信息成功与否反馈 不是好友消息
            JsonObject data=jsonObject.get("data").getAsJsonObject();
            if(data.get("from").getAsString().equals(user.getAccount())){
                return;
            }
        }
        JsonObject data=jsonObject.get("data").getAsJsonObject();
        String content=data.get("content").getAsString();
        String from=data.get("from").getAsString();
        String to=data.get("to").getAsString();
        HashMap<String, ArrayList<ChatInfo>> map=null;
        if(MyUtil.judgeAccount(to)){
            //如果是给单个人发的
            map=user.getMessagesF();
            ArrayList<UserInfo> tmpFriends=user.getFriends();
            UserInfo userInfo=null;
            for (int i = 0; i < tmpFriends.size(); i++) {
                userInfo=tmpFriends.get(i);
                if(userInfo.account.equals(from)) break;
            }
            if(userInfo==null) return;
            if(map.containsKey(from)){
                //将信息记录
                ArrayList<ChatInfo> msgs=map.get(from);
                ChatInfo chatInfo=new ChatInfo(userInfo,content);
                msgs.add(chatInfo);
                updateChat(chatInfo);
            }else{
                ArrayList<ChatInfo> msgs=new ArrayList<>();
                ChatInfo chatInfo=new ChatInfo(userInfo,content);
                msgs.add(chatInfo);
                map.put(from,msgs);
                updateChat(chatInfo);
            }
        }else{
            //to是群聊号，from是群里那个发这条信息的人
            map=user.getMessagesG();
            ArrayList<GroupInfo> tmpGroups=user.getGroups();
            GroupInfo groupInfo=null;
            //找到这个群
            for (int i = 0; i < tmpGroups.size(); i++) {
                groupInfo=tmpGroups.get(i);
                if(groupInfo.account.equals(to)) break;
            }
            if(groupInfo==null) return;
            //找到这个发信息的人
            ArrayList<UserInfo> tmpMembers=groupInfo.members;
            UserInfo userInfo=null;
            for (int i = 0; i < tmpMembers.size(); i++) {
                userInfo=tmpMembers.get(i);
                if(userInfo.account.equals(from)) break;
            }
            if(!map.containsKey(to)){
                //将信息记录
                ArrayList<ChatInfo> msgs=new ArrayList<>();
                ChatInfo chatInfo=new ChatInfo(userInfo,content);
                msgs.add(chatInfo);
                map.put(to,msgs);
                updateGroupChat(chatInfo,to);
            }else{
                ArrayList<ChatInfo> msgs=map.get(to);
                ChatInfo chatInfo=new ChatInfo(userInfo,content);
                msgs.add(chatInfo);
                updateGroupChat(chatInfo,to);
            }
        }

    }



    /**
     * 处理好友申请的反馈，通过的话就将好友加入列表
     * @param json
     * @return boolean
     */
    public boolean addFriendFeedback(String json){
        System.out.println("addFriendFeedback:"+json);
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
        if(jsonObject.get("status").getAsInt()==9000){
            //排除系统返回的成功与否的消息
            JsonObject data=jsonObject.get("data").getAsJsonObject();
            if(data.has("msg"))
                return false;
        }
        ArrayList<RequestInfo> requestInfos=user.getRequests();
        JsonObject data=jsonObject.get("data").getAsJsonObject();
        String account=data.get("account_B").getAsString();
        if(jsonObject.get("code").getAsString().equals("203")){
            //拒绝
            for (int i = 0; i < requestInfos.size(); i++) {
                RequestInfo requestInfo=requestInfos.get(i);
                if(requestInfo.to.equals(account)){
                    requestInfo.type=-1;
                }
            }
            return false;
        }
        for (int i = 0; i < requestInfos.size(); i++) {
            //同意
            RequestInfo requestInfo=requestInfos.get(i);
            if(requestInfo.to.equals(account)){
                requestInfo.type=1;
            }
        }
        UserInfo userInfo=getUserInfo(account);
        System.out.println("加的好友:"+userInfo.account);
        user.getFriends().add(userInfo);
        //在界面上更新好友列表
        updateFriendList();
        return true;
    }

    /**
     * 接收好友申请
     * @param json
     * @return {@link UserInfo}
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
        //更新加好友申请列表
        updateFriendsRequest();
        return userInfo;
    }


    /**
     * 处理系统返回的信息，好友申请、好友申请反馈......
     * @param message
     */
    public void handleMessage(Message message){
        JsonParser jsonParser = new JsonParser();
        String json=message.getContent();
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
        String  code=jsonObject.get("code").getAsString();
        switch (code){
            case "103"://修改密码
                changePasswordFeedback(json);
                break;
            case "109"://这里是登出
                logoutFeedback(json);
                break;
            case "201"://这里是别人的好友申请
                receiveAddFriendRequest(json);
                break;
            case "202"://这里是我给别人发，然后别人给我的回馈
            case "203":
                addFriendFeedback(json);
                break;
            case "204"://删除好友
                removeFriendFeedback(json);
                break;
            case "209"://建群
                buildGroupFeedback(json);
                break;
            case "210"://加入群
                joinGroupFeedback(json);
                break;
            case "301"://修改名字
                modifyUserNameFeedback(json);
                break;
            case "302"://修改头像
                modifyUserAvatarFeedback(json);
                break;
            case "401"://获取用户信息
                getUserInfoFeedback(json);
                break;
            case "402"://初始化朋友
                user.setFriends(getAllFriendsInfoFeedback(json,0));
                break;
            case "404"://更新群
                user.setGroups(getAllGroupsInfoFeedback(json,0));
                updateGroupList();
                break;
            case "405"://更新加好友申请
                user.setRequests(getAllRequestInfoFeedback(json));
                break;
            case "501"://收消息
                receiveText(json);
                break;
        }
    }

    /**
     * 接收所有的消息，后续再根据消息类型作不同处理
     * @return {@link Message}
     */
    public Message receiveMessage(){
        try {
            Message msg=null;
            if(!socket.isClosed())
                msg = (Message) ois.readObject();
            return msg;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     *开启线程
     */
    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }

    /**
     *开启线程，不停地读取信息
     */
    public void run() {
        new Thread(()->{
            while (isLogin) {
                    Message message = receiveMessage();
                    if(message!=null){
                        //如果读到了信息，就开新线程去处理
                        new Thread(()->{
                            handleMessage(message);
                        }).start();
                    }
                    //休息一下吧~
                    sleep(100);
                }
        }).start();
    }

    /**
     * 休眠，，一般用于发送请求过后，等待反馈信息
     * @param time
     */
    public void sleep(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新当前对象的聊天记录界面
     * @param chatInfo
     */
    public static void updateChat(ChatInfo chatInfo){
        System.out.println("update");
        Platform.runLater(() -> {
            chatAppClient.updateChat(chatInfo);
        });
    }
    /**
     * 更新好友请求界面
     */
    public static void updateFriendsRequest(){
        Platform.runLater(() -> {
            chatAppClient.updateNewFriend();
        });
    }

    /**
     * 更新群聊天界面
     * @param chatInfo
     * @param to
     */
    public static void updateGroupChat(ChatInfo chatInfo,String to){
        Platform.runLater(() -> {
            chatAppClient.updateGroupChat(chatInfo,to);
        });
    }
    /**
     * 更新好友列表界面
     */
    public static void updateFriendList(){
        Platform.runLater(() -> {
            chatAppClient.updateFriendList();
        });
    }
    /**
     * 更新群聊界面
     */
    public void updateGroupList(){
        Platform.runLater(() -> {
            chatAppClient.updateGroupList();
        });
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

    public Sender getSender() {
        return sender;
    }
}
