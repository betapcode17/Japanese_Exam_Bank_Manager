package database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtil {

    public static Connection getConnection() {
        Connection con = null;
        try {
          
            Class.forName("com.mysql.cj.jdbc.Driver");

          
            String url = "jdbc:mysql://127.0.0.1:3306/japaneseexambank?useSSL=false&serverTimezone=UTC";
            String user = "QuocDat";
            String password = "17122005";

           
            con = DriverManager.getConnection(url, user, password);
            System.out.println("Kết nối thành công!");

        } catch (ClassNotFoundException e) {
            System.out.println("Không tìm thấy Driver MySQL.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Kết nối thất bại!");
            e.printStackTrace();
        }
        return con;
    }

    public static void closeConnection(Connection c) {
        try {
            if (c != null && !c.isClosed()) {
                c.close();
                System.out.println("Đóng kết nối thành công.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
