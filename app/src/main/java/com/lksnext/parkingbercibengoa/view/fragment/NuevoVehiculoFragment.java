package com.lksnext.parkingbercibengoa.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.lksnext.parkingbercibengoa.configuration.Utils;
import com.lksnext.parkingbercibengoa.databinding.FragmentNuevoVehiculoBinding;
import com.lksnext.parkingbercibengoa.domain.TipoVehiculo;
import com.lksnext.parkingbercibengoa.domain.Vehiculo;
import com.lksnext.parkingbercibengoa.viewmodel.ReservasViewModel;
import com.lksnext.parkingbercibengoa.viewmodel.ReservasViewModelFactory;

import java.util.Arrays;
import java.util.List;

public class NuevoVehiculoFragment extends Fragment {

    private ReservasViewModel viewModel;
    private FragmentNuevoVehiculoBinding binding;
    private List<TipoVehiculo> tiposVehiculo = Arrays.asList(
            TipoVehiculo.COCHE, TipoVehiculo.ELECTRICO, TipoVehiculo.MOTO, TipoVehiculo.DISCAPACITADO
    );

    private TipoVehiculo tipoSeleccionado = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNuevoVehiculoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ReservasViewModelFactory.getSharedInstance();

        binding.vehiculoText.setOnClickListener(v -> showVehicleSelector());
        binding.crearVehiculoButton.setOnClickListener(v -> crearVehiculo());
    }

    private void showVehicleSelector() {
        ListPopupWindow listPopupWindow = new ListPopupWindow(requireContext());
        ArrayAdapter<TipoVehiculo> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                tiposVehiculo
        );
        listPopupWindow.setAdapter(adapter);
        listPopupWindow.setAnchorView(binding.vehiculoText);
        listPopupWindow.setWidth(binding.vehiculoText.getWidth());
        listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);

        listPopupWindow.setOnItemClickListener((parent, view, position, id) -> {
            tipoSeleccionado = adapter.getItem(position);
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
        Log.d("NuevoVehiculoFragment", viewModel.getVehiculos().getValue().toString());
        Vehiculo nuevoVehiculo = new Vehiculo(matricula, modelo, tipo);
        viewModel.añadirVehiculo(nuevoVehiculo);
        Log.d("NuevoVehiculoFragment", viewModel.getVehiculos().getValue().toString());



        new AlertDialog.Builder(requireContext())
                .setTitle("Vehículo guardado")
                .setMessage("El vehículo ha sido añadido correctamente.")
                .setPositiveButton("OK", (dialog, which) -> {
                    requireActivity().onBackPressed();
                })
                .show();
    }
}
