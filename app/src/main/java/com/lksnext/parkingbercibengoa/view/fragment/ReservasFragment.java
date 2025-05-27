package com.lksnext.parkingbercibengoa.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.lksnext.parkingbercibengoa.R;
import com.lksnext.parkingbercibengoa.databinding.FragmentReservasBinding;
import com.lksnext.parkingbercibengoa.databinding.ReservaItemBinding;
import com.lksnext.parkingbercibengoa.domain.Reserva;
import com.lksnext.parkingbercibengoa.viewmodel.ReservasViewModel;

import java.util.List;

public class ReservasFragment extends Fragment {

    private FragmentReservasBinding binding;
    private ReservasViewModel viewModel;


    @Nullable
    @Override
    public android.view.View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                          @Nullable Bundle savedInstanceState) {
        binding = FragmentReservasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull android.view.View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ReservasViewModel.class);

        // Manejar botón para crear nueva reserva
        binding.btnNuevaReserva.setOnClickListener(v -> {
            // TODO: Navegar a pantalla para crear nueva reserva
        });

        // Observar las reservas y añadirlas dinámicamente
        viewModel.getReservas().observe(getViewLifecycleOwner(), reservas -> {
            binding.containerReservas.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(getContext());

            for (Reserva r : reservas) {
                ReservaItemBinding card = ReservaItemBinding.inflate(inflater, binding.containerReservas, false);
                card.textFecha.setText(r.getFechaInicio().toLocalTime().toString());
                card.textPlaza.setText(r.getPlaza().toString());
                card.textMatricula.setText(r.getVehiculo().getMatricula());
                card.textTipo.setText(r.getVehiculo().getTipoVehiculo().toString());

                binding.containerReservas.addView(card.getRoot());
            }
        });

        viewModel.cargarReservasDelUsuario();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Para evitar memory leaks
    }
}