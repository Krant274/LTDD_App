package com.example.myapplication.model;

import com.example.myapplication.R;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Food {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("price")
    @Expose
    private Double price;

    @SerializedName("urlImage")
    @Expose
    private String urlImage;

    private int quantity;
    public Food() {
    }


    public Food(int id, String name, Double price,String urlImage) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.urlImage = urlImage;
    }

    public Food(String name, Double price, String urlImage) {
        this.name = name;
        this.price = price;
        this.urlImage = urlImage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public String getItemName() {
        return name;
    }

    public String getUrlImage() {
        return urlImage;
    }
    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}