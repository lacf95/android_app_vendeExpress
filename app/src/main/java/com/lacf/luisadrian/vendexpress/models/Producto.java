package com.lacf.luisadrian.vendexpress.models;

/**
 * Creado por LuisAdrian el 26/11/2016.
 */

public class Producto {
    private String id;
    private String name;
    private String description;
    private String photo;
    private String price;
    private String seller;
    private String quantity;

    public Producto() {

    }

    public Producto(String name, String description, String photo, String price, String seller) {
        this.name = name;
        this.description = description;
        this.photo = photo;
        this.price = price;
        this.seller = seller;
    }

    public Producto(String id, String name, String description, String photo, String price, String seller) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.photo = photo;
        this.price = price;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
