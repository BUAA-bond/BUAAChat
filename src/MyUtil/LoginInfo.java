package MyUtil;

import ClientMember.User;

import java.sql.SQLException;

/**
 * @author 西西弗
 * @Description:
 * @date 2023/11/23 17:54
 */
public class LoginInfo {
    private String account=null;
    private String password=null;
    public void judgeAndLogin(){
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
        }else if(!MyUtil.queryUser(Integer.parseInt(account))){
            password=account=null;
            System.out.println("用户未注册");
        }else if(!MyUtil.confirmAccountAndPassword(Integer.parseInt(account),password)){
            password=account=null;
            System.out.println("密码不正确");
        }else{
            User user=MyUtil.logIn(Integer.parseInt(account),password);
            System.out.println("登录成功，欢迎用户"+user.getName());
            //TODO
        }
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