package com.lksnext.ParkingBercibengoa.viewmodel;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import com.lksnext.parkingbercibengoa.configuration.SessionManager;
import com.lksnext.parkingbercibengoa.data.AuthRepository;
import com.lksnext.parkingbercibengoa.domain.LoginCallback;
import com.lksnext.parkingbercibengoa.domain.Usuario;
import com.lksnext.parkingbercibengoa.viewmodel.LoginViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

public class LoginViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private AuthRepository mockAuthRepository;
    
    @Mock
    private Context mockContext;

    private LoginViewModel loginViewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        loginViewModel = new LoginViewModel(mockAuthRepository);
    }

    @Test
    public void testInitialState() {
        // Verificar estado inicial
        LiveData<Boolean> loggedLiveData = loginViewModel.isLogged();
        LiveData<String> errorLiveData = loginViewModel.getError();
        
        assertNotNull(loggedLiveData);
        assertNotNull(errorLiveData);
        assertNull(loggedLiveData.getValue());
        assertNull(errorLiveData.getValue());
    }

    @Test
    public void testLoginMethodCallsRepository() {
        String email = "correo@gmail.com";
        String password = "admin1234";
        loginViewModel.loginUser(mockContext, email, password);
        verify(mockAuthRepository).login(anyString(), anyString(), any(LoginCallback.class));
    }

    @Test
    public void testLiveDataObservers() {
        // Verificar que los LiveData son observables
        LiveData<Boolean> loggedLiveData = loginViewModel.isLogged();
        LiveData<String> errorLiveData = loginViewModel.getError();
        
        assertNotNull(loggedLiveData);
        assertNotNull(errorLiveData);
    }
} 