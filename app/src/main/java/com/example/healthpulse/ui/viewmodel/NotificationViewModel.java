package com.example.healthpulse.ui.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.healthpulse.data.local.model.NotificationData;
import com.example.healthpulse.data.repository.NotificationRepository;
import java.util.List;

public class NotificationViewModel extends AndroidViewModel {
    private final NotificationRepository repository;
    private final LiveData<List<NotificationData>> allNotifications;

    public NotificationViewModel(Application application) {
        super(application);
        repository = new NotificationRepository(application);
        allNotifications = repository.getAllNotifications();
    }

    public LiveData<List<NotificationData>> getAllNotifications() {
        return allNotifications;
    }

    public void insert(NotificationData notificationData) {
        repository.insert(notificationData);
    }

    public void delete(NotificationData notificationData) {
        repository.delete(notificationData);
    }
}
