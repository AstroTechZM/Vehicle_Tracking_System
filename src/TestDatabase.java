/*
 * TestDatabase.java
 * 
 * Copyright 2025 henrie mate <henrie@Astro>
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 * 
 * 
 */
import java.sql.*;

public class TestDatabase {
	
	public static void main (String[] args) {
		// the database is being hosted on the cloud at aivencloud.com
		String url = "jdbc:mysql://mysql-1974e506-mu-system1.j.aivencloud.com:19549/defaultdb?ssl=true+&sslmode=require";
		try{
			Connection conn = DriverManager.getConnection(url,"avnadmin","AVNS_Osn2GIElcOxqkzrLhEW");
			Statement stmt = conn.createStatement();
			
			//ResultSet rs = stmt.executeUpdate("ALTER TABLE user ADD COLUMN password VARCHAR(50)");
			stmt.executeUpdate("ALTER TABLE user ADD COLUMN password VARCHAR(50)");

			System.out.println("success");
		} catch ( SQLException e){
			System.err.println("failed"+ e.getMessage());
		}
		
	}
}

