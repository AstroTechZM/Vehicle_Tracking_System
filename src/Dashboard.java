import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.border.LineBorder;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.table.*;
import javax.swing.border.EmptyBorder;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Dashboard extends JFrame {
    // Add main content container reference
    private JLabel registerVehicle;
    private JLabel licencePlate, ownerName, permitType;
    private JTextField licencePlateTextField, ownerNameTextField;
    private JButton addVehicleButton;
    private JComboBox permitTypeComboBox;
    private JButton ExportCSV, ExportPDF , ViewLogs;
    private JPanel mainContentContainer;
    
    public Dashboard() {
        JPanel mainPanel = new JPanel();
        setContentPane(mainPanel);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        add(topMostPanel());
        pack();
        setBackground(Color.WHITE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Make window fullscreen
    }

    private JPanel topMostPanel() {
        JPanel topMost = new JPanel(new BorderLayout());
        topMost.setBackground(Color.WHITE);
        topMost.add(menuBar(), BorderLayout.NORTH);
        topMost.add(mainBody(), BorderLayout.CENTER); // Modified
        return topMost;
    }

    private JPanel mainBody() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setOpaque(false);
        panel.add(sideBar());
        
        // Initialize main content container
        mainContentContainer = new JPanel(new BorderLayout());
        mainContentContainer.add(HomePage.home(), BorderLayout.CENTER);
        
        panel.add(mainContentContainer);
        return panel;
    }

    private JPanel sideBar() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        
        panel.add(sideBarPanel("../lib/homy.png", "Home", HomePage.home()));
        panel.add(sideBarPanel("../lib/register.png", "Register Vehicle", regVehicleUI()));
        panel.add(sideBarPanel("../lib/vehicle.png", "Vehicle History", vehicleHistoryUI()));
        panel.add(sideBarPanel("../lib/logs1.png", "View Logs", viewLogsUI()));
        panel.add(Box.createVerticalGlue());
        panel.add(sideBarPanel("../lib/logOut.png", "Log Out", new JPanel())); // Add proper logout panel
        
        panel.setSize(new Dimension(200, getHeight()));
        return panel;
    }

    private JPanel sideBarPanel(String path, String text, JPanel targetPanel) {
        JPanel panel = new JPanel(new BorderLayout());
        ImageIcon home = new ImageIcon(path);
		Image homeImg = home.getImage().getScaledInstance(25,25,Image.SCALE_SMOOTH);
		ImageIcon resizedHome = new ImageIcon(homeImg);
		
		JLabel label = new JLabel(text);
		label.setBorder(new EmptyBorder(0, 50, 0, 10)); // 0px top/bottom, 30px left/right

		
		panel.setMaximumSize(new Dimension(200,Integer.MAX_VALUE));
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createRaisedBevelBorder());
		panel.setForeground(new Color(73, 88, 181));
		
		panel.add(new JLabel(resizedHome),BorderLayout.WEST);
		panel.add(label,BorderLayout.EAST);
		JPanel myPanel = new JPanel();
        
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                switchContent(targetPanel);
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(240, 240, 240));
                panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(null);
                panel.setCursor(Cursor.getDefaultCursor());
            }
        });
        return panel;
    }

    private void switchContent(JPanel newPanel) {
        mainContentContainer.removeAll();
        mainContentContainer.add(newPanel, BorderLayout.CENTER);
        mainContentContainer.revalidate();
        mainContentContainer.repaint();
    }

    // Add proper panel methods
    private JPanel vehicleHistoryUI() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Vehicle History Content"));
        return panel;
    }
	public JPanel menuBar()
	{
		ImageIcon img = new ImageIcon("../lib/MULogo.jpeg");
		Image image = img.getImage().getScaledInstance(50,50,Image.SCALE_SMOOTH);
		ImageIcon resizedImage = new ImageIcon(image);
		
		JLabel mu = new JLabel("Mulungushi University Vehicle Tracking System");
		mu.setFont(new Font("Arial",Font.BOLD,20));
        mu.setForeground(new Color(73, 88, 181));
		
		ImageIcon logIn = new ImageIcon("../lib/logIn.jpeg");
		Image logInImg = logIn.getImage().getScaledInstance(25,25,Image.SCALE_SMOOTH);
		ImageIcon resizedLogInImg = new ImageIcon(logInImg);
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createRaisedBevelBorder());
		
		JLabel label = new JLabel(resizedLogInImg);
		label.setBorder(new EmptyBorder(0, 10, 0, 10)); // 0px top/bottom, 30px left/right
		
		panel.add(new JLabel(resizedImage));
		panel.add(Box.createHorizontalGlue());
		panel.add(mu);
		panel.add(Box.createHorizontalGlue());
		panel.add(new Notification());
		panel.add(label);
		
		return panel;
		
	}
    
	private JPanel regVehicleUI() {

    RoundedPanel registrationUI = new RoundedPanel(50);
    registrationUI.setBorder(BorderFactory.createRaisedBevelBorder());
    
    // Use GridBagLayout for precise control
    registrationUI.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5); // Padding
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // Header
    JLabel registerVehicleLabel = new JLabel("Register Vehicle");
    registerVehicleLabel.setFont(new Font("Arial", Font.BOLD, 14));
    registerVehicleLabel.setForeground(new Color(73, 88, 181));
    gbc.gridwidth = 2;
    gbc.gridx = 0;
    gbc.gridy = 0;
    registrationUI.add(registerVehicleLabel, gbc);

    // Form fields
    gbc.gridwidth = 1;
    
    // License Plate
    gbc.gridy++;
    JLabel licencePlateLabel = new JLabel("License Plate:");
    registrationUI.add(licencePlateLabel, gbc);

    gbc.gridx = 1;
    JTextField licencePlateTextField = new JTextField(15);
    registrationUI.add(licencePlateTextField, gbc);
    gbc.gridx = 0;

    // Owner's Name
    gbc.gridy++;
    JLabel ownerNameLabel = new JLabel("Owner's Name:");
    registrationUI.add(ownerNameLabel, gbc);

    gbc.gridx = 1;
    JTextField ownerNameTextField = new JTextField(15);
    registrationUI.add(ownerNameTextField, gbc);
    gbc.gridx = 0;

    // Permit Type
    gbc.gridy++;
    JLabel permitTypeLabel = new JLabel("Permit Type:");
    registrationUI.add(permitTypeLabel, gbc);

    gbc.gridx = 1;
    String[] permitTypes = {"Visitor", "Staff", "Student"};
    JComboBox<String> permitTypeComboBox = new JComboBox<>(permitTypes);
    registrationUI.add(permitTypeComboBox, gbc);
    gbc.gridx = 0;

    // Button
    gbc.gridy++;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.NONE;
    gbc.anchor = GridBagConstraints.CENTER;
    RoundedButton addVehicleButton = new RoundedButton("Check In", 20, 
        new Color(73, 88, 181), new Color(59, 89, 182));
    addVehicleButton.setPreferredSize(new Dimension(200, 40));
    registrationUI.add(addVehicleButton, gbc);

    return registrationUI;
}
    private JPanel viewLogsUI() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("View Logs Content"));
        return panel;
    }

    // ... [rest of existing methods remain unchanged]
       private JPanel bottom()
    {
        JPanel bottom = new JPanel(new BorderLayout());

        ExportCSV = new JButton("Export Logs to CSV");
        ExportPDF = new JButton("Export Logs to PDF");
        ViewLogs = new JButton("View System Logs");

        bottom.add(ExportCSV,BorderLayout.EAST);
        bottom.add(ExportPDF, BorderLayout.CENTER);
        bottom.add(ViewLogs, BorderLayout.WEST);
		return bottom;

    }
}


