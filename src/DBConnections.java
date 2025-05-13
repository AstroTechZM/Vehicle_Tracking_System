import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class DBConnections
{
	/*static String url = "jdbc:mysql://localhost:3306/MU_Gate_Vehicle_Tracker?useSSL=false&serverTimezone=UTC";
	static String username = "root";
	static String password = "drexastro";
	static Connection connection = null;*/
	static Connection connection = null;
		public DBConnections(Connection conn)
		{
			this.connection = conn;
		}

		public void createAccount(String F_name,String L_name,String role,String password)
		{
			try {
			//connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO user (user_id,F_name,L_name,role,LPC,FLA,Account_locked,password ) VALUES (?, ?, ?,?,?,?,?,?)");
            // Get a LocalDate object (from Java 8 onwards)
            LocalDateTime localDate = LocalDateTime.now();
            
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT MAX(user_id) from user;");
            int user_id=1;
			while(rs.next())
			{
				int value = rs.getInt(1);
				if(value>0) user_id = value+1;
			}

            // Convert LocalDate to java.sql.Date
            Timestamp timestamp = Timestamp.valueOf(localDate);
            int failedLogInAttempts = 0;
            boolean Account_locked = true;
           
            

            // Set the values for the placeholders
            preparedStatement.setInt(1, user_id); // 1st placeholder (user_id)
            preparedStatement.setString(2, F_name);       // 2nd placeholder (F_name)
            preparedStatement.setString(3, L_name);         // 3rd placeholder (L_name)
            preparedStatement.setString(4, role); 
            preparedStatement.setTimestamp(5,timestamp); 
            preparedStatement.setInt(6, failedLogInAttempts); // 3rd placeholder (L_name)
            preparedStatement.setBoolean(7, Account_locked);         // 3rd placeholder (L_name)
            preparedStatement.setString(8, password);

            // Execute the INSERT statement
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Data inserted successfully using PreparedStatement!");
            } else {
                System.out.println("Failed to insert data using PreparedStatement.");
            }

        } catch (SQLException e) {
            System.err.println("Error inserting data (PreparedStatement): " + e.getMessage());
            e.printStackTrace();
        }
        
    }
    public static String hashPassword(String password) {
        try {
            // Create MessageDigest instance for SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Add password bytes to digest
            byte[] hashedBytes = md.digest(password.getBytes());

            // Convert byte array into signum representation
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }

            // Return the hashed password in hexadecimal format
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }
}
