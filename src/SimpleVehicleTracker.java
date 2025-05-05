

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.border.AbstractBorder;
import java.awt.event.*;
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
            //Dashboard.setVisible(true);
        });
    }
}

class LoginWindow extends JFrame {
	Dashboard Dashboard1 = new Dashboard();
    private JTextField usernameField;
    private JPasswordField passwordField;

    
    public LoginWindow() {
        setTitle("MU Gate Login");
        setResizable(false);
        setSize(450, 300);
        setMinimumSize(new Dimension(350,300));
        setMaximumSize(new Dimension(350,300));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        
        //Main panel with BorderLayout//watashiga?u mean me?
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        
        ImageIcon icon = new ImageIcon("../lib/icon.jpeg");
        
		Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		JLabel iconLabel = new JLabel(new ImageIcon(img));
		JPanel iconPanel = new JPanel();
		iconPanel.setBackground(new Color(209, 219, 229));
		iconPanel.add(iconLabel);
		mainPanel.add(iconPanel,BorderLayout.WEST);
		mainPanel.setBackground(new Color(209, 219, 229));
		
        
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10))
        {
			
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(getBackground());
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
				g2.dispose();
		}};
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(6, 41, 82));
        panel.setSize(350, 300);
        
        
        usernameField = new JTextField();
		usernameField.setText("Enter username");
		usernameField.setForeground(Color.GRAY);
		usernameField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (usernameField.getText().equals("Enter username")) {
					usernameField.setText("");
					usernameField.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (usernameField.getText().isEmpty()) {
					usernameField.setForeground(Color.GRAY);
					usernameField.setText("Enter username");
				}
			}
		});
	
		panel.add(usernameField);
		
		passwordField = new JPasswordField();
		passwordField.setEchoChar((char) 0); // Show plain text (no asterisks)
		passwordField.setForeground(Color.GRAY);
		passwordField.setText("Enter password");

		passwordField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (new String(passwordField.getPassword()).equals("Enter password")) {
					passwordField.setForeground(Color.BLACK);
					passwordField.setText("");
					passwordField.setEchoChar('•'); // Show asterisks when typing
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (passwordField.getPassword().length == 0) {
					passwordField.setForeground(Color.GRAY);
					passwordField.setEchoChar((char) 0); // Hide asterisks for placeholder
					passwordField.setText("Enter password");
				}
			}
		});
        
        panel.add(passwordField);
        
        //buttons
        JButton loginBtn = new JButton("Login");
        loginBtn.addActionListener(this::performLogin);
        
        JButton registerBtn = new JButton("Register");
        // Style buttons
        styleButton(loginBtn, new Color(59, 89, 182), new Color(75, 105, 200), 20);
        styleButton(registerBtn, new Color(200, 50, 50), new Color(220, 70, 70), 15);

				
        panel.add(loginBtn);
        panel.add(registerBtn);
        
        mainPanel.add(panel, BorderLayout.CENTER);
        add(mainPanel);
    }
    public static void styleButton(JButton button, Color baseColor, Color hoverColor, int borderRadius) {
		// Basic styling
		button.setFont(new Font("Arial", Font.BOLD, 14));
		button.setForeground(Color.WHITE);
		button.setBackground(baseColor);
		button.setFocusPainted(false);
		button.setContentAreaFilled(false);
		button.setOpaque(true);
		button.setBorder(new RoundedBorder(borderRadius));

		// Hover effects
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				button.setBackground(hoverColor);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				button.setBackground(baseColor);
			}
		});
	}
    
    private void performLogin(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        
        // Simple authentication (in real app, use database)
        if (username.equals("admin") && password.equals("admin123")) {
            SwingUtilities.invokeLater(() -> {
                Dashboard1.setVisible(true);
                this.dispose();
            });
        } else if (username.equals("guard") && password.equals("guard123")) {
            SwingUtilities.invokeLater(() -> {
                Dashboard1.setVisible(true);
                this.dispose();
            });
        } else {
            JOptionPane.showMessageDialog(this, 
                "Invalid username or password", 
                "Login Error", 
                JOptionPane.ERROR_MESSAGE);
        }
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
class RoundedBorder extends AbstractBorder {
    private int radius;

    public RoundedBorder(int radius) {
        this.radius = radius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(c.getForeground());
        g2.drawRoundRect(x, y, width-1, height-1, radius, radius);
        g2.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(radius+1, radius+1, radius+1, radius+1);
    }
}
