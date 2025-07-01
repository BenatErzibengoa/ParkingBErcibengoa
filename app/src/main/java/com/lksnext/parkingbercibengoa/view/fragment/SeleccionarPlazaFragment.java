package com.lksnext.parkingbercibengoa.view.fragment;

import static com.lksnext.parkingbercibengoa.domain.TipoVehiculo.COCHE;
import static com.lksnext.parkingbercibengoa.domain.TipoVehiculo.DISCAPACITADO;
import static com.lksnext.parkingbercibengoa.domain.TipoVehiculo.ELECTRICO;
import static com.lksnext.parkingbercibengoa.domain.TipoVehiculo.MOTO;

import android.app.AlertDialog;
import android.os.Bundle;
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
import com.lksnext.parkingbercibengoa.domain.Plaza;
import com.lksnext.parkingbercibengoa.domain.Reserva;
import com.lksnext.parkingbercibengoa.domain.TipoVehiculo;
import com.lksnext.parkingbercibengoa.domain.Usuario;
import com.lksnext.parkingbercibengoa.domain.Vehiculo;
import com.lksnext.parkingbercibengoa.viewmodel.ReservasViewModel;
import com.lksnext.parkingbercibengoa.viewmodel.ReservasViewModelFactory;

import java.time.Duration;
import java.time.LocalDateTime;

public class SeleccionarPlazaFragment extends Fragment {

    private ReservasViewModel viewModel;
    private FragmentSeleccionarPlazaBinding binding;
    private ParkingSpot selectedSpot = null;
    private View currentSelectedView = null;

    private final ParkingSpot[][] spots = {
            {new ParkingSpot("A1", COCHE, true, "🚗"),
             new ParkingSpot("A2", COCHE, false, "🚗"),
             new ParkingSpot("A3", COCHE, true, "🚗"),
             new ParkingSpot("A4", ELECTRICO, false, "⚡")},
            {new ParkingSpot("B1", COCHE, true, "🚗"),
             new ParkingSpot("B2", COCHE, true, "🚗"),
             new ParkingSpot("B3", COCHE, false, "🚗"),
             new ParkingSpot("B4", ELECTRICO, false, "⚡")},
            {new ParkingSpot("C1", COCHE, true, "🚗"),
             new ParkingSpot("C2", COCHE, false, "🚗"),
             new ParkingSpot("C3", COCHE, true, "🚗"),
             new ParkingSpot("C4", ELECTRICO, false, "⚡")},
            {new ParkingSpot("D1", MOTO, false, "🏍️"),
             new ParkingSpot("D2", MOTO, false, "🏍️"),
             new ParkingSpot("D3", DISCAPACITADO, false, "♿"),
             new ParkingSpot("D4", DISCAPACITADO, false, "♿")}
    };


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

        setupToolbar();

        String modelo = viewModel.getVehiculoSeleccionado().getValue().getModelo();
        String matricula = viewModel.getVehiculoSeleccionado().getValue().getMatricula();

        binding.textFecha.setText(Utils.parseSeleccionPlazaFecha(viewModel.gethoraInicio().getValue()));
        binding.textHorario.setText(Utils.parseSeleccionPlazaHora(viewModel.gethoraInicio().getValue(), viewModel.gethoraFin().getValue()));
        binding.textVehiculo.setText(modelo + " " + matricula);

        createParkingGrid();

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

