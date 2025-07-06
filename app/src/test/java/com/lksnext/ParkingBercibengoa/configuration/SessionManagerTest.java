package com.lksnext.ParkingBercibengoa.configuration;

import android.content.Context;
import android.content.SharedPreferences;

import com.lksnext.parkingbercibengoa.configuration.SessionManager;
import com.lksnext.parkingbercibengoa.domain.Reserva;
import com.lksnext.parkingbercibengoa.domain.TipoVehiculo;
import com.lksnext.parkingbercibengoa.domain.Usuario;
import com.lksnext.parkingbercibengoa.domain.Vehiculo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SessionManagerTest {

    @Mock
    private Context mockContext;
    
    @Mock
    private SharedPreferences mockSharedPreferences;
    
    @Mock
    private SharedPreferences.Editor mockEditor;

    private SessionManager sessionManager;

    @Before
    public void setUp() {
        when(mockContext.getSharedPreferences(anyString(), anyInt())).thenReturn(mockSharedPreferences);
        when(mockSharedPreferences.edit()).thenReturn(mockEditor);
        when(mockEditor.putString(anyString(), anyString())).thenReturn(mockEditor);
        when(mockEditor.putString(anyString(), any())).thenReturn(mockEditor);
        when(mockEditor.clear()).thenReturn(mockEditor);
        doNothing().when(mockEditor).apply();
        
        sessionManager = new SessionManager(mockContext);
    }

    @Test
    public void testConstructor() {
        assertNotNull(sessionManager);
        verify(mockContext).getSharedPreferences("session_prefs", Context.MODE_PRIVATE);
    }

    @Test
    public void testGuardarUsuario() {
        // Arrange
        Usuario usuario = new Usuario("U001", "Juan Pérez", "juan@example.com");
        List<Vehiculo> vehiculos = new ArrayList<>();
        vehiculos.add(new Vehiculo("1234ABC", "Toyota Corolla", TipoVehiculo.COCHE));
        usuario.setVehiculos(vehiculos);
        
        List<Reserva> reservas = new ArrayList<>();
        reservas.add(new Reserva("R001", usuario, vehiculos.get(0), 
            LocalDateTime.now(), Duration.ofHours(2), null));
        usuario.setReservas(reservas);

        // Act
        sessionManager.guardarUsuario(usuario);

        // Assert
        verify(mockEditor).putString("usuario_id", "U001");
        verify(mockEditor).putString("usuario_nombre", "Juan Pérez");
        verify(mockEditor).putString("usuario_email", "juan@example.com");
        verify(mockEditor).putString("usuario_vehiculos", anyString());
        verify(mockEditor).putString("usuario_reservas", anyString());
        verify(mockEditor).apply();
    }

    @Test
    public void testGuardarUsuarioConDatosNull() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setId(null);
        usuario.setNombre(null);
        usuario.setEmail(null);
        usuario.setVehiculos(null);
        usuario.setReservas(null);

        // Act
        sessionManager.guardarUsuario(usuario);

        // Assert
        verify(mockEditor).putString("usuario_id", null);
        verify(mockEditor).putString("usuario_nombre", null);
        verify(mockEditor).putString("usuario_email", null);
        verify(mockEditor).putString("usuario_vehiculos", "null");
        verify(mockEditor).putString("usuario_reservas", "null");
        verify(mockEditor).apply();
    }

    @Test
    public void testGetUsuario() {
        // Arrange
        when(mockSharedPreferences.getString("usuario_id", null)).thenReturn("U001");
        when(mockSharedPreferences.getString("usuario_nombre", null)).thenReturn("Juan Pérez");
        when(mockSharedPreferences.getString("usuario_email", null)).thenReturn("juan@example.com");
        when(mockSharedPreferences.getString("usuario_vehiculos", null)).thenReturn("[]");
        when(mockSharedPreferences.getString("usuario_reservas", null)).thenReturn("[]");

        // Act
        Usuario result = sessionManager.getUsuario();

        // Assert
        assertNotNull(result);
        assertEquals("U001", result.getId());
        assertEquals("Juan Pérez", result.getNombre());
        assertEquals("juan@example.com", result.getEmail());
        assertNotNull(result.getVehiculos());
        assertNotNull(result.getReservas());
    }

    @Test
    public void testGetUsuarioConSesionVacia() {
        // Arrange
        when(mockSharedPreferences.getString("usuario_id", null)).thenReturn(null);
        when(mockSharedPreferences.getString("usuario_nombre", null)).thenReturn(null);
        when(mockSharedPreferences.getString("usuario_email", null)).thenReturn(null);

        // Act
        Usuario result = sessionManager.getUsuario();

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetUsuarioConEmailNull() {
        // Arrange
        when(mockSharedPreferences.getString("usuario_id", null)).thenReturn("U001");
        when(mockSharedPreferences.getString("usuario_nombre", null)).thenReturn("Juan Pérez");
        when(mockSharedPreferences.getString("usuario_email", null)).thenReturn(null);

        // Act
        Usuario result = sessionManager.getUsuario();

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetUsuarioConNombreNull() {
        // Arrange
        when(mockSharedPreferences.getString("usuario_id", null)).thenReturn("U001");
        when(mockSharedPreferences.getString("usuario_nombre", null)).thenReturn(null);
        when(mockSharedPreferences.getString("usuario_email", null)).thenReturn("juan@example.com");

        // Act
        Usuario result = sessionManager.getUsuario();

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetUsuarioConVehiculosNull() {
        // Arrange
        when(mockSharedPreferences.getString("usuario_id", null)).thenReturn("U001");
        when(mockSharedPreferences.getString("usuario_nombre", null)).thenReturn("Juan Pérez");
        when(mockSharedPreferences.getString("usuario_email", null)).thenReturn("juan@example.com");
        when(mockSharedPreferences.getString("usuario_vehiculos", null)).thenReturn(null);
        when(mockSharedPreferences.getString("usuario_reservas", null)).thenReturn("[]");

        // Act
        Usuario result = sessionManager.getUsuario();

        // Assert
        assertNotNull(result);
        assertNotNull(result.getVehiculos());
        assertTrue(result.getVehiculos().isEmpty());
    }

    @Test
    public void testGetUsuarioConReservasNull() {
        // Arrange
        when(mockSharedPreferences.getString("usuario_id", null)).thenReturn("U001");
        when(mockSharedPreferences.getString("usuario_nombre", null)).thenReturn("Juan Pérez");
        when(mockSharedPreferences.getString("usuario_email", null)).thenReturn("juan@example.com");
        when(mockSharedPreferences.getString("usuario_vehiculos", null)).thenReturn("[]");
        when(mockSharedPreferences.getString("usuario_reservas", null)).thenReturn(null);

        // Act
        Usuario result = sessionManager.getUsuario();

        // Assert
        assertNotNull(result);
        assertNotNull(result.getReservas());
        assertTrue(result.getReservas().isEmpty());
    }

    @Test
    public void testCerrarSesion() {
        // Act
        sessionManager.cerrarSesion();

        // Assert
        verify(mockEditor).clear();
        verify(mockEditor).apply();
    }

    @Test
    public void testHaySesionActiva() {
        // Arrange
        when(mockSharedPreferences.contains("usuario_email")).thenReturn(true);

        // Act
        boolean result = sessionManager.haySesionActiva();

        // Assert
        assertTrue(result);
        verify(mockSharedPreferences).contains("usuario_email");
    }

    @Test
    public void testHaySesionActivaFalse() {
        // Arrange
        when(mockSharedPreferences.contains("usuario_email")).thenReturn(false);

        // Act
        boolean result = sessionManager.haySesionActiva();

        // Assert
        assertFalse(result);
        verify(mockSharedPreferences).contains("usuario_email");
    }

    @Test
    public void testGuardarUsuarioConVehiculosVacios() {
        // Arrange
        Usuario usuario = new Usuario("U001", "Juan Pérez", "juan@example.com");
        usuario.setVehiculos(new ArrayList<>());
        usuario.setReservas(new ArrayList<>());

        // Act
        sessionManager.guardarUsuario(usuario);

        // Assert
        verify(mockEditor).putString("usuario_vehiculos", "[]");
        verify(mockEditor).putString("usuario_reservas", "[]");
        verify(mockEditor).apply();
    }

    @Test
    public void testGetUsuarioConVehiculosInvalidos() {
        // Arrange
        when(mockSharedPreferences.getString("usuario_id", null)).thenReturn("U001");
        when(mockSharedPreferences.getString("usuario_nombre", null)).thenReturn("Juan Pérez");
        when(mockSharedPreferences.getString("usuario_email", null)).thenReturn("juan@example.com");
        when(mockSharedPreferences.getString("usuario_vehiculos", null)).thenReturn("invalid json");
        when(mockSharedPreferences.getString("usuario_reservas", null)).thenReturn("[]");

        // Act
        Usuario result = sessionManager.getUsuario();

        // Assert
        assertNotNull(result);
        assertNotNull(result.getVehiculos());
        assertTrue(result.getVehiculos().isEmpty());
    }

    @Test
    public void testGetUsuarioConReservasInvalidas() {
        // Arrange
        when(mockSharedPreferences.getString("usuario_id", null)).thenReturn("U001");
        when(mockSharedPreferences.getString("usuario_nombre", null)).thenReturn("Juan Pérez");
        when(mockSharedPreferences.getString("usuario_email", null)).thenReturn("juan@example.com");
        when(mockSharedPreferences.getString("usuario_vehiculos", null)).thenReturn("[]");
        when(mockSharedPreferences.getString("usuario_reservas", null)).thenReturn("invalid json");

        // Act
        Usuario result = sessionManager.getUsuario();

        // Assert
        assertNotNull(result);
        assertNotNull(result.getReservas());
        assertTrue(result.getReservas().isEmpty());
    }
} 