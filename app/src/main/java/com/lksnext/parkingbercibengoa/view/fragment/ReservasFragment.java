package com.lksnext.parkingbercibengoa.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;

import com.lksnext.parkingbercibengoa.R;
import com.lksnext.parkingbercibengoa.databinding.FragmentReservasBinding;
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
            viewModel = ReservasViewModelFactory.getSharedInstance();
        }
        return viewModel;
    }

    //Necesario para visualizar las nuevas reservas creadas
    private ActivityResultLauncher<Intent> nuevaReservaLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    getViewModel().cargarReservasDelUsuario();
                }
            }
    );
    @Override
    protected void observarReservas() {
        getViewModel().getReservas().observe(getViewLifecycleOwner(),
                reservas -> mostrarReservas(reservas, false)); // orden ascendente
        getViewModel().cargarReservasDelUsuario();
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
