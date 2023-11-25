import MyUtil.LoginInfo;
import MyUtil.MyUtil;
import MyUtil.RegisterInfo;
import UI.LoginClient;
import UI.RegisterClient;
import javafx.stage.Stage;

public class Main {

    public static void main(String[] args) {
        // 在 JavaFX 主线程中启动应用程序
        javafx.application.Platform.startup(() -> {
            LoginClient loginClient = new LoginClient();
            LoginInfo loginInfo = new LoginInfo();
            RegisterInfo registerInfo = new RegisterInfo();
            // 启动 JavaFX 应用程序
            loginClient.start(new Stage());
            //在数据库里创建一个users表
            MyUtil.createUsersTable();
            // 设置回调函数
            loginClient.setButtonClickListener(message -> {
                // 处理按钮点击后返回的消息
                String loginAccount = message[0];    //account
                String loginPassword = message[1];   //password
                loginInfo.setAccount(loginAccount);
                loginInfo.setPassword(loginPassword);
                loginInfo.judgeAndLogin();
            });
            loginClient.setRegisterClickListener(flag -> {
                //打开注册界面
                if(flag){
                    RegisterClient registerClient = new RegisterClient();
                    registerClient.start(new Stage());
                    registerClient.setButtonClickListener(messages -> {
                        String registerName = messages[0];
                        String registerPassword = messages[1];
                        String registerAgainPassword = messages[2];
                        registerInfo.setName(registerName);
                        registerInfo.setPassword(registerPassword);
                        registerInfo.setPasswordAgain(registerAgainPassword);
                        registerInfo.judgeAndRegister();
                    });
                }
            });

        });
    }
}