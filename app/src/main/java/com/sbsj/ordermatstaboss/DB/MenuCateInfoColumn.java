package com.sbsj.ordermatstaboss.DB;

import com.google.gson.annotations.SerializedName;

public class MenuCateInfoColumn {
    @SerializedName("id")
    private int id;
    @SerializedName("store_id")
    private int store_id;
    @SerializedName("name")
    private String name;
    @SerializedName("sequence")
    private String sequence;

    public int getId() {
        return id;
    }

    public int getStore_id() {
        return store_id;
    }

    public String getName() {
        return name;
    }

    public String getSequence() {
        return sequence;
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

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }
}
