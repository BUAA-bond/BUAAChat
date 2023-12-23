package com.BUAAChat.UI.Controller;

import com.BUAAChat.Client.*;
import com.BUAAChat.Info.ChatInfo;
import com.BUAAChat.Info.GroupInfo;
import com.BUAAChat.Info.RequestInfo;
import com.BUAAChat.Info.UserInfo;
import com.BUAAChat.MyUtil.MyUtil;
import com.BUAAChat.UI.ChatAppClient;
import javafx.collections.ObservableList;
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
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import static com.BUAAChat.Constant.Constant.client;

/**
 * @author 符观集
 * @Description:
 * @date 2023/12/3 20:09
 */
public class ChatAppClientController{
    /**
     *  控制的应用程序
     */
    private ChatAppClient chatAppClient;
    /**
     *  当前聊天对象的名字（也可以是群）
     */
    @FXML
    private Label sendToObjectName;
    /**
     *  当前在线用户的名字
     */
    @FXML
    private Label onlineUserName;

    /**
     *  创建群聊的Tab
     */
    @FXML
    private Tab chooseAddGroupTab;

    /**
     *  好友列表的Tab
     */
    @FXML
    private Tab chooseFriendTab;

    /**
     *  群聊列表的Tab
     */
    @FXML
    private Tab chooseGroupTab;
    /**
     *  创建群聊的选择好友列表
     */
    @FXML
    private ListView<UserInfo> addGroupListView;
    /**
     *  好友列表
     */
    @FXML
    private ListView<UserInfo> friendListView;
    /**
     *  群聊列表
     */
    @FXML
    private ListView<GroupInfo> groupListView;
    /**
     *  搜索好友的界面
     */
    @FXML
    private AnchorPane searchListScene;
    /**
     *  搜索好友的文本框
     */
    @FXML
    private TextField searchField;
    /**
     *  搜索到的好友列表
     */
    @FXML
    private ListView<UserInfo> searchFriendListView;
    /**
     *  切换界面按钮
     */
    @FXML
    private Button changeStyleButton;
    /**
     *  好友申请界面
     */
    @FXML
    private AnchorPane newFriendScene;
    /**
     *  好友申请列表
     */
    @FXML
    private ListView<RequestInfo> newFriendList;
    /**
     *  创建群聊界面
     */
    @FXML
    private AnchorPane addGroupScene;
    /**
     *  聊天界面
     */
    @FXML
    private AnchorPane ChatScene;
    /**
     *  当前聊天的滚动框
     */
    @FXML
    private ScrollPane currentChat;

    /**
     *  输入的文本框
     */
    @FXML
    private TextArea sendMessage;

    /**
     *  发送按钮
     */
    @FXML
    private Button sendButton;
    /**
     *  当前用户头像
     */
    @FXML
    private ImageView AvatarShow;
    /**
     *  创建群聊选择的头像
     */
    @FXML
    private ImageView createGroupAvatar;
    /**
     *  创建群聊的名字文本框
     */
    @FXML
    private TextField createGroupName;
    /**
     *  创建群聊的账号文本框
     */
    @FXML
    private TextField createGroupAccount;
    /**
     *  创建群聊头像选择的滚动框
     */
    @FXML
    private FlowPane groupAvatarFlowPane;
    /**
     *  创建群聊按钮
     */
    @FXML
    private Button createGroupButton;
    /**
     *  更改个人信息界面
     */
    @FXML
    private  AnchorPane changeIdentityScene;
    /**
     *  更改个人信息时选择的新的头像
     */
    @FXML
    private  ImageView newAvatar;
    /**
     *  更改的新昵称输入文本框
     */
    @FXML
    private  TextField newNameField;
    /**
     *  更改的新密码输入文本框
     */
    @FXML
    private  TextField newPasswordField;
    /**
     *  更改个人信息的确认更改按钮
     */
    @FXML
    private Button changeIdentityButton;
    /**
     *  更改个人信息时的头像滚动框
     */
    @FXML
    private FlowPane AvatarFlowPane;
    /**
     *  用于容纳当前聊天消息的容器
     */
    private VBox currentChatVbox;
    /**
     *  创建群聊时选中的好友
     */
    private static ArrayList<UserInfo> selectedUserInfo;
    /**
     *  当前聊天对象的账号
     */
    private String ObjectAccount;
    /**
     *  当前在线的用户
     */
    private User onlineUser;
    /**
     *  更改个人信息时头像的路径
     */
    String newAvatarPath = null;
    /**
     *  创建群聊时群聊头像的路径
     */
    String newGroupAvatarPath = null;
    /**
     *  控制器所对应的主题
     */
    private int Style;
    /**
     * 右键选择好友时显示的菜单栏
     */
    private ContextMenu friendContextMenu;
    /**
     *  被选中的好友（删除用）
     */
    private UserInfo chooseFriend;

