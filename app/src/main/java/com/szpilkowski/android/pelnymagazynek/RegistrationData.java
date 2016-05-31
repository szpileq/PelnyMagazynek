package com.szpilkowski.android.pelnymagazynek;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.annotations.SerializedName;
import com.szpilkowski.android.pelnymagazynek.DbModels.User;

public class RegistrationData {

    @SerializedName("user[first_name]")
    private String userFirstName;

    @SerializedName("user[last_name]")
    private String userLastName;

    @SerializedName("user[email]")
    private String userEmail;

    @SerializedName("user[password]")
    private String userPassword;

    @SerializedName("user[password_confirmation]")
    private String userPasswordConfirmation;

    /**
     * @return The userFirstName
     */
    public String getUserFirstName() {
        return userFirstName;
    }

    /**
     * @param userFirstName The user[first_name]
     */
    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    /**
     * @return The userLastName
     */
    public String getUserLastName() {
        return userLastName;
    }

    /**
     * @param userLastName The user[last_name]
     */
    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    /**
     * @return The userEmail
     */
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * @param userEmail The user[email]
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     * @return The userPassword
     */
    public String getUserPassword() {
        return userPassword;
    }

    /**
     * @param userPassword The user[password]
     */
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    /**
     * @return The userPasswordConfirmation
     */
    public String getUserPasswordConfirmation() {
        return userPasswordConfirmation;
    }

    /**
     * @param userPasswordConfirmation The user[password_confirmation]
     */
    public void setUserPasswordConfirmation(String userPasswordConfirmation) {
        this.userPasswordConfirmation = userPasswordConfirmation;
    }


}