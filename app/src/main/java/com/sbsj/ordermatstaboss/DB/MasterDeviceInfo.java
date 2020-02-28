package com.sbsj.ordermatstaboss.DB;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MasterDeviceInfo {
    @SerializedName("masterdevicelist")
    private ArrayList<MasterDeviceInfoColumn> masterdevicelist = new ArrayList<>();

    public ArrayList<MasterDeviceInfoColumn> getMasterdevicelist() {
        return masterdevicelist;
    }
}
