package MyUtil;

/**
 * @author 西西弗
 * @Description:
 * @date 2023/11/23 18:07
 */
public class RegisterInfo {
    private String password;
    private String name;
    private String passwordAgain;
    private String avatarPath;
    public void judgeAndRegister(){
        if(name==null){
            //重置
            password=name=passwordAgain=null;
            //输出信息
            System.out.println("请输入账号");
        }else if(password==null){
            password=name=passwordAgain=null;
            System.out.println("请输入密码");
        }else if(passwordAgain==null){
            password=name=passwordAgain=null;
            System.out.println("请输入确认密码");
        }else if(!MyUtil.judgeName(name)){
            password=name=passwordAgain=null;
            System.out.println("名字格式不正确");
        }else if(!MyUtil.judgePassword(password)){
            password=name=passwordAgain=null;
            System.out.println("密码格式不正确");
        }else if(!MyUtil.confirmPassword(password,passwordAgain)){
            password=name=passwordAgain=null;
            System.out.println("确认密码不正确");
        }else{
            int account=MyUtil.register(name,password,"");
            System.out.println("您的账号是"+account);
            //TODO
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
}