class Notification extends JLabel {
    private int notificationCount = 2;
    private ImageIcon originalIcon;
    private static final int ICON_SIZE = 25;

    public Notification() {
        try {
            // Load icon from file
            File iconFile = new File("../lib/notificationIcon.png");
            BufferedImage originalImage = ImageIO.read(iconFile);
            originalIcon = new ImageIcon(originalImage);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Error loading icon: " + e.getMessage());
        }
        setIcon(createCompositeIcon());
    }

    public void setNotificationCount(int count) {
        this.notificationCount = Math.max(0, count);
        setIcon(createCompositeIcon());
    }

    private ImageIcon createCompositeIcon() {
        BufferedImage image = new BufferedImage(ICON_SIZE, ICON_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw base icon
        Image scaledIcon = originalIcon.getImage().getScaledInstance(
            ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
        g2d.drawImage(scaledIcon, 0, 0, null);

        // Draw notification badge
        if (notificationCount > 0) {
            int badgeSize = 15;
            int badgeX = ICON_SIZE - badgeSize;
            int badgeY = 0;

            // Red circle background
            g2d.setColor(new Color(255, 59, 48)); // iOS-style red
            g2d.fillOval(badgeX, badgeY, badgeSize, badgeSize);

            // White text
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            
            String countText = notificationCount > 99 ? "99+" : String.valueOf(notificationCount);
            FontMetrics fm = g2d.getFontMetrics();
            int textX = badgeX + (badgeSize - fm.stringWidth(countText)) / 2;
            int textY = badgeY + (badgeSize - fm.getHeight()) / 2 + fm.getAscent();
            
            g2d.drawString(countText, textX, textY);
        }

        g2d.dispose();
        return new ImageIcon(image);
    }


            
}
