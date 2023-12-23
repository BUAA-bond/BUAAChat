package com.BUAAChat.UI.MyInterface;

/**
 * @author 符观集
 * 将内容传递消息给Main函数的监听者
 * @date 2023/11/9 22:09
 */
public interface ButtonClickListener {
    /**
     * 传递信息
     * @param messages 传递的信息
     */
    void onLoginButtonClick(String[] messages);
}
