package com.lksnext.parkingbercibengoa.view.activity;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.lksnext.parkingbercibengoa.databinding.ActivityNuevaReservaBinding;
import com.lksnext.parkingbercibengoa.viewmodel.NuevaReservaViewModel;
import com.lksnext.parkingbercibengoa.viewmodel.PasswordViewModel;

import java.util.ArrayList;
import java.util.Calendar;

public class NuevaReservaActivity extends AppCompatActivity {

    ActivityNuevaReservaBinding binding;
    private NuevaReservaViewModel viewModel;

    private ArrayList<String> listaVehiculos = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNuevaReservaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(NuevaReservaViewModel.class);
        observeViewModel();
        viewModel.cargarVehiculos();

        binding.backButton.setOnClickListener(v -> finish());

        binding.fechaText.setOnClickListener(v -> {showCalendar();});
        binding.horaComienzoText.setOnClickListener(v -> { showTimePicker(binding.horaComienzoText);});
        binding.horaFinText.setOnClickListener(v -> { showTimePicker(binding.horaFinText);});
        binding.vehiculoText.setOnClickListener(v -> { showVehicleSelector();});

        binding.reservarButton.setOnClickListener(v -> {});



    }

    private void showCalendar(){
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Selecciona una fecha")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(selection);
            String selectedDate = String.format("%02d/%02d/%04d",
                    cal.get(Calendar.DAY_OF_MONTH),
                    cal.get(Calendar.MONTH) + 1,
                    cal.get(Calendar.YEAR));
            binding.fechaText.setText(selectedDate);
        });

        datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
    }


    private void showTimePicker(TextInputEditText textInputEditText) {
        MaterialTimePicker picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(7)
                .setMinute(0)
                .setTitleText("Seleccione la hora")
                .build();

        picker.show(getSupportFragmentManager(), "time_picker");

        picker.addOnPositiveButtonClickListener(v -> {
            String amPm = picker.getHour() >= 12 ? "PM" : "AM";
            int hour = picker.getHour() % 12;
            if (hour == 0) hour = 12;

            String formattedTime = String.format("%02d:%02d %s", hour, picker.getMinute(), amPm);
            textInputEditText.setText(formattedTime);
        });
    }

    private void showVehicleSelector() {
        ListPopupWindow listPopupWindow = new ListPopupWindow(this);

        // Crear adaptador para el dropdown
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, listaVehiculos);
        adapter.add("➕ Nuevo vehículo");

        listPopupWindow.setAdapter(adapter);
        listPopupWindow.setAnchorView(binding.vehiculoText);
        listPopupWindow.setWidth(binding.vehiculoText.getWidth());
        listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);

        // Configurar listener para cuando se seleccione una opción
        listPopupWindow.setOnItemClickListener((parent, view, position, id) -> {
            if(position == listaVehiculos.size() - 1){
                //startActivity(new Intent(requireContext(), NuevaReservaActivity.class));
                showVehicleSelector();
            }
            String selectedVehicle = listaVehiculos.get(position);
            binding.vehiculoText.setText(selectedVehicle);
            listPopupWindow.dismiss();
        });
        //Mostrar opciones (dropdown)
        listPopupWindow.show();
    }

    private void observeViewModel() {
        viewModel.getVehiculos().observe(this, vehiculos -> {
            if (vehiculos != null) {
                listaVehiculos.clear();
                listaVehiculos.addAll(vehiculos);
            }
            listaVehiculos.add("+ Nuevo vehículo");
        });
    }
}
