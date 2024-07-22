package com.example.healthtracker.features.notification_setting;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.example.healthtracker.R;
import com.example.healthtracker.data.local.model.NotificationData;
import com.example.healthtracker.databinding.FragmentNotificationSettingsBinding;
import com.example.healthtracker.ui.viewmodel.NotificationViewModel;
import java.util.Calendar;
import java.util.Date;

public class NotificationSettingsFragment extends Fragment {

    private FragmentNotificationSettingsBinding binding;
    private NotificationViewModel notificationViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNotificationSettingsBinding.inflate(inflater, container, false);
        notificationViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonViewNotifications.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_notificationSettingsFragment_to_viewNotificationsFragment);
        });

        binding.buttonSave.setOnClickListener(v -> {
            boolean notificationsEnabled = binding.switchEnableNotifications.isChecked();
            int hour = getHour();
            int minute = getMinute();

            if (notificationsEnabled) {
                scheduleNotification(hour, minute);
            } else {
                cancelNotification();
            }
            saveNotification();
        });
    }

    private int getHour() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return binding.timePickerReminder.getHour();
        } else {
            return getCurrentHourCompat();
        }
    }

    private int getMinute() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return binding.timePickerReminder.getMinute();
        } else {
            return getCurrentMinuteCompat();
        }
    }

    private void scheduleNotification(int hour, int minute) {
        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(requireContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    private void cancelNotification() {
        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(requireContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    private void saveNotification() {
        String notificationMessage = binding.editTextNotificationMessage.getText().toString();
        if (notificationMessage.isEmpty()) {
            Toast.makeText(getContext(), "Please enter a notification message.", Toast.LENGTH_SHORT).show();
            return;
        }

        int hour = getHour();
        int minute = getMinute();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        Date notificationTime = calendar.getTime();

        NotificationData notification = new NotificationData();
        notification.setNotificationTime(notificationTime);
        notification.setNotificationMessage(notificationMessage);

        Log.d("NotificationSettings", "Saving notification: " + notificationMessage + " at " + notificationTime);

        notificationViewModel.insert(notification);

        Toast.makeText(getContext(), "Notification saved successfully!", Toast.LENGTH_SHORT).show();
    }

    @SuppressWarnings("deprecation")
    private int getCurrentHourCompat() {
        return binding.timePickerReminder.getCurrentHour();
    }

    @SuppressWarnings("deprecation")
    private int getCurrentMinuteCompat() {
        return binding.timePickerReminder.getCurrentMinute();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
