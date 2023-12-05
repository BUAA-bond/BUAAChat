package UI;
import Box.PasswordInputBox;
import Button.MyButton;
import Box.InputBox;
import MyInterface.LoginButtonClickListener;
import MyInterface.RegisterButtonClickListener;
import MyUtil.MyUtil;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.input.KeyCode;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class LoginClient extends Application {
    private LoginButtonClickListener messageListener;
    private RegisterButtonClickListener registerClickListener;
    private final String folderPath = "src/image/AvatarImage"; // 指定文件夹路径
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
    private Scene choosseScene;
    private Scene registerSuccessScene;

    @Override
    public void start(Stage stage) {
        privateStage = stage;
        initLoginScene();
        initRegisterScene();
        initRegisterSuccessScene();
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
    public void initRegisterSuccessScene(){
        Group registerSuccesGroup = new Group();
        Image backgroundImage = new Image("image/RegisterImage/registerSuccessImage2.png");
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitHeight(300);
        backgroundImageView.setFitWidth(400);
        registerSuccesGroup.getChildren().add(backgroundImageView);
        registerSuccessScene = new Scene(registerSuccesGroup,400,300);
        registerSuccessScene.setOnMouseClicked((event) -> privateStage.setScene(loginScene));
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
        Avatar.setOnMouseClicked(event -> {
            FlowPane flowPane = new FlowPane(Orientation.HORIZONTAL, 20, 20);
            // 获取特定文件夹内的所有图片
            List<File> imageFiles = getImagesFromFolder(new File(folderPath));
            for (File file : imageFiles) {
                Image image = new Image(file.toURI().toString());
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(60);
                imageView.setFitHeight(60);
                // 添加点击事件监听器
                imageView.setOnMouseClicked(newEvent -> {
                    File selectedFile = file;
                    if (selectedFile != null) {
                        Avatar.setImage(image);
                        privateStage.setScene(registerScene);
                    }
                });
                flowPane.getChildren().add(imageView);
            }
            BorderPane chooseRoot = new BorderPane();
            chooseRoot.setCenter(flowPane); // 将 FlowPane 放置在 BorderPane 的中间

            // 设置 BorderPane 的边距
            chooseRoot.setPadding(new Insets(10));

            // 设置 BorderPane 的边框样式
            chooseRoot.setStyle("-fx-border-color:#947403; " +
                    "-fx-border-width: 2px;" +
                    "-fx-background-color: #7B786F");

            choosseScene = new Scene(chooseRoot);
            privateStage.setScene(choosseScene);
        });
        Image rightImage = new Image("image/RegisterImage/yesHint.png");
        Image wrongImage = new Image("image/RegisterImage/wrongHint.png");
        ImageView accountRightImageView = new ImageView(rightImage);
        accountRightImageView.setFitHeight(20);
        accountRightImageView.setFitWidth(20);
        ImageView accountWrongImageView = new ImageView(wrongImage);
        accountWrongImageView.setFitHeight(20);
        accountWrongImageView.setFitWidth(20);
        registerAccountBox = new InputBox("账号",100,110);
        registerAccountBox.setHint("6-10位数字");
        TextField accountField = registerAccountBox.getField();
        Label registerAccountHint = new Label();
        registerAccountBox.getChildren().add(registerAccountHint);
        accountField.textProperty().addListener((observable, oldValue, newValue) -> {
            // 根据输入内容做出反馈或高亮显示
            if (newValue.isEmpty()) {
                registerAccountHint.setGraphic(accountWrongImageView);
            } else if (!MyUtil.judgeAccount(newValue)) {
                registerAccountHint.setGraphic(accountWrongImageView);
            } else {
                registerAccountHint.setGraphic(accountRightImageView);
            }
        });
        registerNameBox = new InputBox("昵称",100,140);
        TextField nameField = registerNameBox.getField();
        Label nameHint = new Label();
        ImageView nameRightImageView = new ImageView(rightImage);
        nameRightImageView.setFitHeight(20);
        nameRightImageView.setFitWidth(20);
        ImageView nameWrongImageView = new ImageView(wrongImage);
        nameWrongImageView.setFitHeight(20);
        nameWrongImageView.setFitWidth(20);
        registerNameBox.getChildren().add(nameHint);
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            // 根据输入内容做出反馈或高亮显示
            if (newValue.isEmpty()) {
                nameHint.setGraphic(nameWrongImageView);
            } else if (!MyUtil.judgeName(newValue)) {
                nameHint.setGraphic(nameWrongImageView);
            } else {
                nameHint.setGraphic(nameRightImageView);
            }
        });
        registerNameBox.setHint("1-10位任意字符");
        registerPasswordBox = new PasswordInputBox("密码",100,170);
        TextField passwordField = registerPasswordBox.getField();
        Label passwordHint = new Label();
        ImageView passwordRightImageView = new ImageView(rightImage);
        passwordRightImageView.setFitHeight(20);
        passwordRightImageView.setFitWidth(20);
        ImageView passwordWrongImageView = new ImageView(wrongImage);
        passwordWrongImageView.setFitHeight(20);
        passwordWrongImageView.setFitWidth(20);
        registerPasswordBox.getChildren().add(passwordHint);
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            // 根据输入内容做出反馈或高亮显示
            if (newValue.isEmpty()) {
                passwordHint.setGraphic(passwordWrongImageView);
            } else if (!MyUtil.judgePassword(newValue)) {
                passwordHint.setGraphic(passwordWrongImageView);
            } else {
                passwordHint.setGraphic(passwordRightImageView);
            }
        });
        registerPasswordBox.setHint("8-16位，至少含有一位数字和一位英文字符，不含中文");
        registerAgainPasswordBox = new PasswordInputBox("确认密码",76,200);
        TextField agianPasswordField = registerAgainPasswordBox.getField();
        Label againPasswordHint = new Label();
        ImageView againPasswordRightImageView = new ImageView(rightImage);
        againPasswordRightImageView.setFitHeight(20);
        againPasswordRightImageView.setFitWidth(20);
        ImageView againPasswordWrongImageView = new ImageView(wrongImage);
        againPasswordWrongImageView.setFitHeight(20);
        againPasswordWrongImageView.setFitWidth(20);
        registerAgainPasswordBox.getChildren().add(againPasswordHint);
        agianPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            // 根据输入内容做出反馈或高亮显示
            if (newValue.isEmpty()) {
                againPasswordHint.setGraphic(againPasswordWrongImageView);
            } else if (!newValue.equals(passwordField.getText())) {
                againPasswordHint.setGraphic(againPasswordWrongImageView);
            } else {
                againPasswordHint.setGraphic(againPasswordRightImageView);
            }
        });
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
            Image reggisterImage = Avatar.getImage();
            String registerAvatarPath = reggisterImage.getUrl();
            String[] messages = {account,name,password,againPassword,registerAvatarPath};

            registerNotifyCallbackMessage(messages);
        }));
        registerScene = new Scene(registerGroup, 400,300);//内容长宽
    }

    public void initLoginScene()
    {
        loginGroup = new Group();
        BorderPane root = new BorderPane();
        Image backgroundImage = new Image("image/loginImage/loginImage4.png");
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
        TextField accountField = loginAccountBox.getField();
        accountField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    String account=getAccount();
                    String password=getPassword();
                    String[] messages = {account,password};
                    // 调用回调函数，并传递消息
                    loginNotifyCallbackMessage(messages); //返回account和password
                }
            }
        });
        TextField passwordField = loginPasswordBox.getField();
        passwordField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    String account=getAccount();
                    String password=getPassword();
                    String[] messages = {account,password};
                    // 调用回调函数，并传递消息
                    loginNotifyCallbackMessage(messages); //返回account和password
                }
            }
        });
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
    private void registerNotifyCallbackMessage(String[] messages ) {
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
    public void throwError(String string){

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("注册失败");
        alert.setContentText(string);
        alert.showAndWait();

    }
    // 获取特定文件夹中的所有图片文件
    private List<File> getImagesFromFolder(File folder) {
        List<File> imageFiles = new ArrayList<>();
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && isImage(file)) {
                    imageFiles.add(file);
                }
            }
        }
        return imageFiles;
    }

    // 检查文件是否为图片
    private boolean isImage(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") ||
                fileName.endsWith(".png") || fileName.endsWith(".gif") ||
                fileName.endsWith(".bmp");
    }
    public void changeRegisterSuccess(){
        privateStage.setScene(registerSuccessScene);
    }
}