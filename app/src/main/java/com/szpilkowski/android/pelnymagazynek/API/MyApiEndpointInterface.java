package com.szpilkowski.android.pelnymagazynek.API;

import com.szpilkowski.android.pelnymagazynek.DbModels.User;
import com.szpilkowski.android.pelnymagazynek.Info.LoginInfo;
import com.szpilkowski.android.pelnymagazynek.Info.SignUpInfo;
import com.szpilkowski.android.pelnymagazynek.LoginCredentials;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by szpileq on 2016-05-30.
 */

public interface MyApiEndpointInterface {
    // Request method and URL specified in the annotation
    // Callback for the parsed response is the last parameter

    @POST("oauth/token")
    Call<LoginInfo> getToken(@Body LoginCredentials loginCredentials);

    @POST("users")
    Call<SignUpInfo> createUser(@Body SignUpInfo signUpInfo);

}
