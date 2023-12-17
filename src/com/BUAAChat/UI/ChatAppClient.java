package com.BUAAChat.UI;

import com.BUAAChat.Client.GroupInfo;
import com.BUAAChat.Client.User;
import com.BUAAChat.Client.UserInfo;
import com.BUAAChat.UI.Controller.ChatAppClientController;
import com.BUAAChat.UI.Controller.ChatAppClientDarkController;
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
    private ChatAppClientDarkController darkController;
    private ChatAppClientController whiteController;
    private  Scene darkScene;
    private  Scene whiteScene;
    private User user;
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("缘深");
        this.primaryStage.getIcons().add(new Image("com/BUAAChat/image/icon/icon_naxida.jpg"));
        user = client.getUser();
        //改为测试用户
        ArrayList<UserInfo> friends =  user.getFriends();
        //改为测试好友
        UserInfo newFriend = new UserInfo("newFriend","新的好友","com/BUAAChat/image/Controller/newFriend.png");
        friends.add(newFriend);
        ArrayList<GroupInfo> groups =  user.getGroups();
        //改为测试群聊
        GroupInfo group1 = new GroupInfo("1234","群聊1","com/BUAAChat/image/GroupImage/1.png");
        groups.add(group1);
        initWhiteRootLayout();
        initDarkRootStyle();
        darkController.setChatAppClient(this);
        darkController.initUser(user);
        darkController.initFriends(friends);
        darkController.initGroups(groups);
        darkController.initAddGroup(friends);
        whiteController.setChatAppClient(this);
        whiteController.initUser(user);
        whiteController.initFriends(friends);
        whiteController.initGroups(groups);
        changeDarkStyle();
        primaryStage.show();
        //showPersonOverview();
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
