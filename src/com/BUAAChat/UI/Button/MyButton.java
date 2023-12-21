package com.BUAAChat.UI.Button;

import javafx.scene.control.Button;

public class MyButton extends Button {
    public MyButton(String text,double x,double y,double width,double height){
        super(text);
        this.setLayoutX(x);
        this.setLayoutY(y);
        this.setPrefWidth(width);
        this.setPrefHeight(height);
    }
    public MyButton(double x,double y,double width,double height){
        super();
        this.setLayoutX(x);
        this.setLayoutY(y);
        this.setPrefWidth(width);
        this.setPrefHeight(height);
    }
}
