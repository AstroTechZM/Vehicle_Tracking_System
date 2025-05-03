import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.border.AbstractBorder;
import javax.swing.border.LineBorder;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.table.*;

public class Dashboard {
    public static void main(String[] args) {
        new DashboardImplementation();
    }
}

class DashboardImplementation extends JFrame {
    private JLabel registerVehicle;
    private JLabel licencePlate, ownerName, permitType;
    private JTextField licencePlateTextField, ownerNameTextField;
    private JButton addVehicleButton;
    private JComboBox permitTypeComboBox;
    private JButton ExportCSV, ExportPDF , ViewLogs;
    
    public DashboardImplementation() {
        setLayout(new BorderLayout());
        add(topPanel(),BorderLayout.NORTH);
        add(center(),BorderLayout.CENTER);
        add(bottom(), BorderLayout.SOUTH);
        pack();
        setBackground(Color.WHITE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        
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

	private JPanel topPanel()
	{
		JPanel top = new JPanel(new BorderLayout());
		top.add(createSummaryPanel(), BorderLayout.WEST);
		top.add(logo(), BorderLayout.EAST);
		top.setBackground(Color.WHITE);
		return top;
		 
	}
	private JPanel logo()
	{
        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setOpaque(false);
        logoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Load the logo image (make sure the file path is correct)
        ImageIcon originalIcon = new ImageIcon("MULogo.jpeg"); // Or "resources/logo.png" if in a folder
        Image image = originalIcon.getImage().getScaledInstance(100,100,Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(image);
        JLabel logoLabel = new JLabel(resizedIcon, SwingConstants.CENTER);

        logoPanel.add(logoLabel, BorderLayout.NORTH);
        JLabel label = new JLabel("MU VEHICLE TRACKING SYSTEM");
        label.setFont(new Font("Arial",Font.BOLD,20));
        label.setForeground(new Color(73, 88, 181));
        logoPanel.add(label,BorderLayout.SOUTH);
        JPanel finalPanel = new JPanel();
        finalPanel.setOpaque(false);
        finalPanel.add(logoPanel, BorderLayout.NORTH);
        return finalPanel;
	}
	private JPanel center()
	{
		JPanel centerPanel = new JPanel(new GridLayout(1,1,20,20));
		centerPanel.add(ShortCheckInOut());
		centerPanel.add(regVehicleUI());
		centerPanel.setBackground(Color.WHITE);
		return centerPanel;
	}

    private JPanel createSummaryPanel() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setOpaque(false);
        
        JPanel gridPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        gridPanel.setPreferredSize(new Dimension(3 * 220 + 20, 2 * 120 + 10)); // Calculate total size
        
        // Add 6 panels (2 rows × 3 columns)
        for (int i = 0; i < 4; i++) {
            gridPanel.add(livePanel("this is a dummy text", 50,200,100));
        }
        gridPanel.setOpaque(false);
        gridPanel.add(livePanel("Visitors Today", 50,400,100));
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
    String[] columns = new String[]{"Entry ID", "License Plate", "Owner Name", "Check-In Time", "Check-Out","Status"};
    Object[][] data = {
                {"KAB123A", "BAD238","John Doe", "12:00 PM","true", "Checked Out"},
                {"KCD456B", "BAD558","Jane Smith", "9:30 AM", "false", "Checked In"},
            };

    private JScrollPane ShortCheckInOut()
    {
		JTable tabel = new JTable(data, columns);
		tabel.setOpaque(false);
		styleTable(tabel);
		JScrollPane scrollPane = new JScrollPane(tabel);
		scrollPane.setOpaque(false);
		return scrollPane;
      }
      private static void styleTable(JTable table) {
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
      private JPanel regVehicleUI() {

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
}




public class RoundedButton extends JButton {
    private int radius;


    public RoundedButton(String text, int radius,Color baseColor, Color hoverColor) {
		super(text);
		// Hover effects
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				setBackground(hoverColor);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				setBackground(baseColor);
			}
		});
        this.radius = radius;
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        
        setForeground(Color.WHITE);
        setBackground(baseColor);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fill background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        // Draw text
        super.paintComponent(g);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        // Optional: draw custom border if needed
    }

    @Override
    public boolean contains(int x, int y) {
        Shape shape = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius);
        return shape.contains(x, y);
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
