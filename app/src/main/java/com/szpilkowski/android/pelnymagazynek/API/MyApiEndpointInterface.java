package com.szpilkowski.android.pelnymagazynek.API;

import com.szpilkowski.android.pelnymagazynek.DbModels.Item;
import com.szpilkowski.android.pelnymagazynek.DbModels.User;
import com.szpilkowski.android.pelnymagazynek.DbModels.Warehouse;
import com.szpilkowski.android.pelnymagazynek.Info.LoginInfo;
import com.szpilkowski.android.pelnymagazynek.MainScreen.RegistrationData;
import com.szpilkowski.android.pelnymagazynek.MainScreen.LoginCredentials;
import com.szpilkowski.android.pelnymagazynek.Users.UserRequest;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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
    Call<LoginInfo> createUser(@Body RegistrationData signUpRequest);

    @GET("warehouses")
    Call<List<Warehouse>> getWarehouses();

    @POST("warehouses")
    Call<Warehouse> addWarehouse(@Body Warehouse warehouse);

    @DELETE("warehouses/{id}")
    Call<ResponseBody> removeWarehouse(@Path("id") int id);

    @PUT("warehouses/{id}")
    Call<Warehouse> editWarehouse(@Path("id") int id, @Body Warehouse warehouse);

    @GET("warehouses/{id}/items")
    Call<List<Item>> getItems(@Path("id") int warehouse_id);

    @GET("warehouses/{id}/items")
    Call<Item> getItemByQr(@Path("id") int warehouse_id,
                           @Query("qr_code") String qr_code);

    @GET("warehouses/{id}/items")
    Call<Item> getItemByBarcode(@Path("id") int warehouse_id,
                           @Query("bar_code") String bar_code);

    @GET("items/{id}")
    Call<Item> getItem(@Path("id") int item_id);

    @POST("warehouses/{id}/items")
    Call<Item> addItem(@Path("id") int warehouseId, @Body Item item);

    @PUT("items/{id}")
    Call<Item> editItem(@Path("id") int item_id, @Body Item item);

    @DELETE("items/{id}")
    Call<ResponseBody> removeItem(@Path("id") int id);

    @GET("warehouses/{id}/users")
    Call<List<User>> getUsers(@Path("id") int warehouse_id);

    @POST("warehouses/{id}/users")
    Call<User> addUser(@Path("id") int warehouse_id, @Body UserRequest request);

    @DELETE("warehouses/{id}/users/{id2}")
    Call<ResponseBody> removeUser(@Path("id") int warehouse_id, @Path("id2") int userId);

    @PUT("warehouses/{id}/users/{id2}")
    Call<User> editUser(@Path("id") int warehouse_id, @Path("id2") int userId2, @Body UserRequest request);

}
