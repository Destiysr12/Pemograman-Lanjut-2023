package com.echi.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class MainScreen extends JFrame {
    private static final String URL = "jdbc:mysql://localhost:3306/retaurant";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private JTextField userField;
    private JPasswordField passField;
    private JLabel messageLabel;

    public MainScreen() {
        super("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new GridBagLayout());
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        GradientPanel gradientPanel = new GradientPanel();
        setContentPane(gradientPanel);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        ImageIcon originalIcon = new ImageIcon(MainScreen.class.getResource("/images/images.png"));
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(150, 100, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel imageLabel = new JLabel(scaledIcon);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(imageLabel, gbc);

        gbc.gridwidth = 1;

        JLabel userLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(userLabel, gbc);

        userField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(userField, gbc);

        JLabel passLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(passLabel, gbc);

        passField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(passField, gbc);

        JButton waiterButton = new JButton("Login as Waiterss");
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(waiterButton, gbc);

        JButton customerButton = new JButton("Login as Customer");
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(customerButton, gbc);

        messageLabel = new JLabel();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(messageLabel, gbc);

        waiterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticateUser("waitress", "Waitress");
            }
        });

        customerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {authenticateUser("customer", "Customer");
                menuCustomer menuCustomer = new menuCustomer();
                menuCustomer.setVisible(true);
            }
        });

        setVisible(true);
    }

    private void authenticateUser(String table, String role) {
        String username = userField.getText();
        String password = String.valueOf(passField.getPassword());

        String query = "SELECT * FROM " + table + " WHERE username=? AND password=?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                JOptionPane.showMessageDialog(this, "Welcome, " + username + " (" + role + ")");
                this.dispose();
                if (role.equals("Waitress")) {
                    new DashboardWaitresForm().setVisible(true);
                } else {
                    new DashboardWaitresForm().setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid " + role + " username atau password", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            int width = getWidth();
            int height = getHeight();
            Color color1 = Color.darkGray;
            Color color2 = Color.pink;
            GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, width, height);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainScreen();
            }
        });
    }
}
