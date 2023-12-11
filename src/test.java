import Client.Client;
import Message.Message;
import MyException.ConnectException;
import MyUtil.MyUtil;
import com.google.gson.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author 西西弗
 * @Description:
 * @date 2023/12/10 8:57
 */
public class test {
    public static void main(String[] args) {
        JsonObject jsonObject = new JsonObject();
        JsonParser jsonParser = new JsonParser();
        jsonObject.addProperty("code",101);
        jsonObject.addProperty("status",9000);
        JsonObject data = new JsonObject();
        JsonArray friends=new JsonArray();
        data.addProperty("name","ccf");
        data.addProperty("avatarPath","12331123");
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        HashMap<String, String> map1 = new HashMap<>();
        map1.put("name","ccf");
        map1.put("account","110235");
        map1.put("avatar","dasasddas");
        ArrayList<HashMap<String,String>> messages=new ArrayList<>();
        HashMap<String, String> message1 = new HashMap<>();
        message1.put("sender","yf");
        message1.put("content","你说失败");
        message1.put("sendTime","2023.12");
        HashMap<String, String> message2 = new HashMap<>();
        message2.put("sender","ccf");
        message2.put("content","你说成功");
        message2.put("sendTime","2023.10");
        messages.add(message1);
        messages.add(message2);
        JsonElement messageElement=jsonParser.parse(new Gson().toJson(messages));

        HashMap<String, String> map2 = new HashMap<>();
        map2.put("name","yg");
        map2.put("account","asdda213");
        map2.put("avatar","das12312132asddas");
        list.add(map1);
        list.add(map2);
        JsonElement je=jsonParser.parse(new Gson().toJson(list));
        data.add("friend",je);





        jsonObject.add("data",data);
        System.out.println(new Gson().toJson(jsonObject));

    }
    public static void loginSend(String json) {
        JsonObject jj1 = new Gson().fromJson(json,JsonObject.class);
        //TODO
        JsonObject data=jj1.get("data").getAsJsonObject();
        String name=data.get("name").getAsString();
        String avatar=data.get("avatarPath").getAsString();
        System.out.println("myname:"+name+" myavatar:"+avatar);
        System.out.println("===============");
        JsonArray friendsJson=data.get("friends").getAsJsonArray();
        setFriends(friendsJson);
    }
    public static void setFriends(JsonArray jsonArray){
        for(JsonElement friend:jsonArray){
            JsonObject friendObject = friend.getAsJsonObject();
            String name = friendObject.get("name").getAsString();
            String account = friendObject.get("account").getAsString();
            String avatar =friendObject.get("avatarPath").getAsString();
            System.out.println("name:"+name+" account:"+account+" avatar:"+avatar);
            JsonArray messages=friendObject.get("messages").getAsJsonArray();
            setMessages(account,messages);
            System.out.println("============");
        }
    }
    public static void setMessages(String account,JsonArray jsonArray){

        for (JsonElement messageElement : jsonArray) {
            // 在这里处理每个 "messages" 数组元素
            JsonObject messageObject = messageElement.getAsJsonObject();
            String sendTime = messageObject.get("sendTime").getAsString();
            String content = messageObject.get("content").getAsString();
            String sender = messageObject.get("sender").getAsString();
            System.out.println("sender:"+sender+" content:"+content+" sendTime:"+sendTime);
        }

    }
}
