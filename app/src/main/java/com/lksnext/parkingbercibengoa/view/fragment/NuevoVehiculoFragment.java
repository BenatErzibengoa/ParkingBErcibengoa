package com.lksnext.parkingbercibengoa.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.lksnext.parkingbercibengoa.configuration.Utils;
import com.lksnext.parkingbercibengoa.databinding.FragmentNuevoVehiculoBinding;
import com.lksnext.parkingbercibengoa.domain.Reserva;
import com.lksnext.parkingbercibengoa.domain.TipoVehiculo;
import com.lksnext.parkingbercibengoa.domain.Vehiculo;
import com.lksnext.parkingbercibengoa.viewmodel.ReservasViewModel;
import com.lksnext.parkingbercibengoa.viewmodel.ReservasViewModelFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class NuevoVehiculoFragment extends AppCompatActivity {

    private ReservasViewModel viewModel;
    FragmentNuevoVehiculoBinding binding;
    List<TipoVehiculo> tiposVehiculo = Arrays.asList(TipoVehiculo.COCHE, TipoVehiculo.ELECTRICO, TipoVehiculo.MOTO, TipoVehiculo.DISCAPACITADO);

    TipoVehiculo tipoSeleccionado = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ReservasViewModelFactory.getSharedInstance();
        binding = FragmentNuevoVehiculoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.vehiculoText.setOnClickListener(v -> {showVehicleSelector();});
        binding.crearVehiculoButton.setOnClickListener(v -> {crearVehiculo();});

    }
    private void showVehicleSelector() {
        ListPopupWindow listPopupWindow = new ListPopupWindow(this);
        // Crear adaptador para el dropdown
        ArrayAdapter<TipoVehiculo> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, tiposVehiculo);
        listPopupWindow.setAdapter(adapter);
        listPopupWindow.setAnchorView(binding.vehiculoText);
        listPopupWindow.setWidth(binding.vehiculoText.getWidth());
        listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);

        listPopupWindow.setOnItemClickListener((parent, view, position, id) -> {
            tipoSeleccionado = adapter.getItem(position);

            // Mostrar el modelo en pantalla
            binding.vehiculoText.setText(tipoSeleccionado.toString());
            listPopupWindow.dismiss();
        });
        listPopupWindow.show();
    }

    private void crearVehiculo() {
        String matricula = binding.matriculaEditText.getText().toString().trim();
        String modelo = binding.nombreEditText.getText().toString().trim();
        TipoVehiculo tipo = tipoSeleccionado;

        if (matricula.isEmpty() || modelo.isEmpty() || tipo == null) {
            Utils.showError("Todos los campos del vehículo son obligatorios", binding.errorText);
            return;
        }

        Vehiculo nuevoVehiculo = new Vehiculo(matricula, modelo, tipo);

        viewModel.añadirVehiculo(nuevoVehiculo);

        new AlertDialog.Builder(this)
                .setTitle("Vehículo guardado")
                .setMessage("El vehículo ha sido añadido correctamente.")
                .setPositiveButton("OK", (dialog, which) -> {
                    finish();
                })
                .show();
    }


}
