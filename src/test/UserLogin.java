package test;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

import javax.swing.*;
import javax.swing.border.*;

public class UserLogin extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField textField;
    private JPasswordField passwordField;
    private JButton btnNewButton;
    private JPanel contentPane;
    public static String loggedInUser = "";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UserLogin frame = new UserLogin();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public UserLogin() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(450, 190, 600, 500); // Reduced height
        setResizable(false);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(245, 245, 245));
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Login");
        lblNewLabel.setForeground(new Color(70, 130, 180));
        lblNewLabel.setFont(new Font("Verdana", Font.BOLD, 40));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setBounds(150, 20, 300, 50);
        contentPane.add(lblNewLabel);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setForeground(new Color(60, 60, 60));
        lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblUsername.setBounds(50, 120, 200, 40);
        contentPane.add(lblUsername);

        textField = new JTextField();
        textField.setFont(new Font("Tahoma", Font.PLAIN, 20));
        textField.setBounds(200, 120, 300, 40);
        textField.setBorder(new LineBorder(new Color(173, 216, 230), 2, true));
        contentPane.add(textField);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setForeground(new Color(60, 60, 60));
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblPassword.setBounds(50, 200, 200, 40);
        contentPane.add(lblPassword);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Tahoma", Font.PLAIN, 20));
        passwordField.setBounds(200, 200, 300, 40);
        passwordField.setBorder(new LineBorder(new Color(173, 216, 230), 2, true));
        contentPane.add(passwordField);

        btnNewButton = new JButton("Login");
        btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 20));
        btnNewButton.setBackground(new Color(70, 130, 180));
        btnNewButton.setForeground(Color.WHITE);
        btnNewButton.setBounds(200, 300, 150, 50);
        btnNewButton.setFocusPainted(false);
        btnNewButton.setBorder(BorderFactory.createEmptyBorder());
        btnNewButton.setContentAreaFilled(false);
        btnNewButton.setOpaque(true);
        btnNewButton.addActionListener((ActionEvent e) -> {
            String userName = textField.getText();
            String password = new String(passwordField.getPassword());
            try (Connection connection = DatabaseConnection.getConnection()) {
                if (connection == null) {
                    JOptionPane.showMessageDialog(btnNewButton, "Database connection failed!");
                    return;
                }

                PreparedStatement st = connection.prepareStatement(
                        "SELECT username, password FROM users WHERE username=? AND password=?");

                st.setString(1, userName);
                st.setString(2, password);
                ResultSet rs = st.executeQuery();

                if (rs.next()) {
                    dispose();
                    loggedInUser = userName;
                    MainWindow mainframe = new MainWindow();
                    mainframe.setVisible(true);
                    JOptionPane.showMessageDialog(btnNewButton, "You have successfully logged in!");
                } else {
                    JOptionPane.showMessageDialog(btnNewButton, "Wrong Username or Password");
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
                JOptionPane.showMessageDialog(btnNewButton, "An error occurred while processing your request.");
            }
        });

        contentPane.add(btnNewButton);
    }
}