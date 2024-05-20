package com.example.myapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DangXuLi {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("donHang")
    @Expose
    private String donHang;

    public DangXuLi() {
    }

    public DangXuLi(String donHang) {

        this.donHang = donHang;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getDonHang() {
        return donHang;
    }

    public void setDonHang(String donHang) {
        this.donHang = donHang;
    }

}
