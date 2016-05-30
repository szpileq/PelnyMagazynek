package com.szpilkowski.android.pelnymagazynek;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by szpileq on 2016-05-30.
 */
public class ApiConnector {

    private static ApiConnector Instance;

    public static ApiConnector getInstance() {
        if(Instance == null)
            Instance = new ApiConnector();
        return Instance;
    }

    public static final String BASE_URL = "http://pelny-magazynek.herokuapp.com/";

    private Gson gson;
    private Retrofit retrofit;
    public MyApiEndpointInterface apiService;

    public ApiConnector() {
        this.gson  = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();;
        this.retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        this.apiService = retrofit.create(MyApiEndpointInterface.class);
    }
}
