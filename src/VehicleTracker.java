

import javax.swing.*;
/***************************************************
 * 
 * 
 * ***************CONTROL***************************
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */
public class VehicleTracker {

    public static void main(String[] args) {
			String url =  "jdbc:mysql://mysql-1974e506-mu-system1.j.aivencloud.com:19553/defaultdb?" +
             "ssl=true" +
             "&sslmode=require" +
             "&sslrootcert=../lib/ca.pem";
			String username = "avnadmin";
			String password = "AVNS_Osn2GIElcOxqkzrLhEW";
			//try(Connection connection = DriverManager.getConnection(url, username, password)){
        // Use SwingUtilities.invokeLater to ensure thread safety
        SwingUtilities.invokeLater(() -> {
            // Create and show the login window
            //LoginWindow loginWindow = new LoginWindow();
            //loginWindow.setVisible(true);
            Dashboard dashboard = new Dashboard();
            dashboard.setVisible(true);
            
        });
    }
}


/********************************************************************************

* *****************************VIEW**************************************
* 
* 
* 
* 
* 
* 
* 
* 
* 
********************************************************************************/

