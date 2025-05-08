

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
public class SimpleVehicleTracker {

    public static void main(String[] args) {
        // Use SwingUtilities.invokeLater to ensure thread safety
        SwingUtilities.invokeLater(() -> {
            // Create and show the login window
            LoginWindow loginWindow = new LoginWindow();
            loginWindow.setVisible(true);
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

