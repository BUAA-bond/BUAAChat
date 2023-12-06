package UI;

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
import javafx.scene.control.Button;
public class ChatAppClient extends Application {
    private Stage primaryStage;
    private AnchorPane darkRootLayout;
    private AnchorPane whiteRootLayout;
    private ChatAppClientDarkController darkController;
    private ChatAppClientController whiteController;
    private  Scene darkScene;
    private  Scene whiteScene;
    Button sendButton;
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("缘深");
        this.primaryStage.getIcons().add(new Image("image/icon/icon_naxida.jpg"));
        initWhiteRootLayout();
        initDarkRootStyle();
        darkController.setChatAppClient(this);
        whiteController.setChatAppClient(this);
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
            primaryStage.setScene(darkScene);
            primaryStage.show();
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
