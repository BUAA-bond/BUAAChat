import Client.Client;
import MyUtil.MyUtil;
import MyUtil.RegisterInfo;
import UI.ChatAppClient;
import UI.LoginClient;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import static Constant.Constant.client;
public class Main {
    public static void main(String[] args) {
        // 在 JavaFX 主线程中启动应用程序
        javafx.application.Platform.startup(() -> {
            LoginClient loginClient = new LoginClient();
            ChatAppClient chatAppClient=new ChatAppClient();
            RegisterInfo registerInfo = new RegisterInfo();
            client = new Client();
            // 启动 JavaFX 应用程序
            loginClient.start(new Stage());
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
                        //创建客户端线程
                        client.setLogin(true);
                        client.setLive(true);
                        client.start();
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
                registerInfo.setAccount(registerAccount);
                registerInfo.setName(registerName);
                registerInfo.setPassword(registerPassword);
                registerInfo.setPasswordAgain(registerAgainPassword);
                registerInfo.setAvatarPath(registerAvatarURL);
                String info=registerInfo.judgeAndRegister();
                if(!info.equals("正确")){
                    loginClient.throwError(info);
                }else if(!client.register(registerAccount,registerName,registerPassword,registerAvatarURL)){
                    loginClient.throwError("用户已存在");
                }else{
                    loginClient.changeRegisterSuccess();
                }
            });
        });
    }
}