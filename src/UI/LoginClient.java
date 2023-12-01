package UI;
import Box.PasswordInputBox;
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
    Group loginGroup;
    InputBox loginAccountBox;
    PasswordInputBox loginPasswordBox;
    InputBox registerNameBox;
    InputBox registerAccountBox;
    PasswordInputBox registerPasswordBox;
    PasswordInputBox registerAgainPasswordBox;
    private LoginButtonClickListener registerMessageListener;
    Group registerGroup;
    private Scene loginScene;
    private  Scene registerScene;

    @Override
    public void start(Stage stage) {
        privateStage = stage;
        initLoginScene();
        initRegisterScene();
        privateStage.setScene(loginScene);
        privateStage.setTitle("Chat");//设置客户端标题
        privateStage.show();
    }
    public String getAccount(){
        return loginAccountBox.getMessage();
    }
    public String getPassword(){
        return loginPasswordBox.getMessage();
    }
    public void init() throws Exception{
        super.init();
    }
    public void initRegisterScene()
    {
        registerGroup = new Group();
        registerAccountBox = new InputBox("账号",100,110);
        registerNameBox = new InputBox("昵称",100,140);
        registerPasswordBox = new PasswordInputBox("密码",100,170);
        registerAgainPasswordBox = new PasswordInputBox("确认密码",76,200);
        //容器添加子组件
        registerAdd(registerAccountBox);
        registerAdd(registerNameBox);
        registerAdd(registerPasswordBox);
        registerAdd(registerAgainPasswordBox);
        MyButton buttonReturn = new MyButton("返回",25,250,50,20);//设置返回按钮
        MyButton buttonRegister = new MyButton("注册",190,230,50,20);//设置注册按钮
        registerAdd(buttonRegister);
        registerAdd(buttonReturn);
        //设置返回按钮触发事件
        buttonReturn.setOnAction((event) -> privateStage.setScene(loginScene));
        //设置注册按钮触发事件
        buttonRegister.setOnAction((event -> {
            String account = registerGetAccount();
            String name = registerGetName();
            String password = registerGetPassword();
            String againPassword = registerGetAgainPassword();
            String[] messages = {name,password,againPassword};
            registerNotifyCallbackMessage(messages);
        }));

        registerScene = new Scene(registerGroup, 400,300);//内容长宽
    }

    public void initLoginScene()
    {
        loginGroup = new Group();
        loginAccountBox = new InputBox("账号",100,150);
        loginPasswordBox = new PasswordInputBox("密码",100,180);
        loginAdd(loginAccountBox);
        loginAdd(loginPasswordBox);
        MyButton buttonLogin = new MyButton("登录",190,210,50,20);//设置登录按钮
        loginAdd(buttonLogin);
        MyButton buttonRegister = new MyButton("注册",25,250,50,20);//设置注册按钮
        loginAdd(buttonRegister);
        //设置注册按钮单击事件，返回给主程序按钮被点击
        buttonRegister.setOnAction((event) -> privateStage.setScene(registerScene));
        //设置登录按钮单击事件
        buttonLogin.setOnAction((event -> {
            String account=getAccount();
            String password=getPassword();
            String[] messages = {account,password};
            // 调用回调函数，并传递消息
            loginNotifyCallbackMessage(messages); //返回account和password
        }));
        //容器添加子组件

        loginScene = new Scene(loginGroup, 400,300);//内容长宽
    }

    public void loginAdd(Node node){
        loginGroup.getChildren().add(node);
    }
    // 设置回调函数
    public void setButtonClickListener(LoginButtonClickListener listener) {
        this.messageListener = listener;
    }
    private void loginNotifyCallbackMessage(String[] messages) {
        if (messages != null) {
            messageListener.onLoginButtonClick(messages);
        }
    }
    public void registerAdd(Node node){
        registerGroup.getChildren().add(node);
    }
    public void registerSetButtonClickListener(LoginButtonClickListener listener) {
        this.registerMessageListener = listener;
    }
    private void registerNotifyCallbackMessage(String[] messages) {
        if (messages != null) {
            registerMessageListener.onLoginButtonClick(messages);
        }
    }
    public String registerGetName(){
        return registerNameBox.getMessage();
    }
    public String registerGetPassword(){
        return registerPasswordBox.getMessage();
    }
    public String registerGetAgainPassword(){
        return registerAgainPasswordBox.getMessage();
    }
    public String registerGetAccount(){
        return registerAccountBox.getMessage();
    }

    public void close(){
        privateStage.close();
    }
}