        for (int i = 0; i < spots.length; i++) {
            for (int j = 0; j < spots[i].length; j++) {
                ParkingSpot spot = spots[i][j];
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

    private View createSpotView(ParkingSpot spot) {
        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        int padding = (int) (8 * getResources().getDisplayMetrics().density);
        layout.setPadding(padding, padding, padding, padding);

        TextView iconView = new TextView(requireContext());
        iconView.setText(spot.icon);
        iconView.setTextSize(27);
        iconView.setGravity(Gravity.CENTER);
        iconView.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black));

        layout.addView(iconView);

        // Obtener el tipo de vehículo seleccionado
        String tipoVehiculo = viewModel.getVehiculoSeleccionado().getValue().getTipoVehiculo().name().toLowerCase();

        boolean esCompatible = isCompatible(
                viewModel.getVehiculoSeleccionado().getValue().getTipoVehiculo(),
                spot.tipo
        );

        if (esCompatible && spot.isAvailable) {
            layout.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.spot_available));
            layout.setOnClickListener(v -> selectSpot(v, spot));
        } else if (esCompatible && !spot.isAvailable) {
            layout.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.spot_occupied));
        } else {
            layout.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.spot_unavailable));
        }

        return layout;
    }

    private boolean isCompatible(TipoVehiculo tipoVehiculo, TipoVehiculo tipoPlaza) {
        switch (tipoVehiculo) {
            case COCHE:
                return tipoPlaza == TipoVehiculo.COCHE;
            case ELECTRICO:
                return tipoPlaza == TipoVehiculo.COCHE || tipoPlaza == TipoVehiculo.ELECTRICO;
            case DISCAPACITADO:
                return tipoPlaza == TipoVehiculo.COCHE || tipoPlaza == TipoVehiculo.DISCAPACITADO;
            case MOTO:
                return tipoPlaza == TipoVehiculo.MOTO;
            default:
                return false;
        }
    }




    private void selectSpot(View view, ParkingSpot spot) {
        if (currentSelectedView != null) {
            currentSelectedView.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.spot_available));
        }

        view.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.spot_selected));
        currentSelectedView = view;
        selectedSpot = spot;

        binding.selectedInfo.setVisibility(View.VISIBLE);
        binding.selectedDetails.setText("Plaza " + spot.id + " - Tipo: " + spot.tipo +"\nCerca de la entrada principal");

        binding.reservarButton.setEnabled(true);
        binding.reservarButton.setAlpha(1.0f);
    }

    private void confirmReservation() {
        if (selectedSpot != null && getContext() != null) {

            // Recuperamos datos previamente guardados
            Vehiculo vehiculo = viewModel.getVehiculoSeleccionado().getValue();
            Usuario usuario = null;// TODO: Recuperar usuario actual
            LocalDateTime inicio = viewModel.gethoraInicio().getValue();
            LocalDateTime fin = viewModel.gethoraFin().getValue();
            Duration duracion = Duration.between(inicio, fin);

            // Crear objeto Plaza
            Plaza plaza = new Plaza();
            plaza.setId(selectedSpot.id);
            plaza.setTipo(selectedSpot.tipo);

            // Crear la reserva
            Reserva reserva = new Reserva(
                    java.util.UUID.randomUUID().toString(),  // ID aleatorio único
                    usuario,
                    vehiculo,
                    inicio,
                    duracion,
                    plaza
            );

            viewModel.reservarPlaza(reserva);

            // Mostrar confirmación
            new AlertDialog.Builder(requireContext())
                    .setTitle("¡Reserva confirmada!")
                    .setMessage("Plaza: " + selectedSpot.id + "\nFecha: " +
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
        binding = null; // Para evitar leaks
    }

    private static class ParkingSpot {
        String id;
        TipoVehiculo tipo;
        boolean isAvailable;
        String icon;

        ParkingSpot(String id, TipoVehiculo tipo, boolean isAvailable, String icon) {
            this.id = id;
            this.tipo = tipo;
            this.isAvailable = isAvailable;
            this.icon = icon;
        }

        private boolean isCompatible(TipoVehiculo tipoVehiculoStr, TipoVehiculo tipoPlaza) {
            switch (tipoVehiculoStr) {
                case COCHE:
                    return tipoPlaza == COCHE;
                case ELECTRICO:
                    return tipoPlaza == COCHE || tipoPlaza == ELECTRICO;
                case DISCAPACITADO:
                    return tipoPlaza == COCHE || tipoPlaza == DISCAPACITADO;
                case MOTO:
                    return tipoPlaza == MOTO;
                default:
                    return false;
            }
        }


    }
}
