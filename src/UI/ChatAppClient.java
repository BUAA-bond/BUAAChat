package UI;

import Client.GroupInfo;
import Client.User;
import Client.UserInfo;
import UI.Controller.ChatAppClientController;
import UI.Controller.ChatAppClientDarkController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.control.Button;

import static Constant.Constant.client;

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
        this.primaryStage.getIcons().add(new Image("image/icon/icon_naxida.jpg"));
        //user = client.getUser();
        //改为测试用户
        user = new User("114514","胡桃","123456ccf","image/AvatarImage/hutao.png");
        //ArrayList<UserInfo> friends =  user.getFriends();
        //改为测试好友
        ArrayList<UserInfo> friends = new ArrayList<>();
        UserInfo newFriend = new UserInfo("newFriend","新的好友","image/Controller/newFriend.png");
        UserInfo friend1 = new UserInfo("123456","钟离","image/AvatarImage/zhongli.png");
        UserInfo friend2 = new UserInfo("123123","ganyu","image/AvatarImage/ganyu.png");
        //UserInfo group1 = new UserInfo("121212","群聊","image/GroupImage/1.png");
        //friends.put("121212",group1);
        friends.add(newFriend);
        friends.add(friend1);
        friends.add(friend2);
        //ArrayList<GroupInfo> groups =  user.getGroups();
        //改为测试群聊
        ArrayList<GroupInfo> groups = new ArrayList<>();
        GroupInfo newGroup = new GroupInfo("newGroup","新的群聊","image/Controller/newGroup.png");
        GroupInfo group1 = new GroupInfo("1234","群聊1","image/GroupImage/1.png");
        groups.add(newGroup);
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
            URL url = loader.getClassLoader().getResource("UI/View/ChatApp.fxml");
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
            URL url = loader.getClassLoader().getResource("UI/View/ChatAppDarkStyle.fxml");
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
