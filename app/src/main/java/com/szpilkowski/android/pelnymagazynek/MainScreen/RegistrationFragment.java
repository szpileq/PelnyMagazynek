package com.szpilkowski.android.pelnymagazynek.MainScreen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.szpilkowski.android.pelnymagazynek.API.LoginInfo;
import com.szpilkowski.android.pelnymagazynek.API.RegistrationData;
import com.szpilkowski.android.pelnymagazynek.API.UserRegistrationData;
import com.szpilkowski.android.pelnymagazynek.R;
import com.szpilkowski.android.pelnymagazynek.Warehouses.WarehousesActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by szpileq on 2016-05-28.
 */
public class RegistrationFragment extends Fragment {

    private static final String TAG = "LoginFragment";
    ApiConnector connector;
    SharedPreferences pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.registration_screen, null);

        final Button registrationButton = (Button)view.findViewById(R.id.registrationButton);
        final EditText registrationFirstname = (EditText)view.findViewById(R.id.registrationFirstName);
        final EditText registrationLastName = (EditText)view.findViewById(R.id.registrationLastName);
        final EditText registrationEmail = (EditText)view.findViewById(R.id.registrationEmail);
        final EditText registrationPassword = (EditText)view.findViewById(R.id.registrationPassword);
        final EditText registrationPasswordConfirm = (EditText)view.findViewById(R.id.registrationPasswordConfirm);

        connector = ApiConnector.getInstance();
        connector.setupLoginConnector();

        pref = this.getActivity().getSharedPreferences("AppPref", Context.MODE_PRIVATE);

        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //close the keyboard
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

                //prepare object for API call
                UserRegistrationData signUpData = new UserRegistrationData();
                signUpData.setUserFirstName(registrationFirstname.getText().toString());
                signUpData.setUserLastName(registrationLastName.getText().toString());
                signUpData.setUserEmail(registrationEmail.getText().toString());
                signUpData.setUserPassword(registrationPassword.getText().toString());
                signUpData.setUserPasswordConfirmation(registrationPasswordConfirm.getText().toString());

                RegistrationData signUpRequest = new RegistrationData();
                signUpRequest.setUser(signUpData);

                //Use API to obtain a token
                Call<LoginInfo> call = connector.apiService.createUser(signUpRequest);
                call.enqueue(new Callback<LoginInfo>() {
                    @Override
                    public void onResponse(Call<LoginInfo> call, Response<LoginInfo> response) {
                        int statusCode = response.code();

                        switch (statusCode){

                            case 201:
                                //Success, move to next activity, load warehouses
                                LoginInfo loginInfo = response.body(); //save it somewhere - db or intent
                                SharedPreferences.Editor edit = pref.edit();
                                edit.putString("AccessToken", "Bearer " + loginInfo.getAccessToken());
                                edit.apply();
                                Intent WarehousesMain = new Intent(getActivity(), WarehousesActivity.class);

                                startActivity(WarehousesMain);
                            case 422:
                                Snackbar snackbar = Snackbar
                                        .make(view, "E-mail already taken", Snackbar.LENGTH_LONG);

                                snackbar.show();

                        }
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