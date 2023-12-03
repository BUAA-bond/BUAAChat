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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
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
        privateStage.setTitle("缘深");//设置客户端标题
        privateStage.getIcons().add(new Image("image/icon/icon_naxida.jpg"));
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
        BorderPane root = new BorderPane();
        Image backgroundImage = new Image("image/RegisterImage/registerImage.jpg");
        ImageView backgroundImageView = new ImageView();
        backgroundImageView.setImage(backgroundImage);
        backgroundImageView.setFitHeight(350);
        backgroundImageView.setFitWidth(400);
        root.setCenter(backgroundImageView);
        registerAdd(root);
        ImageView Avatar = new ImageView();
        Image RegisterAvatar = new Image("image/AvatarImage/hutao.png");
        Avatar.setImage(RegisterAvatar);
        Avatar.setFitWidth(60);
        Avatar.setFitHeight(60);
        Avatar.setX(180);
        Avatar.setY(40);
        registerAccountBox = new InputBox("账号",100,110);
        registerNameBox = new InputBox("昵称",100,140);
        registerPasswordBox = new PasswordInputBox("密码",100,170);
        registerAgainPasswordBox = new PasswordInputBox("确认密码",76,200);
        //容器添加子组件
        registerAdd(Avatar);
        registerAdd(registerAccountBox);
        registerAdd(registerNameBox);
        registerAdd(registerPasswordBox);
        registerAdd(registerAgainPasswordBox);
        MyButton buttonReturn = new MyButton("返回",25,250,50,20);//设置返回按钮
        MyButton buttonRegister = new MyButton("注册",190,230,50,20);//设置注册按钮
        registerAdd(buttonRegister);
        registerAdd(buttonReturn);
        buttonReturn.setStyle(
                "-fx-background-color:#162eae;"+         //设置背景颜色
                        "-fx-background-radius:20;"+     //设置背景圆角
                        "-fx-text-fill:#9aa6e7;"+        //设置字体颜色
                        "-fx-border-radius:20;"     //设置边框圆角
        );
        buttonReturn.setOnMouseEntered(event -> {
            // 鼠标进入时更换图像为悬停时的图像
            buttonReturn.setStyle(
                    "-fx-background-color:#071870;"+         //设置背景颜色
                            "-fx-background-radius:20;"+     //设置背景圆角
                            "-fx-text-fill:#9aa6e7;"+        //设置字体颜色
                            "-fx-border-radius:20;"     //设置边框圆角
            );
        });
        // 设置鼠标离开按钮的事件处理
        buttonReturn.setOnMouseExited(event -> {
            // 鼠标离开时恢复按钮图像为默认图像
            buttonReturn.setStyle(
                    "-fx-background-color:#162eae;"+         //设置背景颜色
                            "-fx-background-radius:20;"+     //设置背景圆角
                            "-fx-text-fill:#9aa6e7;"+        //设置字体颜色
                            "-fx-border-radius:20;"     //设置边框圆角
            );
        });
        //设置返回按钮触发事件
        buttonReturn.setOnAction((event) -> privateStage.setScene(loginScene));
        buttonRegister.setStyle(
                "-fx-background-color:#162eae;"+         //设置背景颜色
                        "-fx-background-radius:20;"+     //设置背景圆角
                        "-fx-text-fill:#9aa6e7;"+        //设置字体颜色
                        "-fx-border-radius:20;"     //设置边框圆角
        );
        buttonRegister.setOnMouseEntered(event -> {
            // 鼠标进入时更换图像为悬停时的图像
            buttonRegister.setStyle(
                    "-fx-background-color:#071870;"+         //设置背景颜色
                            "-fx-background-radius:20;"+     //设置背景圆角
                            "-fx-text-fill:#9aa6e7;"+        //设置字体颜色
                            "-fx-border-radius:20;"     //设置边框圆角
            );
        });
        // 设置鼠标离开按钮的事件处理
        buttonRegister.setOnMouseExited(event -> {
            // 鼠标离开时恢复按钮图像为默认图像
            buttonRegister.setStyle(
                    "-fx-background-color:#162eae;"+         //设置背景颜色
                            "-fx-background-radius:20;"+     //设置背景圆角
                            "-fx-text-fill:#9aa6e7;"+        //设置字体颜色
                            "-fx-border-radius:20;"     //设置边框圆角
            );
        });
        //设置注册按钮触发事件
        buttonRegister.setOnAction((event -> {
            String account = registerGetAccount();
            String name = registerGetName();
            String password = registerGetPassword();
            String againPassword = registerGetAgainPassword();
            String[] messages = {account,name,password,againPassword};
            registerNotifyCallbackMessage(messages);
        }));
        registerScene = new Scene(registerGroup, 400,300);//内容长宽
    }

    public void initLoginScene()
    {
        loginGroup = new Group();
        BorderPane root = new BorderPane();
        Image backgroundImage = new Image("image/loginImage/loginImage.jpg");
        ImageView backgroundImageView = new ImageView();
        backgroundImageView.setImage(backgroundImage);
        backgroundImageView.setFitHeight(350);
        backgroundImageView.setFitWidth(400);
        root.setCenter(backgroundImageView);
        loginAdd(root);
        loginAccountBox = new InputBox("账号",100,150);
        loginPasswordBox = new PasswordInputBox("密码",100,180);
        loginAccountBox.changeLabelColour("fx-font-size: 16px;\n" +
                "    -fx-font-weight: bold;\n" +
                "    -fx-text-fill: #f5c76d;\n" +
                "    -fx-background-color: #a4710c;\n");
        loginPasswordBox.changeLabelColour("fx-font-size: 16px;\n" +
                "    -fx-font-weight: bold;\n" +
                "    -fx-text-fill: #f5c76d;\n" +
                "    -fx-background-color: #a4710c;\n");
        loginAdd(loginAccountBox);
        loginAdd(loginPasswordBox);
        MyButton buttonLogin = new MyButton(110,200,160,20);//设置登录按钮
        loginAdd(buttonLogin);
        MyButton buttonRegister = new MyButton("注册",25,250,50,20);//设置注册按钮
        loginAdd(buttonRegister);
        buttonRegister.setStyle(
                "-fx-background-color:#91640b;"+         //设置背景颜色
                        "-fx-background-radius:20;"+     //设置背景圆角
                        "-fx-text-fill:#f8b000;"+        //设置字体颜色
                        "-fx-border-radius:20;"     //设置边框圆角
        );
        buttonRegister.setOnMouseEntered(event -> {
            // 鼠标进入时更换图像为悬停时的图像
            buttonRegister.setStyle(
                    "-fx-background-color:#704506;"+         //设置背景颜色
                            "-fx-background-radius:20;"+     //设置背景圆角
                            "-fx-text-fill:#f8b000;"+        //设置字体颜色
                            "-fx-border-radius:20;"     //设置边框圆角
            );
        });
        // 设置鼠标离开按钮的事件处理
        buttonRegister.setOnMouseExited(event -> {
            // 鼠标离开时恢复按钮图像为默认图像
            buttonRegister.setStyle(
                    "-fx-background-color:#91640b;"+         //设置背景颜色
                            "-fx-background-radius:20;"+     //设置背景圆角
                            "-fx-text-fill:#f8b000;"+        //设置字体颜色
                            "-fx-border-radius:20;"     //设置边框圆角
            );
        });
        //设置注册按钮单击事件
        buttonRegister.setOnAction((event) -> privateStage.setScene(registerScene));
        Image loginButtonImage = new Image("image/loginImage/loginBorder1.png");
        Image clickedImage = new Image("image/loginImage/loginBorderClicked.png"); // 替换成悬停后的图像路径
        ImageView loginButtonImageView = new ImageView(loginButtonImage);
        buttonLogin.setGraphic(loginButtonImageView);
        buttonLogin.setStyle("-fx-background-color: transparent;");
        //设置登录按钮单击事件
        buttonLogin.setOnAction((event -> {
            String account=getAccount();
            String password=getPassword();
            String[] messages = {account,password};
            // 调用回调函数，并传递消息
            loginNotifyCallbackMessage(messages); //返回account和password
        }));
        buttonLogin.setOnMouseEntered(event -> {
            // 鼠标进入时更换图像为悬停时的图像
            loginButtonImageView.setImage(clickedImage);
        });

        // 设置鼠标离开按钮的事件处理
        buttonLogin.setOnMouseExited(event -> {
            // 鼠标离开时恢复按钮图像为默认图像
            loginButtonImageView.setImage(loginButtonImage);
        });
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