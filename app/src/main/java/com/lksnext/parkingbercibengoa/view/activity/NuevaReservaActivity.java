package com.lksnext.parkingbercibengoa.view.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.lksnext.parkingbercibengoa.databinding.ActivityNuevaReservaBinding;

import java.util.Calendar;

public class NuevaReservaActivity extends AppCompatActivity {

    ActivityNuevaReservaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNuevaReservaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.fechaText.setOnClickListener(v -> {showCalendar();});
        binding.horaComienzoText.setOnClickListener(v -> { showTimePicker(binding.horaComienzoText);});
        binding.horaFinText.setOnClickListener(v -> { showTimePicker(binding.horaFinText);});




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

}
