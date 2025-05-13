

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
			String url = "jdbc:mysql://localhost:3306/MU_Gate_Vehicle_Tracker?useSSL=false&serverTimezone=UTC";
			String username = "root";
			String password = "drexastro";
			//try(Connection connection = DriverManager.getConnection(url, username, password)){
        // Use SwingUtilities.invokeLater to ensure thread safety
        SwingUtilities.invokeLater(() -> {
            // Create and show the login window
            LoginWindow loginWindow = new LoginWindow();
            loginWindow.setVisible(true);
            //Dashboard dashboard = new Dashboard();
            //dashboard.setVisible(true);
            
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

