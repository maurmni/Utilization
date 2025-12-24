package com.example.utilization;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;

public class AddWasteManager {

    private final AppDataBase db;

    public AddWasteManager(Context context) {
        this.db = AppDataBase.get(context);
    }

    public void addWasteForUser(
            int userId,
            int categoryId,
            float weight,
            int recyclingPointId
    ) {

        Executors.newSingleThreadExecutor().execute(() -> {

            RecyclingHistory history = new RecyclingHistory();
            history.userID = userId;
            history.recyclingPoint = recyclingPointId;

            SimpleDateFormat sdf =
                    new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            history.date = sdf.format(new Date());

            long historyId =
                    db.recyclingHistoryDao().insert(history);

            WasteGiven given = new WasteGiven();
            given.historyId = (int) historyId;   // ← ВАЖНО
            given.wasteCategory = categoryId;
            given.wasteWeight = weight;
            given.givingDate = sdf.format(new Date());

            db.wasteGivenDao().insert(given);
        });
    }
}