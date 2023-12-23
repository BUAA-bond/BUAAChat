package com.BUAAChat.UI.Button;

import javafx.scene.control.Button;
/**
 * 为方便开发自定义的Button类
 * @author 符观集
 * @date 2023/11/9 20:10
 */
public class MyButton extends Button {
    /**
     * @param text      Button的文本信息
     * @param x         Button的X坐标
     * @param y         Button的Y坐标
     * @param width     Button的宽度
     * @param height    Button的高度
     */
    public MyButton(String text,double x,double y,double width,double height){
        super(text);
        this.setLayoutX(x);
        this.setLayoutY(y);
        this.setPrefWidth(width);
        this.setPrefHeight(height);
    }

    /**
     * @param x         Button的X坐标
     * @param y         Button的Y坐标
     * @param width     Button的宽度
     * @param height    Button的高度
     */
    public MyButton(double x,double y,double width,double height){
        super();
        this.setLayoutX(x);
        this.setLayoutY(y);
        this.setPrefWidth(width);
        this.setPrefHeight(height);
    }
}
