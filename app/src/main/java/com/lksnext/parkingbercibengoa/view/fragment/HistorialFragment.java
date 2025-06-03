package com.lksnext.parkingbercibengoa.view.fragment;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.lksnext.parkingbercibengoa.R;
import com.lksnext.parkingbercibengoa.configuration.Utils;
import com.lksnext.parkingbercibengoa.databinding.FragmentHistorialBinding;
import com.lksnext.parkingbercibengoa.databinding.FragmentReservasBinding;
import com.lksnext.parkingbercibengoa.databinding.ReservaItemBinding;
import com.lksnext.parkingbercibengoa.domain.Reserva;
import com.lksnext.parkingbercibengoa.viewmodel.HistorialViewModel;
import com.lksnext.parkingbercibengoa.viewmodel.ReservasViewModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;


public class HistorialFragment extends Fragment {

    private FragmentHistorialBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                          @Nullable Bundle savedInstanceState) {
        binding = FragmentHistorialBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        HistorialViewModel viewModel = new ViewModelProvider(this).get(HistorialViewModel.class);

        // Observar las reservas y añadirlas dinámicamente
        viewModel.getReservas().observe(getViewLifecycleOwner(), reservas -> {
            binding.containerReservas.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(getContext());

            // 1. Agrupar reservas por fecha de inicio
            Map<LocalDate, List<Reserva>> reservasPorFecha = reservas.stream()
                    .collect(Collectors.groupingBy(
                            (Reserva r) -> r.getFechaInicio().toLocalDate(),
                            () -> new TreeMap<LocalDate, List<Reserva>>(Comparator.reverseOrder()),
                            Collectors.toList()
                    ));

            //Agrupar las reservas por fecha
            for (Map.Entry<LocalDate, List<Reserva>> entry : reservasPorFecha.entrySet()) {
                LocalDate fecha = entry.getKey();
                List<Reserva> reservasDelDia = entry.getValue();

                // Encabezado de fecha
                View fechaHeader = inflater.inflate(R.layout.fecha_item, binding.containerReservas, false);
                TextView textFechaHeader = fechaHeader.findViewById(R.id.textFechaHeader);
                textFechaHeader.setText(Utils.formatearFecha(fecha));
                binding.containerReservas.addView(fechaHeader);

                // Todas las reservas de esa fecha
                for (Reserva r : reservasDelDia) {
                    ReservaItemBinding card = ReservaItemBinding.inflate(inflater, binding.containerReservas, false);
                    String horaInicio = r.getFechaInicio().format(DateTimeFormatter.ofPattern("HH:mm"));
                    String horaFinal = r.getFechaInicio().plus(r.getDuracion()).format(DateTimeFormatter.ofPattern("HH:mm"));
                    card.textFecha.setText(String.format("%s - %s", horaInicio, horaFinal));
                    card.textPlaza.setText(HtmlCompat.fromHtml(
                            "<b>Plaza:</b> " + r.getPlaza().toString(), HtmlCompat.FROM_HTML_MODE_LEGACY));
                    card.textTipo.setText(Utils.formatearTipo(r.getVehiculo().getTipoVehiculo()));
                    card.textMatricula.setText(HtmlCompat.fromHtml(
                            "<b>Matrícula:</b> " + r.getVehiculo().getMatricula(), HtmlCompat.FROM_HTML_MODE_LEGACY));
                    //Cambiar el color de la etiqueta del tipo de vehículo
                    GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(getContext(), R.drawable.rounded_background);
                    if (drawable != null) {
                        drawable = (GradientDrawable) drawable.mutate(); // Evita que se afecten otras vistas
                        drawable.setColor(Utils.getColorByTipo(r.getVehiculo().getTipoVehiculo(), getContext()));
                        card.etiquetaTipo.setBackground(drawable);
                    }
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