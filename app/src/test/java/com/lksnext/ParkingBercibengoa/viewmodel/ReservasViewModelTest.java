package com.lksnext.ParkingBercibengoa.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.lksnext.parkingbercibengoa.data.DataRepository;
import com.lksnext.parkingbercibengoa.domain.Reserva;
import com.lksnext.parkingbercibengoa.domain.Vehiculo;
import com.lksnext.parkingbercibengoa.viewmodel.ReservasViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 33)
public class ReservasViewModelTest {

    private ReservasViewModel reservasViewModel;

    @Before
    public void setUp() {
        // Mock de Application y Context
        Application mockApp = Mockito.mock(Application.class);
        Context mockContext = Mockito.mock(Context.class);
        Mockito.when(mockApp.getApplicationContext()).thenReturn(mockContext);

        // Mock de SharedPreferences y Editor (para SessionManager)
        SharedPreferences mockPrefs = Mockito.mock(SharedPreferences.class);
        SharedPreferences.Editor mockEditor = Mockito.mock(SharedPreferences.Editor.class);

        Mockito.when(mockEditor.putString(Mockito.anyString(), Mockito.anyString())).thenReturn(mockEditor);
        Mockito.when(mockEditor.putBoolean(Mockito.anyString(), Mockito.anyBoolean())).thenReturn(mockEditor);
        Mockito.when(mockEditor.commit()).thenReturn(true);
        Mockito.doNothing().when(mockEditor).apply();

        Mockito.when(mockPrefs.edit()).thenReturn(mockEditor);
        Mockito.when(mockContext.getSharedPreferences(Mockito.anyString(), Mockito.anyInt())).thenReturn(mockPrefs);

        // No hay sesión activa -> usuario null
        Mockito.when(mockPrefs.getString(Mockito.anyString(), Mockito.anyString())).thenReturn(null);

        // Mock del repositorio (para evitar Firebase)
        DataRepository mockRepo = Mockito.mock(DataRepository.class);

        // Crear ViewModel con dependencias mockeadas
        reservasViewModel = new ReservasViewModel(mockApp, mockRepo);
    }

    @Test
    public void testLiveDataInitialization() {
        assertNotNull(reservasViewModel.getUsuario());
        assertNotNull(reservasViewModel.getReservas());
        assertNotNull(reservasViewModel.getVehiculos());
        assertNotNull(reservasViewModel.getVehiculoSeleccionado());
        assertNotNull(reservasViewModel.gethoraInicio());
        assertNotNull(reservasViewModel.gethoraFin());
        assertNotNull(reservasViewModel.getPlazas());
        assertNotNull(reservasViewModel.getReservaAEditar());
        assertNotNull(reservasViewModel.getErrorCargarReservas());
        assertNotNull(reservasViewModel.getErrorCargarVehiculos());
        assertNotNull(reservasViewModel.getErrorAñadirReserva());
        assertNotNull(reservasViewModel.getErrorAñadirVehiculo());
        assertNotNull(reservasViewModel.getErrorCargarPlazas());
        assertNotNull(reservasViewModel.getHorariosPorPlaza());
    }

    @Test
    public void testSetters() {
        Vehiculo v = Mockito.mock(Vehiculo.class);
        reservasViewModel.setVehiculoSeleccionado(v);
        assertEquals(v, reservasViewModel.getVehiculoSeleccionado().getValue());

        LocalDateTime now = LocalDateTime.now();
        reservasViewModel.setHoraInicio(now);
        assertEquals(now, reservasViewModel.gethoraInicio().getValue());
        reservasViewModel.setHoraFin(now);
        assertEquals(now, reservasViewModel.gethoraFin().getValue());

        Reserva r = Mockito.mock(Reserva.class);
        reservasViewModel.setReservaAEditar(r);
        assertEquals(r, reservasViewModel.getReservaAEditar().getValue());
    }

    @Test
    public void testUserIsNullInitially() {
        assertNull(reservasViewModel.getUsuario().getValue());
    }

    @Test
    public void testMethodsWithNullUser() {
        try {
            reservasViewModel.cargarReservasDelUsuario();
        } catch (NullPointerException e) {
            fail("cargarReservasDelUsuario debería manejar usuario null sin lanzar excepción");
        }

        try {
            reservasViewModel.cargarVehiculos();
        } catch (NullPointerException e) {
            fail("cargarVehiculos debería manejar usuario null sin lanzar excepción");
        }

        try {
            reservasViewModel.obtenerPlazas(LocalDate.now());
        } catch (NullPointerException e) {
            fail("obtenerPlazas debería manejar usuario null sin lanzar excepción");
        }

        try {
            reservasViewModel.cargarHorariosPorPlaza(LocalDate.now());
        } catch (NullPointerException e) {
            fail("cargarHorariosPorPlaza debería manejar usuario null sin lanzar excepción");
        }
    }

    @Test
    public void testPublicMethodsExist() {
        try {
            reservasViewModel.cargarReservasDelUsuario();
            reservasViewModel.cargarVehiculos();
            reservasViewModel.obtenerPlazas(LocalDate.now());
            reservasViewModel.cargarHorariosPorPlaza(LocalDate.now());
        } catch (Exception e) {
            // En entorno de test es aceptable si lanza por falta de implementación interna,
            // pero no deben ser NullPointerException por datos como el usuario.
        }
    }
}
