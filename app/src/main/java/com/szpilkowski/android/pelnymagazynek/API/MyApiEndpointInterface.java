package com.szpilkowski.android.pelnymagazynek.API;

import com.szpilkowski.android.pelnymagazynek.DbModels.Warehouse;
import com.szpilkowski.android.pelnymagazynek.Info.LoginInfo;
import com.szpilkowski.android.pelnymagazynek.MainScreen.RegistrationData;
import com.szpilkowski.android.pelnymagazynek.MainScreen.LoginCredentials;

import java.util.List;

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
    Call<List<Warehouse>> getWarehouses();

    @POST("warehouses")
    Call<Warehouse> addWarehouse(@Body Warehouse warehouse);

}
