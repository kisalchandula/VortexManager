package views;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class WelcomePage extends JFrame {

    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                WelcomePage frame = new WelcomePage();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public WelcomePage() {
        // Frame setup
        setTitle("Vortex Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(204, 255, 204)); // Light green color
        setLocationRelativeTo(null); // Center the frame on the screen

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Center Panel
        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);

        // Footer Panel
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(204, 255, 204)); // Match the background
        JLabel titleLabel = new JLabel("Welcome to Vortex Manager");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel);
        return headerPanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        centerPanel.setBackground(new Color(204, 255, 204)); // Match the background

        // Create buttons
        JButton loginButton = createRoundedButton("Login");
        JButton registerButton = createRoundedButton("Register");

        // Add buttons and spacers
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(loginButton);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(registerButton);
        centerPanel.add(Box.createVerticalGlue());

        // Add ActionListeners
        loginButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Login button clicked!"));
        registerButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Register button clicked!"));

        return centerPanel;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(204, 255, 204)); // Match the background
        JLabel footerLabel = new JLabel("© 2024 Vortex Manager. All Rights Reserved.");
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        footerPanel.add(footerLabel);
        return footerPanel;
    }

    private JButton createRoundedButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(100, 30));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBorder(new RoundedBorder(15)); // Set rounded border
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        return button;
    }

    // Custom border class for rounded edges
    static class RoundedBorder implements Border {
        private int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius, radius, radius, radius);
        }

        @Override
        public boolean isBorderOpaque() {
            return true;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }
}
