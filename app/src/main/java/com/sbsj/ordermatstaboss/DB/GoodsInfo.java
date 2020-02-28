package com.sbsj.ordermatstaboss.DB;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GoodsInfo {
    @SerializedName("goodslist")
    private ArrayList<GoodsInfoColumn> goodslist = new ArrayList<>();

    public ArrayList<GoodsInfoColumn> getGoodsinfolist() {
        return goodslist;
    }
}
