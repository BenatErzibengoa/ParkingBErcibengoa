package com.lksnext.parkingbercibengoa.view.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import com.lksnext.parkingbercibengoa.databinding.FragmentHistorialBinding;
import com.lksnext.parkingbercibengoa.viewmodel.HistorialViewModel;


public class HistorialFragment extends BaseReservasFragment<HistorialViewModel> {

    private FragmentHistorialBinding binding;
    private HistorialViewModel viewModel;

    @Override
    protected View inflarLayout(LayoutInflater inflater, @Nullable ViewGroup container) {
        binding = FragmentHistorialBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    protected ViewGroup getContainerReservas() {
        return binding.containerReservas;
    }

    @Override
    protected HistorialViewModel getViewModel() {
        if (viewModel == null)
            viewModel = new ViewModelProvider(this).get(HistorialViewModel.class);
        return viewModel;
    }

    @Override
    protected void observarReservas() {
        getViewModel().getReservas().observe(getViewLifecycleOwner(),
                reservas -> mostrarReservas(reservas, true)); // orden descendente
        getViewModel().cargarReservasDelUsuario();
    }

    @Override
    protected void onExtraViewReady() {
        // Nada que hacer aquí por ahora
    }
}
