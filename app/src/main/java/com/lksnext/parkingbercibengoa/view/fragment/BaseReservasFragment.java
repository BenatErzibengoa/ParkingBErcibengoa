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
import androidx.lifecycle.ViewModel;

import com.lksnext.parkingbercibengoa.R;
import com.lksnext.parkingbercibengoa.configuration.Utils;
import com.lksnext.parkingbercibengoa.databinding.ReservaItemBinding;
import com.lksnext.parkingbercibengoa.domain.Reserva;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public abstract class BaseReservasFragment<T extends ViewModel> extends Fragment {

    protected View rootView;

    protected abstract ViewGroup getContainerReservas();
    protected abstract T getViewModel();
    protected abstract void observarReservas();
    protected abstract void onExtraViewReady(); // Para cosas como el botón en ReservasFragment

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflarLayout(inflater, container);
        return rootView;
    }

    protected abstract View inflarLayout(LayoutInflater inflater, @Nullable ViewGroup container);

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onExtraViewReady();
        observarReservas();
    }

    //Este metodo mediante el boolean esFuturo sirve para dadas unas reservas mostrarlas como futuras
    //reservas o reservas del pasado basandose en que día es hoy. El orden en el que muestra dichas reservas
    //cambia(futuro --> hoy, mañana... // pasado --> hoy, ayer...
    protected void mostrarReservas(List<Reserva> reservas, boolean esFuturo) {
        getContainerReservas().removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getContext());

        // Si no hay reservas, mostrar mensaje
        if (reservas == null || reservas.isEmpty()) {
            TextView textNoReservas = new TextView(getContext());
            textNoReservas.setText("No hay reservas");
            textNoReservas.setTextSize(16);
            textNoReservas.setTextColor(ContextCompat.getColor(getContext(), android.R.color.darker_gray));
            textNoReservas.setGravity(android.view.Gravity.CENTER);
            textNoReservas.setPadding(16, 32, 16, 32);
            getContainerReservas().addView(textNoReservas);
            return;
        }

        LocalDate hoy = LocalDate.now();
        LocalDate hace30Dias = hoy.minusDays(30);

        // Filtrar reservas según si son futuras o pasadas
        List<Reserva> reservasFiltradas = reservas.stream()
                .filter(r -> {
                    LocalDate fecha = r.getFechaInicio().toLocalDate();
                    if (esFuturo) {
                        return !fecha.isBefore(hoy);  // Muestra solo las reservas futuras
                    } else {
                        return fecha.isBefore(hoy) && !fecha.isBefore(hace30Dias);  // Muestra solo reservas pasadas de los últimos 30 días
                    }
                })
                .collect(Collectors.toList());

        //No deberia ejecutarse, pero por si acaso
        if (reservasFiltradas.isEmpty()) {
            TextView textNoReservas = new TextView(getContext());
            textNoReservas.setText("No hay reservas " + (esFuturo ? "futuras" : "pasadas"));
            textNoReservas.setTextSize(16);
            textNoReservas.setTextColor(ContextCompat.getColor(getContext(), android.R.color.darker_gray));
            textNoReservas.setGravity(android.view.Gravity.CENTER);
            textNoReservas.setPadding(16, 32, 16, 32);
            getContainerReservas().addView(textNoReservas);
            return;
        }

        //Agrupamos las reservas en grupos (mismo grupo si son del mismo dia)

        Comparator<LocalDate> comparator = esFuturo ? Comparator.naturalOrder() : Comparator.reverseOrder();
        Map<LocalDate, List<Reserva>> reservasPorFecha = reservasFiltradas.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getFechaInicio().toLocalDate(),
                        () -> new TreeMap<>(comparator),
                        Collectors.toList()
                ));

        for (Map.Entry<LocalDate, List<Reserva>> entry : reservasPorFecha.entrySet()) {
            LocalDate fecha = entry.getKey();
            List<Reserva> reservasDelDia = entry.getValue();

            View fechaHeader = inflater.inflate(R.layout.fecha_item, getContainerReservas(), false);
            TextView textFechaHeader = fechaHeader.findViewById(R.id.textFechaHeader);
            textFechaHeader.setText(Utils.formatearFecha(fecha));
            getContainerReservas().addView(fechaHeader);

            for (Reserva r : reservasDelDia) {
                ReservaItemBinding card = ReservaItemBinding.inflate(inflater, getContainerReservas(), false);

                String horaInicio = r.getFechaInicio().format(DateTimeFormatter.ofPattern("HH:mm"));
                String horaFinal = r.getFechaInicio().plus(r.getDuracion()).format(DateTimeFormatter.ofPattern("HH:mm"));
                card.textFecha.setText(String.format("%s - %s", horaInicio, horaFinal));
                card.textPlaza.setText(HtmlCompat.fromHtml("<b>Plaza:</b> " + r.getPlaza(), HtmlCompat.FROM_HTML_MODE_LEGACY));
                card.textTipo.setText(Utils.formatearTipo(r.getVehiculo().getTipoVehiculo()));
                card.textMatricula.setText(HtmlCompat.fromHtml("<b>Matrícula:</b> " + r.getVehiculo().getMatricula(), HtmlCompat.FROM_HTML_MODE_LEGACY));

                GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(getContext(), R.drawable.rounded_background);
                if (drawable != null) {
                    drawable = (GradientDrawable) drawable.mutate();
                    drawable.setColor(Utils.getColorByTipo(r.getVehiculo().getTipoVehiculo(), getContext()));
                    card.etiquetaTipo.setBackground(drawable);
                }

                configurarListenersReserva(card, r, esFuturo);

                getContainerReservas().addView(card.getRoot());
            }
        }
    }

    //Este metodo sirve para que reservasFragment pueda configurar listeners de click por cada reserva
    //Aqui no hace falta definirlo
    protected void configurarListenersReserva(ReservaItemBinding card, Reserva reserva, boolean esFuturo) {
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rootView = null;
    }
}
