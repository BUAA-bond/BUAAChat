package MyUtil;
import Message.*;
import Client.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Random;

import static Constant.Constant.*;

/**
 * @author 西西弗
 * @Description:
 * @date 2023/11/9 19:24
 */
@SuppressWarnings({"all"})
public class MyUtil {
    public static int AllID=-1;
    public static void main(String[] args) {
        System.out.println(judgeAccount("111"));
    }

    /**
     * 判断账号是否合法
     * @param account
     * @return
     */
    public static boolean judgeAccount(String account){
        String account_pattern="^\\d{6,10}$";
        return account.matches(account_pattern);
    }
    /**
     * 判断密码是否合法
     * 要求：8-16位，至少含有一位数字和一位英文字符，不含中文
     * @param pw
     * @return
     */
    public static boolean judgePassword(String pw){
        String pw_pattern="^(?=.*[0-9])(?=.*[a-zA-Z])[\\x21-\\x7E]{8,16}$";
        return pw.matches(pw_pattern);
    }

    /**
     *判断昵称是否合法
     * 要求：1-15位，可以包含任意字符
     * @param name
     * @return
     */
    public static boolean judgeName(String name){
        String name_pattern="^.{1,10}$";
        return name.matches(name_pattern);
    }

    /**
     * 判断密码和确认密码是否一致
     * @param password
     * @param password2
     * @return
     */
    public static boolean confirmPassword(String password,String password2){
        return password.equals(password2);
    }



