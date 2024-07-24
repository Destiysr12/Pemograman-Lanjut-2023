package com.echi.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class menuCustomer extends JFrame {
    private JTable Menu;
    private JTextField tfMenu;
    private JTextField tfHarga;
    private JSpinner spinnerJumlah;
    private JButton btnTotalharga;
    private JButton addButton;
    private JButton deleteButton;
    private JTable tablePesanan;
    private JTextField tfFilter;
    private JButton btnFilter;
    private JPanel panelMain;
    private JButton btnPesan;
    private JButton closeButton;
    private JTextField textField1;

    private DefaultTableModel tableModel;
    private DefaultTableModel tablePesananModel;
    private List<String> selectedItems;
    private double totalHarga = 0.0;

    private static final String URL = "jdbc:mysql://localhost:3306/retaurant";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public menuCustomer() {
        setTitle("Daftar Menu");
        setContentPane(panelMain);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);

        tableModel = new DefaultTableModel(new String[]{"Menu", "Harga"}, 0);
        Menu.setModel(tableModel);
        tablePesananModel = new DefaultTableModel(new String[]{"Menu", "Jumlah"}, 0);
        tablePesanan.setModel(tablePesananModel);
        spinnerJumlah.setModel(new SpinnerNumberModel(1, 1, 100, 1));
        selectedItems = new ArrayList<>();

        loadMenu();

        addButton.addActionListener(e -> {
            int selectedRow = Menu.getSelectedRow();
            if (selectedRow >= 0) {
                String menu = tableModel.getValueAt(selectedRow, 0).toString();
                int jumlah = (int) spinnerJumlah.getValue();
                tablePesananModel.addRow(new Object[]{menu, jumlah});
                selectedItems.add(menu);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = tablePesanan.getSelectedRow();
            if (selectedRow >= 0) {
                tablePesananModel.removeRow(selectedRow);
                selectedItems.remove(selectedRow);
            }
        });

        btnTotalharga.addActionListener(e -> {
            totalHarga = 0.0;
            for (int i = 0; i < tablePesananModel.getRowCount(); i++) {
                String menu = tablePesananModel.getValueAt(i, 0).toString();
                int jumlah = Integer.parseInt(tablePesananModel.getValueAt(i, 1).toString());
                double price = getPriceForItem(menu);
                totalHarga += price * jumlah;
            }
            JOptionPane.showMessageDialog(panelMain, "Total Harga: " + totalHarga);
        });

        btnFilter.addActionListener(e -> {
            String filter = tfFilter.getText();
            filterMenu(filter);
        });

        btnPesan.addActionListener(e -> {
            String customer = textField1.getText();
            String tanggal = LocalDate.now().toString();
            StringBuilder menuBuilder = new StringBuilder();
            for (int i = 0; i < tablePesananModel.getRowCount(); i++) {
                String menu = tablePesananModel.getValueAt(i, 0).toString();
                int jumlah = Integer.parseInt(tablePesananModel.getValueAt(i, 1).toString());
                menuBuilder.append(menu).append(" (").append(jumlah).append("), ");
            }
            String menu = menuBuilder.toString();

            Transaction transaction = new Transaction(customer, menu, totalHarga, tanggal);
            menuCustomer.insertTransaction(transaction);

            JOptionPane.showMessageDialog(panelMain, "Pesanan diterima");

            clearOrderForm();
        });

        Menu.getSelectionModel().addListSelectionListener(event -> {
            int selectedRow = Menu.getSelectedRow();
            if (selectedRow >= 0) {
                String menu = tableModel.getValueAt(selectedRow, 0).toString();
                String harga = tableModel.getValueAt(selectedRow, 1).toString();
                tfMenu.setText(menu);
                tfHarga.setText(harga);
            }
        });

        closeButton.addActionListener(e -> dispose());

        setVisible(true);
    }

    public static void insertTransaction(Transaction transaction) {
        // Implement the logic to insert a transaction here
        // This method should be implemented according to your database schema and requirements
    }

    private void saveTransactionToDatabase(String customer, String menu, double totalHarga, String tanggal) {
        String sql = "INSERT INTO transaksi (nama_customer, pesanan, harga_total, waktu_transaksi) VALUES (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, customer);
            statement.setString(2, menu);
            statement.setDouble(3, totalHarga);
            statement.setString(4, tanggal);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearOrderForm() {
        tfMenu.setText("");
        tfHarga.setText("");
        tablePesananModel.setRowCount(0);
        totalHarga = 0.0;
        spinnerJumlah.setValue(1);
        textField1.setText("");
    }

    private void loadMenu() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM menu")) {

            while (resultSet.next()) {
                String menu = resultSet.getString("menu");
                double harga = resultSet.getDouble("harga");
                tableModel.addRow(new Object[]{menu, harga});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private double getPriceForItem(String menu) {
        double price = 0.0;
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT harga FROM menu WHERE menu = '" + menu + "'")) {

            if (resultSet.next()) {
                price = resultSet.getDouble("harga");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return price;
    }

    private void filterMenu(String filter) {
        tableModel.setRowCount(0);
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM menu WHERE menu LIKE '%" + filter + "%'")) {

            while (resultSet.next()) {
                String menu = resultSet.getString("menu");
                double harga = resultSet.getDouble("harga");
                tableModel.addRow(new Object[]{menu, harga});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(menuCustomer::new);
    }
}


