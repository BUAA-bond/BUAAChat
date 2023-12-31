package com.BUAAChat.UI;
import com.BUAAChat.Client.*;
import com.BUAAChat.Info.ChatInfo;
import com.BUAAChat.Info.GroupInfo;
import com.BUAAChat.Info.RequestInfo;
import com.BUAAChat.Info.UserInfo;
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

/**
 * 应用程序主界面
 * @author 符观集
 * @date 2023/11/29 19:29
 */
public class ChatAppClient extends Application {
    /**
     *  应用程序展示的舞台
     */
    private Stage primaryStage;
    /**
     *  夜间主题界面的controller
     */
    private ChatAppClientController darkController;
    /**
     *  白天主题界面的controller
     */
    private ChatAppClientController whiteController;
    /**
     *  夜间主题界面
     */
    private  Scene darkScene;
    /**
     *  白天主题界面
     */
    private  Scene whiteScene;
    /**
     *  当前在线用户
     */
    private User user;
    /**
     *  当前用户的群聊
     */
    ArrayList<GroupInfo> groups;
    /**
     *  当前用户的好友
     */
    ArrayList<UserInfo> friends;
    /**
     *  当前用户的好友请求
     */
    ArrayList<RequestInfo> newFriendRequest;
    /**
     *  当前主题
     */
    private int Style;

    /**
     * 启动主程序
     * @param primaryStage  展现的舞台
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("缘深");
        this.primaryStage.getIcons().add(new Image("com/BUAAChat/image/icon/icon_naxida.jpg"));
        user = Client.getClient().getUser();
        friends =  user.getFriends();
        groups =  user.getGroups();
        newFriendRequest = user.getRequests();
        initDark();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent event) {
                System.out.println("Window is closing...");
                Client.getClient().logout();
            }
        });
        primaryStage.show();
    }

    /**
     *   初始化/更改 黑夜界面
     */
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

    /**
     *   初始化/更改 白天界面
     */
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


    /**
     *   将舞台转换为白天背景
     */
    public void changeWhiteStyle(){
        primaryStage.setScene(whiteScene);
    }

    /**
     *    将舞台转换为黑夜背景
     */
    public void changeDarkStyle(){
        primaryStage.setScene(darkScene);
    }

    /**
     *   初始化白天主题部件
     */
    public void initWhiteRootLayout() {
        try {
            // 读取Fxml
            FXMLLoader loader = new FXMLLoader();
            URL url = loader.getClassLoader().getResource("com/BUAAChat/UI/View/ChatApp.fxml");
            loader.setLocation(url);
            AnchorPane whiteRootLayout = loader.load();
            whiteController = loader.getController();
            whiteController.setStyle(1);
            whiteScene = new Scene(whiteRootLayout);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  初始化黑夜主题部件
     */
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

    /**
     * 获得主程序舞台
     * @return {@link Stage}    应用给程序主舞台
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * 更改主题
     * @param style 设置主题
     */
    public void setStyle(int style) {
        Style = style;
    }

    /**
     * 更新新的聊天消息
     * @param chatInfo  传入的新的聊天消息
     */
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

    /**
     * 更新群聊消息
     * @param chatInfo  新的群聊聊天信息
     * @param account   群聊的群号
     */
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

    /**
     *  更新好友列表
     */
    public void updateFriendList(){
        if(Style==1){
            if(whiteController!=null){
                friends=user.getFriends();
                whiteController.initFriends(friends);
                whiteController.initAddGroup(friends);
            }
        }
        else {
            if(darkController!=null){
                friends=user.getFriends();
                darkController.initFriends(friends);
                darkController.initAddGroup(friends);
            }
        }
    }

    /**
     *  更新好友请求
     */
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

    /**
     *  更新加入的群聊
     */
    public void updateGroupList(){
        if(Style==1){
            if(whiteController!=null){
                groups=user.getGroups();
                whiteController.initGroups(groups);
            }
        }
        else {
            if(darkController!=null){
                groups=user.getGroups();
                darkController.initGroups(groups);
            }
        }
    }

    /**
     * 测试用main函数（便于设置布局）
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        launch(args);
    }
}
