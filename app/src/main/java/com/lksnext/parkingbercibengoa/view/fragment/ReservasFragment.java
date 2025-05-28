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
import com.lksnext.parkingbercibengoa.configuration.Utils;
import com.lksnext.parkingbercibengoa.databinding.FragmentReservasBinding;
import com.lksnext.parkingbercibengoa.databinding.ReservaItemBinding;
import com.lksnext.parkingbercibengoa.domain.Reserva;
import com.lksnext.parkingbercibengoa.viewmodel.ReservasViewModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.TreeMap;


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

        // boton nueva reserva
        binding.btnNuevaReserva.setOnClickListener(v -> {
            // TODO: Navegar a pantalla para crear nueva reserva
        });

        // Observar las reservas y añadirlas dinámicamente
        viewModel.getReservas().observe(getViewLifecycleOwner(), reservas -> {
            binding.containerReservas.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(getContext());

            // 1. Agrupar reservas por fecha de inicio
            Map<LocalDate, List<Reserva>> reservasPorFecha = reservas.stream()
                    .collect(Collectors.groupingBy(
                            r -> r.getFechaInicio().toLocalDate(),
                            TreeMap::new, // Ordenar por fecha ascendente
                            Collectors.toList()
                    ));
            //Creamos cards para cada fecha distinta
            for (Map.Entry<LocalDate, List<Reserva>> entry : reservasPorFecha.entrySet()) {
                LocalDate fecha = entry.getKey();
                List<Reserva> reservasDelDia = entry.getValue();

                // 2. Añadir encabezado de fecha
                View fechaHeader = inflater.inflate(R.layout.fecha_item, binding.containerReservas, false);
                TextView textFechaHeader = fechaHeader.findViewById(R.id.textFechaHeader);
                textFechaHeader.setText(fecha.toString()); // o usar un formato más bonito
                binding.containerReservas.addView(fechaHeader);

                // 3. Añadir las tarjetas de reserva de ese día
                for (Reserva r : reservasDelDia) {
                    ReservaItemBinding card = ReservaItemBinding.inflate(inflater, binding.containerReservas, false);

                    card.textFecha.setText(r.getFechaInicio().format(DateTimeFormatter.ofPattern("HH:mm")));
                    card.textPlaza.setText(r.getPlaza().toString());
                    card.textMatricula.setText(r.getVehiculo().getMatricula());
                    card.textTipo.setText(r.getVehiculo().getTipoVehiculo().toString());
                    card.textTipo.setBackgroundColor(Utils.getColorByTipo(r.getVehiculo().getTipoVehiculo(), getContext()));

                    binding.containerReservas.addView(card.getRoot());
                }
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