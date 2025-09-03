import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Replace 'bank' with your DB name, if different
    private static final String URL = "jdbc:mysql://localhost:3306/bank";
    private static final String USER = "root";           // Your MySQL username
    private static final String PASSWORD = "1234";   // Your MySQL password

    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the connection
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Database connection successful.");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("❌ Database connection failed.");
            e.printStackTrace();
        }

        return conn;
    }

    // Optional main method to test the connection
    public static void main(String[] args) {
        Connection conn = getConnection();
        if (conn != null) {
            System.out.println("Connection established successfully.");
        } else {
            System.out.println("Failed to connect to the database.");
        }
    }
}
