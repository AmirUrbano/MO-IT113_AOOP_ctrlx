/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;
import service.EmployeeService;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import model.Employee;
import service.AuthService;
import service.ITService;

/**
 *
 * 
 *
 
 *
 * @author Amir
 */
public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private int loginAttempts = 0;
    private static final int MAX_LOGIN_ATTEMPTS = 3;

    public LoginFrame() {
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("MotorPH | Secure Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(400, 550);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(44, 62, 80));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(320, 420));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(30, 30, 30, 30)
        ));

        JLabel lblLogo = new JLabel("MotorPH");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblLogo.setForeground(new Color(44, 62, 80));
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Management System");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(Color.GRAY);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameField = new JTextField();
        styleField(usernameField, "Employee ID");
        passwordField = new JPasswordField();
        styleField(passwordField, "Password");

   
        loginButton = new JButton("LOG IN");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setBackground(new Color(40, 167, 69)); // Success Green 
        loginButton.setForeground(Color.WHITE);
        
      
        loginButton.setPreferredSize(new Dimension(260, 45));
        loginButton.setMaximumSize(new Dimension(260, 45));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT); 
        
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        loginButton.addActionListener(e -> performLogin());

        // Assembly
        card.add(lblLogo);
        card.add(Box.createVerticalStrut(5));
        card.add(lblSub);
        card.add(Box.createVerticalStrut(40));
        card.add(usernameField);
        card.add(Box.createVerticalStrut(20));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(40));
        card.add(loginButton);

        mainPanel.add(card);
        add(mainPanel);

        this.getRootPane().setDefaultButton(loginButton);
    }

    private void styleField(JTextField field, String title) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(200, 200, 200)), title));
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter both username and password",
                "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        AuthService.LoginResult result = AuthService.getInstance().authenticate(username, password);
        if (result != null) {
            Employee user = result.employee;
            if (user == null) {
                JOptionPane.showMessageDialog(this,
                    "Login valid, but employee record not found in database.",
                    "Database Error", JOptionPane.ERROR_MESSAGE);
                return; 
            }

            JOptionPane.showMessageDialog(this,
                "Login successful! Welcome, " + user.getFirstName() + " " + user.getLastName(),
                "Success", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            
            SwingUtilities.invokeLater(() -> {
                switch (result.viewType) {
                    case IT_DASHBOARD:
                        new ITDashboardFrame(user, EmployeeService.getInstance(), ITService.getInstance()).setVisible(true);
                        break;
                    case MAIN_MGMT:
                        new MainFrame(user).setVisible(true);
                        break;
                    case SELF_SERVICE:
                    default:
                        new EmployeeDetailFrame(user, user, false).setVisible(true);
                        break;
                }
            });
        } else {
            loginAttempts++;
            if (loginAttempts >= MAX_LOGIN_ATTEMPTS) {
                JOptionPane.showMessageDialog(this,
                    "Maximum login attempts exceeded. Application will close.",
                    "Access Denied", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            } else {
                int remaining = MAX_LOGIN_ATTEMPTS - loginAttempts;
                JOptionPane.showMessageDialog(this,
                    "Invalid username or password.\nRemaining attempts: " + remaining,
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
                usernameField.requestFocus();
            }
        }
    }
}
