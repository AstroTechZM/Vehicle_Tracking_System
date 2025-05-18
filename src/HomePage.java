import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import javax.swing.border.LineBorder;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;


public class HomePage extends JPanel
{

	public static JPanel home()
	{
        BackgroundPanel top = new BackgroundPanel("../lib/gate2.jpg");
		//JPanel top = new JPanel(new BorderLayout());
		top.add(createSummaryPanel(), BorderLayout.NORTH);
		//top.add(logo(), BorderLayout.EAST);
		//top.setBackground(Color.WHITE);
		return top;
	}
		
	private static JPanel createSummaryPanel() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setOpaque(false);
        
        JPanel gridPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        gridPanel.setSize(new Dimension(400, 300)); // Calculate total size
        
        // Add 6 panels (2 rows × 3 columns)
        gridPanel.setOpaque(false);
        gridPanel.add(livePanel("Total Checkins", DBConnections.getTodayCheckInCount() ,300,150));
        gridPanel.add(livePanel("Active guards", 1,300,150));
        gridPanel.add(livePanel("Total Check-outs", DBConnections.getTodayCheckOutCount(),300,150));
        gridPanel.add(livePanel("Alerts", DBConnections.getOverstayedVehicleCount(),300,150));
        mainPanel.add(gridPanel);
        return mainPanel;
    }
    private static JPanel livePanel(String words, int value,int Dimension1,int Dimension2) {
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

