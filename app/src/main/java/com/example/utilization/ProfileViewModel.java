package com.example.utilization;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.Executors;

public class ProfileViewModel extends AndroidViewModel {

    private final UserDAO userDao;
    private final MutableLiveData<UserEntity> userLiveData =
            new MutableLiveData<>();

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        userDao = AppDataBase.get(application).userDao();

        loadUser();
    }

    private void loadUser() {
        SharedPreferences prefs =
                getApplication()
                        .getSharedPreferences("auth", Context.MODE_PRIVATE);

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
}