    /**
     *  初始化
     */
    @FXML
    public void initialize() {
        // 在此添加控件初始化后的操作
        selectedUserInfo = new ArrayList<>();
        initButton();//初始化所有按钮，设置点击事件
        initMessageArea();//初始化输入文本框，设置输入回车事件
        initFriendList();//设置好友列表点击事件
        initGroupView();//设置群聊列表点击事件
        initSearchField();//初始化搜索好友文本框
        initScene();//设置初始时界面显示
        initTab();
        initAddGroupAvatar();
        initChangeIdentity();
        groupAvatarFlowPane.setVisible(false);
        initCurrentChat();
        // 添加其他控件的事件监听器等
    }

    /**
     * @Description: 初始化所有按钮部件，设置点击时的事件
     */
    public void initButton() {
        sendButton.setOnAction(event -> {
            send(sendMessage.getText());
            sendMessage.clear();
        });
        createGroupButton.setOnAction(event -> {
            String name = createGroupName.getText();
            String account = createGroupAccount.getText();
            System.out.println("gName:"+name+" gAccount:"+account);
            System.out.println("gAvatar:"+newGroupAvatarPath);
            if(!account.isEmpty() && !name.isEmpty() && MyUtil.judgeGroupAccount(account)){
                //TODO 创建群聊 被选中的好友：selectedUserInfo  群头像路径： newGroupAvatarPath
                client.buildGroup(account,name,newGroupAvatarPath,selectedUserInfo);
                System.out.println("创建成功");
                clearAddGroupFriends();
                initCreateGroup();
            }
            else {
                if(name.isEmpty()){
                    throwError("请设置群名");
                }
                else if(account.isEmpty()){
                    throwError("请设置群号");
                }
                else if(!MyUtil.judgeGroupAccount(account)){
                    throwError("群号不正确");
                }
            }
        });
        changeIdentityButton.setOnAction(event -> {
            //更改头像 newAvatarPath
            if(newAvatarPath!=null && !newAvatarPath.isEmpty()){
                Image image = new Image(newAvatarPath);
                client.modifyUserAvatar(newAvatarPath);
                AvatarShow.setImage(image);
            }
            String newName = newNameField.getText();
            newNameField.clear();
            String newPassword = newPasswordField.getText();
            newPasswordField.clear();
            if(!newName.isEmpty()){
                //更改名字
                client.modifyUserName(newName);
                onlineUserName.setText(newName);
            }
            if(!newPassword.isEmpty() && MyUtil.judgePassword(newPassword)){
                //TODO 更改密码
                System.out.println("newPassword: "+newPassword+" hh");
                client.changePassword(newPassword);
            }
        });
        changeStyleButton.setOnAction(event -> {
            if(Style==0){
                chatAppClient.setStyle(1);
                chatAppClient.initWhite();
            }
            else{
                chatAppClient.setStyle(0);
                chatAppClient.initDark();
            }
        });
    }

    /**
     * @Description: 初始化聊天输入框
     */
    void initMessageArea(){
        sendMessage.setOnKeyPressed(event -> {
            // 如果按下的是回车键（KeyCode.ENTER）
            if (event.getCode().getName().equals("Enter")) {
                send(sendMessage.getText().replaceAll("[\r\n]", ""));
                sendMessage.clear(); // 清空 TextArea 内容
            }
        });
        sendMessage.setEditable(false);
        createGroupAccount.setPromptText("5位数字");
    }

