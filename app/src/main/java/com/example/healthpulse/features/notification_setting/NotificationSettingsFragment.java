package com.example.healthpulse.features.notification_setting;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.example.healthpulse.R;
import com.example.healthpulse.data.local.model.NotificationData;
import com.example.healthpulse.databinding.FragmentNotificationSettingsBinding;
import com.example.healthpulse.ui.viewmodel.NotificationViewModel;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NotificationSettingsFragment extends Fragment {

    private FragmentNotificationSettingsBinding binding;
    private NotificationViewModel notificationViewModel;
    private int hour, minute;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNotificationSettingsBinding.inflate(inflater, container, false);
        notificationViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(notificationViewModel);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonViewNotifications.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_notificationSettingsFragment_to_viewNotificationsFragment)
        );

        binding.editTextTime.setOnClickListener(v -> showTimePickerDialog());

        binding.buttonSave.setOnClickListener(v -> saveNotification());
    }

    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        // Create a ContextThemeWrapper with your custom theme
        Context themedContext = new ContextThemeWrapper(getContext(), R.style.CustomTimePicker);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                themedContext,  // Use the themed context here
                (view, hourOfDay, selectedMinute) -> {
                    hour = hourOfDay;
                    minute = selectedMinute;
                    binding.editTextTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
                },
                hour,
                minute,
                true  // 24-hour format
        );

        timePickerDialog.show();
    }


    @SuppressLint("ScheduleExactAlarm")
    private void scheduleNotification(int hour, int minute) {
        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(requireContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    private void saveNotification() {
        String notificationMessage = binding.editTextNotificationMessage.getText() != null ? binding.editTextNotificationMessage.getText().toString() : "";
        if (notificationMessage.isEmpty()) {
            Toast.makeText(getContext(), "Please enter a notification message.", Toast.LENGTH_SHORT).show();
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        Date notificationTime = calendar.getTime();

        NotificationData notification = new NotificationData();
        notification.setNotificationTime(notificationTime);
        notification.setNotificationMessage(notificationMessage);

        Log.d("NotificationSettings", "Saving notification: " + notificationMessage + " at " + notificationTime);

        notificationViewModel.insert(notification);

        Toast.makeText(getContext(), "Notification saved successfully!", Toast.LENGTH_SHORT).show();
        scheduleNotification(hour, minute);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
