package com.example.utilization;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

@RunWith(RobolectricTestRunner.class)
public class AuthRepositoryTest {

    @Rule
    public InstantTaskExecutorRule rule =
            new InstantTaskExecutorRule();
    private AuthRepository repository;
    private AppDataBase db;
    private Context context;
    @Before
    public void setup() {
        context = ApplicationProvider.getApplicationContext();

        db = Room.inMemoryDatabaseBuilder(
                        context,
                        AppDataBase.class
                )
                .allowMainThreadQueries()
                .build();

        AppDataBase.setTestInstance(db);
        repository = new AuthRepository(context);
    }

    @Test
    public void loginFail() throws Exception {
        LiveData<AuthResult> liveData =
                repository.login("wrong@mail.com", "0000");

        AuthResult result =
                LiveDataTestUtil.getValue(liveData);

        assertNotNull(result.getError());
    }

    @Test
    public void loginSuccess() throws Exception {
        UserEntity user = new UserEntity();
        user.email = "d@mail.ru";
        user.password = "123456";
        user.name = "d";

        db.userDao().insert(user);

        LiveData<AuthResult> liveData =
                repository.login("d@mail.ru", "123456");

        AuthResult result =
                LiveDataTestUtil.getValue(liveData);

        assertNull(result.getError());
    }
}