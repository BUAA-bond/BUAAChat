package UI;
import Button.MyButton;
import Box.InputBox;
import MyInterface.LoginButtonClickListener;
import MyInterface.RegisterButtonClickListener;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class LoginClient extends Application {
    private LoginButtonClickListener messageListener;
    private RegisterButtonClickListener registerClickListener;
    Stage privateStage;
    Group group;
    InputBox accountBox;
    InputBox passwordBox;

    @Override
    public void start(Stage stage) {
        privateStage = stage;
        group = new Group();
        MyButton buttonRegister = new MyButton("注册",25,250,50,20);//设置注册按钮
        MyButton buttonLogin = new MyButton("登录",190,210,50,20);//设置登录按钮
        add(buttonRegister);
        add(buttonLogin);
        accountBox = new InputBox("账号",100,150);
        passwordBox = new InputBox("密码",100,180);
        //设置注册按钮单击事件：弹出注册窗口
        buttonRegister.setOnAction((event) ->{
            notifyCallbackRegister();
        });
        //设置登录按钮单击事件
        buttonLogin.setOnAction((event -> {
            String account=getAccount();
            String password=getPassword();
            String[] messages = {account,password};
            // 调用回调函数，并传递消息
            notifyCallbackMessage(messages); //返回account和password
        }));
        //容器添加子组件
        add(accountBox);
        add(passwordBox);
        Scene scene = new Scene(group, 400,300);//内容长宽
        stage.setScene(scene);
        stage.setTitle("Chat");//设置客户端标题
        stage.show();
    }
    public String getAccount(){
        return accountBox.getMessage();
    }
    public String getPassword(){
        return passwordBox.getMessage();
    }
    public void init() throws Exception{
        super.init();
    }

    public void add(Node node){
        group.getChildren().add(node);
    }
    // 设置回调函数
    public void setButtonClickListener(LoginButtonClickListener listener) {
        this.messageListener = listener;
    }
    public void setRegisterClickListener(RegisterButtonClickListener listener){
        this.registerClickListener = listener;
    }
    // 调用回调函数
    private void notifyCallbackMessage(String[] messages) {
        if (messages != null) {
            messageListener.onLoginButtonClick(messages);
        }
    }
    private void notifyCallbackRegister() {
        if (registerClickListener != null) {
            registerClickListener.onClick(true);
        }
    }
    public void close(){
        privateStage.close();
    }
}