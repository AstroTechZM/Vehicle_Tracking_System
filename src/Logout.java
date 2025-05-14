import javax.swing.*;
public class Logout extends JFrame {

    public static JPanel logout() {

        JPanel panel = new JPanel();
        
        //Creating an instance of login window
         // LoginWindow loginWindow = new LoginWindow();
         // Dashboard dashboard = new Dashboard();

        // Diolog box 
        int response = JOptionPane.showConfirmDialog(panel, "Are you sure you want to log out?", "Logout Confirmation", JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
            // Add logout logic here
           //dashboard.setVisible(false); 
           
           JOptionPane.showMessageDialog(panel, "Loged Out Succesfull");

           //loginWindow.setVisible(true);

        } else {

            //dashboard.setVisible(true);
            JOptionPane.showMessageDialog(panel, "Loged Out Succesfull");   
        }

        return panel;
    }
}
