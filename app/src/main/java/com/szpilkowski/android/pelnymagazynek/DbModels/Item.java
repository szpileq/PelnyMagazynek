package com.szpilkowski.android.pelnymagazynek.DbModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by szpileq on 2016-07-24.
 */

public class Item implements Parcelable {

    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String name;

    @SerializedName("quantity")
    private Integer quantity;

    @SerializedName("target_quantity")
    private Integer targetQuantity;

    @SerializedName("min_quantity")
    private Integer minQuantity;

    @SerializedName("bar_code")
    private String barcode;

    @SerializedName("qr_code")
    private String qrcode;

    @SerializedName("latitude")
    private Float latitude;

    @SerializedName("longitude")
    private Float longitude;

    @SerializedName("comment")
    private String comment;

    @SerializedName("updated_at")
    private String updated_at;

    public Item() {

    }

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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getTargetQuantity() {
        return targetQuantity;
    }

    public void setTargetQuantity(Integer targetQuantity) {
        this.targetQuantity = targetQuantity;
    }

    public Integer getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(Integer minQuantity) {
        this.minQuantity = minQuantity;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(quantity);
        dest.writeValue(targetQuantity);
        dest.writeValue(minQuantity);
        dest.writeString(barcode);
        dest.writeString(qrcode);
        dest.writeValue(latitude);
        dest.writeValue(longitude);
        dest.writeString(comment);
        dest.writeString(updated_at);
    }

    public Item(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.quantity = in.readInt();
        this.targetQuantity = (Integer) in.readValue(Integer.class.getClassLoader());
        this.minQuantity =(Integer) in.readValue(Integer.class.getClassLoader());
        this.barcode = in.readString();
        this.qrcode = in.readString();
        this.latitude = (Float) in.readValue(Float.class.getClassLoader());
        this.longitude = (Float) in.readValue(Float.class.getClassLoader());
        this.comment = in.readString();
        this.updated_at = in.readString();
    }

    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {

        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}
