package com.BUAAChat.UI;

import com.BUAAChat.Client.*;
import com.BUAAChat.MyUtil.MyUtil;
import com.BUAAChat.UI.Controller.ChatAppClientController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static com.BUAAChat.Constant.Constant.client;

public class ChatAppClient extends Application {
    private Stage primaryStage;
    private AnchorPane darkRootLayout;
    private AnchorPane whiteRootLayout;
    private ChatAppClientController darkController;
    private ChatAppClientController whiteController;
    private  Scene darkScene;
    private  Scene whiteScene;
    private User user;
    ArrayList<GroupInfo> groups;
    ArrayList<UserInfo> friends;
    ArrayList<RequestInfo> newFriendRequest;
    private String toAccount;
    private boolean isStart=false;
    @Override
    public void start(Stage primaryStage) throws Exception {
        isStart=true;
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("缘深");
        this.primaryStage.getIcons().add(new Image("com/BUAAChat/image/icon/icon_naxida.jpg"));
        user = client.getUser();
        friends =  user.getFriends();
        //测试群聊
        groups =  user.getGroups();
        GroupInfo group1 = new GroupInfo("1234","群聊1","com/BUAAChat/image/GroupImage/1.png");
        groups.add(group1);


        newFriendRequest = user.getRequests();
        initDark();
        primaryStage.show();

    }
    public void initDark(){
        initDarkRootStyle();
        darkController.setChatAppClient(this);
        darkController.initUser(user);
        darkController.initFriends(friends);
        darkController.initGroups(groups);
        darkController.initAddGroup(friends);
        darkController.initNewFriends(newFriendRequest);
        changeDarkStyle();
    }
    public void initWhite(){
        initWhiteRootLayout();
        whiteController.setChatAppClient(this);
        whiteController.initUser(user);
        whiteController.initFriends(friends);
        whiteController.initGroups(groups);
        whiteController.initAddGroup(friends);
        whiteController.initNewFriends(newFriendRequest);
        changeWhiteStyle();
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
            whiteController.setStyle(1);
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
            darkController.setStyle(0);
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
