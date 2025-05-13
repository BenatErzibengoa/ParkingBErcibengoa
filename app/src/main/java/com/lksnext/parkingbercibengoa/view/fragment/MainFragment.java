package com.lksnext.parkingbercibengoa.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.lksnext.parkingbercibengoa.R;


public class MainFragment extends Fragment {
    public MainFragment() {
        // Es necesario un constructor vacio
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Asignar la vista (layout) al fragmento
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
}
