package com.lksnext.parkingbercibengoa.view.activity;

import static android.view.View.GONE;

import static com.lksnext.parkingbercibengoa.configuration.Utils.showError;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.FirebaseApp;
import com.lksnext.parkingbercibengoa.configuration.SessionManager;
import com.lksnext.parkingbercibengoa.configuration.Utils;
import com.lksnext.parkingbercibengoa.databinding.ActivityLoginBinding;
import com.lksnext.parkingbercibengoa.domain.Usuario;
import com.lksnext.parkingbercibengoa.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    private static final int REQUEST_NOTIFICATION_PERMISSION = 1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        LoginViewModel loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

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
                loginViewModel.loginUser(this,email, password);
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                View decor = window.getDecorView();
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); // Texto oscuro
            }
        }
        pedirPermisoNotificaciones();
    }

    private void pedirPermisoNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {

                new AlertDialog.Builder(this)
                        .setTitle("Permiso de notificaciones")
                        .setMessage("Esta aplicación necesita permiso para enviarte notificaciones importantes, como confirmaciones de reservas.")
                        .setPositiveButton("Permitir", (dialog, which) -> {
                            ActivityCompat.requestPermissions(this,
                                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                                    REQUEST_NOTIFICATION_PERMISSION);
                        })
                        .setNegativeButton("No, gracias", null)
                        .show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Permiso", "Permiso de notificaciones concedido");
            } else {
                Log.d("Permiso", "Permiso de notificaciones DENEGADO");
            }
        }
    }
}