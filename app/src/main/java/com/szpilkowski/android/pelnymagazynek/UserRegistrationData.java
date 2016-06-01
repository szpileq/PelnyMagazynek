package com.szpilkowski.android.pelnymagazynek;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class UserRegistrationData {

    @SerializedName("first_name")
    private String userFirstName;

    @SerializedName("last_name")
    private String userLastName;

    @SerializedName("email")
    private String userEmail;

    @SerializedName("password")
    private String userPassword;

    @SerializedName("password_confirmation")
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
