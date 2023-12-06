package UI.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextFlow;

public class ChatAppClientController{
    private String contentStyle;

    @FXML
    private Tab chooseChatTab;

    @FXML
    private Tab chooseFriendTab;

    @FXML
    private Tab chooseGroupTab;

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
                send(sendMessage.getText());
                sendMessage.clear(); // 清空 TextArea 内容
            }
        });
        /*
        Image chooseChatImage = new Image("image/ChatClientImage/darkTheme/chatIcon.png");
        ImageView chooseChatImageView = new ImageView(chooseChatImage);
        chooseChatImageView.setFitWidth(40);
        chooseChatImageView.setFitHeight(40);
        chooseChatTab.setGraphic(chooseChatImageView);


        Image chooseFriendImage = new Image("image/ChatClientImage/darkTheme/friendIcon.png");
        ImageView chooseFriendImageView = new ImageView(chooseFriendImage);
        Image chooseFriendHoverImage = new Image("image/ChatClientImage/darkTheme/friendIconHovered.png");
        ImageView chooseFriendHoverImageView = new ImageView(chooseFriendHoverImage);
        setFit(chooseChatImageView,40,40);
        chooseFriendImageView.setFitWidth(40);
        chooseFriendImageView.setFitHeight(40);
        chooseFriendTab.setGraphic(chooseFriendImageView);



        Image chooseGroupImage = new Image("image/ChatClientImage/darkTheme/groupIcon.png");
        ImageView chooseGroupImageView = new ImageView(chooseGroupImage);
        chooseGroupImageView.setFitWidth(40);
        chooseGroupImageView.setFitHeight(40);
        chooseGroupTab.setGraphic(chooseGroupImageView);*/
        // 添加其他控件的事件监听器等
    }
    void send(String message)
    {
        System.out.println(message);
    }
    void setFit(ImageView imageView,double width,double height){
        imageView.setFitHeight(height);
        imageView.setFitWidth(width);
    }
    // 可以添加其他方法和处理逻辑
}