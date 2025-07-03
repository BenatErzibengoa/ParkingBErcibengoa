package com.lksnext.parkingbercibengoa.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;

import com.lksnext.parkingbercibengoa.R;
import com.lksnext.parkingbercibengoa.databinding.FragmentReservasBinding;
import com.lksnext.parkingbercibengoa.domain.Reserva;
import com.lksnext.parkingbercibengoa.viewmodel.ReservasViewModel;
import com.lksnext.parkingbercibengoa.viewmodel.ReservasViewModelFactory;

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
        if (viewModel == null) {
            viewModel = ReservasViewModelFactory.getSharedInstance(requireActivity().getApplication());
        }
        return viewModel;
    }

    @Override
    protected void observarReservas() {
        getViewModel().getReservas().observe(getViewLifecycleOwner(), reservas -> {
            mostrarReservas(reservas, true);
        });

        getViewModel().getErrorCargarReservas().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), "Error al cargar reservas: " + error, Toast.LENGTH_SHORT).show();
            }
        });

        if (getViewModel().getReservas().getValue() == null || getViewModel().getReservas().getValue().isEmpty()) {
            getViewModel().cargarReservasDelUsuario();
        }
    }

    @Override
    protected void onExtraViewReady() {
        binding.btnNuevaReserva.setOnClickListener(v -> {
            NuevaReservaFragment nuevaReservaFragment = new NuevaReservaFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, nuevaReservaFragment) // flFragment es el contenedor de la Main Activity
                    .addToBackStack(null) // Boton back
                    .commit();
        });
    }
}