    /**
     *  @Description: 初始化好友列表点击事件
     */
    void initFriendList() {
        friendContextMenu = new ContextMenu();
        initFriendContextMenu();
        friendListView.setContextMenu(friendContextMenu);
        friendListView.setOnMouseClicked(event -> {
            UserInfo selectedUser = friendListView.getSelectionModel().getSelectedItem();
            //处理新的好友事件
            if (selectedUser != null) {
                if (selectedUser.account.equals("newFriend")) {
                    initNewFriends(client.getUser().getRequests());
                    newFriendScene.setVisible(true);
                    changeIdentityScene.setVisible(false);
                    friendContextMenu.hide();
                } else {
                    chooseFriend = selectedUser;
                    sendToObjectName.setText(selectedUser.name);
                    changeIdentityScene.setVisible(false);
                    sendMessage.setEditable(true);
                    newFriendScene.setVisible(false);
                    ObjectAccount = selectedUser.account;
                    ArrayList<ChatInfo> chatInfos = onlineUser.getMessagesF().get(ObjectAccount);
                    initChat(chatInfos);
                    System.out.println("Selected Item: " + selectedUser.account + " " + selectedUser.name);
                }
            }
        });
        friendListView.setOnContextMenuRequested(event -> {
            // 检查所选对象
            UserInfo selectedItem = friendListView.getSelectionModel().getSelectedItem();
            if (selectedItem != null && selectedItem.account.equals("newFriend")) {
                friendContextMenu.hide();
                event.consume(); // 消耗事件，防止显示 ContextMenu
            }
        });
    }

    /**
     * @Description: 初始化右键点击好友列表时显示的菜单栏
     */
    public void initFriendContextMenu(){
        MenuItem deleteItem = new MenuItem("删除好友");
        friendContextMenu.getItems().add(deleteItem);
        deleteItem.setOnAction(event -> {
            //TODO 删除好友 ：chooseFriend

            System.out.println("删除该好友:"+chooseFriend.account+" "+chooseFriend.name);
            chatAppClient.updateFriendList();
        });
    }
    /**
     * @Description: 初始化群聊列表点击事件
     */
    void initGroupView(){
        groupListView.setOnMouseClicked(event -> {
            GroupInfo selectedGroup = groupListView.getSelectionModel().getSelectedItem();
            if (selectedGroup!=null){
                sendToObjectName.setText(selectedGroup.name);
                sendMessage.setEditable(true);
                ObjectAccount = selectedGroup.account;
                System.out.println(ObjectAccount);
                ArrayList<ChatInfo> chatInfos = onlineUser.getMessagesG().get(ObjectAccount);
                initChat(chatInfos);
                System.out.println("Selected Item: " + selectedGroup.account+" "+selectedGroup.name);
            }
            // 执行你想要的操作
        });
    }

