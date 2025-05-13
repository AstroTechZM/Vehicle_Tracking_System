import java.sql.*;

public class ConnectionTest {
    public static void main(String[] args) {
        String url = "jdbc:mysql://127.0.0.1:3306/MU_Gate_Vehicle_Tracker?useSSL=false";
        String user = "root";
        String password = "drexastro";
        
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connection successful!");
        } catch (SQLException e) {
            System.err.println("Connection failed:");
            e.printStackTrace();
        }
    }
}
