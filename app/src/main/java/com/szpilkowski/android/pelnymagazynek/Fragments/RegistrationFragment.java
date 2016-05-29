package com.szpilkowski.android.pelnymagazynek.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.szpilkowski.android.pelnymagazynek.R;

/**
 * Created by szpileq on 2016-05-28.
 */
public class RegistrationFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.registration_screen, null);
    }
}