    /**
     * 连接数据库
     * @return
     * @throws SQLException
     */
    public static Connection getConnection(/*String jdbcUrl, String username, String password*/){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(jdbcUrl, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 判断某个表是否已经创建过
     * @param tableName
     * @return
     */
    public static boolean isTableExist(String tableName) {
        try (Connection connection = getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            // 获取数据库中的表信息//TODO
            ResultSet tables = metaData.getTables("BUAAChat", null, tableName, null);
            return tables.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // 处理异常，返回false表示发生错误或表不存在
        }
    }

    /**
     * 创建初始的users表
     * @throws SQLException
     */
    public static void createUsersTable(){
        Connection connection=null;
        Statement statement=null;
        DatabaseMetaData metaData=null;
        if (isTableExist("users")) return;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            metaData = connection.getMetaData();
            //创建表
            String createTable = "CREATE TABLE " + "users" + " (" +
                    //"sequence INT AUTO_INCREMENT PRIMARY KEY,"+
                    "account VARCHAR(15) PRIMARY KEY," +
                    "name VARCHAR(15)," +
                    "password VARCHAR(255)," +
                    "avatarPath VARCHAR(255)," +
                    "salt VARCHAR(255)"+
                    ");";
            statement.executeUpdate(createTable);
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            //关闭资源
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * generate a random salt string (HEX).
     * @param len length of salt
     * @return salt string
     */
    public static String randomSalt(int len){

        if( len <= 0 ) return "";

        Random rand = new Random();
        StringBuilder salt = new StringBuilder();
        for( int i=0; i<len; i++ ){
            salt.append( hexCharSet.charAt(rand.nextInt(16)) );
        }

        return salt.toString();
    }

    /**
     * append salt to string.
     * @param str string to append salt
     * @param len length of salt
     * @return result string
     */
    public static String appendSalt(String str,int len){
        if( str == null ) return null;
        return str + randomSalt(len);
    }

    /**
     * parse byte[] to hex string.
     * @param by byte[] to be parsed
     * @return result hex string
     */
    public static String byteToHex(byte[] by){
        StringBuffer sb = new StringBuffer();
        for (byte b : by) {
            String str = Integer.toHexString(b & 0xFF).toUpperCase();
            if(str.length()<2) sb.append('0');
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * to append salt and encrypt using SHA-256.
     * @param str string to encrypt
     * @param salt salt
     * @return encrypted string.
     */
    public static String encryptString(String str,String salt){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        String str_salt = str + salt ;
        String res = byteToHex( md.digest(str_salt.getBytes()) );

        return res;
    }
    /**
     * 将新用户保存到数据库
     * @param user
     * @throws SQLException
     */
    public static void insertUser(String account,String name,String password,String avatarPath){
        Connection connection=null;
        PreparedStatement insertStatement=null;
        ResultSet rs=null;
        try {
            connection = getConnection();
            String insertQuery = "INSERT INTO users (account, name, password, avatarPath, salt) VALUES (?,?, ?, ?, ?)";
            String salt=randomSalt(password.length());//生成盐
            password=encryptString(password, salt);//加密
            insertStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            insertStatement.setString(1,account);
            insertStatement.setString(2, name);
            insertStatement.setString(3, password);
            insertStatement.setString(4, avatarPath);
            insertStatement.setString(5, salt);
            insertStatement.executeUpdate();

            //创建他的朋友表
            createFriendTable(account);
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            //关闭资源
            try {
                if (insertStatement != null)
                    insertStatement.close();
                if (connection != null)
                    connection.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建一个用户的朋友表 用户account_friends，里面装的是他的所有朋友的account和名字和头像地址
     * @param ownerAccount
     * @throws SQLException
     */
    public static void createFriendTable(String ownerAccount){
        String tableName=ownerAccount+"_friends";
        if(isTableExist(tableName))return ;
        Connection connection=null;
        Statement statement=null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            //创建表
            String createTable = "CREATE TABLE " + tableName + " (" +
                    "account VARCHAR(15) PRIMARY KEY," +
                    "name VARCHAR(15)," +
                    "avatarPath VARCHAR(255)" +
                    ");";
            statement.executeUpdate(createTable);
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            //关闭资源
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建一个用户的信息表 from_用户account_to_朋友account，里面装的是和某个朋友的所有聊天记录
     * @param fromAccount
     * @param toAccount
     * @throws SQLException
     */
    public static void createMessageTable(String fromAccount ,String toAccount){
        String tableName="from_"+fromAccount+"_to_"+toAccount;
        if(isTableExist(tableName)) return;
        Connection connection=null;
        Statement statement=null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            //创建表
            String createTable = "CREATE TABLE " + tableName + " (" +
                    "sequence INT AUTO_INCREMENT PRIMARY KEY," +
                    "content VARCHAR(255)," +
                    "type INT"+
                    ");";
            statement.executeUpdate(createTable);
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                //关闭资源
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }
    /**
     * 将朋友的account和名字加入用户的好友表里
     * @param ownerAccount
     * @param friendAccount
     * @param friendName
     * @throws SQLException
     */
    public static void insertFriend(String ownerAccount,String friendAccount,String friendName,String avatarPath){
        Connection connection=null;
        PreparedStatement insertStatement=null;
        try {
            connection = getConnection();
            //插入
            String insertQuery = "INSERT INTO " + ownerAccount + "_friends" + " (account, name, avatarPath) VALUES (?, ?, ?)";
            insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setString(1, friendAccount);
            insertStatement.setString(2, friendName);
            insertStatement.setString(3, avatarPath);
            insertStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                //关闭资源
                if (insertStatement != null)
                    insertStatement.close();
                if (connection != null)
                    connection.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存用户与好友的聊天记录
     * @param message
     * @throws SQLException
     */
    public static void insertMessage(Message message){
        String fromAccount=message.getFrom();
        String toAccount=message.getTo();
        int type=0;
//        if(message instanceof Text) type=0;
//        else if(message instanceof Photo) type=1;

        String content=message.getData();
        Connection connection=null;
        PreparedStatement insertStatement=null;
        try {
            connection = getConnection();
            String insertQuery = "INSERT INTO " + "from_" + fromAccount + "_to_" + toAccount + " (content, type) VALUES (?, ?)";
            insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setString(1, content);
            insertStatement.setInt(2, type);
            insertStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                if (insertStatement != null)
                    insertStatement.close();
                if (connection != null)
                    connection.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }

    }

    /**
     * 注册
     * @param name
     * @param password
     * @return
     */
    public static void register(String account,String name,String password,String avatarPath){
        insertUser(account,name,password,avatarPath);
    }
    /**
     * 判断用户是否已经注册
     * @param account
     * @return
     * @throws SQLException
     */
    public static boolean queryUser(String account){
        String selectQuery = "SELECT account, name, password, avatarPath FROM users WHERE account = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
        ) {
            preparedStatement.setString(1, account);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 校验登录用户的账号和密码是否对应
     * @param account
     * @param password
     * @return
     * @throws SQLException
     */
    public static boolean confirmAccountAndPassword(String account,String password){
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        try {
            connection = getConnection();
            String selectQuery = "SELECT account, password, salt FROM users WHERE account = ?";
            preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, account);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                String sqlpassword = resultSet.getString("password");
                String sqlsalt=resultSet.getString("salt");
                return sqlpassword.equals(encryptString(password,sqlsalt));
                //return sqlpassword.equals(password);
            }
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (preparedStatement != null)
                    preparedStatement.close();
                if (connection != null)
                    connection.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 登录
     * @param account
     * @param password
     * @return User返回一个登录的user对象
     * @throws SQLException
     */
    public static User logIn(String account,String password){
        try {
            //需要先判断账号密码是否正确
            Connection connection = getConnection();
            String selectSQL = "SELECT account, name, password, avatarPath FROM users WHERE account = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, account);
            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.getResultSet();
            if (resultSet.next()) {
                return new User(account, resultSet.getString("name"), password, resultSet.getString("avatarPath"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}

