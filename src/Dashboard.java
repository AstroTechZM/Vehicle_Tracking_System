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
    private JLabel registerVehicle;
    private JLabel licencePlate, ownerName, permitType;
    private JTextField licencePlateTextField, ownerNameTextField;
    private JButton addVehicleButton;
    private JComboBox permitTypeComboBox;
    private JButton ExportCSV, ExportPDF , ViewLogs;
    
    public Dashboard() {
		JPanel mainPanel = new JPanel();
		setContentPane(mainPanel);
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
        add(topMostPanel());
        //add(center(),BorderLayout.CENTER);
        //add(bottom(), BorderLayout.SOUTH);
        pack();
        setBackground(Color.WHITE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }
	private JPanel topMostPanel()
	{
		JPanel topMost = new JPanel(new BorderLayout());
		topMost.setBackground(Color.WHITE);
		topMost.add(menuBar(),BorderLayout.NORTH);
		topMost.add(mainBody(),BorderLayout.WEST);
		return topMost;
	}
	private JPanel mainBody()
	{
		JPanel panel = new JPanel(new BorderLayout());
		panel.setOpaque(false);
		panel.add(sideBar(),BorderLayout.WEST);
		//panel.add(topPanel(),BorderLayout.CENTER);
		return panel;
	}
	private JPanel sideBar()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		panel.setOpaque(false);
		
		panel.add(sideBarPanel("../lib/homy.png","Home"));
		panel.add(sideBarPanel("../lib/register.png","Register Vehicle"));
		panel.add(sideBarPanel("../lib/vehicle.png","Vehicle History"));
		panel.add(sideBarPanel("../lib/logs1.png","Veiw Logs"));
		panel.add(Box.createVerticalGlue());
		panel.add(sideBarPanel("../lib/logOut.png","Log Out"));
		panel.setPreferredSize(new Dimension(200, getHeight())); // Width 200p
		return panel;
	}
	private JPanel sideBarPanel(String path, String text)
	{
		JPanel panel = new JPanel(new BorderLayout());
		ImageIcon home = new ImageIcon(path);
		Image homeImg = home.getImage().getScaledInstance(25,25,Image.SCALE_SMOOTH);
		ImageIcon resizedHome = new ImageIcon(homeImg);
		
		JLabel label = new JLabel(text);
		label.setBorder(new EmptyBorder(0, 50, 0, 10)); // 0px top/bottom, 30px left/right

		
		panel.setSize(new Dimension(200,40));
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createRaisedBevelBorder());
		
		panel.add(new JLabel(resizedHome),BorderLayout.WEST);
		panel.add(label,BorderLayout.EAST);
		return panel;
	}
	private JPanel topPanel()
	{
		JPanel top = new JPanel(new BorderLayout());
		top.add(createSummaryPanel(), BorderLayout.WEST);
		//top.add(logo(), BorderLayout.EAST);
		top.setBackground(Color.WHITE);
		return top;
		 
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

	/*private JPanel center()
	{
		JPanel centerPanel = new JPanel(new GridLayout(1,1,20,20));
		centerPanel.add(topPanel());
		centerPanel.setBackground(Color.WHITE);
		return centerPanel;
	}*/

    private JPanel createSummaryPanel() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setOpaque(false);
        
        JPanel gridPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        gridPanel.setSize(new Dimension(400, 300)); // Calculate total size
        
        // Add 6 panels (2 rows × 3 columns)
        gridPanel.setOpaque(false);
        gridPanel.add(livePanel("Total Checkins", 75,300,150));
        gridPanel.add(livePanel("active guards", 1,300,150));
        gridPanel.add(livePanel("Total Check-outs", 25,300,150));
        gridPanel.add(livePanel("alerts", 25,300,150));
        mainPanel.add(gridPanel);
        return mainPanel;
    }

    private JPanel livePanel(String words, int value,int Dimension1,int Dimension2) {
        //JPanel panel = new JPanel();
        RoundedPanel panel = new RoundedPanel(20);
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setPreferredSize(new Dimension(Dimension1, Dimension2));
        
        JLabel text = new JLabel(words);
        
        JPanel textHolder = new JPanel(new BorderLayout());
        textHolder.add(text, BorderLayout.EAST);
        textHolder.setOpaque(false);
        
        
        JLabel sum = new JLabel(Integer.toString(value));
        sum.setFont(new Font("Arial",Font.BOLD,50));
        
        JPanel sumHolder = new JPanel(new BorderLayout());
        sumHolder.add(sum, BorderLayout.EAST);
        sumHolder.setOpaque(false);
        
        
        
        panel.add(Box.createHorizontalGlue());
        panel.add(sumHolder);
        panel.add(Box.createHorizontalGlue());
        panel.add(textHolder);
        
        
        panel.setBackground(new Color(73, 88, 181));
        text.setForeground(Color.WHITE);
        sum.setForeground(Color.WHITE);
        
        
        
        panel.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseEntered(MouseEvent e) {
			panel.setBackground(new Color(213, 59, 51));
			panel.repaint();
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			panel.setBackground(new Color(73,88,181));
			panel.repaint();
		}
	});
        
        
        
        
        
        
        
        
        
        
        
        
        
        return panel;
    }

    private JScrollPane ShortCheckInOut()
    {
		String[] columns = new String[]{"Entry ID", "License Plate", "Owner Name", "Check-In Time", "Check-Out","Status"};
		Object[][] data = {
                {"KAB123A", "BAD238","John Doe", "12:00 PM","true", "Checked Out"},
                {"KCD456B", "BAD558","Jane Smith", "9:30 AM", "false", "Checked In"},
        };
		JTable tabel = new JTable(data, columns);
		tabel.setOpaque(false);
		styleTable(tabel);
		JScrollPane scrollPane = new JScrollPane(tabel);
		scrollPane.setOpaque(false);
		return scrollPane;
      }
    private static void styleTable(JTable table)
    {
        // Font and Row Height
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(28);

        // Header Styling
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(30, 58, 95));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(100, 35));

        // Row striping (alternating colors)
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean isSelected,
                                                           boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, isSelected, hasFocus, row, col);
                if (isSelected) {
                    c.setBackground(new Color(46, 90, 138));
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        });

        // Grid color and spacing
        table.setGridColor(new Color(220, 220, 220));
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
	}
    private JPanel regVehicleUI()
    {

		RoundedPanel registrationUI = new RoundedPanel(50);
        JLabel registerVehicleLabel = new JLabel("Register Vehicle");
        registerVehicleLabel.setBounds(10, 20, 300, 40); // Adjust size for better visibility
        registerVehicleLabel.setFont(new Font("Arial", Font.BOLD, 14)); // Set font style, weight, and size
        registerVehicleLabel.setForeground(new Color(73,88,181)); // Set text color
        registrationUI.setBorder(BorderFactory.createRaisedBevelBorder());
        registrationUI.add(registerVehicleLabel);

        JLabel licencePlateLabel = new JLabel("License Plate:");
        licencePlateLabel.setBounds(10, 70, 100, 25);
        registrationUI.add(licencePlateLabel);
        
        JTextField licencePlateTextField = new JTextField();
        licencePlateTextField.setBounds(150, 70, 200, 25);
        registrationUI.add(licencePlateTextField);

        JLabel ownerNameLabel = new JLabel("Owner's Name:");
        ownerNameLabel.setBounds(10, 110, 100, 25);
        registrationUI.add(ownerNameLabel);

        JTextField ownerNameTextField = new JTextField();
        ownerNameTextField.setBounds(150, 110, 200, 25);
        registrationUI.add(ownerNameTextField);

        JLabel permitTypeLabel = new JLabel("Permit Type:");
        permitTypeLabel.setBounds(10, 150, 100, 25);
        registrationUI.add(permitTypeLabel);

        // Replace JTextField with JComboBox for a dropdown list of permit types
        String[] permitTypes = {"Visitor", "Staff", "Student"};
        JComboBox<String> permitTypeComboBox = new JComboBox<>(permitTypes);
        permitTypeComboBox.setBounds(150, 150, 200, 25);
        registrationUI.add(permitTypeComboBox);

        RoundedButton addVehicleButton = new RoundedButton("Check In",20,new Color(73,88,181),new Color(59, 89, 182));
        addVehicleButton.setBounds(150, 200, 120, 30);
        addVehicleButton.setSize(new Dimension(200,40));
        registrationUI.add(addVehicleButton);
        registrationUI.setSize(500, 300);
        registrationUI.setLayout(new BorderLayout(10, 10));
        
        
        return registrationUI;
        
    }
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


class RoundedPanel extends JPanel {
    private int cornerRadius;

    public RoundedPanel(int cornerRadius) {
        this.cornerRadius = cornerRadius;
        setOpaque(false); // Make panel transparent to see rounded corners
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        
        // Enable anti-aliasing for smoother edges
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Fill a rounded rectangle
        g2d.setColor(getBackground());
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        
        g2d.dispose();
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

