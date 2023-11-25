package MyUtil;
import ClientMember.Message;
import ClientMember.Photo;
import ClientMember.Text;
import ClientMember.User;

import java.sql.*;
/**
 * @author 西西弗
 * @Description:
 * @date 2023/11/9 19:24
 */
@SuppressWarnings({"all"})
public class MyUtil {
    public static int AllID=-1;
    public static void main(String[] args) {
        createUsersTable();
    }
    public static boolean judgeAccount(String account){
        String account_pattern="^1\\d{5}$";
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
        String name_pattern="^.{1,15}$";
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


    static final String jdbcUrl="jdbc:mysql://182.92.202.183:3306/BUAAChat";
    static final String username="root";
    static final String password="Buaachat123";
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
            System.out.println("MySQL JDBC Driver not found");
            return null;
        }catch (SQLException e){
            System.out.println("getConnection");
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
            // 获取数据库中的表信息
            ResultSet tables = metaData.getTables("buaachat", null, tableName, null);
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
                    "account INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(15)," +
                    "password VARCHAR(16)," +
                    "avatarPath VARCHAR(255)" +
                    ")AUTO_INCREMENT = 100001;";
            statement.executeUpdate(createTable);
        }catch (SQLException e){
            System.out.println("createUsersTable");
        }finally {
            //关闭资源
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            }catch (SQLException e){
                System.out.println("createUsersTable close");
            }
        }
    }
    /**
     * 将新用户保存到数据库
     * @param user
     * @throws SQLException
     */
    public static int insertUser(String name,String password,String avatarPath){
        Connection connection=null;
        PreparedStatement insertStatement=null;
        ResultSet rs=null;
        int account = 0;
        try {
            connection = getConnection();
            String insertQuery = "INSERT INTO users (name, password, avatarPath) VALUES (?, ?, ?)";
            insertStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            insertStatement.setString(1, name);
            insertStatement.setString(2, password);
            insertStatement.setString(3, avatarPath);
            insertStatement.executeUpdate();

            rs = insertStatement.getGeneratedKeys();
            if (rs.next()) account = rs.getInt(1);
            //创建他的朋友表
            createFriendTable(account);
        }catch (SQLException e){
            System.out.println("insertUser");
        }finally {
            //关闭资源
            try {
                if (insertStatement != null)
                    insertStatement.close();
                if (connection != null)
                    connection.close();
            }catch (SQLException e){
                System.out.println("insertUser close");
            }
        }
        return account;
    }

    /**
     * 创建一个用户的朋友表 用户account_friends，里面装的是他的所有朋友的account和名字和头像地址
     * @param ownerAccount
     * @throws SQLException
     */
    public static void createFriendTable(int ownerAccount){
        String tableName=ownerAccount+"_friends";
        if(isTableExist(tableName))return ;
        Connection connection=null;
        Statement statement=null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            //创建表
            String createTable = "CREATE TABLE " + tableName + " (" +
                    "account INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(15)," +
                    "avatarPath VARCHAR(255)" +
                    ");";
            statement.executeUpdate(createTable);
        }catch (SQLException e){
            System.out.println("createFriendTable");
        }finally {
            //关闭资源
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            }catch (SQLException e){
                System.out.println("createFriendTable close");
            }
        }
    }

    /**
     * 创建一个用户的信息表 from_用户account_to_朋友account，里面装的是和某个朋友的所有聊天记录
     * @param fromAccount
     * @param toAccount
     * @throws SQLException
     */
    public static void createMessageTable(int fromAccount ,int toAccount){
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
                    "type INT";
            statement.executeUpdate(createTable);
        }catch (SQLException e){
            System.out.println("createMessageTable");
        }finally {
            try {
                //关闭资源
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            }catch (SQLException e){
                System.out.println("createMessageTable close");
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
    public static void insertFriend(int ownerAccount,int friendAccount,String friendName,String avatarPath){
        Connection connection=null;
        PreparedStatement insertStatement=null;
        try {
            connection = getConnection();
            //插入
            String insertQuery = "INSERT INTO " + ownerAccount + "_friends" + " (account, name, avatarPath) VALUES (?, ?, ?)";
            insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setInt(1, friendAccount);
            insertStatement.setString(2, friendName);
            insertStatement.setString(3, avatarPath);
            insertStatement.executeUpdate();
        }catch (SQLException e){
            System.out.println("insertFriend");
        }finally {
            try {
                //关闭资源
                if (insertStatement != null)
                    insertStatement.close();
                if (connection != null)
                    connection.close();
            }catch (SQLException e){
                System.out.println("insertFriend close");
            }
        }
    }

    /**
     * 保存用户与好友的聊天记录
     * @param message
     * @throws SQLException
     */
    public static void insertMessage(Message message){
        int fromAccount=message.getFromUser();
        int toAccount=message.getToUser();
        int type=message.type;
        String content=message.getContent();
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
            System.out.println("insertMessage");
        }finally {
            try {
                if (insertStatement != null)
                    insertStatement.close();
                if (connection != null)
                    connection.close();
            }catch (SQLException e){
                System.out.println("insertMessage close");
            }
        }

    }

    /**
     * 注册
     * @param name
     * @param password
     * @return
     */
    public static int register(String name,String password,String avatarPath){
        return insertUser(name,password,avatarPath);
//        try {
//            int account=insertUser(name,password,avatarPath);
//            //TODO
//            return new User(account,name,password,avatarPath);
//        } catch (SQLException e) {
//            System.out.println("register");
//        }
//        return null;
    }
    /**
     * 判断用户是否已经注册
     * @param account
     * @return
     * @throws SQLException
     */
    public static boolean queryUser(int account){
        String selectQuery = "SELECT account, name, password, avatarPath FROM users WHERE account = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
        ) {
            preparedStatement.setInt(1, account);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }catch (SQLException e){
            System.out.println("queryUser");
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
    public static boolean confirmAccountAndPassword(int account,String password){
        //需要先判断存不存在
        //if(!queryUser(ID)) return false;//在最外面判断可以，这里只是标志性写一下，到时候删了
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        try {
            connection = getConnection();
            String selectQuery = "SELECT account, name, password, avatarPath FROM users WHERE account = ?";
            preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1, account);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                String tmppassword = resultSet.getString("password");
                return tmppassword.equals(password);
            }
        }catch (SQLException e){
            System.out.println("confirmAccountAndPassword");
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
                System.out.println("confirmAccountAndPassword close");
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
    public static User logIn(int account,String password){
        try {
            //需要先判断账号密码是否正确
            Connection connection = getConnection();
            String selectSQL = "SELECT account, name, password, avatarPath FROM users WHERE account = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setInt(1, account);
            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.getResultSet();
            if (resultSet.next()) {
                return new User(account, resultSet.getString("name"), password, resultSet.getString("avatarPath"));
            }
        }catch (SQLException e){
            System.out.println("logIn");
        }
        return null;
    }
}

