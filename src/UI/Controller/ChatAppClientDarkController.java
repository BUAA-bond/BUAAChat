package UI.Controller;

import Client.GroupInfo;
import Client.User;
import Client.UserInfo;
import UI.ChatAppClient;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
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
    @FXML
    private HBox createGroupAvatarHbox;
    private static ArrayList<UserInfo> selectedUserInfo;
    class UserEvent extends Event {
        public static final EventType<UserEvent> ANY = new EventType<>(Event.ANY, "ANY");

        public static final EventType<UserEvent> CLICKED = new EventType<>(ANY,"CLICKED");


        public UserEvent(EventType<? extends Event> eventType) {
            super(eventType);
        }
    }

    // 可以在这里添加初始化方法或处理事件的方法

    // 例如，如果你想在控件初始化后执行一些操作，可以使用@FXML注解的initialize方法
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
    }
    void initGroupView(){
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
    }
    void initSearchField(){
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
    }
    void initScene(){
        searchListScene.setVisible(false);
        newGroupScene.setVisible(false);
        addGroupScene.setVisible(false);
        newFriendScene.setVisible(false);
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
        selectedUserInfo.clear();
        //addGroupListView.refresh();
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
    public void getSelectedUserInfo()
    {
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
}