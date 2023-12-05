import Client.Client;
import Message.LoginInfo;
import MyUtil.MyUtil;
import Message.RegisterInfo;
import UI.ChatAppClient;
import UI.LoginClient;
import javafx.stage.Stage;

public class Main {
    public static void main(String[] args) {
        // 在 JavaFX 主线程中启动应用程序
        javafx.application.Platform.startup(() -> {
            LoginClient loginClient = new LoginClient();
            ChatAppClient chatAppClient=new ChatAppClient();
            Client client = new Client();
            // 启动 JavaFX 应用程序
            loginClient.start(new Stage());
            //创建客户端线程
            client.start();
            //在数据库里创建一个users表
            MyUtil.createUsersTable();
            // 设置回调函数
            loginClient.setButtonClickListener(message -> {
                // 处理按钮点击后返回的消息
                String loginAccount = message[0];    //account
                String loginPassword = message[1];   //password
                if(client.login(loginAccount,loginPassword)){
                    loginClient.close();
                    try {
                        chatAppClient.start(new Stage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            loginClient.registerSetButtonClickListener(messages -> {
                String registerAccount = messages[0];
                String registerName = messages[1];
                String registerPassword = messages[2];
                String registerAgainPassword = messages[3];
                String registerAvatarURL = messages[4];
                if(!client.register(registerAccount,registerName,registerPassword,registerAvatarURL)){
                    loginClient.throwError("错了喵");
                }
                else{
                    loginClient.changeRegisterSuccess();
                }
            });
        });
    }
}