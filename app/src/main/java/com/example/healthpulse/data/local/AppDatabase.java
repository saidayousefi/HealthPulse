package com.example.healthpulse.data.local;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.example.healthpulse.data.Converters;
import com.example.healthpulse.data.local.dao.NotificationDao;
import com.example.healthpulse.data.local.dao.RecordDao;
import com.example.healthpulse.data.local.dao.ProfileDao;
import com.example.healthpulse.data.local.model.NotificationData;
import com.example.healthpulse.data.local.model.RecordData;
import com.example.healthpulse.data.local.model.Profile;


@Database(entities = {RecordData.class, Profile.class, NotificationData.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract RecordDao recordDao();
    public abstract ProfileDao profileDao();
    public abstract NotificationDao notificationDao();

    private static volatile AppDatabase INSTANCE;



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
