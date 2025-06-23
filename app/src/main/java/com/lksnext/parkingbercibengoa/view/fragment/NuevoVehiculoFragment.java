package com.lksnext.parkingbercibengoa.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;

import androidx.appcompat.app.AppCompatActivity;

import com.lksnext.parkingbercibengoa.databinding.FragmentNuevoVehiculoBinding;
import com.lksnext.parkingbercibengoa.domain.TipoVehiculo;
import com.lksnext.parkingbercibengoa.domain.Vehiculo;

import java.util.Arrays;
import java.util.List;

public class NuevoVehiculoFragment extends AppCompatActivity {

    FragmentNuevoVehiculoBinding binding;
    List<TipoVehiculo> tiposVehiculo = Arrays.asList(TipoVehiculo.COCHE, TipoVehiculo.ELECTRICO, TipoVehiculo.MOTO, TipoVehiculo.DISCAPACITADO);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentNuevoVehiculoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.vehiculoText.setOnClickListener(v -> {showVehicleSelector();});
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
            TipoVehiculo seleccionado = adapter.getItem(position);

            // Mostrar el modelo en pantalla
            binding.vehiculoText.setText(seleccionado.toString());
            listPopupWindow.dismiss();
        });
        listPopupWindow.show();
    }

}
