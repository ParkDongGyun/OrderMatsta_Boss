package com.sbsj.ordermatstaboss.DB;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MasterDeviceInfoColumn implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("master_id")
    private int master_id;
    @SerializedName("device_id")
    private String device_id;
    @SerializedName("fb_token")
    private String fb_token;

    public int getId() {
        return id;
    }

    public int getMaster_id() {
        return master_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public String getFb_token() {
        return fb_token;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMaster_id(int master_id) {
        this.master_id = master_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public void setFb_token(String fb_token) {
        this.fb_token = fb_token;
    }
}
