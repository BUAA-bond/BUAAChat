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
    }
    public String getMessage(){
        return field.getText();
    }
}
