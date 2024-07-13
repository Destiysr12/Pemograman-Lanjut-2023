package com.desti.gui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MainScreen extends JFrame{
    private static final String URL = "jdbc:mysql://localhost:3306/contoh";
    private static final String USER = "root";
    private static final String PASSWORD = "destiyh12";
    private JPanel panelMain;
    private JTable jTableMahasiswa;
    private JTextField textFieldNim;
    private JTextField textFieldNama;
    private JTextField textFieldIpk;
    private JButton buttonAdd;
    private JButton buttonUpdate;
    private JButton buttonDelete;
    private JTextField textFieldFilter;
    private JButton buttonFilter;
    private JButton buttonClear;
    private JScrollPane scrollPane;
    private DefaultTableModel defaultTableModel = new DefaultTableModel();
    private String selectedNim = "";

    public MainScreen() {
        this.setContentPane(panelMain);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();

        refreshTable(
                getMahasiswa()
        );
        buttonAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nim = textFieldNim.getText();
                String nama = textFieldNama.getText();
                double ipk = Double.parseDouble(textFieldIpk.getText());

                Mahasiswa mahasiswa = new Mahasiswa();
                mahasiswa.setNim(nim);
                mahasiswa.setNama(nama);
                mahasiswa.setIpk(ipk);


                insertMahasiswa(mahasiswa);
                clearForm();
                refreshTable(getMahasiswa());
            }
        });

        this.jTableMahasiswa.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent event) {
                        int row = jTableMahasiswa.getSelectedRow();

                        if (row < 0)
                            return;

                        String nim = jTableMahasiswa.getValueAt(row, 0).toString();

                        if (selectedNim.equals(nim))
                            return;

                        selectedNim = nim;

                        String nama = jTableMahasiswa.getValueAt(row, 1).toString();
                        String ipk = jTableMahasiswa.getValueAt(row, 2).toString();

                        textFieldNim.setText(nim);
                        textFieldNama.setText(nama);
                        textFieldIpk.setText(ipk);

                    }
                }
        );
        buttonUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nim = textFieldNim.getText();
                String nama = textFieldNama.getText();
                double ipk = Double.parseDouble(textFieldIpk.getText());

                Mahasiswa mahasiswa = new Mahasiswa();
                mahasiswa.setNim(nim);
                mahasiswa.setNama(nama);
                mahasiswa.setIpk(ipk);

                updateMahasiswa(mahasiswa);
                clearForm();
                refreshTable(getMahasiswa());
            }
        });
        buttonDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nim = textFieldNim.getText();
                deleteMahasiswa(nim);
                clearForm();
                refreshTable(getMahasiswa());
            }
        });
        buttonClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });
        buttonFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nama = textFieldFilter.getText();

                refreshTable(filterMahasiswa(nama));
            }
        });
    }

    public static void main(String [] args) {
        MainScreen mainScreen = new MainScreen();
        mainScreen.setVisible(true);
    }

    private void clearForm() {
        textFieldNama.setText("");
        textFieldNim.setText("");
        textFieldIpk.setText("");
    }

    public void refreshTable(List<Mahasiswa> arrayListMahasiswa) {
        Object [][] data = new Object[arrayListMahasiswa.size()][3];

        for(int i = 0; i < arrayListMahasiswa.size(); i++) {
            data[i] = new Object[] {
                        arrayListMahasiswa.get(i).getNim(),
                        arrayListMahasiswa.get(i).getNama(),
                        arrayListMahasiswa.get(i).getIpk(),
                        };

        }

        defaultTableModel = new DefaultTableModel(
                data,
                new String[] {"NIM", "Nama", "Ipk"}
        );

        jTableMahasiswa.setModel(defaultTableModel);
    }

    private static void executeSql(String sql) {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            Statement statement = connection.createStatement();

            statement.executeUpdate(sql);

        } catch (Exception e) {
        }
    }

    private static ResultSet executeQuery(String query) {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            Statement statement = connection.createStatement();

            return statement.executeQuery(query);

        } catch (Exception e) {
            return null;
        }

    }

    private static void insertMahasiswa(Mahasiswa mahasiswa) {
        String sql = "insert into mahasiswa values (" +
                "'" + mahasiswa.getNim() + "', " +
                "'" + mahasiswa.getNama() + "', " +
                "'" + mahasiswa.getIpk() + "')";

        executeSql(sql);
    }

    private static void updateMahasiswa(Mahasiswa mahasiswa) {
        String sql = "update mahasiswa set  " +
                "nama = '" + mahasiswa.getNama() +"', " +
                "ipk = '" + mahasiswa.getIpk() +"' " +
                "where nim = '" + mahasiswa.getNim() +"'";

        executeSql(sql);
    }

    private static void deleteMahasiswa(String nim) {
        String sql = "delete from mahasiswa   " +
                "where nim = '" + nim +"'";

        executeSql(sql);
    }

    private static List<Mahasiswa> getMahasiswa() {
        List <Mahasiswa> arrayListMahasiswa = new ArrayList<>();

        ResultSet resultSet = executeQuery("select * from mahasiswa");

        try {
            while (resultSet.next()) {
                String nim = resultSet.getString("nim");
                String nama = resultSet.getString("nama");
                double ipk = Double.parseDouble(resultSet.getString("ipk"));

                System.out.print(nim);
                System.out.print(nama);
                System.out.print(ipk);
                System.out.println();

                Mahasiswa mahasiswa = new Mahasiswa();
                mahasiswa.setNim(nim);
                mahasiswa.setNama(nama);
                mahasiswa.setIpk(ipk);

                arrayListMahasiswa.add(mahasiswa);

            }
        } catch (Exception e) {
            return null;
        }

        return arrayListMahasiswa;

    }

    private static List<Mahasiswa> filterMahasiswa(String filterNama) {
        List <Mahasiswa> arrayListMahasiswa = new ArrayList<>();

        ResultSet resultSet = executeQuery("select * from mahasiswa where nama like '%" + filterNama + "%'");

        try {
            while (resultSet.next()) {
                String nim = resultSet.getString("nim");
                String nama = resultSet.getString("nama");
                double ipk = Double.parseDouble(resultSet.getString("ipk"));

                System.out.print(nim);
                System.out.print(nama);
                System.out.print(ipk);
                System.out.println();

                Mahasiswa mahasiswa = new Mahasiswa();
                mahasiswa.setNim(nim);
                mahasiswa.setNama(nama);
                mahasiswa.setIpk(ipk);

                arrayListMahasiswa.add(mahasiswa);

            }
        } catch (Exception e) {
            return null;
        }

        return arrayListMahasiswa;

    }

}
