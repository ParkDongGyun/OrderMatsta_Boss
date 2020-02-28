package com.sbsj.ordermatstaboss.DB;

import com.google.gson.annotations.SerializedName;

public class MasterInfoColumn {
    @SerializedName("id")
    private int id;
    @SerializedName("directory")
    private String directory;
    @SerializedName("sns_id")
    private String sns_id;
    @SerializedName("name")
    private String name;
    @SerializedName("phone")
    private String phone;
    @SerializedName("gender")
    private String gender;
    @SerializedName("birthday")
    private String birthday;
    @SerializedName("date")
    private String date;

    public int getId() {
        return id;
    }

    public String getDirectory() {
        return directory;
    }

    public String getSns_id() {
        return sns_id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getDate() {
        return date;
    }
}
