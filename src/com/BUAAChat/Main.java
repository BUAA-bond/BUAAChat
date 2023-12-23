package com.BUAAChat;

/**
 * @author 西西弗
 * @Description: 程序主入口
 * @date 2023/11/9 19:24
 */

import com.BUAAChat.Client.Client;
import com.BUAAChat.MyUtil.MyUtil;
import com.BUAAChat.Info.RegisterInfo;
import com.BUAAChat.UI.ChatAppClient;
import com.BUAAChat.UI.LoginClient;
import javafx.stage.Stage;

import java.io.IOException;

import static com.BUAAChat.Constant.Constant.chatAppClient;
public class Main {
    public static void main(String[] args) {
        // 在 JavaFX 主线程中启动应用程序
        new Main().start();
    }

    /**
     *启动
     */
    public void start(){
        javafx.application.Platform.startup(() -> {
            LoginClient loginClient = new LoginClient();
            chatAppClient=new ChatAppClient();
            RegisterInfo registerInfo = new RegisterInfo();
            //单例
            Client client = Client.getClient();
            // 启动 JavaFX 应用程序
            loginClient.start(new Stage());
            // 设置回调函数
            loginClient.setButtonClickListener(message -> {
                //建立socket连接
                client.connect();
                // 处理按钮点击后返回的消息
                String loginAccount = message[0];
                String loginPassword = message[1];
                if(!MyUtil.judgeAccount(loginAccount)){
                    try {
                        if(client.getSocket()!=null)
                            client.getSocket().close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    loginClient.throwLoginError("账号格式不正确");
                }else if(!MyUtil.judgePassword(loginPassword)){
                    try {
                        if(client.getSocket()!=null)
                            client.getSocket().close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    loginClient.throwLoginError("密码格式不正确");
                }else if(client.login(loginAccount,loginPassword)){
                    loginClient.close();
                    try {
                        //创建客户端线程
                        client.setLogin(true);
                        client.start();
                        //先初始化，在登录进去
                        client.userInit(loginAccount,loginPassword);
                        chatAppClient.start(new Stage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    loginClient.throwLoginError("账号或密码错误");
                }
            });
            loginClient.registerSetButtonClickListener(messages -> {
                //建立socket连接
                client.connect();
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
                    try {
                        if(client.getSocket()!=null)
                            client.getSocket().close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        });
    }
}