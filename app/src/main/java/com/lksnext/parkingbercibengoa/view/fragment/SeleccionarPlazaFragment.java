package com.lksnext.parkingbercibengoa.view.fragment;

import static com.lksnext.parkingbercibengoa.domain.TipoVehiculo.COCHE;
import static com.lksnext.parkingbercibengoa.domain.TipoVehiculo.DISCAPACITADO;
import static com.lksnext.parkingbercibengoa.domain.TipoVehiculo.ELECTRICO;
import static com.lksnext.parkingbercibengoa.domain.TipoVehiculo.MOTO;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.gridlayout.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.lksnext.parkingbercibengoa.R;
import com.lksnext.parkingbercibengoa.configuration.Utils;
import com.lksnext.parkingbercibengoa.databinding.FragmentSeleccionarPlazaBinding;
import com.lksnext.parkingbercibengoa.domain.HorarioPlaza;
import com.lksnext.parkingbercibengoa.domain.Plaza;
import com.lksnext.parkingbercibengoa.domain.Reserva;
import com.lksnext.parkingbercibengoa.domain.TipoVehiculo;
import com.lksnext.parkingbercibengoa.domain.Usuario;
import com.lksnext.parkingbercibengoa.domain.Vehiculo;
import com.lksnext.parkingbercibengoa.viewmodel.ReservasViewModel;
import com.lksnext.parkingbercibengoa.viewmodel.ReservasViewModelFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class SeleccionarPlazaFragment extends Fragment {

    private ReservasViewModel viewModel;
    private FragmentSeleccionarPlazaBinding binding;
    private Plaza selectedSpot = null;
    private View currentSelectedView = null;
    private List<Plaza> plazas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSeleccionarPlazaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ReservasViewModelFactory.getSharedInstance(requireActivity().getApplication());

        if(viewModel.getReservaAEditar().getValue() != null){
            binding.reservarButton.setText("Editar reserva");
        }

        viewModel.getPlazas().observe(getViewLifecycleOwner(), plazas -> {
            Log.d("SeleccionarPlazaFragment", "plazas cargadas ");
            this.plazas = plazas;
            Map<Plaza, HorarioPlaza> horarios = viewModel.getHorariosPorPlaza().getValue();
            if (horarios != null) {
                createParkingGrid();
            }
        });

        viewModel.getHorariosPorPlaza().observe(getViewLifecycleOwner(), horarios -> {
            Log.d("SeleccionarPlazaFragment", "horarios de plaza cargados");
            if (this.plazas != null) {
                createParkingGrid();
            }
        });


        viewModel.obtenerPlazas(viewModel.gethoraInicio().getValue().toLocalDate());

        setupToolbar();

        Vehiculo vehiculo = viewModel.getVehiculoSeleccionado().getValue();
        if (vehiculo != null) {
            binding.textFecha.setText(Utils.parseSeleccionPlazaFecha(viewModel.gethoraInicio().getValue()));
            binding.textHorario.setText(Utils.parseSeleccionPlazaHora(viewModel.gethoraInicio().getValue(), viewModel.gethoraFin().getValue()));
            binding.textVehiculo.setText(vehiculo.getModelo() + " " + vehiculo.getMatricula());
        }

        binding.reservarButton.setOnClickListener(v -> confirmReservation());
    }

    private void setupToolbar() {
        Toolbar toolbar = binding.toolbar;
        if (getActivity() instanceof androidx.appcompat.app.AppCompatActivity) {
            androidx.appcompat.app.AppCompatActivity activity = (androidx.appcompat.app.AppCompatActivity) getActivity();
            activity.setSupportActionBar(toolbar);
            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                activity.getSupportActionBar().setTitle("Seleccionar plaza");
            }
        }

        toolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });
    }

    private void createParkingGrid() {
        GridLayout grid = binding.parkingGrid;
        grid.removeAllViews();

        if (plazas == null) return;

        int total = plazas.size();
        int rows = (int) Math.ceil(total / 4.0);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < 4; j++) {
                int index = i * 4 + j;
                if (index >= plazas.size()) break;

                Plaza spot = plazas.get(index);
                View spotView = createSpotView(spot);

                int squareSize = (int) (80 * getResources().getDisplayMetrics().density);
                squareSize = (int) Math.ceil(0.90 * squareSize);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = squareSize;
                params.height = squareSize;
                params.columnSpec = GridLayout.spec(j);
                params.rowSpec = GridLayout.spec(i);
                params.setMargins(8, 8, 8, 8);

                spotView.setLayoutParams(params);
                grid.addView(spotView);
            }
        }
    }

    private View createSpotView(Plaza spot) {
        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        int padding = (int) (8 * getResources().getDisplayMetrics().density);
        layout.setPadding(padding, padding, padding, padding);

        TextView iconView = new TextView(requireContext());
        iconView.setText(getEmojiForTipo(spot.getTipo()));
        iconView.setTextSize(27);
        iconView.setGravity(Gravity.CENTER);
        iconView.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black));

        layout.addView(iconView);

        TipoVehiculo tipoVehiculo = viewModel.getVehiculoSeleccionado().getValue().getTipoVehiculo();
        boolean esCompatible = isCompatible(tipoVehiculo, spot.getTipo());
        Map<Plaza, HorarioPlaza> horariosMap = viewModel.getHorariosPorPlaza().getValue();

        boolean isAvailable = false;
        Log.d("SeleccionarPlazaFragment", "horariosMap: " + horariosMap);
        if (horariosMap != null && horariosMap.containsKey(spot)) {
            HorarioPlaza horario = horariosMap.get(spot);

            LocalDateTime inicio = viewModel.gethoraInicio().getValue();
            LocalDateTime fin = viewModel.gethoraFin().getValue();
            Duration duracion = Duration.between(inicio, fin);

            isAvailable = horario.estaLibreDurante(inicio, duracion);    // Comprobar disponibilidad
            Log.d("SeleccionarPlazaFragment", "Plaza " + spot.getId() + " está disponible: " + isAvailable);

        };

        if (esCompatible && isAvailable) {
            layout.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.spot_available));
            layout.setOnClickListener(v -> selectSpot(v, spot));
        } else if (esCompatible) {
            layout.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.spot_occupied));
        } else {
            layout.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.spot_unavailable));
        }

        return layout;
    }

    private String getEmojiForTipo(TipoVehiculo tipo) {
        switch (tipo) {
            case COCHE:
                return "🚗";
            case ELECTRICO:
                return "⚡";
            case MOTO:
                return "🏍️";
            case DISCAPACITADO:
                return "♿";
            default:
                return "❓";
        }
    }

    private boolean isCompatible(TipoVehiculo vehiculo, TipoVehiculo plaza) {
        switch (vehiculo) {
            case COCHE:
                return plaza == COCHE;
            case ELECTRICO:
                return plaza == COCHE || plaza == ELECTRICO;
            case DISCAPACITADO:
                return plaza == COCHE || plaza == DISCAPACITADO;
            case MOTO:
                return plaza == MOTO;
            default:
                return false;
        }
    }

    private void selectSpot(View view, Plaza spot) {
        if (currentSelectedView != null) {
            currentSelectedView.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.spot_available));
        }

        view.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.spot_selected));
        currentSelectedView = view;
        selectedSpot = spot;

        binding.selectedInfo.setVisibility(View.VISIBLE);
        binding.selectedDetails.setText("Plaza " + spot.getId() + " - Tipo: " + spot.getTipo() + "\nCerca de la entrada principal");
        binding.reservarButton.setEnabled(true);
        binding.reservarButton.setAlpha(1.0f);
    }

    private void confirmReservation() {
        if (selectedSpot != null && getContext() != null) {

            Vehiculo vehiculo = viewModel.getVehiculoSeleccionado().getValue();
            Usuario usuario = viewModel.getUsuario().getValue();
            LocalDateTime inicio = viewModel.gethoraInicio().getValue();
            LocalDateTime fin = viewModel.gethoraFin().getValue();
            Duration duracion = Duration.between(inicio, fin);

            boolean estaEditando = viewModel.getReservaAEditar().getValue() != null;

            Reserva reserva = new Reserva(java.util.UUID.randomUUID().toString(), usuario, vehiculo, inicio, duracion, selectedSpot);


            if (estaEditando) {
                Reserva reservaVieja = viewModel.getReservaAEditar().getValue();
                reserva.setFechaInicio(inicio);
                reserva.setDuracion(duracion);
                reserva.setPlaza(selectedSpot);
                viewModel.editarReserva(reservaVieja, reserva);
                viewModel.setReservaAEditar(null);
            } else {
                viewModel.reservarPlaza(reserva);
            }

            new AlertDialog.Builder(requireContext())
                    .setTitle(estaEditando ? "¡Reserva actualizada!" : "¡Reserva confirmada!")
                    .setMessage("Plaza: " + selectedSpot.getId() + "\nFecha: " +
                                Utils.parseSeleccionPlazaFecha(inicio) + "\nHorario: " +
                                Utils.parseSeleccionPlazaHora(inicio, fin))
                    .setPositiveButton("OK", null)
                    .show();

            requireActivity().getSupportFragmentManager().popBackStack();
            requireActivity().getSupportFragmentManager().popBackStack();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

