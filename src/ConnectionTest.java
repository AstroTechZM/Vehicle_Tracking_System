import java.sql.*;

public class ConnectionTest {
    public static void main(String[] args) {
        String url = "jdbc:mysql://mysql-1974e506-mu-system1.j.aivencloud.com:19549/defaultdb?ssl=true+&sslmode=require";
        String user = "avnadmin";
        String password = "drexastro";
        
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connection successful!");
        } catch (SQLException e) {
            System.err.println("Connection failed:");
            e.printStackTrace();
        }
    }
}
