package Box;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class PasswordInputBox extends HBox {
    Label label;
    PasswordField field;
    public PasswordInputBox(String text,double x,double y){
        super(10);//设置组件间距为10
        label = new Label(text);
        field = new PasswordField();//创建单行密码输入框
        field.setEditable(true);//设置输入框为可编辑
        this.getChildren().addAll(label,field);
        this.setAlignment(Pos.CENTER);//
        this.setLayoutX(x);
        this.setLayoutY(y);
        field.setStyle("-fx-opacity: 0.7;");
        label.setStyle("fx-font-size: 16px;\n" +
                "    -fx-font-weight: bold;\n" +
                "    -fx-text-fill: #d0dafc;\n" +
                "    -fx-background-color: #447bd4;\n");
    }
    public void changeLabelColour(String fx){
        label.setStyle(fx);
    }
    public String getMessage(){
        return field.getText();
    }
    public void setHint(String string){
        field.setPromptText(string);
    }

    public PasswordField getField() {
        return field;
    }
}
