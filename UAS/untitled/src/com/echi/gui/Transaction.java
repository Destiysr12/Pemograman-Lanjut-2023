package com.echi.gui;
public class Transaction {
    private String customer;
    private String menu;
    private double totalHarga;
    private String tanggal;

    public Transaction(String customer, String menu, double totalHarga, String tanggal) {
        this.customer = customer;
        this.menu = menu;
        this.totalHarga = totalHarga;
        this.tanggal = tanggal;
    }

    public String getCustomer() {
        return customer;
    }

    public String getMenu() {
        return menu;
    }

    public double getTotalHarga() {
        return totalHarga;
    }

    public String getTanggal() {
        return tanggal;
    }
}