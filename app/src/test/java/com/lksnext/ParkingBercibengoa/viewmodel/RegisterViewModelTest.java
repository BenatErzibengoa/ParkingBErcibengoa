package com.lksnext.ParkingBercibengoa.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import com.lksnext.parkingbercibengoa.data.AuthRepository;
import com.lksnext.parkingbercibengoa.domain.Callback;
import com.lksnext.parkingbercibengoa.viewmodel.RegisterViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

public class RegisterViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private AuthRepository mockAuthRepository;

    private RegisterViewModel registerViewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        registerViewModel = new RegisterViewModel(mockAuthRepository);
    }

    @Test
    public void testInitialState() {
        // Verificar estado inicial
        LiveData<Boolean> registeredLiveData = registerViewModel.isRegistered();
        LiveData<String> errorLiveData = registerViewModel.getError();
        
        assertNotNull(registeredLiveData);
        assertNotNull(errorLiveData);
        assertNull(registeredLiveData.getValue());
        assertNull(errorLiveData.getValue());
    }

    @Test
    public void testRegisterMethodCallsRepository() {
        // Arrange
        String fullName = "Test User";
        String email = "test@example.com";
        String password = "password123";

        // Act
        registerViewModel.registerUser(fullName, email, password);

        // Assert
        verify(mockAuthRepository).register(anyString(), anyString(), anyString(), any(Callback.class));
    }

    @Test
    public void testLiveDataObservers() {
        // Verificar que los LiveData son observables
        LiveData<Boolean> registeredLiveData = registerViewModel.isRegistered();
        LiveData<String> errorLiveData = registerViewModel.getError();
        
        assertNotNull(registeredLiveData);
        assertNotNull(errorLiveData);
    }
} 