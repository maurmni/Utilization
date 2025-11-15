package com.example.utilization;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AuthRepository {
    private static final String PREF_NAME = "auth_prefs";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_NAME = "user_name";
    private SharedPreferences sharedPreferences;
    private ExecutorService executorService;

    public AuthRepository(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.executorService = Executors.newSingleThreadExecutor();
    }

    //логин
    public LiveData<AuthResult> login(String email, String password) {
        MutableLiveData<AuthResult> result = new MutableLiveData<>();

        executorService.execute(() -> {
            try {
                Thread.sleep(1000);

                if (isValidCredentials(email, password)) {
                    User user = new User(
                            UUID.randomUUID().toString(),
                            "User Name",
                            email,
                            new Date(),
                            0
                    );

                    saveUserData(user);
                    result.postValue(new AuthResult(user));
                } else {
                    result.postValue(new AuthResult("Неверный email или пароль"));
                }
            } catch (Exception e) {
                result.postValue(new AuthResult("Ошибка подключения"));
            }
        });

        return result;
    }

    //регистрация
    public LiveData<AuthResult> register(String name, String email, String password) {
        MutableLiveData<AuthResult> result = new MutableLiveData<>();

        executorService.execute(() -> {
            try {
                Thread.sleep(1000);

                if (isEmailTaken(email)) {
                    result.postValue(new AuthResult("Email уже зарегистрирован"));
                    return;
                }

                if (!isValidPassword(password)) {
                    result.postValue(new AuthResult("Пароль должен содержать минимум 6 символов"));
                    return;
                }

                User user = new User(
                        UUID.randomUUID().toString(),
                        name,
                        email,
                        new Date(),
                        0
                );

                saveUserData(user);
                result.postValue(new AuthResult(user));

            } catch (Exception e) {
                result.postValue(new AuthResult("Ошибка регистрации"));
            }
        });

        return result;
    }

    //восстановление пароля
    public LiveData<Boolean> resetPassword(String email) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();

        executorService.execute(() -> {
            try {
                Thread.sleep(1000);
                // Имитация отправки email
                result.postValue(true);
            } catch (Exception e) {
                result.postValue(false);
            }
        });

        return result;
    }

    //проверка авторизации
    public boolean isUserLoggedIn() {
        return sharedPreferences.contains(KEY_USER_ID);
    }
    public User getCurrentUser() {
        if (!isUserLoggedIn()) return null;

        return new User(
                sharedPreferences.getString(KEY_USER_ID, ""),
                sharedPreferences.getString(KEY_USER_NAME, ""),
                sharedPreferences.getString(KEY_USER_EMAIL, ""),
                new Date(sharedPreferences.getLong("registration_date", 0)),
                sharedPreferences.getInt("total_points", 0)
        );
    }

    //выход
    public void logout() {
        sharedPreferences.edit().clear().apply();
    }

    private void saveUserData(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_ID, user.getUserId());
        editor.putString(KEY_USER_NAME, user.getUserName());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.putLong("registration_date", user.getRegistrationDate().getTime());
        editor.putInt("total_points", user.getTotalPoints());
        editor.apply();
    }

    private boolean isValidCredentials(String email, String password) {
        return !email.isEmpty() && password.length() >= 6;
    }

    private boolean isEmailTaken(String email) {
        return false;
    }

    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
}