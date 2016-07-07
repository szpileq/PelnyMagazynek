package com.szpilkowski.android.pelnymagazynek.DbModels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Szpileq on 2016-07-06.
 */
public class Warehouse {

    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String name;

    @SerializedName("count")
    private Integer count;

    @SerializedName("role")
    private String role;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
