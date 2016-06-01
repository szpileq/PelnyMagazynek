package com.szpilkowski.android.pelnymagazynek;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.annotations.SerializedName;
import com.szpilkowski.android.pelnymagazynek.DbModels.User;

public class RegistrationData{

        @SerializedName("user")
        private UserRegistrationData user;

        /**
         *
         * @return
         * The user
         */
        public UserRegistrationData getUser() {
            return user;
        }

        /**
         *
         * @param user
         * The user
         */
        public void setUser(UserRegistrationData user) {
            this.user = user;
        }
}

