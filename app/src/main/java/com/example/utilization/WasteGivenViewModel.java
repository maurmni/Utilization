package com.example.utilization;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.Executors;

public class WasteGivenViewModel extends AndroidViewModel {

    private final WasteGivenDao wasteGivenDao;
    private final RecyclingHistoryDao historyDao;

    public WasteGivenViewModel(@NonNull Application application) {
        super(application);

        AppDataBase db = AppDataBase.get(application);
        wasteGivenDao = db.wasteGivenDao();
        historyDao = db.recyclingHistoryDao();
    }

    public void delete(WasteGiven item) {
        Executors.newSingleThreadExecutor().execute(() -> {

            wasteGivenDao.delete(item);

            int count = wasteGivenDao.countByHistory(item.historyId);

            if (count == 0) {
                historyDao.deleteById(item.historyId);
            }
        });
    }

    public void update(WasteGiven item) {
        Executors.newSingleThreadExecutor().execute(() ->
                wasteGivenDao.update(item)
        );
    }
    public void insert(WasteGiven item) {
        Executors.newSingleThreadExecutor().execute(() ->
                wasteGivenDao.insert(item)
        );
    }
    public void insertWithHistory(
            int userId,
            int categoryId,
            float weight,
            int recyclingPointId,
            String date
    ) {
        Executors.newSingleThreadExecutor().execute(() -> {

            RecyclingHistory history = new RecyclingHistory();
            history.userID = userId;
            history.recyclingPoint = recyclingPointId;
            history.date = date;

            long historyId = historyDao.insert(history);

            WasteGiven wg = new WasteGiven();
            wg.historyId = (int) historyId;
            wg.userID = userId;
            wg.wasteCategory = categoryId;
            wg.wasteWeight = weight;
            wg.givingDate = date;

            wasteGivenDao.insert(wg);
        });
    }
}