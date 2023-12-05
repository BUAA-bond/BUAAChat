
package Client;

import Message.SysMsg;
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
import java.net.InetAddress;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.HashMap;

public class Client implements Runnable {
    private Socket socket;
    private InputStream is;
    private OutputStream os;
    private boolean isLive = true;
    private User user;

    public Client() {
        try {
            //this.socket = new Socket("182.92.202.183", 10000);
            this.socket=new Socket("localhost",9999);
        } catch (IOException e) {
            System.err.println("连接被拒绝: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean register(String account, String name, String password, String avatarPath) {
        registerSend(account, name, password, avatarPath);
        return registerReceive();
    }

    public void registerSend(String account, String name, String password, String avatarPath) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("account", account);
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("password", password);
        jsonObject.addProperty("avatarPath", avatarPath);
        Gson gson = new Gson();
        String content = gson.toJson(jsonObject);
        SysMsg sysMsg = new SysMsg(account, 2, content);
        ObjectOutputStream oos = null;
        try {
            if(socket.isClosed()) socket=new Socket("localhost",9999);
            os = socket.getOutputStream();
            oos = new ObjectOutputStream(os);
            oos.writeObject(sysMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean registerReceive() {
        SysMsg msg = null;
        ObjectInputStream ois = null;
        try {
            if(socket.isClosed()){
                System.out.println("连接中断");
                return false;
            }
            is = socket.getInputStream();
            ois = new ObjectInputStream(is);
            msg = (SysMsg)ois.readObject();
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = jsonParser.parse(msg.getContent()).getAsJsonObject();
            if (jsonObject.get("code").getAsInt() != 1) return false;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if(ois!=null)
                ois.close();
                if(!socket.isClosed()) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

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

    public void loginSend(String account, String password) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("account", account);
        jsonObject.addProperty("password", password);
        Gson gson = new Gson();
        String content = gson.toJson(jsonObject);
        ObjectOutputStream oos = null;
        SysMsg msg = new SysMsg(account, 1, content);
        try {
            if(socket.isClosed()) socket=new Socket("localhost",9999);
            oos = new ObjectOutputStream(this.socket.getOutputStream());
            oos.writeObject(msg);
            oos.flush();
            //TODO
            //这里的oos没有close，因为oos.close，socket也close了，暂时没想到好的解决方法
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User loginReceive() {
        SysMsg msg = null;
        ObjectInputStream ois = null;
        try {
            if(socket.isClosed()){
                System.out.println("连接中断");
                return null;
            }
            is = socket.getInputStream();
            ois = new ObjectInputStream(is);
            msg = (SysMsg)ois.readObject();
            String json = msg.getContent();
            JsonParser jsonParser = new JsonParser();
            JsonObject jj = jsonParser.parse(json).getAsJsonObject();
            if (jj.get("code").getAsInt() != 1) return null;
            User user1 = new User();
            user1.setName(jj.get("name").getAsString());
            user1.setAvatarPath(jj.get("avatarPath").getAsString());
            JsonElement friendElement = jj.getAsJsonObject("friends");
            JsonElement groupElement = jj.getAsJsonObject("groups");
            HashMap<String, UserInfo> friendsMap = new Gson().fromJson(friendElement, new TypeToken<HashMap<String, UserInfo>>(){}.getType());
            user1.setFriends(friendsMap);
            HashMap<String, GroupInfo> groupsMap = new Gson().fromJson(groupElement, new TypeToken<HashMap<String, GroupInfo>>(){}.getType());
            user1.setGroups(groupsMap);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if(!socket.isClosed()) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }
    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }
    public void run() {
        while(isLive) {

        }
    }
}
