package com.sbsj.ordermatstaboss.DB;

import com.google.gson.annotations.SerializedName;

public class GoodsInfoColumn {
    @SerializedName("id")
    private int id;
    @SerializedName("store_id")
    private int store_id;
    @SerializedName("name")
    private String name;
    @SerializedName("price")
    private int price;
    @SerializedName("detail")
    private String detail;
    @SerializedName("image")
    private String image;

    public int getId() {
        return id;
    }

    public int getStore_id() {
        return store_id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getDetail() {
        return detail;
    }

    public String getImage() {
        return image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
