package com.lksnext.ParkingBercibengoa.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import com.lksnext.parkingbercibengoa.data.AuthRepository;
import com.lksnext.parkingbercibengoa.domain.Callback;
import com.lksnext.parkingbercibengoa.viewmodel.PasswordViewModel;

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

public class PasswordViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private AuthRepository mockAuthRepository;

    private PasswordViewModel passwordViewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        passwordViewModel = new PasswordViewModel(mockAuthRepository);
    }

    @Test
    public void testInitialState() {
        // Verificar estado inicial
        LiveData<Boolean> passwordResetResult = passwordViewModel.getPasswordResetResult();
        
        assertNotNull(passwordResetResult);
        assertNull(passwordResetResult.getValue());
    }

    @Test
    public void testChangePasswordMethodCallsRepository() {
        // Arrange
        String email = "test@example.com";

        // Act
        passwordViewModel.changePassword(email);

        // Assert
        verify(mockAuthRepository).changePassword(anyString(), any(Callback.class));
    }

    @Test
    public void testLiveDataObservers() {
        // Verificar que el LiveData es observable
        LiveData<Boolean> passwordResetResult = passwordViewModel.getPasswordResetResult();
        
        assertNotNull(passwordResetResult);
    }
} 