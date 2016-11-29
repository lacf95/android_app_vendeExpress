package com.lacf.luisadrian.vendexpress.models;

/**
 * Creado por LuisAdrian el 24/11/2016.
 */

public class Cliente {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String seller;

    public Cliente() {

    }

    public Cliente(String name, String email, String phone, String address, String seller) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.seller = seller;
    }

    public Cliente(String id, String name, String email, String phone, String address, String seller) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.seller = seller;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }
}
