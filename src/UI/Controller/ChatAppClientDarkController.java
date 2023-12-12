package UI.Controller;

import Client.GroupInfo;
import Client.User;
import Client.UserInfo;
import UI.ChatAppClient;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.TextFlow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ChatAppClientDarkController{
    private final String folderPath = "src/image/GroupImage"; // 指定文件夹路径
    private ChatAppClient chatAppClient;
    @FXML
    private Label sendToObjectName;
    @FXML
    private Label onlineUserName;

    @FXML
    private Tab chooseAddGroupTab;

    @FXML
    private Tab chooseFriendTab;

    @FXML
    private Tab chooseGroupTab;
    @FXML
    private ListView<UserInfo> addGroupListView;
    @FXML
    private ListView<UserInfo> friendListView;
    @FXML
    private ListView<GroupInfo> groupListView;
    @FXML
    private AnchorPane searchListScene;

    @FXML
    private AnchorPane chatListShow;

    @FXML
    private AnchorPane friendListShow;

    @FXML
    private AnchorPane groupListShow;

    @FXML
    private TextField searchField;
    @FXML
    private ListView<UserInfo> searchFriendListView;
    @FXML
    private Button changeStyleButton;



    @FXML
    private AnchorPane newFriendScene;
    @FXML
    private AnchorPane newGroupScene;
    @FXML
    private AnchorPane addGroupScene;
    @FXML
    private AnchorPane ChatScene;
    @FXML
    private TextFlow currentChat;

    @FXML
    private TextArea sendMessage;

    @FXML
    private Button sendButton;
    @FXML
    private ImageView AvatarShow;
    @FXML
    private ImageView createGroupAvatar;
    @FXML
    private TextField createGroupName;
    @FXML
    private TextField createGroupAccount;
    @FXML
    private FlowPane groupAvatarFlowPane;
    @FXML
    private Button createGroupButton;
    // 可以在这里添加初始化方法或处理事件的方法

    // 例如，如果你想在控件初始化后执行一些操作，可以使用@FXML注解的initialize方法
    @FXML
    public void initialize() {
        // 在此添加控件初始化后的操作
        initButton();
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

            //处理新的好友事件
            if(selectedUser.account.equals("newFriend")){
                //TODO
                newFriendScene.setVisible(true);
            }
            else{
                sendToObjectName.setText(selectedUser.name);
                sendMessage.setEditable(true);
                newFriendScene.setVisible(false);
                System.out.println("Selected Item: " + selectedUser.account+" "+selectedUser.name);
            }
            // 执行你想要的操作
        });
        groupListView.setOnMouseClicked(event -> {
            GroupInfo selectedGroup = groupListView.getSelectionModel().getSelectedItem();


            //处理新的群聊事件
            if(selectedGroup.account.equals("newGroup")){
                //TODO
                newGroupScene.setVisible(true);
            }
            else{
                sendToObjectName.setText(selectedGroup.name);
                sendMessage.setEditable(true);
                newGroupScene.setVisible(false);
                System.out.println("Selected Item: " + selectedGroup.account+" "+selectedGroup.name);
            }
            // 执行你想要的操作
        });

        sendMessage.setEditable(false);

        searchField.setOnKeyPressed(event -> {
            if (event.getCode().getName().equals("Enter")) {
                searchFriend(searchField.getText().replaceAll("[\r\n]", ""));
                System.out.println(searchField.getText().replaceAll("[\r\n]", ""));
                searchField.clear(); // 清空 TextArea 内容
                //searchFriends = getFriends;
                //getSearchFriendListView(searchFriends);
            }
        });
        searchField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                // 文本框被选中时执行的操作
                searchListScene.setVisible(true);
            }
            else {
                searchListScene.setVisible(false);
            }
        });
        //initSearchFriendListView();
        searchFriendListView.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                // ListView 失去焦点时执行的操作
                searchListScene.setVisible(false);
            }
            else {
                searchListScene.setVisible(true);
            }
        });
        searchListScene.setVisible(false);
        newGroupScene.setVisible(false);
        addGroupScene.setVisible(false);
        newFriendScene.setVisible(false);
        initTab();
        initAddGroupAvatar();
        groupAvatarFlowPane.setVisible(false);
        // 添加其他控件的事件监听器等
    }
    public void initButton()
    {
        sendButton.setOnAction(event -> {
            send(sendMessage.getText());
            sendMessage.clear();
            // 处理按钮点击事件
        });
        createGroupButton.setOnAction(event -> {
            String name = createGroupName.getText();
            String account = createGroupAccount.getText();
            //TODO
            initCreateGroup();
        });
    }
    void send(String message)
    {
        //TODO
        //需要一个能获取当前聊天对象account的东西
        //if(client!=null) client.sendText(message,"");
    }
    void searchFriend(String s){
        //TODO
    }
    public void setChatAppClient(ChatAppClient chatAppClient){
        this.chatAppClient = chatAppClient;
    }
    public void initUser(User user){
        onlineUserName.setText(user.getName());
        Image AvatarImage = new Image(user.getAvatarPath());
        AvatarShow.setImage(AvatarImage);
    }
    static class FriendListCell<T extends UserInfo> extends ListCell<T> {
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
    static class GroupListCell<T extends GroupInfo> extends ListCell<T> {
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
    static class addGroupListCell<T extends UserInfo> extends ListCell<T> {
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
    public void initFriends(ArrayList<UserInfo> friends){
        for(int i = 0;i<friends.size();i++){
            UserInfo userInfo = friends.get(i);
            friendListView.getItems().add(userInfo);
        }
        // 设置列表的单元格工厂，以便自定义单元格显示内容
        friendListView.setCellFactory(param -> new FriendListCell<UserInfo>());
    }
    public void initGroups(ArrayList<GroupInfo> groups){
        for(int i = 0;i<groups.size();i++){
            GroupInfo groupInfo = groups.get(i);
            groupListView.getItems().add(groupInfo);
        }
        // 设置列表的单元格工厂，以便自定义单元格显示内容
        groupListView.setCellFactory(param -> new GroupListCell<GroupInfo>());
    }
    public void initAddGroup(ArrayList<UserInfo> friends){
        for(int i = 0;i<friends.size();i++){
            UserInfo userInfo = friends.get(i);
            if(!userInfo.account.equals("newFriend")) addGroupListView.getItems().add(userInfo);
        }
        // 设置列表的单元格工厂，以便自定义单元格显示内容
        addGroupListView.setCellFactory(param -> new addGroupListCell<UserInfo>());
    }
    public void getSearchFriendListView(ArrayList<UserInfo> friends){
        for(int i = 0;i<friends.size();i++){
            UserInfo userInfo = friends.get(i);
            searchFriendListView.getItems().add(userInfo);
        }
        // 设置列表的单元格工厂，以便自定义单元格显示内容
        searchFriendListView.setCellFactory(param -> new FriendListCell<UserInfo>());
    }
    public void initTab(){
        chooseAddGroupTab.setOnSelectionChanged(event -> {
            addGroupScene.setVisible(true);
            newGroupScene.setVisible(false);
            newFriendScene.setVisible(false);
            ChatScene.setVisible(false);
            cleanRight();
            initCreateGroup();
        });
        chooseFriendTab.setOnSelectionChanged(event -> {
            addGroupScene.setVisible(false);
            newGroupScene.setVisible(false);
            ChatScene.setVisible(true);
        });
        chooseGroupTab.setOnSelectionChanged(event -> {
            addGroupScene.setVisible(false);
            newFriendScene.setVisible(false);
            ChatScene.setVisible(true);
        });
    }
    public void cleanRight(){
        sendToObjectName.setText("");
        sendMessage.clear();
        sendMessage.setEditable(false);
    }
    public void initCreateGroup(){
        Image firstImage = new Image("image/GroupImage/7.png");
        createGroupAvatar.setImage(firstImage);
        createGroupName.clear();
        createGroupAccount.clear();
    }
    public void initAddGroupAvatar()
    {
        // 获取特定文件夹内的所有图片
        List<File> imageFiles = getImagesFromFolder(new File(folderPath));
        for (File file : imageFiles) {
            Image image = new Image(file.toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            // 添加点击事件监听器
            imageView.setOnMouseClicked(newEvent -> {
                File selectedFile = file;
                if (selectedFile != null) {
                    createGroupAvatar.setImage(image);
                    groupAvatarFlowPane.setVisible(false);
                }
            });
            groupAvatarFlowPane.getChildren().add(imageView);
        }
        createGroupAvatar.setOnMouseClicked(event -> {
            groupAvatarFlowPane.setVisible(true);
        });
    }
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
}