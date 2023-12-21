package com.BUAAChat.UI;
import com.BUAAChat.UI.Box.PasswordInputBox;
import com.BUAAChat.UI.Button.MyButton;
import com.BUAAChat.UI.Box.InputBox;
import com.BUAAChat.UI.MyInterface.ButtonClickListener;
import com.BUAAChat.MyUtil.MyUtil;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.input.KeyCode;

import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
/**
 * @author 符观集
 * @Description:   登录和注册的界面
 * @date 2023/12/16 20:09
 */

public class LoginClient extends Application {
    /**
     *  登录传递信息的监听者
     */
    private ButtonClickListener messageListener;
    /**
     *  头像文件夹路径
     */
    private final String folderPath = "src/com/BUAAChat/image/AvatarImage"; // 指定文件夹路径
    /**
     *  应用程序的展示舞台
     */
    Stage privateStage;
    /**
     *  登录界面的部件群
     */
    Group loginGroup;
    /**
     *  登录的账号输入部件
     */
    InputBox loginAccountBox;
    /**
     *  登录的密码输入部件
     */
    PasswordInputBox loginPasswordBox;
    /**
     *  注册的昵称输入部件
     */
    InputBox registerNameBox;
    /**
     *  注册的账号输入部件
     */
    InputBox registerAccountBox;
    /**
     *  注册的密码输入部件
     */
    PasswordInputBox registerPasswordBox;
    /**
     *  注册的确认密码输入部件
     */
    PasswordInputBox registerAgainPasswordBox;
    /**
     *  注册传递信息的监听者
     */
    private ButtonClickListener registerMessageListener;
    /**
     *  注册部件群
     */
    Group registerGroup;
    /**
     *  登录界面
     */
    private Scene loginScene;
    /**
     *  注册界面
     */
    private  Scene registerScene;
    /**
     *  选择头像界面
     */
    private Scene choosseScene;
    /**
     *  注册成功界面
     */
    private Scene registerSuccessScene;

    /**
     * @param stage 应用程序展现的stage
     * @Description: 初始化登录注册的界面并显示
     */
    @Override
    public void start(Stage stage) {
        privateStage = stage;
        initLoginScene();
        initRegisterScene();
        initRegisterSuccessScene();
        privateStage.setScene(loginScene);
        privateStage.setTitle("缘深");
        privateStage.getIcons().add(new Image("com/BUAAChat/image/icon/icon_naxida.jpg"));
        privateStage.show();
    }

    /**
     * @return {@link String} 返回登录时输入的账号
     */
    public String getAccount(){
        return loginAccountBox.getMessage();
    }

    /**
     * @return {@link String} 返回登录时输入的密码
     */
    public String getPassword(){
        return loginPasswordBox.getMessage();
    }

    public void init() throws Exception{
        super.init();
    }

    /**
     * @Description: 初始化登录成功的界面
     */
    public void initRegisterSuccessScene(){
        Group registerSuccesGroup = new Group();
        Image backgroundImage = new Image("com/BUAAChat/image/RegisterImage/registerSuccessImage2.png");
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitHeight(300);
        backgroundImageView.setFitWidth(400);
        registerSuccesGroup.getChildren().add(backgroundImageView);
        registerSuccessScene = new Scene(registerSuccesGroup,400,300);
        registerSuccessScene.setOnMouseClicked((event) -> privateStage.setScene(loginScene));
    }

