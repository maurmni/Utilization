package com.example.utilization;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.Executors;

//ViewModel для работы с отданными отходами
public class WasteGivenViewModel extends AndroidViewModel {

    //Dao для операций с отходами
    private final WasteGivenDao wasteGivenDao;
    //Dao для истории сдачи
    private final RecyclingHistoryDao historyDao;

    public WasteGivenViewModel(@NonNull Application application) {
        super(application);

        //получение бд и DAO
        AppDataBase db = AppDataBase.get(application);
        wasteGivenDao = db.wasteGivenDao();
        historyDao = db.recyclingHistoryDao();
    }

    //удаление записи о сданных отходах
    public void delete(WasteGiven item) {
        Executors.newSingleThreadExecutor().execute(() -> {

            //удаление записи
            wasteGivenDao.delete(item);

            //подсчет оставшихся записей в истории
            int count = wasteGivenDao.countByHistory(item.historyId);

            //если нет записей, то история удаляется
            if (count == 0) {
                historyDao.deleteById(item.historyId);
            }
        });
    }

    //обновление записи о сданных отходах
    public void update(WasteGiven item) {
        Executors.newSingleThreadExecutor().execute(() ->
                wasteGivenDao.update(item)
        );
    }

    //добавление новой записи о сданных отходах
    public void insert(WasteGiven item) {
        Executors.newSingleThreadExecutor().execute(() ->
                wasteGivenDao.insert(item)
        );
    }
    //добавление отходов с одновременнмым созданием истории сдачи
    public void insertWithHistory(
            int userId,
            int categoryId,
            float weight,
            int recyclingPointId,
            String date
    ) {
        Executors.newSingleThreadExecutor().execute(() -> {

            //создание записи истории
            RecyclingHistory history = new RecyclingHistory();
            history.userID = userId;
            history.recyclingPoint = recyclingPointId;
            history.date = date;

            long historyId = historyDao.insert(history);

            //создание записи о сданных отходах
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