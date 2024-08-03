package com.example.healthpulse.features.notification_setting;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import com.example.healthpulse.MainActivity;
import com.example.healthpulse.R;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("NotificationReceiver", "onReceive called");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "HealthPulse_channel")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Health Data Reminder")
                .setContentText("Don't forget to log your health data!")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        Intent resultIntent = new Intent(context, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        builder.setContentIntent(resultPendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }
}
