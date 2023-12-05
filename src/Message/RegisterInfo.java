package Message;

import MyUtil.MyUtil;

/**
 * @author 西西弗
 * @Description:
 * @date 2023/11/23 18:07
 */
public class RegisterInfo {
    private String password;
    private String account;
    private String name;
    private String passwordAgain;
    private String avatarPath;
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
