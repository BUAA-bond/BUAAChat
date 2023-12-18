package com.BUAAChat.UI;

import com.BUAAChat.Client.*;
import com.BUAAChat.MyUtil.MyUtil;
import com.BUAAChat.UI.Controller.ChatAppClientController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import static com.BUAAChat.Constant.Constant.client;

public class ChatAppClient extends Application {
    private Stage primaryStage;
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
    private int Style;
    @Override
    public void start(Stage primaryStage) throws Exception {
        isStart=true;
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("缘深");
        this.primaryStage.getIcons().add(new Image("com/BUAAChat/image/icon/icon_naxida.jpg"));
        user = client.getUser();
        friends =  user.getFriends();
        groups =  user.getGroups();
        newFriendRequest = user.getRequests();
        initDark();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent event) {
                System.out.println("Window is closing...");
                client.logout();
            }
        });
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
            AnchorPane whiteRootLayout = loader.load();
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
            AnchorPane darkRootLayout = loader.load();
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

    public void setStyle(int style) {
        Style = style;
    }
    public void updateChat(ChatInfo chatInfo){
        if(Style==1){
            if(whiteController!=null){
                whiteController.updateChatObject(chatInfo);
            }
        }
        else {
            if(darkController!=null)
                darkController.updateChatObject(chatInfo);
        }
    }
    public void updateGroupChat(ChatInfo chatInfo,String account){
        if(Style==1){
            if(whiteController!=null){
                whiteController.updateChatObject(chatInfo,account);
            }
        }
        else {
            if(darkController!=null)
                darkController.updateChatObject(chatInfo,account);
        }
    }
    public void updateFriendList(){
        if(Style==1){
            if(whiteController!=null){
                whiteController.initFriends(friends);
                whiteController.initAddGroup(friends);
            }
        }
        else {
            if(darkController!=null){
                darkController.initFriends(friends);
                darkController.initAddGroup(friends);
            }
        }
    }
    public void updateNewFriend(){
        if(Style==1){
            if(whiteController!=null){
                whiteController.initNewFriends(newFriendRequest);
            }
        }
        else {
            if(darkController!=null){
                darkController.initNewFriends(newFriendRequest);
            }
        }
    }
    public void updateGroupList(){
        if(Style==1){
            if(whiteController!=null){
                whiteController.initGroups(groups);
            }
        }
        else {
            if(darkController!=null){
                darkController.initGroups(groups);
            }
        }
    }
    public static void main(String[] args) throws Exception {
        launch(args);
    }
}
