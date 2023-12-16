package com.BUAAChat.UI.Controller;

import com.BUAAChat.Client.Group;
import com.BUAAChat.Client.GroupInfo;
import com.BUAAChat.Client.User;
import com.BUAAChat.Client.UserInfo;
import com.BUAAChat.UI.ChatAppClient;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;

import java.io.PushbackInputStream;
import java.util.HashMap;
import java.util.Map;

import static com.BUAAChat.Constant.Constant.client;
public class ChatAppClientDarkController{
    private ChatAppClient chatAppClient;
    @FXML
    private Label sendToObjectName;
    @FXML
    private Label onlineUserName;

    @FXML
    private Tab chooseChatTab;

    @FXML
    private Tab chooseFriendTab;

    @FXML
    private Tab chooseGroupTab;
    //@FXML
    //private ListView<User> chatHistoryListView;
    @FXML
    private ListView<UserInfo> friendListView;
    @FXML
    private ListView<GroupInfo> groupListView;

    @FXML
    private AnchorPane chatListShow;

    @FXML
    private AnchorPane friendListShow;

    @FXML
    private AnchorPane groupListShow;

    @FXML
    private TextField searchField;
    @FXML
    private Button changeStyleButton;

    @FXML
    private ImageView AvatarShow;

    @FXML
    private TextFlow currentChat;

    @FXML
    private TextArea sendMessage;

    @FXML
    private Button sendButton;

    // 可以在这里添加初始化方法或处理事件的方法

    // 例如，如果你想在控件初始化后执行一些操作，可以使用@FXML注解的initialize方法
    @FXML
    public void initialize() {
        // 在此添加控件初始化后的操作
        sendButton.setOnAction(event -> {
            send(sendMessage.getText());
            sendMessage.clear();
            // 处理按钮点击事件
        });
        searchField.setOnKeyPressed(event -> {

            // 处理搜索框按键事件
        });
        sendMessage.setOnKeyPressed(event -> {
            // 如果按下的是回车键（KeyCode.ENTER）
            if (event.getCode().getName().equals("Enter")) {
                send(sendMessage.getText().replaceAll("[\r\n]", ""));
                sendMessage.clear(); // 清空 TextArea 内容
            }
        });
        changeStyleButton.setOnAction(event -> {
                chatAppClient.changeWhiteStyle();
        });
        //好友被选中
        friendListView.setOnMouseClicked(event -> {
            UserInfo selectedUser = friendListView.getSelectionModel().getSelectedItem();
            sendToObjectName.setText(selectedUser.name);
            //处理添加好友事件
            if(selectedUser.account.equals("addFriend")){
                //TODO
            }
            //处理新的好友事件
            else if(selectedUser.account.equals("newFriend")){
                //TODO
            }
            else{
                sendMessage.setEditable(true);
                System.out.println("Selected Item: " + selectedUser.account+" "+selectedUser.name);
            }

            // 执行你想要的操作
        });

        sendMessage.setEditable(false);
        // 添加其他控件的事件监听器等
    }
    void send(String message)
    {
        //TODO
        //需要一个能获取当前聊天对象account的东西
        //if(client!=null) client.sendText(message,"");
    }
    void setFit(ImageView imageView,double width,double height){
        imageView.setFitHeight(height);
        imageView.setFitWidth(width);
    }
    public void setChatAppClient(ChatAppClient chatAppClient){
        this.chatAppClient = chatAppClient;
    }
    public void initUser(User user){
        onlineUserName.setText(user.getName());
        Image AvatarImage = new Image(user.getAvatarPath());
        AvatarShow.setImage(AvatarImage);
    }
    static class CustomListCell<T extends UserInfo> extends ListCell<T> {
        @Override
        protected void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);

            if (item == null || empty) {
                setText(null);
                setGraphic(null);
                setBackground(Background.EMPTY);
            } else {
                setText(item.name);
                Image image = new Image(item.avatarPath);
                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(40); // 设置图片高度
                imageView.setFitWidth(40); // 设置图片宽度
                setGraphic(imageView);/**/
                setStyle("-fx-control-inner-background: rgba(255, 255, 255, 0.35);");

            }
        }
        // 可以添加其他方法和处理逻辑
    }
    public void initFriends(HashMap<String, UserInfo> friends){
        friendListView.getItems().addAll(friends.values());
        // 设置列表的单元格工厂，以便自定义单元格显示内容
        friendListView.setCellFactory(param -> new CustomListCell<UserInfo>());
    }
}