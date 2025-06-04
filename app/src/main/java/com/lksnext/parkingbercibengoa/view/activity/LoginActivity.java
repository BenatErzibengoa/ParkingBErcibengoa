package com.lksnext.parkingbercibengoa.view.activity;

import static android.view.View.GONE;

import static com.lksnext.parkingbercibengoa.configuration.Utils.showError;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.lksnext.parkingbercibengoa.configuration.Utils;
import com.lksnext.parkingbercibengoa.databinding.ActivityLoginBinding;
import com.lksnext.parkingbercibengoa.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        LoginViewModel loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        //TODO: BORRAR ESTAS DOS LINEAS AL FINALIZAR
        binding.emailText.setText("bengoaerzi@gmail.com");
        binding.passwordText.setText("admin1234");


        // Intentar logearse
        binding.loginButton.setOnClickListener(v -> {
            String email = binding.emailText.getText().toString();
            String password = binding.passwordText.getText().toString();
            if(email.isEmpty() || password.isEmpty()){
                showError("Todos los campos son obligatorios", binding.loginErrorText);
            }else if(!Utils.validateEmail(email)){
                showError("Introduzca un email válido", binding.loginErrorText);
            }else if(password.length() < 6){
                showError("La contraseña debe contener al menos 6 caracteres", binding.loginErrorText);
            } else{
                binding.loginErrorText.setVisibility(GONE);
                loginViewModel.loginUser(email, password);
            }
        });

        // Login --> Register
        binding.createAccount.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        //¿Olvidaste tu contraseña?
        binding.tvForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, PasswordActivity.class));
        });

        //logged
        loginViewModel.isLogged().observe(this, logged -> {
            if (logged != null) {
                if (logged) {
                    Log.d("LoginActivity", "Login correcto");
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {
                    //La logica para mostrar el error se hace en otro listener
                    Log.d("LoginActivity", "Login incorrecto");
                }
            }
        });

        loginViewModel.getError().observe(this, errorCode -> {
            if (errorCode != null && !errorCode.isEmpty()) {
                String mensaje = Utils.getMensajeError(errorCode);
                Utils.showError(mensaje, binding.loginErrorText);
            }
        });
    }
}