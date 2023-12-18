package com.BUAAChat;

import com.BUAAChat.Client.Client;
import com.BUAAChat.MyUtil.MyUtil;
import com.BUAAChat.MyUtil.RegisterInfo;
import com.BUAAChat.UI.ChatAppClient;
import com.BUAAChat.UI.LoginClient;
import javafx.stage.Stage;

import java.io.IOException;

import static com.BUAAChat.Constant.Constant.client;
import static com.BUAAChat.Constant.Constant.chatAppClient;
public class Main {
    public static void main(String[] args) {
        // 在 JavaFX 主线程中启动应用程序
        new Main().start();
    }
    public void start(){
        javafx.application.Platform.startup(() -> {
            LoginClient loginClient = new LoginClient();
            chatAppClient=new ChatAppClient();
            RegisterInfo registerInfo = new RegisterInfo();
            client = new Client();
            // 启动 JavaFX 应用程序
            loginClient.start(new Stage());
            // 设置回调函数
            loginClient.setButtonClickListener(message -> {
                //建立socket连接
                client.init();
                // 处理按钮点击后返回的消息
                String loginAccount = message[0];    //account
                String loginPassword = message[1];   //password
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
                        //TODO
                        client.setLogin(true);
                        client.setLive(true);
                        client.userInit(loginAccount,loginPassword);//先初始化，在登录进去
                        client.start();
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
                client.init();
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