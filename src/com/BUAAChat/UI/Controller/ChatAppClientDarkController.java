package com.BUAAChat.UI.Controller;

import com.BUAAChat.Client.*;
import com.BUAAChat.UI.ChatAppClient;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.BUAAChat.Constant.Constant.client;

public class ChatAppClientDarkController{
    private final String folderPath = "src/com/BUAAChat/image/GroupImage"; // 指定文件夹路径
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
    private ListView<RequestInfo> newFriendList;
    @FXML
    private AnchorPane addGroupScene;
    @FXML
    private AnchorPane ChatScene;
    @FXML
    private ScrollPane currentChat;

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
    @FXML
    private HBox createGroupAvatarHbox;
    private VBox currentChatVbox;
    private static ArrayList<UserInfo> selectedUserInfo;
    private String ObjectAccount;
    @FXML
    public void initialize() {
        // 在此添加控件初始化后的操作
        selectedUserInfo = new ArrayList<>();
        initButton();//初始化所有按钮，设置点击事件
        initMessageArea();//初始化输入文本框，设置输入回车事件
        initFriendList();//设置好友列表点击事件
        initGroupView();//设置群聊列表点击事件
        //initCreateGroupView();//设置创建群聊列表对象被选中的事件
        initSearchField();//初始化搜索好友文本框
        initScene();//设置初始时界面显示
        initTab();
        initAddGroupAvatar();
        groupAvatarFlowPane.setVisible(false);
        initCurrentChat();
        // 添加其他控件的事件监听器等
    }
    public void initButton() {
        sendButton.setOnAction(event -> {
            send(sendMessage.getText());
            sendMessage.clear();
            UserInfo testUserInfo = new UserInfo("123456","zhongli","com/BUAAChat/image/AvatarImage/zhongli.png");
            updateOtherUserMessage(testUserInfo,"你好");
            // 处理按钮点击事件
        });
        createGroupButton.setOnAction(event -> {
            String name = createGroupName.getText();
            String account = createGroupAccount.getText();
            getSelectedUserInfo();
            //TODO
            clearAddGroupFriends();
            initCreateGroup();
        });
        changeStyleButton.setOnAction(event -> {
            chatAppClient.changeWhiteStyle();
        });
    }
    void initMessageArea(){
        sendMessage.setOnKeyPressed(event -> {
            // 如果按下的是回车键（KeyCode.ENTER）
            if (event.getCode().getName().equals("Enter")) {
                send(sendMessage.getText().replaceAll("[\r\n]", ""));
                sendMessage.clear(); // 清空 TextArea 内容
            }
        });
        sendMessage.setEditable(false);
    }
    void initFriendList(){
        friendListView.setOnMouseClicked(event -> {
            UserInfo selectedUser = friendListView.getSelectionModel().getSelectedItem();
            //处理新的好友事件
            if(selectedUser.account.equals("newFriend")){
                initNewFriends(client.getUser().getRequests());
                newFriendScene.setVisible(true);
            }
            else{
                sendToObjectName.setText(selectedUser.name);
                sendMessage.setEditable(true);
                newFriendScene.setVisible(false);
                ObjectAccount = selectedUser.account;
                chatAppClient.setToAccount(ObjectAccount);
                System.out.println("Selected Item: " + selectedUser.account+" "+selectedUser.name);
            }
            // 执行你想要的操作
        });
    }
    void initGroupView(){
        groupListView.setOnMouseClicked(event -> {
            GroupInfo selectedGroup = groupListView.getSelectionModel().getSelectedItem();
            sendToObjectName.setText(selectedGroup.name);
            sendMessage.setEditable(true);
            ObjectAccount = selectedGroup.account;
            chatAppClient.setToAccount(ObjectAccount);
            System.out.println("Selected Item: " + selectedGroup.account+" "+selectedGroup.name);
            // 执行你想要的操作
        });
    }
    void initSearchField(){
        searchField.setOnKeyPressed(event -> {
            if (event.getCode().getName().equals("Enter")) {
                searchFriend(searchField.getText().replaceAll("[\r\n]", ""));
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
                //searchListScene.setVisible(false);
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
    }
    void initScene(){
        searchListScene.setVisible(false);
        addGroupScene.setVisible(false);
        newFriendScene.setVisible(false);
    }
    void send(String message) {
        if (message.isEmpty()) return;
        client.sendText(ObjectAccount,message);
        updateOnlineUserMessage(message);
    }
    void searchFriend(String s){
        ArrayList<UserInfo> users=client.searchUser(s);
        getSearchFriendListView(users);
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
         public final CheckBox checkBox = new CheckBox();
         private final Label userInfoName = new Label();
         private final ImageView imageView = new ImageView();;
         public final HBox content = new HBox(10,checkBox, imageView, userInfoName);
        {
            checkBox.setOnAction(event -> {
                UserInfo item = getItem();
                if (checkBox.isSelected()) {
                    selectedUserInfo.add(item);
                } else {
                    selectedUserInfo.remove(item);
                }
            });
            content.setAlignment(Pos.CENTER_LEFT);
        }
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
                imageView.setImage(image);
                imageView.setFitHeight(40); // 设置图片高度
                imageView.setFitWidth(40); // 设置图片宽度
                setGraphic(content);
                setStyle("-fx-control-inner-background: rgba(255, 255, 255, 0.35);");
            }
        }
        public void clear(){
            if (checkBox.isSelected()){
                checkBox.setSelected(false);
            }
        }
        // 可以添加其他方法和处理逻辑
    }
    public void initFriends(ArrayList<UserInfo> friends){
        UserInfo newFriend = new UserInfo("newFriend","新的好友","com/BUAAChat/image/Controller/newFriend.png");
        friendListView.getItems().add(newFriend);
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
    // 设置创建群聊的好友列表显示及其功能
    public void initAddGroup(ArrayList<UserInfo> friends){
        for(int i = 0;i<friends.size();i++){
            UserInfo userInfo = friends.get(i);
            if(!userInfo.account.equals("newFriend")) addGroupListView.getItems().add(userInfo);
        }
        // 设置列表的单元格工厂，以便自定义单元格显示内容
        addGroupListView.setCellFactory(param -> new addGroupListCell<>());
    }
    public void initTab(){
        chooseAddGroupTab.setOnSelectionChanged(event -> {
            addGroupScene.setVisible(true);
            newFriendScene.setVisible(false);
            ChatScene.setVisible(false);
            searchListScene.setVisible(false);
            //cleanRight();
            initCreateGroup();
        });
        chooseFriendTab.setOnSelectionChanged(event -> {
            addGroupScene.setVisible(false);
            ChatScene.setVisible(true);
            searchListScene.setVisible(false);
        });
        chooseGroupTab.setOnSelectionChanged(event -> {
            addGroupScene.setVisible(false);
            newFriendScene.setVisible(false);
            ChatScene.setVisible(true);
            searchListScene.setVisible(false);
        });
    }
    public void cleanRight(){
        sendToObjectName.setText("");
        sendMessage.clear();
        sendMessage.setEditable(false);
    }
    public void initCreateGroup(){
        Image firstImage = new Image("com/BUAAChat/image/GroupImage/7.png");
        createGroupAvatar.setImage(firstImage);
        createGroupName.clear();
        createGroupAccount.clear();
        selectedUserInfo.clear();
        //addGroupListView.refresh();
    }
    public void initAddGroupAvatar() {
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
    public void clearCurrentChat(){
        currentChatVbox.getChildren().clear();
    }
    public void getSelectedUserInfo() {
        System.out.println("Selected Items:");
        for (UserInfo selectedItem : selectedUserInfo) {
            System.out.println(selectedItem.account+" "+selectedItem.name);
        }
    }
    public void clearAddGroupFriends() {
        for (Node node : addGroupListView.lookupAll(".list-cell")) {
            if (node instanceof addGroupListCell) {
                addGroupListCell<UserInfo> cell = (addGroupListCell<UserInfo>) node;
                cell.clear();
            }
        }
    }
    public void initCurrentChat(){
        currentChatVbox = new VBox();
        currentChatVbox.setPadding(new Insets(10));
        currentChatVbox.setSpacing(10);
        currentChatVbox.setMaxWidth(Double.MAX_VALUE);
        currentChat.setContent(currentChatVbox);
        currentChat.setFitToWidth(true);
        sendToObjectName.textProperty().addListener((observable, oldValue, newValue) -> {
            clearCurrentChat();
        });
        clearCurrentChat();
    }
    public void updateOnlineUserMessage(String message){
        HBox content = new HBox();
        content.setSpacing(10);
        content.setAlignment(Pos.CENTER);
        Text text = new Text(message);
        text.setWrappingWidth(200); // 设置固定宽度
        text.setTextAlignment(TextAlignment.RIGHT);
        double textHeight = text.getLayoutBounds().getHeight(); // 获取文本的高度
        text.prefHeight(textHeight);
        StackPane textPane = new StackPane();
        textPane.getChildren().add(text);
        textPane.setStyle("-fx-background-color: lightblue; " +
                "-fx-border-color: black;" +
                " -fx-border-width: 1px;" +
                "-fx-background-radius: 15;"+
                "-fx-border-radius: 15;" +
                " -fx-padding: 10px");
        ImageView Avatar = new ImageView(AvatarShow.getImage());
        Avatar.setFitWidth(50);
        Avatar.setFitHeight(50);
        content.getChildren().add(textPane);
        content.getChildren().add(Avatar);
        content.setAlignment(Pos.CENTER_RIGHT);
        currentChatVbox.getChildren().add(content);
        chatAppClient.getPrimaryStage().show();
        currentChat.setVvalue(1.0);
    }
    public void updateOtherUserMessage(UserInfo otherUser,String message){
        HBox content = new HBox();
        content.setSpacing(10);
        content.setAlignment(Pos.CENTER);
        Text text = new Text(message);
        text.setWrappingWidth(200); // 设置固定宽度
        text.setTextAlignment(TextAlignment.LEFT);
        double textHeight = text.getLayoutBounds().getHeight(); // 获取文本的高度
        text.prefHeight(textHeight);
        StackPane textPane = new StackPane();
        textPane.getChildren().add(text);
        textPane.setStyle("-fx-background-color: lightblue; " +
                "-fx-border-color: black;" +
                " -fx-border-width: 1px;" +
                "-fx-background-radius: 15;"+
                "-fx-border-radius: 15;" +
                " -fx-padding: 10px");
        Image image = new Image(otherUser.avatarPath);
        ImageView Avatar = new ImageView(image);
        Avatar.setFitWidth(50);
        Avatar.setFitHeight(50);
        content.getChildren().add(Avatar);
        content.getChildren().add(textPane);
        content.setAlignment(Pos.CENTER_LEFT);
        currentChatVbox.getChildren().add(content);
        chatAppClient.getPrimaryStage().show();
        currentChat.setVvalue(1.0);
    }
    public void initNewFriends(ArrayList<RequestInfo> newFriends){
        for(int i = 0;i<newFriends.size();i++){
            RequestInfo requestInfo = newFriends.get(i);
            newFriendList.getItems().add(requestInfo);
        }
        // 设置列表的单元格工厂，以便自定义单元格显示内容
        newFriendList.setCellFactory(param -> new newFriendListCell<RequestInfo>());
    }
    static class newFriendListCell<T extends RequestInfo> extends ListCell<T> {
        private final Label userInfoName = new Label();
        private final Label Type = new Label();
        private final ImageView imageView = new ImageView();
        @Override
        protected void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setText(null);
                setGraphic(null);
                setBackground(Background.EMPTY);
            } else {
                HBox content = new HBox();
                content.setSpacing(10);
                userInfoName.setText(item.name);
                Image image = new Image(item.avatarPath);
                imageView.setImage(image);
                imageView.setFitHeight(40); // 设置图片高度
                imageView.setFitWidth(40); // 设置图片宽度
                content.getChildren().addAll(imageView,userInfoName);
                Type.setAlignment(Pos.CENTER_RIGHT);
                HBox rightHbox = new HBox();
                HBox.setHgrow(rightHbox, Priority.ALWAYS);
                rightHbox.setAlignment(Pos.CENTER_RIGHT);
                if(item.type==1){
                    Type.setText("已接受");
                    rightHbox.getChildren().add(Type);
                } else if (item.type == -1) {
                    Type.setText("已拒绝");
                    rightHbox.getChildren().add(Type);
                }
                else{
                    Button accept = new Button("接受");
                    Button reject = new Button("拒绝");
                    accept.setOnAction(event -> {
                        client.sendRequestFeedback(item.from,true);
                        item.type = 1;
                        Type.setText("已接受");
                        rightHbox.getChildren().clear();
                        content.getChildren().add(Type);
                        updateItem(item,false);
                    });
                    reject.setOnAction(event -> {
                        client.sendRequestFeedback(item.from,false);
                        item.type = -1;
                        Type.setText("已拒绝");
                        rightHbox.getChildren().clear();
                        content.getChildren().add(Type);
                        updateItem(item,false);
                    });
                    rightHbox.getChildren().addAll(accept,reject);
                    rightHbox.setSpacing(10);
                }
                content.getChildren().add(rightHbox);
                setGraphic(content);/**/
                setStyle("-fx-control-inner-background: rgba(255, 255, 255, 0.35);");
            }
        }
        // 可以添加其他方法和处理逻辑
    }
    public void getSearchFriendListView(ArrayList<UserInfo> friends){
        for(int i = 0;i<friends.size();i++){
            UserInfo userInfo = friends.get(i);
            searchFriendListView.getItems().add(userInfo);
        }
        // 设置列表的单元格工厂，以便自定义单元格显示内容
        searchFriendListView.setCellFactory(param -> new searchFriendListCell<UserInfo>());
    }
    class searchFriendListCell<T extends UserInfo> extends ListCell<T> {
        private final Label userInfoName = new Label();
        private final Label Type = new Label();
        private final ImageView imageView = new ImageView();
        private int type;
        @Override
        protected void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setText(null);
                setGraphic(null);
                setBackground(Background.EMPTY);
            } else {
                HBox content = new HBox();
                content.setSpacing(10);
                userInfoName.setText(item.name);
                Image image = new Image(item.avatarPath);
                imageView.setImage(image);
                imageView.setFitHeight(40); // 设置图片高度
                imageView.setFitWidth(40); // 设置图片宽度
                content.getChildren().addAll(imageView,userInfoName);
                Type.setAlignment(Pos.CENTER_RIGHT);
                HBox rightHbox = new HBox();
                HBox.setHgrow(rightHbox, Priority.ALWAYS);
                rightHbox.setAlignment(Pos.CENTER_RIGHT);
                ObservableList<UserInfo> friends =  friendListView.getItems();
                int size =friends.size();
                for(int i = 0;i<size;i++){
                    UserInfo userInfo = friends.get(i);
                    if(userInfo.account.equals(item.account)) type = 1;
                }
                if(type==1){
                    Type.setText("已添加");
                    rightHbox.getChildren().add(Type);
                }
                else if(type == 2){
                    Type.setText("已申请");
                    rightHbox.getChildren().add(Type);
                }
                else{
                    Button add = new Button("加好友");
                    add.setOnAction(event -> {
                        client.addFriendRequest(item.account, item.name,item.avatarPath);
                        type = 2;
                        Type.setText("已申请");
                        rightHbox.getChildren().clear();
                        rightHbox.getChildren().add(Type);
                        searchListScene.setVisible(true);
                        updateItem(item,false);
                    });
                    rightHbox.getChildren().add(add);
                    rightHbox.setSpacing(10);
                }
                content.getChildren().add(rightHbox);
                setGraphic(content);/**/
                setStyle("-fx-control-inner-background: rgba(255, 255, 255, 0.35);");
            }
        }
        // 可以添加其他方法和处理逻辑
    }
}