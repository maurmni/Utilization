package com.example.utilization;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.AndroidViewModel;

//ViewModel для авторизации (связывает фрагменты и репозитории)
public class AuthViewModel extends AndroidViewModel {

    private final AuthRepository authRepository;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application);
    }

    public LiveData<UserEntity> getCurrentUser() {
        return authRepository.getCurrentUser();
    }

    public LiveData<AuthResult> login(String email, String password) {
        return authRepository.login(email, password);
    }

    public LiveData<AuthResult> register(String name, String email, String password) {
        return authRepository.register(name, email, password);
    }

    public LiveData<Boolean> resetPassword(String email, String newPassword) {
        return authRepository.resetPassword(email, newPassword);
    }

    public void logout() {
        authRepository.logout();
    }

    public boolean isUserLoggedIn() {
        return authRepository.isUserLoggedIn();
    }
}