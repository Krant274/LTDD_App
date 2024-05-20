package com.example.myapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class HoanTat {

    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("lastUpdate")
    @Expose
    private String lastUpdate;
    @SerializedName("donHang")
    @Expose
    private String donHang;

    public long getId() {
return id;
}

    public void setId(long id) {
this.id = id;
}

    public HoanTat(long id, String lastUpdate, String donHang) {
        this.id = id;
        this.lastUpdate = lastUpdate;
        this.donHang = donHang;
    }

    public String getLastUpdate() {
return lastUpdate;
}

    public void setLastUpdate(String lastUpdate) {
this.lastUpdate = lastUpdate;
}

    public String getDonHang() {
return donHang;
}

    public void setDonHang(String donHang) {
this.donHang = donHang;
}

}