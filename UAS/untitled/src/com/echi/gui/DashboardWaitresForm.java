package com.echi.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardWaitresForm extends JFrame {
    private JPanel mainPanel;
    private JButton manageMenuButton;
    private JButton daftarTransaksiButton;
    private JButton backButton;

    public DashboardWaitresForm() {
        setTitle("Dashboard Waitress");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);

        mainPanel = new JPanel();
        mainPanel.setBackground(new Color(175, 166, 170));
        mainPanel.setLayout(null);

        JLabel titleLabel = new JLabel("DASHBOARD WAITRESS", JLabel.CENTER);
        titleLabel.setBounds(50, 10, 300, 30);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(titleLabel);

        manageMenuButton = new JButton("MANAGE MENU");
        manageMenuButton.setBounds(50, 60, 130, 40);
        manageMenuButton.setBackground(new Color(255, 204, 204));
        manageMenuButton.setFont(new Font("Arial", Font.PLAIN, 11));
        manageMenuButton.addActionListener(e -> showMenuPanel());

        daftarTransaksiButton = new JButton("DAFTAR TRANSAKSI");
        daftarTransaksiButton.setBounds(200, 60, 160, 40);
        daftarTransaksiButton.setBackground(new Color(204, 229, 255));
        daftarTransaksiButton.setFont(new Font("Arial", Font.PLAIN, 11));
        daftarTransaksiButton.addActionListener(e -> showTransactionPanel());

        backButton = new JButton("Back");
        backButton.setBounds(150, 120, 100, 30);
        backButton.setBackground(new Color(200, 200, 200));
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.addActionListener(e -> {
            new MainScreen().setVisible(true);
            dispose();
        });

        mainPanel.add(manageMenuButton);
        mainPanel.add(daftarTransaksiButton);
        mainPanel.add(backButton);

        add(mainPanel);
        setVisible(true);
    }

    private void showMenuPanel() {
        ManageMenu manageMenu = new ManageMenu();
        manageMenu.setVisible(true);
    }

    private void showTransactionPanel() {
        transaksi transaksi = new transaksi();
        transaksi.setVisible(true);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DashboardWaitresForm().setVisible(true));
    }
}
