package com.example.healthpulse.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.healthpulse.data.local.model.NotificationData;
import java.util.List;

@Dao
public interface NotificationDao {

    @Insert
    void insert(NotificationData notification);

    @Delete
    void delete(NotificationData notification);

    @Query("SELECT * FROM notification_data ORDER BY notificationTime DESC")
    LiveData<List<NotificationData>> getAllNotifications();
}
