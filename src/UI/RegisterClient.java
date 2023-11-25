package UI;

import Box.InputBox;
import Button.MyButton;
import MyInterface.LoginButtonClickListener;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RegisterClient extends Application {
    InputBox nameBox;
    InputBox passwordBox;
    InputBox againPasswordBox;
    private LoginButtonClickListener messageListener;
    Group group;
    public void start(Stage stage){
        group = new Group();
        MyButton buttonReturn = new MyButton("返回",25,250,50,20);//设置返回按钮
        MyButton buttonRegister = new MyButton("注册",190,210,50,20);//设置注册按钮
        add(buttonReturn);
        add(buttonRegister);
        //设置返回按钮触发事件
        buttonReturn.setOnAction((event) -> stage.close());
        //设置注册按钮触发事件
        buttonRegister.setOnAction((event -> {
            String name = getName();
            String password = getPassword();
            String againPassword = getAgainPassword();
            String[] messages = {name,password,againPassword};
            notifyCallbackMessage(messages);
        }));
        nameBox = new InputBox("昵称",100,120);
        passwordBox = new InputBox("密码",100,150);
        againPasswordBox = new InputBox("确认密码",76,180);
        //容器添加子组件
        add(againPasswordBox);
        add(nameBox);
        add(passwordBox);
        Scene scene = new Scene(group, 400,300);//内容长宽
        stage.setScene(scene);
        stage.setTitle("注册");//设置客户端标题
        stage.show();
    }
    public void add(Node node){
        group.getChildren().add(node);
    }
    public void setButtonClickListener(LoginButtonClickListener listener) {
        this.messageListener = listener;
    }
    private void notifyCallbackMessage(String[] messages) {
        if (messages != null) {
            messageListener.onLoginButtonClick(messages);
        }
    }
    public String getName(){
        return nameBox.getMessage();
    }
    public String getPassword(){
        return passwordBox.getMessage();
    }
    public String getAgainPassword(){
        return againPasswordBox.getMessage();
    }

}
