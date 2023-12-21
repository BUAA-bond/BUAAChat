package com.BUAAChat.MyUtil;
/**
 * @author 西西弗
 * @Description: 工具类
 * @date 2023/11/9 19:24
 */
@SuppressWarnings({"all"})
public class MyUtil {
    /**
     * 用来测试
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(judgeAccount("111"));
    }

    /**
     * 判断账号是否合法
     * @param account
     * @return boolean
     */
    public static boolean judgeAccount(String account) {
        String account_pattern = "^\\d{6,10}$";
        return account.matches(account_pattern);
    }

    /**
     * 判读群账号是否合法
     * @param account
     * @return boolean
     */
    public static boolean judgeGroupAccount(String account) {
        String account_pattern = "^\\d{5}$";
        return account.matches(account_pattern);
    }

    /**
     * 判断密码是否合法
     * 要求：8-16位，至少含有一位数字和一位英文字符，不含中文
     * @param pw
     * @return boolean
     */
    public static boolean judgePassword(String pw) {
        String pw_pattern = "^(?=.*[0-9])(?=.*[a-zA-Z])[\\x21-\\x7E]{8,16}$";
        return pw.matches(pw_pattern);
    }

    /**
     * 判断昵称是否合法
     * 要求：1-15位，可以包含任意字符
     * @param name
     * @return boolean
     */
    public static boolean judgeName(String name) {
        String name_pattern = "^.{1,10}$";
        return name.matches(name_pattern);
    }

    /**
     * 判断密码和确认密码是否一致
     * @param password
     * @param password2
     * @return boolean
     */
    public static boolean confirmPassword(String password, String password2) {
        return password.equals(password2);
    }
}
