package com.lksnext.parkingbercibengoa.view.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.lksnext.parkingbercibengoa.configuration.Utils;
import com.lksnext.parkingbercibengoa.databinding.ActivityNuevaReservaBinding;
import com.lksnext.parkingbercibengoa.domain.Plaza;
import com.lksnext.parkingbercibengoa.domain.Reserva;
import com.lksnext.parkingbercibengoa.domain.TipoVehiculo;
import com.lksnext.parkingbercibengoa.domain.Usuario;
import com.lksnext.parkingbercibengoa.domain.Vehiculo;
import com.lksnext.parkingbercibengoa.viewmodel.ReservasViewModel;
import com.lksnext.parkingbercibengoa.viewmodel.ReservasViewModelFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;

public class NuevaReservaActivity extends AppCompatActivity {

    ActivityNuevaReservaBinding binding;
    private ReservasViewModel viewModel;

    private ArrayList<Vehiculo> listaVehiculos = new ArrayList<>();

    Usuario usuarioActual = null;
    private Vehiculo vehiculoSeleccionado = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNuevaReservaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = ReservasViewModelFactory.getSharedInstance();
        observeViewModel();
        viewModel.cargarUsuario(this);
        viewModel.cargarVehiculos();

        binding.backButton.setOnClickListener(v -> finish());

        binding.fechaText.setOnClickListener(v -> {showCalendar();});
        binding.horaComienzoText.setOnClickListener(v -> {
            showTimePicker(binding.horaComienzoText);});
        binding.horaFinText.setOnClickListener(v -> { showTimePicker(binding.horaFinText);});
        binding.vehiculoText.setOnClickListener(v -> { showVehicleSelector();});

        binding.reservarButton.setOnClickListener(v -> {reservar();});



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
                .setTimeFormat(TimeFormat.CLOCK_24H)  // Formato 24 horas
                .setHour(7)
                .setMinute(0)
                .setTitleText("Seleccione la hora")
                .build();

        picker.show(getSupportFragmentManager(), "time_picker");

        picker.addOnPositiveButtonClickListener(v -> {
            String formattedTime = String.format("%02d:%02d", picker.getHour(), picker.getMinute());
            textInputEditText.setText(formattedTime);
        });
    }

    private void showVehicleSelector() {
        ListPopupWindow listPopupWindow = new ListPopupWindow(this);

        // Crear adaptador para el dropdown
        ArrayAdapter<Vehiculo> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, listaVehiculos);
        listPopupWindow.setAdapter(adapter);
        listPopupWindow.setAnchorView(binding.vehiculoText);
        listPopupWindow.setWidth(binding.vehiculoText.getWidth());
        listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);

        listPopupWindow.setOnItemClickListener((parent, view, position, id) -> {
            Vehiculo seleccionado = adapter.getItem(position);
            if (seleccionado.getMarca().equals("➕ Nuevo vehículo")) {
                // startActivity(new Intent(this, NuevaReservaActivity.class));
                return;
            }
            vehiculoSeleccionado = seleccionado;

            // Mostrar el modelo en pantalla
            binding.vehiculoText.setText(seleccionado.getMarca());
            listPopupWindow.dismiss();
        });
        listPopupWindow.show();
    }

    private void reservar() {
        String fecha = binding.fechaText.getText().toString().trim();
        String horaInicio = binding.horaComienzoText.getText().toString().trim();
        String horaFin = binding.horaFinText.getText().toString().trim();
        Vehiculo vehiculo = vehiculoSeleccionado;

        if (fecha.isEmpty() || horaInicio.isEmpty() || horaFin.isEmpty() || vehiculo == null) {
            Utils.showError("Todos los campos son obligatorios", binding.errorText);
            return;
        }

        try {
            LocalDateTime inicio = Utils.parseFechaHora(fecha, horaInicio);
            LocalDateTime fin = Utils.parseFechaHora(fecha, horaFin);

            if (!fin.isAfter(inicio)) {
                Utils.showError("La hora de fin debe ser después de la hora de inicio", binding.errorText);
                return;
            }

            Duration duracion = Duration.between(inicio, fin);

            Reserva reserva = new Reserva(Utils.generarUUID(), usuarioActual, vehiculo, inicio, duracion, null);

            viewModel.reservarPlaza(reserva);

            Log.d("NuevaReservaActivity", "Reserva enviada al ViewModel");

            new AlertDialog.Builder(this)
                    .setTitle("Reserva confirmada")
                    .setMessage("Tu reserva ha sido registrada correctamente.")
                    .setPositiveButton("OK", (dialog, which) -> {
                        finish();
                    })
                    .show();

        } catch (Exception e) {
            Utils.showError("Error al procesar la reserva: " + e.getMessage(), binding.errorText);
            Log.e("NuevaReservaActivity", "Excepción general", e);
        }
    }
    private void observeViewModel() {
        /*
        viewModel.getUsuarioLiveData().observe(this, usuario -> {
            if (usuario == null) {
                Utils.showError("Usuario no encontrado", binding.errorText);
            } else {
                usuarioActual = usuario;  // guardo el usuario para usar en reservar()
            }
        });
        */
        viewModel.getVehiculos().observe(this, vehiculos -> {
            if (vehiculos != null) {
                listaVehiculos.clear();
                listaVehiculos.addAll(vehiculos);
                listaVehiculos.add(new Vehiculo("", "➕ Nuevo vehículo", TipoVehiculo.COCHE));
            }
        });
    }



}
