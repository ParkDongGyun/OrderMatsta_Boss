package com.sbsj.ordermatstaboss.DB;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderDetailInfoColumn implements Serializable {

    @SerializedName("id")
    private int id;
    @SerializedName("order_id")
    private int order_id;
    @SerializedName("goods_id")
    private int goods_id;
    @SerializedName("amount")
    private int amount;
    @SerializedName("price")
    private int price;

    public int getId() {
        return id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public int getAmount() {
        return amount;
    }

    public int getPrice() {
        return price;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}