package com.szpilkowski.android.pelnymagazynek.Users;

import com.google.gson.annotations.SerializedName;

/**
 * Created by szpileq on 2016-07-28.
 */
public class NewUserRequest {

    @SerializedName("user_email")
    private String userEmail;

    @SerializedName("role_type")
    private String roleType;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String email) {
        this.userEmail = email;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }
}
