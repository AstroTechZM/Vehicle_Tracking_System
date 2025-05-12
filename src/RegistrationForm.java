import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class RegistrationForm {
    private static final Color PRIMARY_COLOR = new Color(0, 51, 102);
    private static final Color SECONDARY_COLOR = new Color(255, 255, 255);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);

    public RegistrationForm() {
        JFrame frame = new JFrame("University Vehicle Registration");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(240, 240, 240));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Header Section
        JLabel titleLabel = new JLabel("Please fill in your details");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20))
        );
        formPanel.setOpaque(false);
		
		JTextField
        // Form Fields
        addFormField(formPanel, "First Name:", , 0);
        addFormField(formPanel, "Last Name:", new JTextField(20), 1);
        addFormField(formPanel, "Email:", new JTextField(20), 2);
        addFormField(formPanel, "Password:", new JPasswordField(20), 3);

        // Gender Selection
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ButtonGroup genderGroup = new ButtonGroup();
        JRadioButton maleRadio = createRadioButton("Male");
        JRadioButton femaleRadio = createRadioButton("Female");
        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);
        genderPanel.add(maleRadio);
        genderPanel.add(femaleRadio);
        addFormField(formPanel, "Gender:", genderPanel, 4);

        

        // Registration Button
        RoundedButton registerButton = new RoundedButton("Register",20,new Color(200, 50, 50), new Color(220, 70, 70));
        registerButton.setPreferredSize(new Dimension(200,40));
        //registerButton.setBounds(150, 200, 120, 30);
        registerButton.addActionListener(this::validateForm);

        gbc.gridx = 0;
        gbc.gridy = 6;
        
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        
        formPanel.add(registerButton, gbc);
		
		gbc.gridy = 7;
		gbc.gridx = 0;
		JLabel haveAccount = new JLabel("Already Have An account? log in");
		formPanel.add(haveAccount, gbc);
        // Add form to main panel
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(formPanel, gbc);
        

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private static void addFormField(JPanel panel, String label, Component field, int yPos) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel jLabel = new JLabel(label);
        jLabel.setFont(LABEL_FONT);
        jLabel.setForeground(PRIMARY_COLOR);

        gbc.gridx = 0;
        gbc.gridy = yPos;
        panel.add(jLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(field, gbc);
    }

    private static JRadioButton createRadioButton(String text) {
        JRadioButton radio = new JRadioButton(text);
        radio.setFont(LABEL_FONT);
        radio.setOpaque(false);
        radio.setFocusPainted(false);
        return radio;
    }

    private static void styleButton(AbstractButton button) {
        button.setFont(LABEL_FONT);
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(SECONDARY_COLOR);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
            BorderFactory.createEmptyBorder(10, 30, 10, 30))
        );
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
    private static void validateForm(String F_name,String L_name,String role,String password)
    {
		DBConnections.CreateAccount(F_name,L_name,role,password)
	}
}
