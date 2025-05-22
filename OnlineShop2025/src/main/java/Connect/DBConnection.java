package Connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
// Bỏ các import không cần thiết cho việc kết nối cơ bản nếu có
// import java.io.IOException;
// import java.nio.charset.StandardCharsets;
// import java.nio.file.Files;
// import java.nio.file.Paths;
// import java.sql.Statement;

public class DBConnection {

    private static final String DB_USER = "root";
    // !!! QUAN TRỌNG: Đảm bảo mật khẩu này khớp với mật khẩu root MySQL bạn đã đặt !!!
    private static final String DB_PASSWORD = "son240406"; // <-- THAY BẰNG MẬT KHẨU CỦA BẠN, hoặc "" nếu không đặt
    private static final String DB_NAME = "onlineshop"; // Tên database bạn vừa tạo
    private static final String MYSQL_SERVER_URL = "jdbc:mysql://localhost:3306/"; // Đúng nếu MySQL chạy cục bộ, cổng 3306

    public static Connection getConnection() {
        try {
            // Thêm các tham số này vào URL để tránh một số lỗi phổ biến
            String url = MYSQL_SERVER_URL + DB_NAME + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            
            // 1. Đảm bảo MySQL Connector/J JAR đã có trong Build Path (Classpath hoặc Modulepath)
            // 2. Nếu dùng Modulepath và `requires com.mysql.cj;` trong module-info.java,
            //    thì không cần Class.forName() nữa nếu driver là JDBC 4.0+ (hầu hết hiện nay là vậy).
            //    Tuy nhiên, để nó ở đây cũng không sao, nó sẽ đảm bảo driver được nạp.
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            System.out.println("Attempting to connect to database: " + url); // Dòng debug
            Connection conn = DriverManager.getConnection(url, DB_USER, DB_PASSWORD);
            System.out.println("Successfully connected to database: " + DB_NAME); // Dòng debug
            return conn;
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
            e.printStackTrace(); // In chi tiết lỗi để dễ debug
            return null;
        } catch (SQLException e) {
            System.err.println("Error connecting to database '" + DB_NAME + "': " + e.getMessage());
            System.err.println("SQLState: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace(); // In chi tiết lỗi để dễ debug
            return null;
        }
    }

    // Phương thức setupOrResetDatabase và main() trong lớp này có thể không cần thiết nữa
    // nếu bạn đã setup database bằng MySQL Workbench.
    // Bạn có thể comment out hoặc xóa chúng để tránh nhầm lẫn hoặc chạy lại script không mong muốn.
    /*
    public static void setupOrResetDatabase(String sqlFilePath) {
        // ... code ...
    }

    public static void main(String[] args) {
        // ... code ...
        // Tạm thời không chạy lại script SQL từ đây
        // String scriptPath = "E:\\onlineshop_setup.sql";
        // setupOrResetDatabase(scriptPath);

        System.out.println("\nTesting connection to database '" + DB_NAME + "' from DBConnection main method...");
        try (Connection testConn = getConnection()) {
            if (testConn != null) {
                System.out.println("Test connection successful from DBConnection main!");
            } else {
                System.err.println("Test connection failed from DBConnection main.");
            }
        } catch (SQLException e) {
            System.err.println("Error during test connection from DBConnection main: " + e.getMessage());
        }
    }
    */
}