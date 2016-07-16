package com.szpilkowski.android.pelnymagazynek.API;

import com.szpilkowski.android.pelnymagazynek.Info.LoginInfo;
import com.szpilkowski.android.pelnymagazynek.RegistrationData;
import com.szpilkowski.android.pelnymagazynek.LoginCredentials;

import java.lang.reflect.Array;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by szpileq on 2016-05-30.
 */

public interface MyApiEndpointInterface {
    // Request method and URL specified in the annotation
    // Callback for the parsed response is the last parameter

    @POST("oauth/token")
    Call<LoginInfo> getToken(@Body LoginCredentials loginCredentials);

    @POST("users")
    Call<LoginInfo> createUser(@Body RegistrationData signUpRequest);

    @GET("warehouses")
    Call<ArrayList> getWarehouses();

}