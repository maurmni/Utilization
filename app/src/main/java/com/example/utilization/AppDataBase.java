package com.example.utilization;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import androidx.annotation.NonNull;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.Executors;

@Database(
        entities = {
                UserEntity.class,
                WasteCategory.class,
                WasteGiven.class,
                RecyclingHistory.class,
                RecyclingPoint.class
        },
        version = 11,
        exportSchema = false
)
public abstract class AppDataBase extends RoomDatabase {

    private static AppDataBase INSTANCE;

    public abstract UserDAO userDao();
    public abstract WasteCategoryDao wasteCategoryDao();
    public abstract WasteGivenDao wasteGivenDao();
    public abstract RecyclingHistoryDao recyclingHistoryDao();
    public abstract RecyclingPointDao recyclingPointDao();

    public static synchronized AppDataBase get(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDataBase.class,
                            "app_db"
                    )
                    .fallbackToDestructiveMigration()
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);

                            Executors.newSingleThreadExecutor().execute(() -> {
                                AppDataBase database = INSTANCE;

                                //категории
                                database.wasteCategoryDao().insert(
                                        new WasteCategory("Бумага", "Офисная бумага, газеты, картон", 5)
                                );
                                database.wasteCategoryDao().insert(
                                        new WasteCategory("Пластик", "Бутылки, упаковка, контейнеры", 8)
                                );
                                database.wasteCategoryDao().insert(
                                        new WasteCategory("Стекло", "Банки, бутылки", 3)
                                );
                                database.wasteCategoryDao().insert(
                                        new WasteCategory("Металл", "Алюминий, жесть", 10)
                                );
                                database.wasteCategoryDao().insert(
                                        new WasteCategory("Органика", "Пищевые отходы", 2)
                                );

                                //пункты приёма
                                database.recyclingPointDao().insert(
                                        new RecyclingPoint("Экопункт №1", "ул. Ленина 10", "89457628473", 1)
                                );
                                database.recyclingPointDao().insert(
                                        new RecyclingPoint("Экопункт №2", "пр. Мира 25", "89305274062", 2)
                                );
                            });
                        }
                    })
                    .build();
        }
        return INSTANCE;
    }
}
