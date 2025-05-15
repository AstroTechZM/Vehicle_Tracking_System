
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
 import java.io.*;
import java.util.*;
import java.util.List;

public class VehicleTrackingSystem extends JFrame { private String currentRole; private String currentUserName;

    private JTextField plateField, ownerField;
    private JComboBox<String> permitType;
    private JTextArea logsDisplay;
    private JTable historyTable;
    private DefaultListModel<String> historyModel;
    private JPanel homeStatsPanel;

    private List<String> logs = new ArrayList<>();
    private int totalCheckIns = 0;
    private int totalCheckOuts = 0;
    private final int activeGuards = 1;
    private int alerts = 0;

    private File statsFile = new File("stats.txt");
    private File logsFile = new File("logs.txt");
    private File historyFile = new File("vehicle_history.csv");

    private String currentVehicle = null;
    private String checkInTime = null;

    public VehicleTrackingSystem(String role, String userName) {
        this.currentRole = role;
        this.currentUserName = userName;

        setTitle("Vehicle Tracking - " + role);
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        loadStats();
        loadLogs();

        JPanel sidebar = new JPanel(new GridLayout(6, 1));
        JButton homeBtn = new JButton("Home");
        JButton registerBtn = new JButton("Register Vehicle");
        JButton logsBtn = new JButton("View Logs");
        JButton historyBtn = new JButton("Vehicle History");
        JButton logoutBtn = new JButton("Log Out");
        JButton exitBtn = new JButton("Exit");

        sidebar.add(homeBtn);
        if (!role.equals("Visitor")) sidebar.add(registerBtn);
        if (role.equals("Staff")) {
            sidebar.add(logsBtn);
            sidebar.add(historyBtn);
        }
        sidebar.add(logoutBtn);
        sidebar.add(exitBtn);

        add(sidebar, BorderLayout.WEST);

        JPanel contentPanel = new JPanel(new CardLayout());
        add(contentPanel, BorderLayout.CENTER);

        // Home Panel
        homeStatsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        updateHomeStats();
        JPanel homeWrapper = new JPanel(new BorderLayout());
        homeWrapper.add(homeStatsPanel, BorderLayout.CENTER);

        // Register Panel
        JPanel registerPanel = new JPanel(new GridLayout(4, 2));
        plateField = new JTextField();
        ownerField = new JTextField();
        permitType = new JComboBox<>(new String[]{"Staff", "Student", "Visitor"});
        JButton checkInBtn = new JButton("Check In");

        registerPanel.add(new JLabel("License Plate:"));
        registerPanel.add(plateField);
        registerPanel.add(new JLabel("Owner's Name:"));
        registerPanel.add(ownerField);
        registerPanel.add(new JLabel("Permit Type:"));
        registerPanel.add(permitType);
        registerPanel.add(new JLabel(""));
        registerPanel.add(checkInBtn);

        // Logs Panel
        logsDisplay = new JTextArea();
        logsDisplay.setEditable(false);
        JScrollPane logsScroll = new JScrollPane(logsDisplay);
        JPanel logsPanel = new JPanel(new BorderLayout());
        logsPanel.add(logsScroll, BorderLayout.CENTER);

        // History Panel
        String[] cols = {"License Plate", "Owner", "Permit", "Check-In", "Check-Out"};
        String[][] data = loadVehicleHistory();
        historyTable = new JTable(data, cols);
        JScrollPane historyScroll = new JScrollPane(historyTable);
        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.add(historyScroll, BorderLayout.CENTER);

        // Add panels
        contentPanel.add(homeWrapper, "Home");
        contentPanel.add(registerPanel, "Register");
        contentPanel.add(logsPanel, "Logs");
        contentPanel.add(historyPanel, "History");

        CardLayout cl = (CardLayout) contentPanel.getLayout();

        homeBtn.addActionListener(e -> {
            updateHomeStats();
            cl.show(contentPanel, "Home");
        });
        registerBtn.addActionListener(e -> cl.show(contentPanel, "Register"));
        logsBtn.addActionListener(e -> {
            updateLogsDisplay();
            cl.show(contentPanel, "Logs");
        });
        historyBtn.addActionListener(e -> cl.show(contentPanel, "History"));

        checkInBtn.addActionListener(e -> checkIn());
        logoutBtn.addActionListener(e -> logOut());
        exitBtn.addActionListener(e -> {
            saveStats();
            saveLogs();
            System.exit(0);
        });

        setVisible(true);
    }

    private void checkIn() {
        String plate = plateField.getText().trim();
        String owner = ownerField.getText().trim();
        String permit = (String) permitType.getSelectedItem();

        if (plate.isEmpty() || owner.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled.");
            return;
        }

        currentVehicle = plate;
        checkInTime = new Date().toString();

        logs.add("CHECKED IN: " + plate + ", Owner: " + owner + ", Type: " + permit);
        totalCheckIns++;
        saveStats();
        saveLogs();
        appendVehicleHistory(plate, owner, permit, checkInTime, "-");
        updateHomeStats();

        JOptionPane.showMessageDialog(this, "Vehicle Checked In Successfully!");
    }