    /**
     * @Description: 初始化注册的界面
     */
    public void initRegisterScene() {
        final String[] AvatarName = {"hutao.png"};
        registerGroup = new Group();
        BorderPane root = new BorderPane();
        Image backgroundImage = new Image("com/BUAAChat/image/RegisterImage/registerImage.jpg");
        ImageView backgroundImageView = new ImageView();
        backgroundImageView.setImage(backgroundImage);
        backgroundImageView.setFitHeight(350);
        backgroundImageView.setFitWidth(400);
        root.setCenter(backgroundImageView);
        registerAdd(root);
        ImageView Avatar = new ImageView();
        Image RegisterAvatar = new Image("com/BUAAChat/image/AvatarImage/hutao.png");
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
                    Avatar.setImage(image);
                    privateStage.setScene(registerScene);
                    AvatarName[0] = file.getName();
                });
                flowPane.getChildren().add(imageView);
            }
            BorderPane chooseRoot = new BorderPane();
            chooseRoot.setCenter(flowPane);
            // 将 FlowPane 放置在 BorderPane 的中间
            chooseRoot.setPadding(new Insets(10));
            // 设置 BorderPane 的边距
            chooseRoot.setStyle("-fx-border-color:#947403; " +
                    "-fx-border-width: 2px;" +
                    "-fx-background-color: #7B786F");
            // 设置 BorderPane 的边框样式
            choosseScene = new Scene(chooseRoot);
            privateStage.setScene(choosseScene);
        });
        Image rightImage = new Image("com/BUAAChat/image/RegisterImage/yesHint.png");
        Image wrongImage = new Image("com/BUAAChat/image/RegisterImage/wrongHint.png");
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
            String registerAvatarPath = "com/BUAAChat/image/AvatarImage/"+AvatarName[0];
            String[] messages = {account,name,password,againPassword,registerAvatarPath};
            registerNotifyCallbackMessage(messages);
        }));
        registerScene = new Scene(registerGroup, 400,300);//内容长宽
    }

    /**
     * @Description: 初始化注册的界面
     */
    public void initLoginScene() {
        loginGroup = new Group();
        BorderPane root = new BorderPane();
        Image backgroundImage = new Image("com/BUAAChat/image/loginImage/loginImage4.png");
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
        Image loginButtonImage = new Image("com/BUAAChat/image/loginImage/loginBorder1.png");
        Image clickedImage = new Image("com/BUAAChat/image/loginImage/loginBorderClicked.png"); // 替换成悬停后的图像路径
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

    /**
     * @param node 将部件加入登录部件群中
     */
    public void loginAdd(Node node){
        loginGroup.getChildren().add(node);
    }

    /**
     * @param listener 设置的登录事件监听者
     */
    public void setButtonClickListener(ButtonClickListener listener) {
        this.messageListener = listener;
    }

    /**
     * @param messages  传递的登录用户信息  message[0]:account 用户账号
     *                                  message[1]:password 用户密码
     */
    private void loginNotifyCallbackMessage(String[] messages) {
        if (messages != null) {
            messageListener.onLoginButtonClick(messages);
        }
    }

    /**
     * @param node 将部件加入注册部件群中
     */
    public void registerAdd(Node node){
        registerGroup.getChildren().add(node);
    }

    /**
     * @param listener  设置的注册事件监听者
     */
    public void registerSetButtonClickListener(ButtonClickListener listener) {
        this.registerMessageListener = listener;
    }

    /**
     * @param messages  传递的注册用户信息  message[0]:account               用户账号
     *                                  message[1]:name                  用户昵称
     *                                  message[2]:password              用户密码
     *                                  message[3]:againPassword         用户输入的确认密码
     *                                  message[4]:registerAvatarPath    用户头像路径
     */
    private void registerNotifyCallbackMessage(String[] messages ) {
        if (messages != null) {
            registerMessageListener.onLoginButtonClick(messages);
        }
    }

    /**
     * @return {@link String}   注册时用户输入的名字
     */
    public String registerGetName(){
        return registerNameBox.getMessage();
    }

    /**
     * @return {@link String}   注册时用户输入的密码
     */
    public String registerGetPassword(){
        return registerPasswordBox.getMessage();
    }

    /**
     * @return {@link String}   注册时用户输入的确认密码
     */
    public String registerGetAgainPassword(){
        return registerAgainPasswordBox.getMessage();
    }

    /**
     * @return {@link String}   注册时用户输入的账号
     */
    public String registerGetAccount(){
        return registerAccountBox.getMessage();
    }

    /**
     *  关闭窗口
     */
    public void close(){
        privateStage.close();
    }

    /**
     * @param string    注册失败时抛出的错误信息
     * @Description:    用户注册失败时反馈错误信息
     */
    public void throwError(String string){

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("注册失败");
        alert.setContentText(string);
        alert.showAndWait();

    }

    /**
     * @param string    登录失败时抛出的错误信息
     * @Description:    登录失败时向用户反馈错误信息
     */
    public void throwLoginError(String string){

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("登录失败");
        alert.setContentText(string);
        alert.showAndWait();

    }

    /**
     * @param folder    指定的文件路径
     * @return {@link List}<{@link File}>   指定文件夹下的所有图像文件
     */
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

    /**
     * @param file  用于判断的文件
     * @Description: 判断文件是否为图片
     * @return boolean
     */
    private boolean isImage(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") ||
                fileName.endsWith(".png") || fileName.endsWith(".gif") ||
                fileName.endsWith(".bmp");
    }

    /**
     * @Description: 转换为注册成功界面
     */
    public void changeRegisterSuccess(){
        privateStage.setScene(registerSuccessScene);
    }
}
