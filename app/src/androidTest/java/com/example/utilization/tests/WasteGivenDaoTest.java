package com.example.utilization.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.utilization.AppDataBase;
import com.example.utilization.RecyclingHistory;
import com.example.utilization.RecyclingHistoryDao;
import com.example.utilization.RecyclingPoint;
import com.example.utilization.RecyclingPointDao;
import com.example.utilization.UserDAO;
import com.example.utilization.UserEntity;
import com.example.utilization.WasteCategory;
import com.example.utilization.WasteCategoryDao;
import com.example.utilization.WasteGiven;
import com.example.utilization.WasteGivenDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

@RunWith(AndroidJUnit4.class)
public class WasteGivenDaoTest {

    private AppDataBase db;
    private WasteGivenDao wasteGivenDao;
    private UserDAO userDao;
    private RecyclingHistoryDao historyDao;
    private RecyclingPointDao pointDao;
    private WasteCategoryDao categoryDao;
    private int userId;
    private int historyId;
    private int pointId;
    private int categoryId;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();

        db = Room.inMemoryDatabaseBuilder(
                        context,
                        AppDataBase.class
                )
                .allowMainThreadQueries()
                .build();

        userDao = db.userDao();
        historyDao = db.recyclingHistoryDao();
        wasteGivenDao = db.wasteGivenDao();
        pointDao = db.recyclingPointDao();
        categoryDao = db.wasteCategoryDao();

        //создание user
        UserEntity user = new UserEntity();
        user.id = 1;
        user.email = "test@mail.ru";
        user.password = "123456";
        user.name = "Test";
        user.registrationDate = new Date().getTime();
        userId = (int) userDao.insert(user);

        //создание пункта приема
        RecyclingPoint point = new RecyclingPoint(
                "Test Point",
                "Test Address",
                "1234567890",
                1
        );
        pointId = (int) pointDao.insert(point);

        //создание категории отходов
        WasteCategory category = new WasteCategory(
                "Test Category",
                "Test Description",
                10
        );
        categoryId = (int) categoryDao.insert(category);

        //создание history с userID
        RecyclingHistory history = new RecyclingHistory();
        history.userID = userId;
        history.recyclingPoint = pointId;
        history.date = "2025-12-24";
        historyId = (int) historyDao.insert(history);
    }

    @After
    public void tearDown() {
        db.close();
    }

    @Test
    public void insertAndDeleteWasteGiven() throws Exception{
        //создание WasteGiven
        WasteGiven waste = new WasteGiven();
        waste.userID = userId;
        waste.historyId = historyId;
        waste.wasteWeight = 2.5f;
        waste.wasteCategory = categoryId;
        waste.givingDate = "2025-12-24";
        int id = (int) wasteGivenDao.insert(waste);

        WasteGiven fromDb = LiveDataTestUtil.getValue(wasteGivenDao.getByIdLive(id));
        assertNotNull("WasteGiven должен существовать",fromDb);

        //проверка что данные совпадают
        assertEquals("UserID должен совпадать", userId, fromDb.userID);
        assertEquals("HistoryId должен совпадать", historyId, fromDb.historyId);
        assertEquals("Вес должен совпадать", 2.5f, fromDb.wasteWeight, 0.01);
        assertEquals("Категория должна совпадать", categoryId, fromDb.wasteCategory);

        //удаление
        wasteGivenDao.delete(fromDb);

        WasteGiven deleted = LiveDataTestUtil.getValue(
                wasteGivenDao.getByIdLive(id)
        );
        assertNull(deleted);
    }
    @Test
    public void testForeignKeyConstraints() {
        //тест на проверку foreign key constraint
        try {
            WasteGiven waste = new WasteGiven();
            waste.userID = 9999; // несуществующий пользователь
            waste.historyId = historyId;
            waste.wasteWeight = 2.5f;
            waste.wasteCategory = categoryId;
            waste.givingDate = "2025-12-24";

            wasteGivenDao.insert(waste);

            //ошибка, должно было быть исключение
            throw new AssertionError("Должна была быть ошибка FOREIGN KEY constraint");
        } catch (android.database.sqlite.SQLiteConstraintException e) {
            //ожидаемое исключение
            System.out.println("Ожидаемая ошибка FOREIGN KEY: " + e.getMessage());
        }
    }
    @Test
    public void printDatabaseSchema() {
        //проверка на существование таблиц
        System.out.println("Таблицы в базе:");

        //запрос для проверки
        int userCount = userDao.countUsers();
        int historyCount = historyDao.countHistories();
        int wasteGivenCount = wasteGivenDao.countWasteGivens();

        System.out.println("Пользователей: " + userCount);
        System.out.println("Историй: " + historyCount);
        System.out.println("WasteGiven: " + wasteGivenCount);
    }
}