package com.lacf.luisadrian.vendexpress.models;

/**
 * Creado por LuisAdrian el 30/11/2016.
 */

public class Abono {
    private String id;
    private String payment;
    private String date;
    private String state;
    private String sale;
    private String customerName;
    private String customerAddress;
    private String customerPhone;
    private String total;

    public Abono(String payment, String date, String state, String sale, String customerName, String customerAddress, String customerPhone) {
        this.payment = payment;
        this.date = date;
        this.state = state;
        this.sale = sale;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPhone = customerPhone;
    }

    public Abono(String id, String payment, String date, String state, String sale, String customerName, String customerAddress, String customerPhone) {
        this.id = id;
        this.payment = payment;
        this.date = date;
        this.state = state;
        this.sale = sale;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPhone = customerPhone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
