package com.example.utilization;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
public class AuthViewModel extends ViewModel {
    private AuthRepository authRepository;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<AuthResult> authResult = new MutableLiveData<>();
    private MutableLiveData<Boolean> passwordResetResult = new MutableLiveData<>();

    public void setAuthRepository(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void login(String email, String password) {
        if (authRepository == null) return;

        if (!isValidEmail(email)) {
            authResult.setValue(new AuthResult("Введите корректный email"));
            return;
        }

        if (password.isEmpty()) {
            authResult.setValue(new AuthResult("Введите пароль"));
            return;
        }

        isLoading.setValue(true);
        authRepository.login(email, password).observeForever(result -> {
            isLoading.setValue(false);
            authResult.setValue(result);
        });
    }

    public void register(String name, String email, String password, String confirmPassword) {
        if (authRepository == null) return;

        if (name.isEmpty()) {
            authResult.setValue(new AuthResult("Введите имя"));
            return;
        }

        if (!isValidEmail(email)) {
            authResult.setValue(new AuthResult("Введите корректный email"));
            return;
        }

        if (password.length() < 6) {
            authResult.setValue(new AuthResult("Пароль должен содержать минимум 6 символов"));
            return;
        }

        if (!password.equals(confirmPassword)) {
            authResult.setValue(new AuthResult("Пароли не совпадают"));
            return;
        }

        isLoading.setValue(true);
        authRepository.register(name, email, password).observeForever(result -> {
            isLoading.setValue(false);
            authResult.setValue(result);
        });
    }

    public void resetPassword(String email) {
        if (authRepository == null) return;

        if (!isValidEmail(email)) {
            authResult.setValue(new AuthResult("Введите корректный email"));
            return;
        }

        isLoading.setValue(true);
        authRepository.resetPassword(email).observeForever(success -> {
            isLoading.setValue(false);
            passwordResetResult.setValue(success);
        });
    }

    public boolean isUserLoggedIn() {
        return authRepository != null && authRepository.isUserLoggedIn();
    }

    public User getCurrentUser() {
        return authRepository != null ? authRepository.getCurrentUser() : null;
    }

    public void logout() {
        if (authRepository != null) {
            authRepository.logout();
        }
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<AuthResult> getAuthResult() { return authResult; }
    public LiveData<Boolean> getPasswordResetResult() { return passwordResetResult; }
}