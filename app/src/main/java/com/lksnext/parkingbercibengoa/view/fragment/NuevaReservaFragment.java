package com.lksnext.parkingbercibengoa.view.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.lksnext.parkingbercibengoa.R;
import com.lksnext.parkingbercibengoa.configuration.Utils;
import com.lksnext.parkingbercibengoa.databinding.FragmentNuevaReservaBinding;
import com.lksnext.parkingbercibengoa.domain.Reserva;
import com.lksnext.parkingbercibengoa.domain.TipoVehiculo;
import com.lksnext.parkingbercibengoa.domain.Usuario;
import com.lksnext.parkingbercibengoa.domain.Vehiculo;
import com.lksnext.parkingbercibengoa.viewmodel.ReservasViewModel;
import com.lksnext.parkingbercibengoa.viewmodel.ReservasViewModelFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;

public class NuevaReservaFragment extends Fragment {

    private FragmentNuevaReservaBinding binding;
    private ReservasViewModel viewModel;

    private ArrayList<Vehiculo> listaVehiculos = new ArrayList<>();
    private Vehiculo vehiculoSeleccionado = null;

    NavController navController;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNuevaReservaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(requireActivity(), R.id.flFragment);

        viewModel = ReservasViewModelFactory.getSharedInstance(requireActivity().getApplication());
        if (viewModel.getReservaAEditar().getValue() != null) {
            cargarDatosReserva(viewModel.getReservaAEditar().getValue());
            binding.titulo.setText("Editar reserva");
        }

        observeViewModel();
        viewModel.cargarVehiculos();

        binding.backButton.setOnClickListener(v -> {
            // Volver atrás en la pila de fragments
            viewModel.setReservaAEditar(null);
            navController.popBackStack();
        });


        binding.fechaText.setOnClickListener(v -> showCalendar());
        binding.horaComienzoText.setOnClickListener(v -> showTimePicker(binding.horaComienzoText));
        binding.horaFinText.setOnClickListener(v -> showTimePicker(binding.horaFinText));
        binding.vehiculoText.setOnClickListener(v -> showVehicleSelector());
        binding.reservarButton.setOnClickListener(v -> buscarPlazas());

    }

    //Para que cuando vuelva de NuevoVehiculo se cargue el nuevo coche
    @Override
    public void onResume() {
        super.onResume();
        viewModel.cargarVehiculos();
    }

    private void showCalendar() {
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

        datePicker.show(getParentFragmentManager(), "DATE_PICKER");
    }

    private void showTimePicker(TextInputEditText textInputEditText) {
        MaterialTimePicker picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)  // Formato 24 horas
                .setHour(7)
                .setMinute(0)
                .setTitleText("Seleccione la hora")
                .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)  // Modo circular
                .build();

        picker.show(getParentFragmentManager(), "time_picker");

        picker.addOnPositiveButtonClickListener(v -> {
            String formattedTime = String.format("%02d:%02d", picker.getHour(), picker.getMinute());
            textInputEditText.setText(formattedTime);
        });
    }

    private void showVehicleSelector() {
        Log.d("NuevaReserva", listaVehiculos.toString());
        Log.d("NuevaReserva", viewModel.getVehiculos().getValue().toString());

        ListPopupWindow listPopupWindow = new ListPopupWindow(requireContext());

        // Crear adaptador para el dropdown
        ArrayAdapter<Vehiculo> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, listaVehiculos);
        listPopupWindow.setAdapter(adapter);
        listPopupWindow.setAnchorView(binding.vehiculoText);
        listPopupWindow.setWidth(binding.vehiculoText.getWidth());
        listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);

        listPopupWindow.setOnItemClickListener((parent, view, position, id) -> {
            Vehiculo seleccionado = adapter.getItem(position);
            if (seleccionado != null && "➕ Nuevo vehículo".equals(seleccionado.getModelo())) {
                navController.navigate(R.id.action_nuevaReservaFragment_to_nuevoVehiculoFragment);
                listPopupWindow.dismiss();
                return;
            }
            if (seleccionado != null) {
                binding.vehiculoText.setText(seleccionado.getModelo());
                vehiculoSeleccionado = seleccionado;
                viewModel.setVehiculoSeleccionado(vehiculoSeleccionado);
            }
            listPopupWindow.dismiss();
        });
        listPopupWindow.show();
    }

    private void buscarPlazas() {
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
            viewModel.setHoraInicio(inicio);
            viewModel.setHoraFin(fin);

            navController.navigate(R.id.action_nuevaReservaFragment_to_seleccionarPlazaFragment);
        } catch (Exception e) {
            Utils.showError("Error al buscar plazas: " + e.getMessage(), binding.errorText);
            Log.e("NuevaReservaFragment", "Excepción general", e);
        }
    }
    private void observeViewModel() {
        viewModel.getVehiculos().observe(getViewLifecycleOwner(), vehiculos -> {
            if (vehiculos != null) {
                listaVehiculos.clear();
                listaVehiculos.addAll(vehiculos);
                listaVehiculos.add(new Vehiculo("", "➕ Nuevo vehículo", TipoVehiculo.COCHE));
            }
        });
    }
    private void cargarDatosReserva(Reserva reserva) {
        LocalDateTime inicio = reserva.getFechaInicio();
        LocalDateTime fin = inicio.plus(reserva.getDuracion());

        binding.fechaText.setText(Utils.formatFecha(inicio));
        binding.horaComienzoText.setText(Utils.formatHora(inicio));
        binding.horaFinText.setText(Utils.formatHora(fin));
        binding.vehiculoText.setText(reserva.getVehiculo().getModelo());
        vehiculoSeleccionado = reserva.getVehiculo();

        viewModel.setVehiculoSeleccionado(vehiculoSeleccionado);
        viewModel.setHoraInicio(inicio);
        viewModel.setHoraFin(fin);
    }
}
