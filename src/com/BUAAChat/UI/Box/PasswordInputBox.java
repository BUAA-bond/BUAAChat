package com.BUAAChat.UI.Box;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
/**
 * 为了便于UI设置自己定义的HBox，主要表现形式为label加PasswordField
 * @author 符观集
 * @date 2023/12/1 20:15
 */
public class PasswordInputBox extends HBox {
    /**
     *  PasswordInputBox标签
     */
    Label label;
    /**
     *  PasswordInputBox输入框
     */
    PasswordField field;
    /**
     *  更改label的风格
     * @param text    设置输入框的标签
     * @param x       设置位置
     * @param y       设置位置
     **/
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
        label.setStyle("""
                fx-font-size: 16px;
                    -fx-font-weight: bold;
                    -fx-text-fill: #d0dafc;
                    -fx-background-color: #447bd4;
                """);
    }
    /**
     *  更改label的风格
     * @param fx    自定义的fxStyle
     **/
    public void changeLabelColour(String fx){
        label.setStyle(fx);
    }
    /**
     *  返回输入框的内容
     * @return 输入的文本信息
     **/
    public String getMessage(){
        return field.getText();
    }
    /**
     *  设置输入框提示
     * @param hint    输入框提示内容
     **/
    public void setHint(String hint){
        field.setPromptText(hint);
    }
    /**
     *  返回field便于修改
     * @return field 该Hox的PasswordField
     **/
    public PasswordField getField() {
        return field;
    }
}
