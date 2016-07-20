package com.szpilkowski.android.pelnymagazynek;

import com.google.gson.annotations.SerializedName;

/**
 * Created by szpileq on 2016-07-20.
 */
public class NewWarehouseRequest {

    @SerializedName("user")
    private String warehouseName;

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

}
