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

//для экрана пользователя (зашрузка и обновление данных пользователя)
public class ProfileViewModel extends AndroidViewModel {

    private final UserDAO userDao;

    //LiveData с текущими данными юзера
    private final MutableLiveData<UserEntity> userLiveData = new MutableLiveData<>();

    //конструктор ViewModel
    public ProfileViewModel(@NonNull Application application) {
        super(application);
        //получение DAO из бд
        userDao = AppDataBase.get(application).userDao();
        //загрузка юзера при создании ViewModel
        loadUser();
    }

    //загрузка тек пользователя из SharedPreferences и бд
    private void loadUser() {
        //получение id юзера
        SharedPreferences prefs = getApplication().getSharedPreferences("auth", Context.MODE_PRIVATE);
        int userId = prefs.getInt("USER_ID", -1);

        //если пользователь не авторзован то выход
        if (userId == -1) return;

        //выполнение запроса к бд
        Executors.newSingleThreadExecutor().execute(() -> {
            UserEntity user = userDao.getById(userId);
            //передача данных в LiveData
            userLiveData.postValue(user);
        });
    }

    //возвращает LiveData с тек юзером
    public LiveData<UserEntity> getUser() {
        return userLiveData;
    }

    //обновление данных юзера
    public void updateUser(String name, String email, String password) {
        Executors.newSingleThreadExecutor().execute(() -> {
            UserEntity user = userLiveData.getValue();
            if (user == null) return;

            //обновление полей
            user.name = name;
            user.email = email;
            user.password = password;

            userDao.update(user); // метод update в UserDAO, сохранение изменений в бд

            //обновление LiveData
            userLiveData.postValue(user);
        });
    }

    //обновление профиля юзера
    public LiveData<Boolean> updateProfile(int userId, String newEmail, String newPassword) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                UserEntity user = userDao.getById(userId);
                if (user != null) {
                    //обновление только заполненных полей
                    if (!newEmail.isEmpty()) user.email = newEmail;
                    if (!newPassword.isEmpty()) user.password = newPassword;
                    userDao.update(user);
                    result.postValue(true);
                    userLiveData.postValue(user);
                } else {
                    result.postValue(false);
                }
            } catch (Exception e) {
                //ошибка
                result.postValue(false);
            }
        });
        return result;
    }
}