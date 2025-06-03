package com.lksnext.parkingbercibengoa.view.activity;

import static android.view.View.GONE;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.lksnext.parkingbercibengoa.configuration.Utils;
import com.lksnext.parkingbercibengoa.databinding.ActivityPasswordBinding;
import com.lksnext.parkingbercibengoa.viewmodel.PasswordViewModel;

public class PasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPasswordBinding binding = ActivityPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        PasswordViewModel passwordViewModel = new ViewModelProvider(this).get(PasswordViewModel.class);


        binding.backButton.setOnClickListener(v -> {
            finish(); // vuelve a login
        });

        //Boton enviar email
        binding.sendLinkButton.setOnClickListener(v -> {
            String email = binding.emailText.getText().toString().trim();
            if(email.isEmpty()){
                Utils.showError("Email vacio", binding.emailErrorText);
            }else if(!Utils.validateEmail(email)){
                Utils.showError("Por favor, introduzca un email válido", binding.emailErrorText);
            } else{
                binding.emailErrorText.setVisibility(GONE);
                passwordViewModel.changePassword(email);
            }
        });

        passwordViewModel.getPasswordResetResult().observe(this, success -> {
            if (success != null) {
                if (success) {
                    Toast.makeText(this, "Hemos enviado un enlace para restablecer tu contraseña", Toast.LENGTH_LONG).show();
                    finish(); // navega de vuelta al login
                } else {
                    Utils.showError("No se pudo enviar el correo. Verifica el email o intenta más tarde", binding.emailErrorText);
                }
            }
        });

    }
}