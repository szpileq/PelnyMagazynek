package com.szpilkowski.android.pelnymagazynek.API;

import com.google.gson.annotations.SerializedName;

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

