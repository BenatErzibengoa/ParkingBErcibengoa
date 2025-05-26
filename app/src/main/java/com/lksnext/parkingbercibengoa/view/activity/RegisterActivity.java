package com.lksnext.parkingbercibengoa.view.activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import static com.lksnext.parkingbercibengoa.configuration.Utils.showError;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.lksnext.parkingbercibengoa.configuration.Utils;
import com.lksnext.parkingbercibengoa.databinding.ActivityRegisterBinding;
import com.lksnext.parkingbercibengoa.viewmodel.RegisterViewModel;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Asignamos la vista/interfaz de registro
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //Asignamos el viewModel de register
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        //Flecha de volver
        binding.backButton.setOnClickListener(v -> {
            finish(); // Cierra la actividad actual y vuelve a la anterior
        });

        //Boton crear cuenta
        binding.createAccountButton.setOnClickListener(v -> {
            String fullNameText = binding.fullNameText.getText().toString().trim();
            String email = binding.emailText.getText().toString().trim();
            String password = binding.passwordText.getText().toString().trim();
            String confirmPassword = binding.confirmPasswordText.getText().toString().trim();
            if(fullNameText.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
                showError("Todos los campos son obligatorios", binding.registerErrorText);
            }else if(!Utils.validateEmail(email)){
                showError("Introduzca un email válido", binding.registerErrorText);
            }else if(password.length() < 6){
                showError("La contraseña debe contener al menos 6 caracteres", binding.registerErrorText);
            }else if(!password.equals(confirmPassword)){
                showError("Las contraseñas no coinciden", binding.registerErrorText);
            } else{
                binding.registerErrorText.setVisibility(GONE);
                registerViewModel.registerUser(email, password);
            }
        });

        //Observamos la variable registered para navegar cuando el registro sea exitoso
        registerViewModel.isRegistered().observe(this, registered -> {
            if (registered != null) {
                if (registered) {
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                } else {
                    showError("Existe una cuenta asociada a este email", binding.registerErrorText);
                }
            }
        });
    }
}