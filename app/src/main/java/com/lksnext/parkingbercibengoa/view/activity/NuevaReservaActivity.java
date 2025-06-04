package com.lksnext.parkingbercibengoa.view.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.lksnext.parkingbercibengoa.databinding.ActivityNuevaReservaBinding;

import java.util.Calendar;

public class NuevaReservaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityNuevaReservaBinding binding = ActivityNuevaReservaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TextInputEditText fechaText = binding.fechaText;

        fechaText.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    NuevaReservaActivity.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth, // estilo tipo popup
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // Formateo dd/MM/yyyy
                        String selectedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                        fechaText.setText(selectedDate);
                    },
                    year, month, day
            );

            // Establece fecha mínima: hoy
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

            // Establece fecha máximo: 7 días
            datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis() + 7 * 24 * 60 * 60 * 1000);

            // Muestra el calendario como popup centrado
            datePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            datePickerDialog.show();
        });
    }

}
