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

    protected void mostrarReservas(List<Reserva> reservas, boolean ordenDescendente) {
        getContainerReservas().removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getContext());

        Comparator<LocalDate> comparator = ordenDescendente ? Comparator.reverseOrder() : Comparator.naturalOrder();

        Map<LocalDate, List<Reserva>> reservasPorFecha = reservas.stream()
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

                getContainerReservas().addView(card.getRoot());
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rootView = null;
    }
}
