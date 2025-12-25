package com.example.utilization;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;

public class AddWasteManager {

    private final AppDataBase db;

    //класс для добавления сдачи отходов без интерфейса (используется для объединения безнес-логики работы с бд)
    public AddWasteManager(Context context) {
        this.db = AppDataBase.get(context);
    }

    //добавляет запись о сдаче отходов для пользователя
    public void addWasteForUser(
            int userId,
            int categoryId,
            float weight,
            int recyclingPointId
    ) {

        Executors.newSingleThreadExecutor().execute(() -> {

            //создание истории сдачи RecyclingHistory
            RecyclingHistory history = new RecyclingHistory();
            history.userID = userId;
            history.recyclingPoint = recyclingPointId;

            SimpleDateFormat sdf =
                    new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            history.date = sdf.format(new Date());

            //создание истории и получение id
            long historyId =
                    db.recyclingHistoryDao().insert(history);

            WasteGiven given = new WasteGiven();
            given.historyId = (int) historyId;
            given.wasteCategory = categoryId;
            given.wasteWeight = weight;
            given.givingDate = sdf.format(new Date());

            //сохранение отходов
            db.wasteGivenDao().insert(given);
        });
    }
}