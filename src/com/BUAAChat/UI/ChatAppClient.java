package com.BUAAChat.UI;

import com.BUAAChat.Client.*;
import com.BUAAChat.MyUtil.MyUtil;
import com.BUAAChat.UI.Controller.ChatAppClientController;
import com.BUAAChat.UI.Controller.ChatAppClientDarkController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import javafx.stage.WindowEvent;
import static com.BUAAChat.Constant.Constant.client;

public class ChatAppClient extends Application {
    private Stage primaryStage;
    private AnchorPane darkRootLayout;
    private AnchorPane whiteRootLayout;
    private ChatAppClientDarkController darkController;
    private ChatAppClientController whiteController;
    private  Scene darkScene;
    private  Scene whiteScene;
    private User user;
    private String toAccount;
    private boolean isStart=false;
    @Override
    public void start(Stage primaryStage) throws Exception {
        isStart=true;
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("缘深");
        this.primaryStage.getIcons().add(new Image("com/BUAAChat/image/icon/icon_naxida.jpg"));
        user = client.getUser();
        //改为测试用户
        ArrayList<UserInfo> friends =  user.getFriends();
        ArrayList<GroupInfo> groups =  user.getGroups();
        //改为测试群聊
        GroupInfo group1 = new GroupInfo("1234","群聊1","com/BUAAChat/image/GroupImage/1.png");
        groups.add(group1);
        ArrayList<RequestInfo> newFriendRequest = user.getRequests();
        //改为测试请求
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent event) {
                System.out.println("Window is closing...");
                client.logout();
                // 如果你希望阻止窗口关闭，可以调用 event.consume();
                // event.consume();
            }
        });

        initWhiteRootLayout();
        initDarkRootStyle();
        darkController.setChatAppClient(this);
        darkController.initUser(user);
        darkController.initFriends(friends);
        darkController.initGroups(groups);
        darkController.initAddGroup(friends);
        darkController.initNewFriends(newFriendRequest);

        whiteController.setChatAppClient(this);
        whiteController.initUser(user);
        whiteController.initFriends(friends);
        whiteController.initGroups(groups);
        whiteController.initAddGroup(friends);
        whiteController.initNewFriends(newFriendRequest);
        changeDarkStyle();
        primaryStage.show();

    }
    public void close(){

    }
    public void openThread(){//当前聊天对象的账号
        new Thread(()->{
            while(isStart){
                if(toAccount!=null){
                    ArrayList<ChatInfo> chatInfos=null;
                    if(MyUtil.judgeAccount(toAccount)){//好友
                        chatInfos=user.getMessagesF().get(toAccount);
                    }else{
                        chatInfos=user.getMessagesG().get(toAccount);
                    }
                    //TODO
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    /**
     * Initializes the root layout.
     */
    public void changeWhiteStyle(){
        primaryStage.setScene(whiteScene);
    }
    public void changeDarkStyle(){
        primaryStage.setScene(darkScene);
    }

    public void initWhiteRootLayout() {
        try {
            // 读取Fxml
            FXMLLoader loader = new FXMLLoader();
            URL url = loader.getClassLoader().getResource("com/BUAAChat/UI/View/ChatApp.fxml");
            loader.setLocation(url);
            whiteRootLayout = loader.load();
            whiteController = loader.getController();
            // Show the scene containing the root layout.
            whiteScene = new Scene(whiteRootLayout);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void initDarkRootStyle() {
        try {
            // 读取Fxml
            FXMLLoader loader = new FXMLLoader();
            URL url = loader.getClassLoader().getResource("com/BUAAChat/UI/View/ChatAppDarkStyle.fxml");
            loader.setLocation(url);
            darkRootLayout = loader.load();
            // 设置控制器
            darkController = loader.getController();
            // Show the scene containing the root layout.
            darkScene = new Scene(darkRootLayout);
            changeDarkStyle();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Stage getPrimaryStage() {
        return primaryStage;
    }


    public static void main(String[] args) throws Exception {
        launch(args);
    }
}
