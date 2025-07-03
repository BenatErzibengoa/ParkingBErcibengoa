package com.lksnext.parkingbercibengoa.view.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lksnext.parkingbercibengoa.R;
import com.lksnext.parkingbercibengoa.databinding.ActivityMainBinding;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.lksnext.parkingbercibengoa.configuration.SessionManager;
import com.lksnext.parkingbercibengoa.viewmodel.ReservasViewModelFactory;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ActivityMainBinding binding;
    NavController navController;
    AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Asignamos la vista/interfaz main (layout)
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);


        //Con el NavigationHost podremos movernos por distintas pestañas dentro de la misma pantalla
        NavHostFragment navHostFragment =
            (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.flFragment);
        navController = navHostFragment.getNavController();

        //Asignamos los botones de navegacion que se encuentran en la vista (layout)
        bottomNavigationView = binding.bottomNavigationView;
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        //Empezar en pestaña reservas
        navController.navigate(R.id.reservasFragment);

        //Dependendiendo que boton clique el usuario de la navegacion se hacen distintas cosas
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.newres) {
                navController.navigate(R.id.reservasFragment);
                return true;
            } else if (itemId == R.id.reservations) {
                navController.navigate(R.id.historialFragment);
                return true;
            }
            return false;
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            //  Cerrar sesión Firebase
            FirebaseAuth.getInstance().signOut();

            //  Borrar sesión local
            SessionManager sessionManager = new SessionManager(this);
            sessionManager.cerrarSesion();

            //Limpiar viewmodel
            ReservasViewModelFactory.clearInstance();


            //  Ir a LoginActivity
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}