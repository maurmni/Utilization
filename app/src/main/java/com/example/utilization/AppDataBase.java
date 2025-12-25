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
        version = 13,
        exportSchema = false
)
//инициализация бд room (при первом создании бд авоматически заполняется)
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
                                        new RecyclingPoint("Экопункт №2", "ул. Советская 20/5", "89305274062", 2)
                                );
                                database.recyclingPointDao().insert(
                                        new RecyclingPoint("Экопункт №3", "ул. Кобозева 33", "89570265826", 3)
                                );
                                database.recyclingPointDao().insert(
                                        new RecyclingPoint("Экопункт №4", "пр. Победы 1а", "89257015916", 4)
                                );
                                database.recyclingPointDao().insert(
                                        new RecyclingPoint("Экопункт №5", "пр. Гагарина 43/2", "89861937098", 5)
                                );
                                database.recyclingPointDao().insert(
                                        new RecyclingPoint("Экопункт №6", "пр. Гагарина 27/5/2", "89892351679", 1)
                                );
                                database.recyclingPointDao().insert(
                                        new RecyclingPoint("Экопункт №7", "ул. Новая 25", "89096426786", 2)
                                );
                                database.recyclingPointDao().insert(
                                        new RecyclingPoint("Экопункт №8", "ул. Калининградская 35", "89986416787", 3)
                                );
                                database.recyclingPointDao().insert(
                                        new RecyclingPoint("Экопункт №9", "ул. Автомобилистов 23/1", "89094572856", 4)
                                );
                                database.recyclingPointDao().insert(
                                        new RecyclingPoint("Экопункт №10", "ул. Берёзка 2/5", "8935198093", 5)
                                );
                                database.recyclingPointDao().insert(
                                        new RecyclingPoint("Экопункт №11", "ул. Джержинского 23", "89850253851", 1)
                                );
                                database.recyclingPointDao().insert(
                                        new RecyclingPoint("Экопункт №12", "ул. Джержинского 2/3", "89892650275", 2)
                                );
                                database.recyclingPointDao().insert(
                                        new RecyclingPoint("Разработано:", "Ахметова Дарья 4ПК2", "89325316153", 3)
                                );
                            });
                        }
                    })
                    .build();
        }
        return INSTANCE;
    }
    public static void setTestInstance(AppDataBase testDb) {
        INSTANCE = testDb;
    }
}
