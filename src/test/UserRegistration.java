package test;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class UserRegistration extends JFrame {
	
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField firstname;
    private JTextField lastname;
    private JTextField email;
    private JTextField username;
    private JTextField mob;
    private JPasswordField passwordField;
    private JButton btnNewButton;
    
    
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UserRegistration frame = new UserRegistration();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public UserRegistration() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(400, 160, 900, 550); // Reduced height
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(245, 245, 245));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewUserRegister = new JLabel("New User Registration");
        lblNewUserRegister.setFont(new Font("Verdana", Font.BOLD, 30));
        lblNewUserRegister.setForeground(new Color(70, 130, 180));
        lblNewUserRegister.setBounds(250, 30, 400, 40);
        contentPane.add(lblNewUserRegister);

        JLabel lblName = new JLabel("First Name:");
        lblName.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblName.setBounds(50, 100, 120, 40);
        contentPane.add(lblName);

        JLabel lblNewLabel = new JLabel("Last Name:");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblNewLabel.setBounds(50, 170, 120, 40);
        contentPane.add(lblNewLabel);

        JLabel lblEmailAddress = new JLabel("Email Address:");
        lblEmailAddress.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblEmailAddress.setBounds(50, 240, 140, 40);
        contentPane.add(lblEmailAddress);

        firstname = new JTextField();
        firstname.setFont(new Font("Tahoma", Font.PLAIN, 16));
        firstname.setBounds(190, 100, 250, 40);
        contentPane.add(firstname);

        lastname = new JTextField();
        lastname.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lastname.setBounds(190, 170, 250, 40);
        contentPane.add(lastname);

        email = new JTextField();
        email.setFont(new Font("Tahoma", Font.PLAIN, 16));
        email.setBounds(190, 240, 250, 40);
        contentPane.add(email);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblUsername.setBounds(500, 100, 120, 40);
        contentPane.add(lblUsername);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblPassword.setBounds(500, 170, 120, 40);
        contentPane.add(lblPassword);

        JLabel lblMobileNumber = new JLabel("Mobile No:");
        lblMobileNumber.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblMobileNumber.setBounds(500, 240, 120, 40);
        contentPane.add(lblMobileNumber);

        username = new JTextField();
        username.setFont(new Font("Tahoma", Font.PLAIN, 16));
        username.setBounds(630, 100, 220, 40);
        contentPane.add(username);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Tahoma", Font.PLAIN, 16));
        passwordField.setBounds(630, 170, 220, 40);
        contentPane.add(passwordField);

        mob = new JTextField();
        mob.setFont(new Font("Tahoma", Font.PLAIN, 16));
        mob.setBounds(630, 240, 220, 40);

        contentPane.add(mob);

        btnNewButton = new JButton("Register");
        btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 18));
        btnNewButton.setBackground(new Color(70, 130, 180));
        btnNewButton.setForeground(Color.WHITE);
        btnNewButton.setBounds(630, 350, 200, 50);
        btnNewButton.setFocusPainted(false);
        btnNewButton.setBorder(BorderFactory.createEmptyBorder());
        btnNewButton.setContentAreaFilled(false);
        btnNewButton.setOpaque(true);
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String firstName = firstname.getText();
                String lastName = lastname.getText();
                String emailId = email.getText();
                String userName = username.getText();
                String mobileNumber = mob.getText();
                String password = new String(passwordField.getPassword());
                int len = mobileNumber.length();

                if (len != 12) {
                    JOptionPane.showMessageDialog(btnNewButton, "Enter a valid mobile number");
                    return;
                }

                try {
                    Connection connection = DriverManager.getConnection(
                            "jdbc:mysql://localhost:3306/mydb", "root", "Admin_2022");

                    String query = "INSERT INTO user VALUES('" + firstName + "','" + lastName + "','" +
                            emailId + "','" + userName + "','" + password + "','" + mobileNumber + "')";

                    Statement sta = connection.createStatement();
                    int x = sta.executeUpdate(query);
                    if (x == 0) {
                        JOptionPane.showMessageDialog(btnNewButton, "User already exists.");
                    } else {
                        JOptionPane.showMessageDialog(btnNewButton,
                                "Welcome, " + firstName + "! Your account has been created.");
                        firstname.setText("");
                        lastname.setText("");
                        email.setText("");
                        username.setText("");
                        passwordField.setText("");
                        mob.setText("");
                    }
                    connection.close();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        contentPane.add(btnNewButton);
    }

}
