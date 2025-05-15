import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Test extends JLabel {
    private int notificationCount = 0;
    private ImageIcon originalIcon;
    private static final int ICON_SIZE = 48;

    public Test() {
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
            int badgeSize = 20;
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



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Notification Icon Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

            Test notificationIcon = new Test();
            
            JPanel controlPanel = new JPanel();
            controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
            controlPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

            JButton addButton = new JButton("Add Notification");
            JButton clearButton = new JButton("Clear Notifications");

            addButton.addActionListener(e -> 
                notificationIcon.setNotificationCount(notificationIcon.notificationCount + 1)
            );

            clearButton.addActionListener(e -> 
                notificationIcon.setNotificationCount(0)
            );

            controlPanel.add(addButton);
            controlPanel.add(Box.createVerticalStrut(10));
            controlPanel.add(clearButton);

            frame.add(notificationIcon);
            frame.add(controlPanel);
            
            frame.pack();
            frame.setSize(300, 200);
            frame.setVisible(true);
        });
    }
}
