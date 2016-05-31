package com.szpilkowski.android.pelnymagazynek.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.szpilkowski.android.pelnymagazynek.API.ApiConnector;
import com.szpilkowski.android.pelnymagazynek.Info.LoginInfo;
import com.szpilkowski.android.pelnymagazynek.LoginCredentials;
import com.szpilkowski.android.pelnymagazynek.R;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
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
        final View view = inflater.inflate(R.layout.login_screen, null);

        final Button loginButton = (Button)view.findViewById(R.id.loginButton);
        final EditText email = (EditText)view.findViewById(R.id.emailEditText);
        final EditText password = (EditText)view.findViewById(R.id.passwordEditText);

        connector = ApiConnector.getInstance();
        connector.setupLoginConnector();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //close the keyboard
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

                //prepare object for API call
                LoginCredentials loginData = new LoginCredentials();
                loginData.setUsername(email.getText().toString());
                loginData.setPassword(password.getText().toString());
                loginData.setGrantType("password");

                //Use API to obtain a token
                Call<LoginInfo> call = connector.apiService.getToken(loginData);
                call.enqueue(new Callback<LoginInfo>() {
                    @Override
                    public void onResponse(Call<LoginInfo> call, Response<LoginInfo> response) {
                        int statusCode = response.code();
                        if (statusCode == 200){
                            //Success, move to next activity, load warehouses
                            LoginInfo loginInfo = response.body(); //save it somewhere - db or intent

                        }
                        else if (statusCode == 401)
                        {
                            Snackbar snackbar = Snackbar
                                    .make(view, "Wrong email or password. Try again.", Snackbar.LENGTH_LONG);

                            snackbar.show();
                        }
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