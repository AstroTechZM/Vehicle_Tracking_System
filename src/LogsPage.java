 import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class LogsPage {
    private static JPanel mainPanel;
    private static JFrame frame;
    private static JScrollPane scrollPane;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("System Logs Viewer");
            
            // Main panel setup
            mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            // Scroll pane setup
            scrollPane = new JScrollPane(mainPanel);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            
            // Button setup
            JButton btn = new JButton("Add Log Entry");
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(200, 30));
            btn.addActionListener(LogsPage::addLogEntry);
            
            // Add components
            mainPanel.add(btn);
            frame.add(scrollPane);
            
            // Frame configuration
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setVisible(true);
        });
    }

    private static void addLogEntry(ActionEvent e) {
        JPanel newLog = logPanel();
        mainPanel.add(newLog, mainPanel.getComponentCount() - 1); // Add before the button
        mainPanel.revalidate();
        mainPanel.repaint();
        
        // Scroll to the bottom of the viewport
        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }

    private static JPanel logPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // Log type
        JLabel typeLabel = new JLabel("LOGIN");
        typeLabel.setForeground(new Color(70, 130, 180));
        typeLabel.setFont(new Font("Arial", Font.BOLD, 12));
        typeLabel.setPreferredSize(new Dimension(80, 20));
        panel.add(typeLabel);
        
        panel.add(Box.createHorizontalStrut(20));
        
        
        // Get current timestamp
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    
        // Timestamp
        JLabel dateLabel = new JLabel(timestamp);
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        dateLabel.setPreferredSize(new Dimension(180, 20));
        panel.add(dateLabel);
        
        panel.add(Box.createHorizontalStrut(20));
        
        // IP Address
        
        JLabel ipLabel = new JLabel( NetworkUtils.getLocalIPAddress());
        ipLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        ipLabel.setPreferredSize(new Dimension(120, 20));
        panel.add(ipLabel);
        
        panel.add(Box.createHorizontalStrut(20));
        
        // User info
        JLabel userLabel = new JLabel("admin_user");
        userLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        panel.add(userLabel);
        
        // Push everything left
        panel.add(Box.createHorizontalGlue());
        
        return panel;
    }
}



class NetworkUtils {
    public static String getLocalIPAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                // Skip loopback and inactive interfaces
                if (iface.isLoopback() || !iface.isUp()) continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    // Filter out IPv6 and loopback addresses
                    if (addr.isLoopbackAddress() || addr.getHostAddress().contains(":")) continue;
                    return addr.getHostAddress();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "127.0.0.1"; // Fallback to localhost
    }
}