    private void logOut() {
        if (currentVehicle != null) {
            String checkoutTime = new Date().toString();
            logs.add("CHECKED OUT: " + currentVehicle);
            totalCheckOuts++;
            saveStats();
            saveLogs();
            updateLastCheckOutTime(currentVehicle, checkoutTime);
            updateHomeStats();
            currentVehicle = null;
            checkInTime = null;

            JOptionPane.showMessageDialog(this, "Vehicle Checked Out.");
        } else {
            JOptionPane.showMessageDialog(this, "No vehicle is currently checked in.");
        }
    }

    private JPanel createStatCard(String label, int value) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(33, 150, 243));
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel lbl = new JLabel(label);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Arial", Font.PLAIN, 16));
        JLabel val = new JLabel(String.valueOf(value), SwingConstants.CENTER);
        val.setForeground(Color.WHITE);
        val.setFont(new Font("Arial", Font.BOLD, 28));
        card.add(lbl, BorderLayout.NORTH);
        card.add(val, BorderLayout.CENTER);
        return card;
    }

    private void updateHomeStats() {
        homeStatsPanel.removeAll();
        homeStatsPanel.add(createStatCard("Total Checkins", totalCheckIns));
        homeStatsPanel.add(createStatCard("Active Guards", activeGuards));
        homeStatsPanel.add(createStatCard("Total Checkouts", totalCheckOuts));
        homeStatsPanel.add(createStatCard("Alerts", alerts));
        homeStatsPanel.revalidate();
        homeStatsPanel.repaint();
    }

    private void updateLogsDisplay() {
        StringBuilder builder = new StringBuilder();
        for (String log : logs) {
            builder.append(log).append("\n");
        }
        logsDisplay.setText(builder.toString());
    }

    private void saveStats() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(statsFile))) {
            bw.write(totalCheckIns + "\n" + totalCheckOuts + "\n" + alerts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadStats() {
        try {
            if (statsFile.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(statsFile));
                totalCheckIns = Integer.parseInt(br.readLine());
                totalCheckOuts = Integer.parseInt(br.readLine());
                alerts = Integer.parseInt(br.readLine());
                br.close();
            }
        } catch (Exception e) {
            totalCheckIns = 0;
            totalCheckOuts = 0;
            alerts = 0;
        }
    }

    private void saveLogs() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(logsFile))) {
            for (String log : logs) {
                bw.write(log + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadLogs() {
        try {
            if (logsFile.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(logsFile));
                String line;
                while ((line = br.readLine()) != null) {
                    logs.add(line);
                }
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void appendVehicleHistory(String plate, String owner, String permit, String checkIn, String checkOut) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(historyFile, true))) {
            bw.write(String.join(",", plate, owner, permit, checkIn, checkOut));
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateLastCheckOutTime(String plate, String checkoutTime) {
        try {
            List<String> lines = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader(historyFile));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(plate) && parts[4].equals("-")) {
                    parts[4] = checkoutTime;
                }
                lines.add(String.join(",", parts));
            }
            br.close();
            BufferedWriter bw = new BufferedWriter(new FileWriter(historyFile));
            for (String l : lines) bw.write(l + "\n");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[][] loadVehicleHistory() {
        List<String[]> data = new ArrayList<>();
        try {
            if (historyFile.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(historyFile));
                String line;
                while ((line = br.readLine()) != null) {
                    data.add(line.split(","));
                }
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data.toArray(new String[0][]);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JDialog loginDialog = new JDialog();
            loginDialog.setTitle("Login");
            loginDialog.setModal(true);
            loginDialog.setSize(300, 200);
            loginDialog.setLayout(new GridLayout(4, 2));

            JComboBox<String> roleBox = new JComboBox<>(new String[]{"Staff", "Student", "Visitor"});
            JTextField nameField = new JTextField();
            JPasswordField passField = new JPasswordField();

            loginDialog.add(new JLabel("Role:"));
            loginDialog.add(roleBox);
            loginDialog.add(new JLabel("Name/ID:"));
            loginDialog.add(nameField);
            loginDialog.add(new JLabel("Password (Staff only):"));
            loginDialog.add(passField);

            JButton loginBtn = new JButton("Login");
            loginDialog.add(new JLabel(""));
            loginDialog.add(loginBtn);

            loginBtn.addActionListener(e -> {
                String role = (String) roleBox.getSelectedItem();
                String name = nameField.getText();
                String pass = new String(passField.getPassword());

                if (role.equals("Staff") && !pass.equals("staff123")) {
                    JOptionPane.showMessageDialog(loginDialog, "Invalid staff password");
                    return;
                }
                loginDialog.dispose();
                new VehicleTrackingSystem(role, name);
            });

            loginDialog.setLocationRelativeTo(null);
            loginDialog.setVisible(true);
        });
    }

}