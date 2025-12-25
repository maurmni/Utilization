package com.example.utilization.tests;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class LiveDataTestUtil {

    public static <T> T getValue(LiveData<T> liveData) throws InterruptedException {
        final Object[] data = new Object[1];
        final CountDownLatch latch = new CountDownLatch(1);

        //создание observer
        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(T t) {
                data[0] = t;
                latch.countDown();
                liveData.removeObserver(this);
            }
        };

        //запуск на главном потоке
        androidx.test.platform.app.InstrumentationRegistry.getInstrumentation()
                .runOnMainSync(() -> {
                    liveData.observeForever(observer);
                });

        //ожидание данных (2 секунды макс)
        latch.await(2, TimeUnit.SECONDS);

        return (T) data[0];
    }
}