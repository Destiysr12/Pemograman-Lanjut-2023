package com.echi.gui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ManageMenu extends JFrame {
    private static final String URL = "jdbc:mysql://localhost:3306/retaurant";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private int index;
    private JPanel panelMain;
    private JTable jTableMenu;
    private JTextField tfMenu;
    private JTextField tfHarga;
    private JButton btnTambah;
    private JButton btnEdit;
    private JButton buttonHapus;
    private JButton backButton;
    private DefaultTableModel defaultTableModel = new DefaultTableModel();
    private String selectedMenu = "";

    public ManageMenu() {
        this.setContentPane(panelMain);
        this.setMinimumSize(new Dimension(450, 460));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);



        btnTambah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String Menu = tfMenu.getText();
                double harga = Double.parseDouble(tfHarga.getText());

                Menu menu = new Menu();
                menu.setMenu(Menu);
                menu.setHarga(harga);

                insertMenu(menu);
                refreshTable(getMenu());
                clearForm();
            }
        });

        jTableMenu.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                int row = jTableMenu.getSelectedRow();

                if (row < 0)
                    return;

                String Menu = jTableMenu.getValueAt(row, 1).toString();

                if (selectedMenu.equals(Menu))
                    return;

                selectedMenu = Menu;

                String harga = jTableMenu.getValueAt(row, 2).toString();
                index = row;
                System.out.println(index);
                tfMenu.setText(Menu);
                tfHarga.setText(harga);
            }
        });

        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedMenu.isEmpty()) return;

                String Menu = tfMenu.getText();
                double harga = Double.parseDouble(tfHarga.getText());

                Menu updateMenu = new Menu();
                updateMenu.setMenu(Menu);
                updateMenu.setHarga(harga);

                updateMenu(updateMenu);
                refreshTable(getMenu());
                clearForm();
            }
        });

        buttonHapus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String Menu = tfMenu.getText();
                hapusMenu(Menu);
                refreshTable(getMenu());
                clearForm();
            }
        });
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


        refreshTable(getMenu());

    }

    private void clearForm() {
        tfMenu.setText("");
        tfHarga.setText("");
    }

    public void refreshTable(List<Menu> arrayListMenu) {
        Object[][] data = new Object[arrayListMenu.size()][3];

        for (int i = 0; i < arrayListMenu.size(); i++) {
            data[i] = new Object[]{
                    arrayListMenu.get(i).getNo(),
                    arrayListMenu.get(i).getMenu(),
                    arrayListMenu.get(i).getHarga(),
            };
        }

        defaultTableModel = new DefaultTableModel(
                data,
                new String[]{"No", "Menu", "Harga"}
        );

        jTableMenu.setModel(defaultTableModel);
    }

    public static void executeSql(String sql) {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ResultSet executeQuery(String sql) {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement statement = connection.createStatement();
            return statement.executeQuery(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void insertMenu(Menu menu) {
        String sql = "INSERT INTO menu (menu,harga) VALUES (" +
                "'" + menu.getMenu() + "', " +
                menu.getHarga() + ")";
        executeSql(sql);
    }

    private static void updateMenu(Menu menu) {
        String sql = "UPDATE menu SET " +
                "harga = " + menu.getHarga() +
                " WHERE menu = '" + menu.getMenu() + "'";
        executeSql(sql);
    }

    private static void hapusMenu(String Menu) {
        String sql = "DELETE FROM menu WHERE menu = '" + Menu + "'";
        executeSql(sql);
    }

    private static List<Menu> getMenu() {
        List<Menu> arrayListMenu = new ArrayList<>();
        ResultSet resultSet = executeQuery("SELECT * FROM menu");

        try {
            while (resultSet.next()) {
                int No = resultSet.getInt("id");
                String nama = resultSet.getString("menu");
                double harga = resultSet.getDouble("harga");

                Menu menu = new Menu();
                menu.setNo(No);
                menu.setMenu(nama);
                menu.setHarga(harga);

                arrayListMenu.add(menu);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return arrayListMenu;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ManageMenu().setVisible(true);
            }
        });
    }
}

