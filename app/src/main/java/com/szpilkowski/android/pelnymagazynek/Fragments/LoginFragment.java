package com.szpilkowski.android.pelnymagazynek.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.szpilkowski.android.pelnymagazynek.ApiConnector;
import com.szpilkowski.android.pelnymagazynek.Info.LoginInfo;
import com.szpilkowski.android.pelnymagazynek.LoginCredentials;
import com.szpilkowski.android.pelnymagazynek.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by szpileq on 2016-05-28.
 */
public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";
    ApiConnector connector;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_screen, null);

        final Button loginButton = (Button)view.findViewById(R.id.loginButton);
        final EditText email = (EditText)view.findViewById(R.id.emailEditText);
        final EditText password = (EditText)view.findViewById(R.id.passwordEditText);

        connector = ApiConnector.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginCredentials loginData = new LoginCredentials();
                loginData.setUsername(email.toString());
                loginData.setPassword(password.toString());
                loginData.setGrantType("password");

                Call<LoginInfo> call = connector.apiService.getToken(loginData);
                call.enqueue(new Callback<LoginInfo>() {
                    @Override
                    public void onResponse(Call<LoginInfo> call, Response<LoginInfo> response) {
                        int statusCode = response.code();
                        LoginInfo loginInfo = response.body();
                        Log.i(TAG, "onResponse: API response handled.");
                    }

                    @Override
                    public void onFailure(Call<LoginInfo> call, Throwable t) {
                        Log.i(TAG, "onFailure: API call for logging failed");
                        // Log error here since request failed
                    }
                });
            }
        });

        return view;
    }

}