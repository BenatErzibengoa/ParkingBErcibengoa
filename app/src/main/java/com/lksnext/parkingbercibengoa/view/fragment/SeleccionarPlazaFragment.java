package com.lksnext.parkingbercibengoa.view.fragment;

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
import com.lksnext.parkingbercibengoa.databinding.FragmentSeleccionarPlazaBinding;

public class SeleccionarPlazaFragment extends Fragment {

    private FragmentSeleccionarPlazaBinding binding;
    private String selectedSpot = null;
    private View currentSelectedView = null;

    private final ParkingSpot[][] spots = {
            {new ParkingSpot("A1", "coche", true, "🚗"),
             new ParkingSpot("A2", "coche", false, "🚗"),
             new ParkingSpot("A3", "coche", true, "🚗"),
             new ParkingSpot("A4", "electrico", false, "⚡")},
            {new ParkingSpot("B1", "coche", true, "🚗"),
             new ParkingSpot("B2", "coche", true, "🚗"),
             new ParkingSpot("B3", "coche", false, "🚗"),
             new ParkingSpot("B4", "electrico", false, "⚡")},
            {new ParkingSpot("C1", "coche", true, "🚗"),
             new ParkingSpot("C2", "coche", false, "🚗"),
             new ParkingSpot("C3", "coche", true, "🚗"),
             new ParkingSpot("C4", "electrico", false, "⚡")},
            {new ParkingSpot("D1", "moto", false, "🏍️"),
             new ParkingSpot("D2", "moto", false, "🏍️"),
             new ParkingSpot("D3", "discapacitado", false, "♿"),
             new ParkingSpot("D4", "discapacitado", false, "♿")}
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

        setupToolbar();
        setupListeners();
        createParkingGrid();
    }

    private void setupToolbar() {
        // Si usás un Toolbar dentro del fragment (requiere que la activity soporte Toolbar)
        Toolbar toolbar = binding.toolbar;
        // Necesitás que la Activity sea AppCompatActivity para setSupportActionBar:
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

    private void setupListeners() {
        binding.reservarButton.setOnClickListener(v -> confirmReservation());
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

        int squareSize = (int) (80 * getResources().getDisplayMetrics().density);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = squareSize;
        params.height = squareSize;
        params.setMargins(8, 8, 8, 8);
        layout.setLayoutParams(params);

        if (spot.isCompatibleWithCar() && spot.isAvailable) {
            layout.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.spot_available));
            layout.setOnClickListener(v -> selectSpot(v, spot));
        } else if (!spot.isAvailable && spot.isCompatibleWithCar()) {
            layout.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.spot_occupied));
        } else {
            layout.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.spot_unavailable));
        }

        return layout;
    }


    private void selectSpot(View view, ParkingSpot spot) {
        if (currentSelectedView != null) {
            currentSelectedView.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.spot_available));
        }

        view.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.spot_selected));
        currentSelectedView = view;
        selectedSpot = spot.id;

        binding.selectedInfo.setVisibility(View.VISIBLE);
        binding.selectedDetails.setText("Plaza " + spot.id + " - Tipo: " +
                                        spot.type.substring(0, 1).toUpperCase() + spot.type.substring(1) +
                                        "\nCerca de la entrada principal");

        binding.reservarButton.setEnabled(true);
        binding.reservarButton.setAlpha(1.0f);
    }

    private void confirmReservation() {
        if (selectedSpot != null && getContext() != null) {
            new AlertDialog.Builder(requireContext())
                    .setTitle("¡Reserva confirmada!")
                    .setMessage("Plaza: " + selectedSpot + "\nFecha: 25 Jun 2025\nHorario: 09:00 - 17:00")
                    .setPositiveButton("OK", null)
                    .show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Para evitar leaks
    }

    private static class ParkingSpot {
        String id;
        String type;
        boolean isAvailable;
        String icon;

        ParkingSpot(String id, String type, boolean isAvailable, String icon) {
            this.id = id;
            this.type = type;
            this.isAvailable = isAvailable;
            this.icon = icon;
        }

        boolean isCompatibleWithCar() {
            return "coche".equals(type);
        }
    }
}
