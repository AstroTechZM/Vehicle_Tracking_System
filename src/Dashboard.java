import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.function.Supplier;
import javax.swing.Timer;

public class Dashboard extends JFrame {
    // Add main content container reference
    private JLabel registerVehicle;
    private JLabel licencePlate, ownerName, permitType;
    private JTextField licencePlateTextField, ownerNameTextField;
    private JButton addVehicleButton;
    private JComboBox permitTypeComboBox;
    private JButton ExportCSV, ExportPDF , ViewLogs;
    private JPanel mainContentContainer;
    private final Notification notification = new Notification();
    

    
    public Dashboard() {
        JPanel mainPanel = new JPanel();
        setContentPane(mainPanel);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        add(topMostPanel());
        pack();
        setBackground(Color.WHITE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Make window fullscreen
		new Timer(2_000, e -> {
			int unread = DBConnections.getUnreadLogCount();
			notification.setNotificationCount(unread);
		}).start();

		  

    }

    private JPanel topMostPanel() {
        JPanel topMost = new JPanel(new BorderLayout());
        topMost.setBackground(Color.WHITE);
        topMost.add(menuBar(), BorderLayout.NORTH);
        topMost.add(mainBody(), BorderLayout.CENTER);
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

        
        panel.add(sideBarPanel("../lib/homy.png", "Home", () -> HomePage.home()));
		panel.add(sideBarPanel("../lib/register.png", "Register Vehicle", () -> regVehicleUI()));
		panel.add(sideBarPanel("../lib/vehicle.png", "Vehicle History", () -> vehicleHistoryUI()));
		panel.add(sideBarPanel("../lib/logs1.png", "View Logs", () -> viewLogsUI()));
		panel.add(Box.createVerticalGlue());
        panel.add(sideLogOutPanel("../lib/logOut.png", "Log Out")); 

        
        panel.setSize(new Dimension(200, getHeight()));
        return panel;
    }

    private JPanel sideBarPanel(String iconPath, String text, Supplier<JPanel> panelSupplier) {
        JPanel item = new JPanel(new BorderLayout());
        item.setMaximumSize(new Dimension(200, Integer.MAX_VALUE));

        // Icon
        ImageIcon icon = new ImageIcon(iconPath);
        Image img = icon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        item.add(new JLabel(new ImageIcon(img)), BorderLayout.WEST);

        // Label
        JLabel label = new JLabel(text);
        label.setBorder(new EmptyBorder(0, 10, 0, 10));
        item.add(label, BorderLayout.CENTER);

        item.setOpaque(false);
        item.setBorder(BorderFactory.createRaisedBevelBorder());

        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                switchContent(panelSupplier.get());
            }
            @Override public void mouseEntered(MouseEvent e) {
                item.setBackground(new Color(240,240,240));
                item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            @Override public void mouseExited(MouseEvent e) {
                item.setBackground(null);
                item.setCursor(Cursor.getDefaultCursor());
            }
        });

        return item;
    }
