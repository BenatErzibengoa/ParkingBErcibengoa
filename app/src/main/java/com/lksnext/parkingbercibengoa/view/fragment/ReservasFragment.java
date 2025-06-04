package com.lksnext.parkingbercibengoa.view.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.Nullable;

import androidx.lifecycle.ViewModelProvider;
import com.lksnext.parkingbercibengoa.databinding.FragmentReservasBinding;
import com.lksnext.parkingbercibengoa.view.activity.NuevaReservaActivity;
import com.lksnext.parkingbercibengoa.viewmodel.ReservasViewModel;

public class ReservasFragment extends BaseReservasFragment<ReservasViewModel> {

    private FragmentReservasBinding binding;
    private ReservasViewModel viewModel;

    @Override
    protected View inflarLayout(LayoutInflater inflater, @Nullable ViewGroup container) {
        binding = FragmentReservasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    protected ViewGroup getContainerReservas() {
        return binding.containerReservas;
    }

    @Override
    protected ReservasViewModel getViewModel() {
        if (viewModel == null)
            viewModel = new ViewModelProvider(this).get(ReservasViewModel.class);
        return viewModel;
    }

    @Override
    protected void observarReservas() {
        getViewModel().getReservas().observe(getViewLifecycleOwner(),
                reservas -> mostrarReservas(reservas, false)); // orden ascendente
        getViewModel().cargarReservasDelUsuario();
    }

    @Override
    protected void onExtraViewReady() {
        binding.btnNuevaReserva.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), NuevaReservaActivity.class));
        });
    }
}
