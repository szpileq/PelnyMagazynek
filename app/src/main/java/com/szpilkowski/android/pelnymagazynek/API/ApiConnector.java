package com.szpilkowski.android.pelnymagazynek.API;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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

    public Gson gson;
    public Retrofit retrofit;
    public MyApiEndpointInterface apiService;
    public OkHttpClient client;

    public void setupLoginConnector() {
        this.gson  = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
        this.client = new OkHttpClient();
        this.retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
        this.apiService = retrofit.create(MyApiEndpointInterface.class);
    }

    public void setupApiConnector(final String authToken){

        this.gson  = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
        this.client = new OkHttpClient.Builder()
                .addInterceptor(
                        new Interceptor() {
                            @Override
                            public Response intercept(Interceptor.Chain chain) throws IOException {
                                Request original = chain.request();

                                // Request customization: add request headers
                                Request.Builder requestBuilder = original.newBuilder()
                                        .header("Authorization", authToken)
                                        .method(original.method(), original.body());

                                Request request = requestBuilder.build();
                                return chain.proceed(request);
                            }
                        })
                .build();
        this.retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
        this.apiService = retrofit.create(MyApiEndpointInterface.class);
    }
}
