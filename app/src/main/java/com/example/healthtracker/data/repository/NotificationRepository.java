package com.example.healthtracker.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.healthtracker.data.local.AppDatabase;
import com.example.healthtracker.data.local.dao.NotificationDao;
import com.example.healthtracker.data.local.model.NotificationData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotificationRepository {

    private final NotificationDao notificationDao;
    private final LiveData<List<NotificationData>> allNotifications;
    private final ExecutorService executorService;

    public NotificationRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        notificationDao = db.notificationDao();
        allNotifications = notificationDao.getAllNotifications();
        executorService = Executors.newFixedThreadPool(2);
    }

    public LiveData<List<NotificationData>> getAllNotifications() {
        return allNotifications;
    }

    public void insert(NotificationData notification) {
        executorService.execute(() -> notificationDao.insert(notification));
    }

    public void delete(NotificationData notification) {
        executorService.execute(() -> notificationDao.delete(notification));
    }
}
