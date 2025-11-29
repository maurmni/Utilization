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

    private final UserDAO userDao;
    private final ExecutorService executor =
            Executors.newSingleThreadExecutor();

    private final SharedPreferences prefs;

    public AuthRepository(Context context) {
        userDao = AppDataBase.get(context).userDao();
        prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
    }

    //логин
    public LiveData<AuthResult> login(String email, String password) {
        MutableLiveData<AuthResult> result = new MutableLiveData<>();

        executor.execute(() -> {
            UserEntity user = userDao.login(email, password);

            if (user == null) {
                result.postValue(
                        new AuthResult("Неверный логин или пароль")
                );
            } else {
                prefs.edit()
                        .putInt("USER_ID", user.id)
                        .apply();

                result.postValue(new AuthResult(null));
            }
        });

        return result;
    }

    //регистрация
    public LiveData<AuthResult> register(
            String name,
            String email,
            String password
    ) {
        MutableLiveData<AuthResult> result = new MutableLiveData<>();

        executor.execute(() -> {
            if (userDao.findByEmail(email) != null) {
                result.postValue(
                        new AuthResult("Email уже существует")
                );
                return;
            }

            UserEntity user = new UserEntity(
                    name,
                    email,
                    password,
                    System.currentTimeMillis()
            );

            userDao.insert(user);

            UserEntity saved =
                    userDao.findByEmail(email);

            prefs.edit()
                    .putInt("USER_ID", saved.id)
                    .apply();

            result.postValue(new AuthResult(null));
        });

        return result;
    }

    //проверка авторизации
    public boolean isUserLoggedIn() {
        return prefs.getInt("USER_ID", -1) != -1;
    }

    //текущий пользователь
    public LiveData<UserEntity> getCurrentUser() {
        MutableLiveData<UserEntity> user = new MutableLiveData<>();

        int id = prefs.getInt("USER_ID", -1);
        if (id == -1) {
            user.setValue(null);
            return user;
        }

        executor.execute(() -> {
            user.postValue(userDao.getById(id));
        });

        return user;
    }

    //выход
    public void logout() {
        prefs.edit().clear().apply();
    }
    public LiveData<Boolean> resetPassword(
            String email,
            String newPassword
    ) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();

        executor.execute(() -> {
            int updated = userDao.resetPassword(email, newPassword);
            result.postValue(updated > 0);
        });

        return result;
    }


}