private JPanel sideLogOutPanel(String path, String text) {
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
            int result = JOptionPane.showConfirmDialog(
                panel,
                "Are you sure you want to exit?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            if (result == JOptionPane.YES_OPTION) {
                dispose();
            }
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
        JPanel panel = new JPanel(new BorderLayout());
        JTable table = new JTable();
        JScrollPane scroll = new JScrollPane(table);
        
        

        scroll.setBorder(BorderFactory.createTitledBorder("Vehicle History"));
        panel.add(scroll, BorderLayout.CENTER);

        // Load data in background
        new SwingWorker<DefaultTableModel, Void>() {
            @Override
            protected DefaultTableModel doInBackground() throws Exception {
                return DBConnections.fetchVehicleHistoryModel("SELECT plate_number, owner_fname, user_id,check_in_time,check_out_time" + " FROM vehicle_history");
            }
            @Override
		protected void done() {
			try {
				DefaultTableModel model = get();
				table.setModel(model);
				TableUtils.installCheckinCheckoutHighlighter(table);

				//  attach right‐click delete popup
				attachVehicleHistoryPopup(table, panel);

			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(panel,
					"Error loading vehicle history: " + ex.getMessage());
			}
		}

        }.execute();

        // search button on logs
        RoundedPanel search = new RoundedPanel(10);
        search.setBorder(BorderFactory.createRaisedBevelBorder());
        
        // Use GridBagLayout for precise control
        search.setSize(new Dimension(200,300));
        search.setLayout(new FlowLayout());
        

        JTextField searchTextField = new JTextField(15);
        searchTextField.setText("PLate NO");
        search.add(searchTextField);
        
        RoundedButton searchButton = new RoundedButton("search", 15, new Color(73, 88, 181), new Color(59, 89, 182));
        searchButton.addActionListener(e -> 
			// Load data in background
			new SwingWorker<DefaultTableModel, Void>() {
				@Override
				protected DefaultTableModel doInBackground() throws Exception {
					String plate = searchTextField.getText().trim();
					return DBConnections.fetchVehicleHistoryModel("SELECT plate_number, owner_fname, user_id,check_in_time,check_out_time" + " FROM vehicle_history WHERE plate_number = '" +plate+ "'" );
				}
				@Override
				protected void done() {
					try {
						table.setModel(get());
					} catch (Exception ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(panel, "Error loading vehicle history: " + ex.getMessage());
					}
				}
			}.execute()
			);
        searchButton.setPreferredSize(new Dimension(100, 20));
        search.add(searchButton);
        
        /*JTextField deleteTextField = new JTextField(15);
        deleteTextField.setText("check in time");
        
        RoundedButton deleteButton = new RoundedButton("Delete", 15, new Color(73, 88, 181), new Color(59, 89, 182));
        deleteButton.addActionListener(e -> 
			// Load data in background
			new SwingWorker<DefaultTableModel, Void>() {
				@Override
				protected DefaultTableModel doInBackground() throws Exception {
					String value = deleteTextField.getText().trim();
					DBConnections.deleteVehicleByCheckIn(value);
					return DBConnections.fetchVehicleHistoryModel("SELECT plate_number, owner_fname, user_id,check_in_time,check_out_time FROM vehicle_history" );
				}
				@Override
				protected void done() {
					try {
						table.setModel(get());
					} catch (Exception ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(panel, "Error loading vehicle history: " + ex.getMessage());
					}
				}
			}.execute()
			);
        searchButton.setPreferredSize(new Dimension(100, 20));
        search.add(deleteButton);
        // export datebase panel
        JPanel bottom = new JPanel(new FlowLayout());
        bottom.add(deleteTextField);

		
        deleteButton.setPreferredSize(new Dimension(200, 20));
        bottom.add(deleteButton);


        
        panel.add(bottom, BorderLayout.SOUTH);*/

        panel.add(search, BorderLayout.NORTH);


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
		panel.add(notification);
		panel.add(label);
		
		return panel;
		
	}
	private JPanel regVehicleUI() {
    
    JPanel registrationUI = new JPanel(new BorderLayout());
    //registrationUI.setLayout(new BorderLayout());

    registrationUI.setBorder(BorderFactory.createRaisedBevelBorder());


    RoundedPanel login = new RoundedPanel(10);
    login.setBorder(BorderFactory.createRaisedBevelBorder());
    
    // Use GridBagLayout for precise control
    login.setSize(new Dimension(400,350));
    login.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(8, 8, 8, 8); // Padding
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;


    // Header
    JLabel registerVehicleLabel = new JLabel("Register Vehicle");
    registerVehicleLabel.setFont(new Font("Arial", Font.BOLD, 20));
    registerVehicleLabel.setForeground(new Color(73, 88, 181));
    gbc.gridwidth = 2;
    gbc.gridx = 0;
    gbc.gridy = 0;
    login.add(registerVehicleLabel, gbc);

    // Form fields
    gbc.gridwidth = 1;
    
    // License Plate
    gbc.gridy++;
    JLabel licencePlateLabel = new JLabel("License Plate:");
    login.add(licencePlateLabel, gbc);

    gbc.gridx = 1;
    licencePlateTextField = new JTextField(15);
    login.add(licencePlateTextField, gbc);
    gbc.gridx = 0;

    // Owner's Name
    gbc.gridy++;
    JLabel ownerNameLabel = new JLabel("Vehicle Type:");
    login.add(ownerNameLabel, gbc);

    gbc.gridx = 1;
    ownerNameTextField = new JTextField(15);
    login.add(ownerNameTextField, gbc);
    gbc.gridx = 0;

    // Permit Type
    gbc.gridy++;
    JLabel permitTypeLabel = new JLabel("Permit Type:");
    login.add(permitTypeLabel, gbc);

    gbc.gridx = 1;
    String[] permitTypes = {"Visitor", "Staff", "Student"};
    JComboBox<String> permitTypeComboBox = new JComboBox<>(permitTypes);
    login.add(permitTypeComboBox, gbc);

    // Button
    gbc.gridy++;
    gbc.gridwidth = 1;
    gbc.fill = GridBagConstraints.NONE;
    gbc.anchor = GridBagConstraints.CENTER;
    RoundedButton addVehicleButton = new RoundedButton("Check In", 20, new Color(73, 88, 181), new Color(59, 89, 182));
    addVehicleButton.addActionListener(e -> addVehicle(licencePlateTextField.getText(),ownerNameTextField.getText(),permitTypeComboBox.getSelectedItem()));
    addVehicleButton.setPreferredSize(new Dimension(200, 40));
    login.add(addVehicleButton, gbc);

    
   // adding login in panel to registerUI panel
    registrationUI.add(login);
    registrationUI.add(login, BorderLayout.NORTH);
    
    RoundedPanel checkout = new RoundedPanel(10);
    checkout.setBorder(BorderFactory.createRaisedBevelBorder());
    
    // Use GridBagLayout for precise control
    checkout.setSize(new Dimension(200,300));
    checkout.setLayout(new GridBagLayout());
    GridBagConstraints gbg = new GridBagConstraints();
    gbg.insets = new Insets(8, 8, 8, 8); // Padding
    gbg.anchor = GridBagConstraints.WEST;
    gbg.fill = GridBagConstraints.HORIZONTAL;


    // Header
    JLabel registerVehicle1Label = new JLabel("Check-Out Vehicle");
    registerVehicle1Label.setFont(new Font("Arial", Font.BOLD, 20));
    registerVehicle1Label.setForeground(new Color(73, 88, 181));
    gbg.gridwidth = 2;
    gbg.gridx = 0;
    gbg.gridy = 0;
    checkout.add(registerVehicle1Label, gbg);


    // Form fields
    gbg.gridwidth = 1;
    
    // License Plate
    gbg.gridy++;
    JLabel licencePlate1Label = new JLabel("License Plate:");
    checkout.add(licencePlate1Label, gbg);
    
    gbg.gridx = 1;
    JTextField licencePlate1TextField = new JTextField(15);
    checkout.add(licencePlate1TextField, gbg);
    //gbg.gridx = 0;



    // Button
    gbg.gridy++;
    //gbg.gridwidth = 2;
    gbg.fill = GridBagConstraints.NONE;
    gbg.anchor = GridBagConstraints.CENTER;
    RoundedButton addVehicleButton1 = new RoundedButton("Check Out", 20, new Color(73, 88, 181), new Color(59, 89, 182));
	addVehicleButton1.addActionListener(e -> {
		String plate = licencePlate1TextField.getText();
		if (plate.isBlank()) {
			JOptionPane.showMessageDialog(null, "Please enter a plate number.");
			return;
		}
		boolean success = DBConnections.checkOutVehicle(plate);
		if (success) {
			JOptionPane.showMessageDialog(null, 
				"Vehicle checked out successfully.");
		} else {
			JOptionPane.showMessageDialog(null, 
				"No active check-in found for plate: " + plate);
		}
	});

    addVehicleButton1.setPreferredSize(new Dimension(200, 40));
    checkout.add(addVehicleButton1, gbg);

    registrationUI.add(checkout);
    registrationUI.add(checkout, BorderLayout.SOUTH);
    return registrationUI;
}
// BackgroundPanel class
class BackgroundPanel extends JPanel {
    private ImageIcon backgroundImage;

