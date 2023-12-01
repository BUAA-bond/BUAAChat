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
    public boolean judgeAndRegister(){
        if(account==null){
            account=password=name=passwordAgain=null;
            System.out.println("请输入账号");
        }if(name==null){
            //重置
            account=password=name=passwordAgain=null;
            //输出信息
            System.out.println("请输入昵称");
        }else if(password==null){
            account=password=name=passwordAgain=null;
            System.out.println("请输入密码");
        }else if(passwordAgain==null){
            account=password=name=passwordAgain=null;
            System.out.println("请输入确认密码");
        }else if(!MyUtil.judgeAccount(account)){
            account=password=name=passwordAgain=null;
            System.out.println("账号格式不正确");
        }else if(!MyUtil.judgeName(name)){
            account=password=name=passwordAgain=null;
            System.out.println("名字格式不正确");
        }else if(!MyUtil.judgePassword(password)){
            account=password=name=passwordAgain=null;
            System.out.println("密码格式不正确");
        }else if(!MyUtil.confirmPassword(password,passwordAgain)){
            account=password=name=passwordAgain=null;
            System.out.println("确认密码不正确");
        }else{
            MyUtil.register(account,name,password,"");
            System.out.println("注册成功");
            //TODO
            return true;
        }
        return false;
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
