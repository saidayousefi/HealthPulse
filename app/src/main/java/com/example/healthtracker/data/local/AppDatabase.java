package com.example.healthtracker.data.local;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.example.healthtracker.data.Converters;
import com.example.healthtracker.data.local.dao.NotificationDao;
import com.example.healthtracker.data.local.dao.RecordDao;
import com.example.healthtracker.data.local.dao.ProfileDao;
import com.example.healthtracker.data.local.model.NotificationData;
import com.example.healthtracker.data.local.model.RecordData;
import com.example.healthtracker.data.local.model.Profile;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {RecordData.class, Profile.class, NotificationData.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract RecordDao recordDao();
    public abstract ProfileDao profileDao();
    public abstract NotificationDao notificationDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "health_tracker_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
