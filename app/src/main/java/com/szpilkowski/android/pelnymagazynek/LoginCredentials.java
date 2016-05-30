package com.szpilkowski.android.pelnymagazynek;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by szpileq on 2016-05-30.
 */
public class LoginCredentials {

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("grant_type")
    private String grantType;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();


    /**
     *
     * @return
     * The username
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     * The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return
     * The password
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password
     * The password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @return
     * The grantType
     */
    public String getGrantType() {
        return grantType;
    }

    /**
     *
     * @param grantType
     * The grant_type
     */
    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}