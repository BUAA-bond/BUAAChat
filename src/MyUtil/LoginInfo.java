package MyUtil;

import Client.User;
import MyUtil.MyUtil;

/**
 * @author 西西弗
 * @Description:
 * @date 2023/11/23 17:54
 */
public class LoginInfo {
    private String account=null;
    private String password=null;
    public boolean judgeAndLogin(){
        if(account==null){
            //重置
            password=null;
            //输出信息
            System.out.println("请输入账号");
        }else if(password==null){
            //重置
            account=null;
            //输出信息
            System.out.println("请输入密码");
        }else if(!MyUtil.judgeAccount(account)){
            password=account=null;
            System.out.println("账号格式不正确");
        }else if(!MyUtil.judgePassword(password)){
            password=account=null;
            System.out.println("密码格式不正确");
        }else if(!MyUtil.queryUser(account)){
            password=account=null;
            System.out.println("用户未注册");
        }else if(!MyUtil.confirmAccountAndPassword(account,password)){
            password=account=null;
            System.out.println("密码不正确");
        }else{
            return true;
            //new Thread(new ClientServer(user)).start();
            //TODO
        }
        return false;
    }
    public void setAccount(String account) {
        this.account = account;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }
}