package com.example.utilization;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class ProfileViewModel extends AndroidViewModel {

    private final UserDAO userDao;
    private final MutableLiveData<UserEntity> userLiveData = new MutableLiveData<>();

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        userDao = AppDataBase.get(application).userDao();
        loadUser();
    }

    private void loadUser() {
        SharedPreferences prefs = getApplication().getSharedPreferences("auth", Context.MODE_PRIVATE);
        int userId = prefs.getInt("USER_ID", -1);
        if (userId == -1) return;

        Executors.newSingleThreadExecutor().execute(() -> {
            UserEntity user = userDao.getById(userId);
            userLiveData.postValue(user);
        });
    }

    public LiveData<UserEntity> getUser() {
        return userLiveData;
    }

    public void updateUser(String name, String email, String password) {
        Executors.newSingleThreadExecutor().execute(() -> {
            UserEntity user = userLiveData.getValue();
            if (user == null) return;

            user.name = name;
            user.email = email;
            user.password = password;

            userDao.update(user); // метод update в UserDAO
            userLiveData.postValue(user);
        });
    }

    public LiveData<Boolean> updateProfile(int userId, String newEmail, String newPassword) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                UserEntity user = userDao.getById(userId);
                if (user != null) {
                    if (!newEmail.isEmpty()) user.email = newEmail;
                    if (!newPassword.isEmpty()) user.password = newPassword;
                    userDao.update(user);
                    result.postValue(true);
                    userLiveData.postValue(user);
                } else {
                    result.postValue(false);
                }
            } catch (Exception e) {
                result.postValue(false);
            }
        });
        return result;
    }
}