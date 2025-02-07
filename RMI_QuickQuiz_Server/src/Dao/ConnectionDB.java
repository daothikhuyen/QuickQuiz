package Dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionDB {
    static String DB_URL = "jdbc:mysql://localhost:3306/quick_quiz";
    static String USER_NAME = "root";
    static String PASSWORD = "";
    
    // Kết nối chỉ được tạo một lần
    private static Connection con = null;

    // Phương thức lấy kết nối
    public static Connection getConnection() {
        try {
            // Kiểm tra nếu kết nối chưa tồn tại hoặc đã bị đóng, mới tạo kết nối
            if (con == null || con.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
//                System.out.println("Connect successfully!");
            }
        } catch (Exception ex) {
            System.out.println("Connect failure!");
            ex.printStackTrace();
        }
        return con;
    }
}