    public BackgroundPanel(String imagePath) {
        backgroundImage = new ImageIcon(imagePath);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), null);
    }
}


    private JPanel viewLogsUI() {
        JPanel panel = new JPanel(new BorderLayout());
        JTable table = new JTable();
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder("System Logs"));
        panel.add(scroll, BorderLayout.CENTER);

        // Load data in background
        new SwingWorker<DefaultTableModel, Void>() {
            @Override
            protected DefaultTableModel doInBackground() throws Exception {
                return DBConnections.fetchLogsTableModel();
            }
            @Override
            protected void done() {
                try {
                    table.setModel(get());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(panel, "Error loading logs: " + ex.getMessage());
                }
            }
        }.execute();

        // search button on logs
        RoundedPanel search = new RoundedPanel(10);
        search.setBorder(BorderFactory.createRaisedBevelBorder());
        



        // export datebase panel
        JPanel bottom = new JPanel(new FlowLayout());


        RoundedButton csv = new RoundedButton("Export As CSV ", 15, new Color(73, 88, 181), new Color(59, 89, 182));
        csv.addActionListener(e -> {
			try {
				LogsCsvExporter.exportLogsToCsv("../data/logs.csv");
				JOptionPane.showMessageDialog(
					this,
					"Logs exported successfully to logs.csv",
					"Export Complete",
					JOptionPane.INFORMATION_MESSAGE
				);
			} catch (IOException | SQLException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(
					this,
					"Error exporting logs:\n" + ex.getMessage(),
					"Export Error",
					JOptionPane.ERROR_MESSAGE
				);
			}
		});
        csv.setPreferredSize(new Dimension(200, 20));
        bottom.add(csv);

        RoundedButton pdf = new RoundedButton("Export As PDF ", 15, new Color(73, 88, 181), new Color(59, 89, 182));
        pdf.addActionListener(e -> {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Save Logs as PDF");
		if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			try {
				// Assume you have a JTable named 'table' for logs:
				JTable tables = table;
				TablePdfExporter.exportTableToPdf(tables, file.getAbsolutePath());
				JOptionPane.showMessageDialog(this,
					"PDF exported to:\n" + file.getAbsolutePath(),
					"Export Successful",
					JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(this,
					"Error exporting PDF:\n" + ex.getMessage(),
					"Export Failed",
					JOptionPane.ERROR_MESSAGE);
			}
		}
	});

        pdf.setPreferredSize(new Dimension(200, 20));
        bottom.add(pdf);

        
        panel.add(bottom, BorderLayout.SOUTH);


        return panel;
    }
    public void addVehicle(String licencePlateTextField, String ownerNameTextField, Object permitTypeComboBox) {
        
        if (licencePlateTextField.isBlank()){

            JOptionPane.showMessageDialog(null, "Please Enter Plate No!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        else if(ownerNameTextField.isBlank()) {

        JOptionPane.showMessageDialog(null, "Please Enter OwnerName!", "Warning", JOptionPane.WARNING_MESSAGE);

        }
        else{
        String permit = (String)permitTypeComboBox;
        DBConnections.addVehicle(licencePlateTextField, ownerNameTextField, permit);

        JOptionPane.showMessageDialog(null, 
                   "Vehicle CheckedIn Suceesfull");
           this.licencePlateTextField.setText("");
           this.ownerNameTextField.setText("");
        }
    }
    public void deleteVehicle() {
        // 1) Prompt for the check-in time
        String input = JOptionPane.showInputDialog(
            this,
            "Enter the check-in timestamp to delete a vehicle record:\n" +
            "(format: yyyy-MM-dd HH:mm:ss)",
            "Delete Vehicle",
            JOptionPane.QUESTION_MESSAGE
        );
        if (input == null || input.trim().isEmpty()) {
            return;  // user cancelled or didn’t enter anything
        }

        try {
            // 2) Delegate to DBConnections
            boolean deleted = DBConnections.deleteVehicleByCheckIn(input.trim());
            if (deleted) {
                JOptionPane.showMessageDialog(
                    this,
                    "Vehicle record deleted successfully.",
                    "Deleted",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "No vehicle found with that check-in time.",
                    "Not Found",
                    JOptionPane.WARNING_MESSAGE
                );
            }
        } catch (IllegalArgumentException ex) {
            // Thrown if the timestamp string couldn’t be parsed
            JOptionPane.showMessageDialog(
                this,
                "Invalid timestamp format. Please use yyyy-MM-dd HH:mm:ss.",
                "Format Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    private void attachVehicleHistoryPopup(JTable table, JPanel parentPanel) {
    // Create the popup menu
    JPopupMenu popup = new JPopupMenu();
    JMenuItem deleteItem = new JMenuItem("Delete this entry");
    popup.add(deleteItem);

    // When “Delete this entry” is clicked
    deleteItem.addActionListener(e -> {
        int row = table.getSelectedRow();
        if (row < 0) return;

        // Get the check_in_time value from the model
        Object tsObj = table.getModel()
                            .getValueAt(row, table.getColumnModel()
                                                 .getColumnIndex("check_in_time"));
        if (!(tsObj instanceof java.sql.Timestamp)) return;
        String tsString = tsObj.toString(); // "YYYY-MM-DD HH:MM:SS.0"

        // Confirm with user
        int choice = JOptionPane.showConfirmDialog(
            parentPanel,
            "Delete vehicle checked in at " + tsString + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );
        if (choice != JOptionPane.YES_OPTION) return;

        // Perform deletion
        boolean ok = DBConnections.deleteVehicleByCheckIn(
                         tsString.substring(0, tsString.length() - 2)  // drop ".0"
                     );
        if (ok) {
            JOptionPane.showMessageDialog(parentPanel,
                "Deleted successfully.", "Deleted",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(parentPanel,
                "Could not delete. Row may already be gone.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Reload table model
        new SwingWorker<DefaultTableModel, Void>() {
            protected DefaultTableModel doInBackground() throws Exception {
                String sql = "SELECT plate_number, owner_fname, user_id, check_in_time, check_out_time "
                           + "FROM vehicle_history";
                return DBConnections.fetchVehicleHistoryModel(sql);
            }
            protected void done() {
                try {
                    table.setModel(get());
                    TableUtils.installCheckinCheckoutHighlighter(table);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.execute();
    });

    // Attach mouse listener for popup trigger
    table.addMouseListener(new MouseAdapter() {
        private void showIfPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    table.setRowSelectionInterval(row, row);
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        }
        @Override public void mousePressed(MouseEvent e)  { showIfPopup(e); }
        @Override public void mouseReleased(MouseEvent e) { showIfPopup(e); }
    });
}

}  

class Notification extends JLabel {
    private int notificationCount = DBConnections.getUnreadLogCount();

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
    private void attachVehicleHistoryPopup(JTable table, JPanel parentPanel) {
    // Create the popup menu
    JPopupMenu popup = new JPopupMenu();
    JMenuItem deleteItem = new JMenuItem("Delete this entry");
    popup.add(deleteItem);

    // When “Delete this entry” is clicked
    deleteItem.addActionListener(e -> {
        int row = table.getSelectedRow();
        if (row < 0) return;

        // Get the check_in_time value from the model
        Object tsObj = table.getModel()
                            .getValueAt(row, table.getColumnModel()
                                                 .getColumnIndex("check_in_time"));
        if (!(tsObj instanceof java.sql.Timestamp)) return;
        String tsString = tsObj.toString(); // "YYYY-MM-DD HH:MM:SS.0"

        // Confirm with user
        int choice = JOptionPane.showConfirmDialog(
            parentPanel,
            "Delete vehicle checked in at " + tsString + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );
        if (choice != JOptionPane.YES_OPTION) return;

        // Perform deletion
        boolean ok = DBConnections.deleteVehicleByCheckIn(
                         tsString.substring(0, tsString.length() - 2)  // drop ".0"
                     );
        if (ok) {
            JOptionPane.showMessageDialog(parentPanel,
                "Deleted successfully.", "Deleted",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(parentPanel,
                "Could not delete. Row may already be gone.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Reload table model
        new SwingWorker<DefaultTableModel, Void>() {
            protected DefaultTableModel doInBackground() throws Exception {
                String sql = "SELECT plate_number, owner_fname, user_id, check_in_time, check_out_time "
                           + "FROM vehicle_history";
                return DBConnections.fetchVehicleHistoryModel(sql);
            }
            protected void done() {
                try {
                    table.setModel(get());
                    TableUtils.installCheckinCheckoutHighlighter(table);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.execute();
    });

    // Attach mouse listener for popup trigger
    table.addMouseListener(new MouseAdapter() {
        private void showIfPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    table.setRowSelectionInterval(row, row);
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        }
        @Override public void mousePressed(MouseEvent e)  { showIfPopup(e); }
        @Override public void mouseReleased(MouseEvent e) { showIfPopup(e); }
    });
}
}
class TableUtils {
    private static final Color CHECKED_OUT_COLOR = new Color(200, 255, 200); // light green
    private static final Color LONG_IN_COLOR     = new Color(255, 200, 200); // light red

    /**
     * Installs a row‐coloring renderer on the given table.
     * 
     * Rows with a non‐null "check_out_time" cell will be painted green.
     * Rows with null "check_out_time" and in‐table "check_in_time" older
     * than 24h will be painted red. Others stay white.
     */
    public static void installCheckinCheckoutHighlighter(JTable table) {
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl,
                                                           Object value,
                                                           boolean isSelected,
                                                           boolean hasFocus,
                                                           int row,
                                                           int column) {
                // Let the default renderer set up the component
                Component c = super.getTableCellRendererComponent(
                                  tbl, value, isSelected, hasFocus, row, column);

                // If selected, preserve the L&F selection color
                if (isSelected) return c;

                // Look up the model and column indices
                DefaultTableModel model = (DefaultTableModel) tbl.getModel();
                int idxOut  = model.findColumn("check_out_time");
                int idxIn   = model.findColumn("check_in_time");

                Object outObj = idxOut >= 0 ? model.getValueAt(row, idxOut) : null;
                Object inObj  = idxIn  >= 0 ? model.getValueAt(row, idxIn ) : null;

                // Rule 1: checked out → green
                if (outObj != null) {
                    c.setBackground(CHECKED_OUT_COLOR);

                } else if (inObj instanceof java.sql.Timestamp) {
                    // Rule 2: still in & >24h → red
                    java.sql.Timestamp inTs = (java.sql.Timestamp) inObj;
                    long hours = java.time.Duration
                        .between(inTs.toLocalDateTime(), java.time.LocalDateTime.now())
                        .toHours();
                    c.setBackground(hours >= 24 ? LONG_IN_COLOR : Color.WHITE);

                } else {
                    c.setBackground(Color.WHITE);
                }

                return c;
            }
        });
    }
}