    /**
     * @Description: 初始化修改个人信息界面
     */
    void initChangeIdentity(){
        AvatarShow.setOnMouseClicked(event -> {
            newAvatar.setImage(AvatarShow.getImage());
            newFriendScene.setVisible(false);
            addGroupScene.setVisible(false);
            changeIdentityScene.setVisible(true);
        });
        String AvatarPath = "src/com/BUAAChat/image/AvatarImage";
        List<File> imageFiles = getImagesFromFolder(new File(AvatarPath));
        for (File file : imageFiles) {
            Image image = new Image(file.toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            // 添加点击事件监听器
            imageView.setOnMouseClicked(newEvent -> {
                newAvatar.setImage(image);
                newAvatarPath = "com/BUAAChat/image/AvatarImage/"+file.getName();
                AvatarFlowPane.setVisible(false);
            });
            AvatarFlowPane.getChildren().add(imageView);
        }
        newAvatar.setOnMouseClicked(event -> {
            AvatarFlowPane.setVisible(true);
        });
    }

    /**
     *  @Description: 初始化搜索好友输入栏的相关事件
     */
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
        });
        //initSearchFriendListView();
        searchFriendListView.focusedProperty().addListener((observable, oldValue, newValue) -> {
            // ListView 失去焦点时执行的操作
            searchListScene.setVisible(newValue);
        });
    }

    /**
     *  初始化可见界面
     */
    void initScene(){
        searchListScene.setVisible(false);
        addGroupScene.setVisible(false);
        newFriendScene.setVisible(false);
        changeIdentityScene.setVisible(false);
    }

    /**
     * @param message 用户输入的聊天消息
     * @Description: 发送当前用户输入的消息
     */
    void send(String message) {
        if (message.isEmpty()) return;
        client.getSender().sendText(ObjectAccount,message);
        updateOnlineUserMessage(message);
    }

    /**
     * @param s 搜索好友时输入的字符串
     * @Description: 根据用户输入的字符串获得对应好友
     */
    void searchFriend(String s){
        ArrayList<UserInfo> users=client.searchUser(s);
        getSearchFriendListView(users);
    }

    /**
     * @param chatAppClient 控制的主应用程序
     * @Description: 指向应用程序
     */
    public void setChatAppClient(ChatAppClient chatAppClient){
        this.chatAppClient = chatAppClient;
    }

    /**
     * @param user 当前在线用户
     * @Description 指向当前在线用户
     */
    public void initUser(User user){
        this.onlineUser = user;
        onlineUserName.setText(user.getName());
        Image AvatarImage = new Image(user.getAvatarPath());
        AvatarShow.setImage(AvatarImage);
    }

    /**
     * @author 符观集
     * @date 2023/12/8
     * @Description: 设置好友列表的展示样式
     */
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

    /**
     * @author 符观集
     * @date 2023/12/19
     * @Description: 设置群聊列表的展示样式
     */
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

    /**
     * @author 符观集
     * @date 2023/12/19
     * @Description: 设置创建群聊时好友列表的展示样式
     */
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

        /**
         *  清空选中好友
         */
        public void clear(){
            if (checkBox.isSelected()){
                checkBox.setSelected(false);
            }
        }
    }

    /**
     * @param friends 好友列表
     * @Description: 初始化/更新 好友列表
     */
    public void initFriends(ArrayList<UserInfo> friends){
        if(friends==null)return;
        friendListView.getItems().clear();
        UserInfo newFriend = new UserInfo("newFriend","新的好友","com/BUAAChat/image/Controller/newFriend.png");
        friendListView.getItems().add(newFriend);
        for (UserInfo userInfo : friends) {
            friendListView.getItems().add(userInfo);
        }
        // 设置列表的单元格工厂，以便自定义单元格显示内容
        friendListView.setCellFactory(param -> new FriendListCell<UserInfo>());
    }

    /**
     * @param groups 群聊列表
     * @Description: 初始化/更新 群聊列表
     */
    public void initGroups(ArrayList<GroupInfo> groups){
        if (groups==null)return;
        groupListView.getItems().clear();
        for (GroupInfo groupInfo : groups) {
            groupListView.getItems().add(groupInfo);
        }
        // 设置列表的单元格工厂，以便自定义单元格显示内容
        groupListView.setCellFactory(param -> new GroupListCell<GroupInfo>());
    }

    /**
     * @param friends   好友列表
     * @Description: 初始化/更新 创建群聊时的好友列表
     */
    public void initAddGroup(ArrayList<UserInfo> friends){
        if(friends==null)return;
        addGroupListView.getItems().clear();
        for (UserInfo userInfo : friends) {
            if (!userInfo.account.equals("newFriend")) addGroupListView.getItems().add(userInfo);
        }
        // 设置列表的单元格工厂，以便自定义单元格显示内容
        addGroupListView.setCellFactory(param -> new addGroupListCell<>());
    }

    /**
     * @Description: 初始化所有Tab
     */
    public void initTab(){
        chooseAddGroupTab.setOnSelectionChanged(event -> {
            if(chooseAddGroupTab.isSelected()){
                addGroupScene.setVisible(true);
                newFriendScene.setVisible(false);
                ChatScene.setVisible(false);
                searchListScene.setVisible(false);
                changeIdentityScene.setVisible(false);
                initCreateGroup();
            }

        });
        chooseFriendTab.setOnSelectionChanged(event -> {
            if(chooseFriendTab.isSelected()){
                addGroupScene.setVisible(false);
                ChatScene.setVisible(true);
                searchListScene.setVisible(false);
                changeIdentityScene.setVisible(false);
            }
        });
        chooseGroupTab.setOnSelectionChanged(event -> {
            if(chooseGroupTab.isSelected()) {
                client.getSender().getAllGroupsInfoRequest(onlineUser.getAccount());
                addGroupScene.setVisible(false);
                newFriendScene.setVisible(false);
                ChatScene.setVisible(true);
                searchListScene.setVisible(false);
                changeIdentityScene.setVisible(false);
            }
        });
    }

    /**
     * @Description: 初始化创建群聊界面
     */
    public void initCreateGroup(){
        Image firstImage = new Image("com/BUAAChat/image/GroupImage/7.png");
        createGroupAvatar.setImage(firstImage);
        createGroupName.clear();
        createGroupAccount.clear();
        selectedUserInfo.clear();
    }

    /**
     * @Description: 初始化可获得的所有群聊头像（用于创建群聊）
     */
    public void initAddGroupAvatar() {
        // 获取特定文件夹内的所有图片
        String groupAvatarPath = "src/com/BUAAChat/image/GroupImage";
        newGroupAvatarPath = "com/BUAAChat/image/GroupImage/7.png";
        List<File> imageFiles = getImagesFromFolder(new File(groupAvatarPath));
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
                    newGroupAvatarPath = "com/BUAAChat/image/GroupImage/"+selectedFile.getName();
                }
            });
            groupAvatarFlowPane.getChildren().add(imageView);
        }
        createGroupAvatar.setOnMouseClicked(event -> {
            groupAvatarFlowPane.setVisible(true);
        });
    }

    /**
     * @param folder    文件路径
     * @return {@link List}<{@link File}> 获取到的所有图片文件
     */
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

    /**
     * @param file 判断的文件
     * @return boolean
     * @Description: 判断文件是否为图片
     */
    private boolean isImage(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") ||
                fileName.endsWith(".png") || fileName.endsWith(".gif") ||
                fileName.endsWith(".bmp");
    }

    /**
     * @Description: 清空当前聊天界面
     */
    public void clearCurrentChat(){
        currentChatVbox.getChildren().clear();
    }

    /**
     * @Description: 清空所有选中的好友，更改状态为未选中
     */
    public void clearAddGroupFriends() {
        selectedUserInfo.clear();
        for (Node node : addGroupListView.lookupAll(".list-cell")) {
            if (node instanceof addGroupListCell) {
                addGroupListCell<UserInfo> cell = (addGroupListCell<UserInfo>) node;
                cell.clear();
            }
        }
    }

    /**
     * @Description: 初始化聊天界面
     */
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

    /**
     * @param message   更新当前用户输入的消息
     * @Description:    将当前用户输入的消息呈现出来
     */
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

    /**
     * @param otherUser     其他用户
     * @param message       其他用户发的消息
     * @Description:        更新其他用户传来的信息并呈现出来
     */
    public void updateOtherUserMessage(UserInfo otherUser,String message){
        HBox content = new HBox();
        content.setSpacing(10);
        content.setAlignment(Pos.CENTER);
        VBox vBox = new VBox();
        Label name = new Label(otherUser.name);
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
        vBox.getChildren().addAll(name,textPane);
        content.getChildren().add(vBox);
        content.setAlignment(Pos.CENTER_LEFT);
        currentChatVbox.getChildren().add(content);
        chatAppClient.getPrimaryStage().show();
        currentChat.setVvalue(1.0);
    }

    /**
     * @param newFriends 好友申请
     * @Description: 初始化/更新 好友申请
     */
    public void initNewFriends(ArrayList<RequestInfo> newFriends){
        newFriendList.getItems().clear();
        if(newFriends==null) return;
        for (RequestInfo requestInfo : newFriends) {
            if(!requestInfo.from.equals(onlineUser.getAccount())) newFriendList.getItems().add(requestInfo);
        }
        // 设置列表的单元格工厂，以便自定义单元格显示内容
        newFriendList.setCellFactory(param -> new newFriendListCell<RequestInfo>());
    }

    /**
     * @author 符观集
     * @date 2023/12/17
     * @Description: 设置好友申请呈现的样式
     */
    class newFriendListCell<T extends RequestInfo> extends ListCell<T> {
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
                rightHbox.setMaxHeight(40);
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
                        client.getSender().sendRequestFeedback(item.from,item.name, item.avatarPath, true);
                        item.type = 1;
                        Type.setText("已接受");
                        rightHbox.getChildren().clear();
                        content.getChildren().add(Type);
                        updateItem(item,false);
                    });
                    reject.setOnAction(event -> {
                        client.getSender().sendRequestFeedback(item.from,item.name, item.avatarPath, false);
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
                setStyle("-fx-control-inner-background: rgba(255, 255, 255, 0.55);");
            }
            newFriendList.requestLayout();
        }
        // 可以添加其他方法和处理逻辑
    }

    /**
     * @param friends 搜索到的好友信息列表
     * @Description: 更新搜索到的好友列表
     */
    public void getSearchFriendListView(ArrayList<UserInfo> friends){
        searchFriendListView.getItems().clear();
        if(friends==null) return;
        for (UserInfo userInfo : friends) {
            searchFriendListView.getItems().add(userInfo);
        }
        // 设置列表的单元格工厂，以便自定义单元格显示内容
        searchFriendListView.setCellFactory(param -> new searchFriendListCell<UserInfo>());
    }

    /**
     * @author 符观集
     * @date 2023/12/17
     * @Description: 设置搜索到的好友展示的样式
     */
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
                for (UserInfo userInfo : friends) {
                    if (userInfo.account.equals(item.account)) {
                        type = 1;
                        break;
                    }
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
                        client.getSender().addFriendRequest(item.account, item.name,item.avatarPath);
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

    /**
     * @param chatInfos 聊天消息
     * @Description: 初始化聊天消息
     */
    public void initChat(ArrayList<ChatInfo>chatInfos){
        currentChatVbox.getChildren().clear();
        if(chatInfos==null) return;
        for (ChatInfo chatInfo : chatInfos) {
            if (chatInfo.fromUser.account.equals(onlineUser.getAccount())) {
                updateOnlineUserMessage(chatInfo.content);
            } else {
                updateOtherUserMessage(chatInfo.fromUser, chatInfo.content);
            }
        }
    }

    /**
     * @param chatInfo 其他用户发送的聊天消息
     * @Description: 判断发来的消息是否为当前聊天对象并决定是否呈现
     */
    public void updateChatObject(ChatInfo chatInfo){
        if(chatInfo==null) return;
        if(ObjectAccount.equals(chatInfo.fromUser.account)){
            updateOtherUserMessage(chatInfo.fromUser,chatInfo.content);
        }
    }

    /**
     * @param chatInfo  其他用户在群聊中发送的消息
     * @param account   群聊账号
     * @Description:    判断群聊是否为当前聊天对象并决定是否呈现聊天消息
     */
    public void updateChatObject(ChatInfo chatInfo,String account){
        if(chatInfo==null || account==null) return;
        if(ObjectAccount.equals(account)){
            updateOtherUserMessage(chatInfo.fromUser,chatInfo.content);
        }
    }

    /**
     * @param string    创建群聊失败的具体错误信息
     * @Description:    向用户反馈创建群聊失败的具体信息
     */
    public void throwError(String string){

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("创建失败");
        alert.setContentText(string);
        alert.showAndWait();

    }

    /**
     * @param style 设置当前部件所对应的主题
     */
    public void setStyle(int style) {
        Style = style;
    }
}