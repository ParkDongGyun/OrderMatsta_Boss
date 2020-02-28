package com.sbsj.ordermatstaboss.DB;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MasterInfo {
    @SerializedName("masterlist")
    private ArrayList<MasterInfoColumn> masterlist = new ArrayList<>();

    public ArrayList<MasterInfoColumn> getMasterlist() {
        return masterlist;
    }
}
