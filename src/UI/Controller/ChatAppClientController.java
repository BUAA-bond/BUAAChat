package UI.Controller;

import UI.ChatAppClient;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextFlow;
import static Constant.Constant.client;
public class ChatAppClientController{
    private ChatAppClient chatAppClient;

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
                send(sendMessage.getText().replaceAll("[\r\n]", ""));
                sendMessage.clear(); // 清空 TextArea 内容
            }
        });
        changeStyleButton.setOnAction(event -> {
                chatAppClient.changeDarkStyle();
        });
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
    public void setChatAppClient(ChatAppClient chatAppClient){
        this.chatAppClient = chatAppClient;
    }
    // 可以添加其他方法和处理逻辑
}