package com.sbsj.ordermatstaboss.DB;

import com.google.gson.annotations.SerializedName;

public class StoreInfoColumn {
    @SerializedName("id")
    private int id;
    @SerializedName("master_id")
    private int master_id;
    @SerializedName("name")
    private String name;
    @SerializedName("phone")
    private String phone;
    @SerializedName("category")
    private String category;
    @SerializedName("address")
    private String address;
    @SerializedName("address_detail")
    private String address_detail;
    @SerializedName("image")
    private String image;
    @SerializedName("detail")
    private String detail;
    @SerializedName("open")
    private int open;

    public int getId() {
        return id;
    }

    public int getMaster_id() {
        return master_id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getCategory() {
        return category;
    }

    public String getAddress() {
        return address;
    }

    public String getAddress_detail() {
        return address_detail;
    }

    public String getImage() {
        return image;
    }

    public String getDetail() {
        return detail;
    }

    public int getOpen() {
        return open;
    }

    public void setOpen(int open) {
        this.open = open;
    }
}
