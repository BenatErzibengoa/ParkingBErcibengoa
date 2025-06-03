package com.lksnext.ParkingBercibengoa.data;

import static org.mockito.Mockito.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lksnext.parkingbercibengoa.data.DataRepository;
import com.lksnext.parkingbercibengoa.domain.Callback;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class DataRepositoryTest {

    @Mock
    private FirebaseAuth mockAuth;
    @Mock
    private FirebaseUser mockUser;

    private DataRepository dataRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        dataRepository = new DataRepository(null, mockAuth);
    }

    @SuppressWarnings("unchecked")
    private <T> void simulateTaskSuccess(Task<T> task) {
        when(task.isSuccessful()).thenReturn(true);
        doAnswer(invocation -> {
            OnCompleteListener<T> listener = invocation.getArgument(0);
            listener.onComplete(task);
            return task;
        }).when(task).addOnCompleteListener(any());
    }

    @SuppressWarnings("unchecked")
    private <T> void simulateTaskFailure(Task<T> task, Exception ex) {
        when(task.isSuccessful()).thenReturn(false);
        when(task.getException()).thenReturn(ex);
        doAnswer(invocation -> {
            OnCompleteListener<T> listener = invocation.getArgument(0);
            listener.onComplete(task);
            return task;
        }).when(task).addOnCompleteListener(any());
    }

    @Test
    public void testLogin_Success() {
        String email = "test@example.com";
        String password = "password";
        Callback callback = mock(Callback.class);

        Task<AuthResult> mockTask = mock(Task.class);
        when(mockAuth.signInWithEmailAndPassword(email, password)).thenReturn(mockTask);
        simulateTaskSuccess(mockTask);

        dataRepository.login(email, password, callback);

        verify(mockAuth).signInWithEmailAndPassword(email, password);
        verify(callback).onSuccess();
        verify(callback, never()).onFailure(any());
    }

    @Test
    public void testLogin_Failure() {
        String email = "test@example.com";
        String password = "wrongpass";
        Callback callback = mock(Callback.class);

        Task<AuthResult> mockTask = mock(Task.class);
        Exception ex = new Exception("Some error");
        when(mockAuth.signInWithEmailAndPassword(email, password)).thenReturn(mockTask);
        simulateTaskFailure(mockTask, ex);

        dataRepository.login(email, password, callback);

        verify(mockAuth).signInWithEmailAndPassword(email, password);
        verify(callback).onFailure(anyString());
        verify(callback, never()).onSuccess();
    }

    @Test
    public void testRegister_Success() {
        String fullName = "John Doe";
        String email = "john@example.com";
        String password = "password";
        Callback callback = mock(Callback.class);

        Task<AuthResult> createUserTask = mock(Task.class);
        Task<Void> updateProfileTask = mock(Task.class);

        when(mockAuth.createUserWithEmailAndPassword(email, password)).thenReturn(createUserTask);
        simulateTaskSuccess(createUserTask);

        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.updateProfile(any())).thenReturn(updateProfileTask);
        simulateTaskSuccess(updateProfileTask);

        dataRepository.register(fullName, email, password, callback);

        verify(mockAuth).createUserWithEmailAndPassword(email, password);
        verify(mockUser).updateProfile(any());
        verify(callback).onSuccess();
        verify(callback, never()).onFailure(any());
    }

    @Test
    public void testRegister_ProfileUpdateFailure() {
        String fullName = "John Doe";
        String email = "john@example.com";
        String password = "password";
        Callback callback = mock(Callback.class);

        Task<AuthResult> createUserTask = mock(Task.class);
        Task<Void> updateProfileTask = mock(Task.class);

        when(mockAuth.createUserWithEmailAndPassword(email, password)).thenReturn(createUserTask);
        simulateTaskSuccess(createUserTask);

        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.updateProfile(any())).thenReturn(updateProfileTask);
        simulateTaskFailure(updateProfileTask, new Exception("Profile update failed"));

        dataRepository.register(fullName, email, password, callback);

        verify(mockAuth).createUserWithEmailAndPassword(email, password);
        verify(mockUser).updateProfile(any());
        verify(callback).onFailure("server_error");
        verify(callback, never()).onSuccess();
    }

    @Test
    public void testRegister_Failure() {
        String fullName = "John Doe";
        String email = "john@example.com";
        String password = "password";
        Callback callback = mock(Callback.class);

        Task<AuthResult> createUserTask = mock(Task.class);
        Exception ex = new Exception("Register error");

        when(mockAuth.createUserWithEmailAndPassword(email, password)).thenReturn(createUserTask);
        simulateTaskFailure(createUserTask, ex);

        dataRepository.register(fullName, email, password, callback);

        verify(mockAuth).createUserWithEmailAndPassword(email, password);
        verify(callback).onFailure(anyString());
        verify(callback, never()).onSuccess();
    }

    @Test
    public void testChangePassword_Success() {
        String email = "user@example.com";
        Callback callback = mock(Callback.class);

        Task<Void> resetTask = mock(Task.class);

        when(mockAuth.sendPasswordResetEmail(email)).thenReturn(resetTask);
        simulateTaskSuccess(resetTask);

        dataRepository.changePassword(email, callback);

        verify(mockAuth).sendPasswordResetEmail(email);
        verify(callback).onSuccess();
        verify(callback, never()).onFailure(any());
    }

    @Test
    public void testChangePassword_Failure() {
        String email = "user@example.com";
        Callback callback = mock(Callback.class);

        Task<Void> resetTask = mock(Task.class);
        Exception ex = new Exception("Reset failed");

        when(mockAuth.sendPasswordResetEmail(email)).thenReturn(resetTask);
        simulateTaskFailure(resetTask, ex);

        dataRepository.changePassword(email, callback);

        verify(mockAuth).sendPasswordResetEmail(email);
        verify(callback).onFailure(anyString());
        verify(callback, never()).onSuccess();
    }
}
