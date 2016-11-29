package com.lacf.luisadrian.vendexpress.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lacf.luisadrian.vendexpress.R;

public class Ventas extends Fragment {

    public Ventas() {
        // Required empty public constructor
    }

    public static Ventas newInstance() {
        Ventas fragment = new Ventas();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ventas, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }
}
