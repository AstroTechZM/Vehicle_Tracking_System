import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.table.DefaultTableModel;

public class DBConnections
{
	static Connect conn = new Connect();
	static Connection connection = conn.conn();
	static int user_id=1;

	public static Connection getConnection(){
		return connection;
	}
	public static void createAccount(String F_name,String L_name,String role,String password)
		{
			try 
			{
            PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO user (user_id,F_name,L_name,role,LPC,FLA,Account_locked,password ) VALUES (?, ?, ?,?,?,?,?,?)");
            // Get a LocalDate object (from Java 8 onwards)
            LocalDateTime localDate = LocalDateTime.now();
            
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT MAX(user_id) from user;");
            
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
            preparedStatement.setString(8, hashPassword(password));

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

	public static boolean logIn(String user_name,String password)
	{
		try 
			{
				
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT password FROM user WHERE F_name=?");
            preparedStatement.setString(1,user_name);
            
             try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					// Process results
					String cpassword = rs.getString("password");
					//String name = rs.getString("fname");
					System.out.println("ID: " + cpassword);
					if(hashPassword(password).equals(cpassword)){
						addLog("N/A","N/A" , user_name+" logged in");
						return true;
					}
					else {
						return false;
					}
				}
			} catch(SQLException e) {
				System.err.println("Error inserting data (PreparedStatement): " + e.getMessage());
				e.printStackTrace();
        }
		
	} catch(SQLException e) {
				System.err.println("Error inserting data (PreparedStatement): " + e.getMessage());
				e.printStackTrace();
     }
     return false;
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
	public static boolean addLog(String plate, String ownername, String action) {
		System.out.println("Starting vehicle logging...");
		
		// Validate required parameters first
		if (plate == null || plate.trim().isEmpty() || ownername == null || ownername.trim().isEmpty()) {
			System.err.println("Error: Plate number and owner name cannot be empty");
			return false;
		}

		try {
			// 1. Verify the user exists
			if (!isValidUser(user_id)) {
				System.err.println("Error: Invalid user ID " + user_id + " - user doesn't exist");
				return false;
			}

			// 2. Get current timestamp
			LocalDateTime now = LocalDateTime.now();
			Timestamp time = Timestamp.valueOf(now);

			// 3. Get IP address
			String ipAddress = getIP();
			if (ipAddress == null || ipAddress.trim().isEmpty()) {
				ipAddress = "0.0.0.0"; // Default value if IP can't be obtained
			}

			// 4. Create log entry
			String logSql = "INSERT INTO logs " +
						   "(plate_number, owner_fname, user_id, time_stamp, ip_address, action) " +
						   "VALUES (?, ?, ?, ?, ?, ?)";
			
			try (PreparedStatement pstmt = connection.prepareStatement(logSql, Statement.RETURN_GENERATED_KEYS)) {
				pstmt.setString(1, plate.trim());
				pstmt.setString(2, ownername.trim());
				pstmt.setInt(3, user_id);
				pstmt.setTimestamp(4, time);
				pstmt.setString(5, ipAddress);
				pstmt.setString(6, action);

				int affectedRows = pstmt.executeUpdate();
				
				if (affectedRows > 0) {
					try (ResultSet rs = pstmt.getGeneratedKeys()) {
						if (rs.next()) {
							int logId = rs.getInt(1);
							System.out.println("log added successfully with ID: " + logId);
							
							return true;
						}
					}
				}
			}
		} catch (SQLException e) {
			handleDatabaseError(e);
		}
		return false;
	}
	public static boolean addVehicle(String plate, String ownername, String permit) {
		System.out.println("Starting vehicle logging...");
		
		// Validate required parameters first
		if (plate == null || plate.trim().isEmpty() || ownername == null || ownername.trim().isEmpty()) {
			System.err.println("Error: Plate number and owner name cannot be empty");
			return false;
		}

		try {
			// 1. Verify the user exists
			if (!isValidUser(user_id)) {
				System.err.println("Error: Invalid user ID " + user_id + " - user doesn't exist");
				return false;
			}

			// 2. Get current timestamp
			LocalDateTime now = LocalDateTime.now();
			Timestamp time = Timestamp.valueOf(now);
			// 4. Create log entry
			String logSql = "INSERT INTO vehicle_history " +
						   "(plate_number, owner_fname, user_id, check_in_time,permit_type) " +
						   "VALUES (?, ?, ?, ?, ?)";
			
			try (PreparedStatement pstmt = connection.prepareStatement(logSql, Statement.RETURN_GENERATED_KEYS)) {
				pstmt.setString(1, plate.trim());
				pstmt.setString(2, ownername.trim());
				pstmt.setInt(3, user_id);
				pstmt.setTimestamp(4, time);
				pstmt.setString(5, permit);

				int affectedRows = pstmt.executeUpdate();
				
				if (affectedRows > 0) {
					try (ResultSet rs = pstmt.getGeneratedKeys()) {
						if (rs.next()) {
							int logId = rs.getInt(1);
							System.out.println("Vehicle added successfully with ID: " + logId);
							addLog(plate,ownername,"vehicle checked_in");
							return true;
						}
					}
				}
			}
		} catch (SQLException e) {
			handleDatabaseError(e);
		}
		return false;
	}

	// Helper method to validate user existence
	private static boolean isValidUser(int userId) throws SQLException {
		String sql = "SELECT COUNT(*) FROM user WHERE user_id = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, userId);
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next() && rs.getInt(1) > 0;
			}
		}
	}

	// Improved error handling
	private static void handleDatabaseError(SQLException e) {
    if (e.getMessage().contains("foreign key constraint fails")) {
        System.err.println("Database error: Referenced user doesn't exist");
    } else if (e.getMessage().contains("Data too long")) {
        System.err.println("Database error: One or more values exceed column length");
    } else {
        System.err.println("Database error: " + e.getMessage());
    }
    e.printStackTrace();
}
    public static String getIP() {
	
        try {
            InetAddress ip = InetAddress.getLocalHost();
            return ip.getHostAddress();
        } catch (UnknownHostException e) {
            System.out.println("Error getting IP address: " + e.getMessage());
        }
        return "error";
    }
    
    

    /**
     * Fetches all rows from the 'logs' table and returns them
     * as a DefaultTableModel ready to plug into a JTable.
     */
    public static DefaultTableModel fetchLogsTableModel() {
        String[] columnNames = {
            "LOG ID", "PLATE NUMBER", "OWNER NAME",
            "ACTION BY", "TIME STAMP", "IP ADDRESS", "ACTION"
        };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        String sql = "SELECT log_id, plate_number, owner_fname, user_id, time_stamp, ip_address, action "
                   + "FROM logs ORDER BY time_stamp DESC";

        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Object[] row = new Object[columnNames.length];
                row[0] = rs.getInt("log_id");
                row[1] = rs.getString("plate_number");
                row[2] = rs.getString("owner_fname");
                row[3] = rs.getInt("user_id");
                row[4] = rs.getTimestamp("time_stamp");
                row[5] = rs.getString("ip_address");
                row[6] = rs.getString("action");
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // you might want to return an empty model on error
        }

        return model;
    }
    public static DefaultTableModel fetchVehicleHistoryModel(String sqlQuery) {
        // adjust column names to match your DB schema
        //log_id | plate_number | owner_fname | user_id | time_stamp          | ip_address    | action  
        String[] cols = {
            "plate_number",
            "owner_fname",
            "user_id",
            "check_in_time",
            "check_out_time"
        };
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        try (PreparedStatement ps = connection.prepareStatement(sqlQuery);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] row = new Object[cols.length];
                //row[0] = rs.getInt("vehicle_id");
                row[0] = rs.getString("plate_number");
                row[1] = rs.getString("owner_fname");
                row[2] = rs.getInt("user_id");
                row[3] = rs.getTimestamp("check_in_time");
                row[4] = rs.getTimestamp("check_out_time");
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return model;
    }
		/**
	 * Marks the currently checked-in record for the given plate_number as checked out.
	 * Returns true if a row was updated.
	 */
	public static boolean checkOutVehicle(String plate) 
	{
		String sql = "UPDATE logs " +
					 "SET checked = 1, action = 'vehicle checked out' " +
					 "WHERE plate_number = ? AND (checked = 0 OR checked IS NULL)";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, plate.trim());
			System.out.println(plate);
			int updated = ps.executeUpdate();
			return updated > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	public static int getOverstayedVehicleCount() {
    String sql = "SELECT COUNT(*) AS over_24_hours " +
                 "FROM vehicle_history " +
                 "WHERE check_out_time IS NULL " +
                 "AND TIMESTAMPDIFF(HOUR, check_in_time, NOW()) >= 24";
    try (PreparedStatement ps = connection.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
            return rs.getInt("over_24_hours");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return 0;
}
	public static int getTodayCheckInCount() {
		String sql = "SELECT COUNT(*) AS today_count " +
					 "FROM vehicle_history " +
					 "WHERE DATE(check_in_time) = CURDATE()";
		try (PreparedStatement ps = connection.prepareStatement(sql);
			 ResultSet rs = ps.executeQuery()) {
			if (rs.next()) {
				return rs.getInt("today_count");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
     * Deletes a vehicle_history row by its exact check_in_time.
     * If found-and-deleted, logs the deletion via addLog(...)
     * 
     * @param checkInTimestamp  the timestamp string in "yyyy-MM-dd HH:mm:ss" format
     * @return true if a row was deleted; false if no matching row existed
     * @throws IllegalArgumentException if the timestamp string is invalid
     */
    public static boolean deleteVehicleByCheckIn(String checkInTimestamp) {
        // 1) Parse the timestamp (throws IllegalArgumentException on bad format)
        java.sql.Timestamp ts = java.sql.Timestamp.valueOf(checkInTimestamp.trim());

        // 2) Look up the record to get plate & owner
        String selectSql = 
            "SELECT plate_number, owner_fname " +
            "FROM vehicle_history " +
            "WHERE check_in_time = ?";
        String deleteSql =
            "DELETE FROM vehicle_history " +
            "WHERE check_in_time = ?";

        try (PreparedStatement selectPs = connection.prepareStatement(selectSql)) {
            selectPs.setTimestamp(1, ts);

            try (ResultSet rs = selectPs.executeQuery()) {
                if (!rs.next()) {
                    // no such check-in timestamp
                    return false;
                }
                String plate = rs.getString("plate_number");
                String owner = rs.getString("owner_fname");

                // 3) Perform the deletion
                try (PreparedStatement deletePs = connection.prepareStatement(deleteSql)) {
                    deletePs.setTimestamp(1, ts);
                    int rows = deletePs.executeUpdate();
                    if (rows > 0) {
                        // 4) Log the deletion in your logs table
                        addLog(plate, owner, "vehicle deleted");
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // you might rethrow or handle differently in production
        }

        return false;
    }

}


class Connect
{
	public Connection conn(){
		Connection connection= null;
		try
		{
			/*connection = DriverManager.getConnection("jdbc:mysql://my-sql-mu-system1.d.aivencloud.com:19549/defaultdb?" +
             "ssl=true" +
             "&sslmode=require" +
             "&sslrootcert=../lib/ca.pem",
              "avnadmin", 
              "AVNS_MO_JeellTaHHdiu0U7v");*/
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/MU_DB",
              "root", 
              "drexastro");
              
              return connection;
		} catch(SQLException e)
		{
			System.out.println(e);
		}
		return connection;
	}
}
