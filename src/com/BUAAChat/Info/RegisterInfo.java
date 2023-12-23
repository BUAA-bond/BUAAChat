package com.BUAAChat.Info;

import com.BUAAChat.MyUtil.MyUtil;

/**
 * 用于判断注册信息格式的类
 * @author 西西弗
 * @date 2023/11/23 18:07
 */
public class RegisterInfo {
    /**
     *注册用户密码
     */
    private String password;

    /**
     *注册用户账号
     */
    private String account;

    /**
     *注册用户名字
     */
    private String name;

    /**
     *确认密码
     */
    private String passwordAgain;

    /**
     *注册用户头像路径
     */
    private String avatarPath;

    /**
     * 判断一系列的注册信息格式、是否为空
     * @return {@link String}
     */
    public String judgeAndRegister(){
        if(account==null){
            return "请输入账号";
        }if(name==null){
            return "请输入昵称";
        }else if(password==null){
            return"请输入密码";
        }else if(passwordAgain==null){
            return "请输入确认密码";
        }else if(!MyUtil.judgeAccount(account)){
            return "账号格式不正确";
        }else if(!MyUtil.judgeName(name)){
            return "名字格式不正确";
        }else if(!MyUtil.judgePassword(password)){
            return "密码格式不正确";
        }else if(!MyUtil.confirmPassword(password,passwordAgain)){
            return "确认密码不正确";
        }else{
            return "正确";
        }
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPasswordAgain(String passwordAgain) {
        this.passwordAgain = passwordAgain;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordAgain() {
        return passwordAgain;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccount() {
        return account;
    }
}
