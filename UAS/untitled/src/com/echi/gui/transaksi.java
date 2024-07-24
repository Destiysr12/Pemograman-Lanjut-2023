package com.echi.gui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;

public class transaksi extends JFrame {
    private JTable table1;
    private JPanel panelMain;
    private JLabel no;
    private JLabel nama;
    private JLabel total;
    private JLabel waktu;
    private JButton backButton;
    private DefaultTableModel tableModel;

    private static final String URL = "jdbc:mysql://localhost:3306/retaurant";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public transaksi() {
        setTitle("Daftar Transaksi");
        setContentPane(panelMain);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        tableModel = new DefaultTableModel(new String[]{"No", "Nama Customer", "Total", "Waktu Transaksi"}, 0);
        table1.setModel(tableModel);

        loadTransaksi();

        table1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                int selectedRow = table1.getSelectedRow();
                if (selectedRow >= 0) {
                    String noText = tableModel.getValueAt(selectedRow, 0).toString();
                    String namaText = tableModel.getValueAt(selectedRow, 1).toString();
                    String totalText = tableModel.getValueAt(selectedRow, 2).toString();
                    String waktuText = tableModel.getValueAt(selectedRow, 3).toString();
                    no.setText(noText);
                    nama.setText(namaText);
                    total.setText(totalText);
                    waktu.setText(waktuText);
                }
            }
        });

        setVisible(true);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showOptionDialog(
                        panelMain,
                        "Ke mana kamu mau pergi?",
                        "Pilih Tujuan",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new String[]{"Dashboard", "Login", "Cancel"},
                        "Dashboard"
                );
                if (option == 0) {
                    new DashboardWaitresForm().setVisible(true);
                    dispose();
                } else if (option == 1) {
                    new MainScreen().setVisible(true);
                    dispose();
                }
            }
        });
    }

    public static void insertTransaction(String customer, double totalHarga, String tanggal) {
        String query = "INSERT INTO transaksi (nama_customer, harga_total, waktu_transaksi) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, customer);
            statement.setDouble(2, totalHarga);
            statement.setString(3, tanggal);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertTransaction(Transaction transaction) {
        insertTransaction(transaction.getCustomer(), transaction.getTotalHarga(), transaction.getTanggal());
    }

    private void loadTransaksi() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM transaksi")) {

            int no = 1;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            while (resultSet.next()) {
                String namaCustomer = resultSet.getString("nama_customer");
                Timestamp waktuTransaksi = resultSet.getTimestamp("waktu_transaksi");
                int hargaTotal = resultSet.getInt("harga_total");
                String waktuTransaksiStr = dateFormat.format(waktuTransaksi);
                tableModel.addRow(new Object[]{no++, namaCustomer, hargaTotal, waktuTransaksiStr});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new transaksi());
    }
}
