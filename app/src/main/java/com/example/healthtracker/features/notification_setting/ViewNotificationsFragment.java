package com.example.healthtracker.features.notification_setting;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.healthtracker.MyDividerItemDecoration;
import com.example.healthtracker.R;
import com.example.healthtracker.data.local.model.NotificationData;
import com.example.healthtracker.databinding.FragmentViewNotificationsBinding;
import com.example.healthtracker.ui.Navigator;
import com.example.healthtracker.ui.adapter.NotificationAdapter;
import com.example.healthtracker.ui.viewmodel.NotificationViewModel;
import java.util.Collections;
import java.util.Comparator;

public class ViewNotificationsFragment extends Fragment implements Navigator {

    private NotificationViewModel notificationViewModel;
    private FragmentViewNotificationsBinding binding;
    private NotificationAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        notificationViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_notifications, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        adapter = new NotificationAdapter(new NotificationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(NotificationData notificationData) {

            }

            @Override
            public void onDeleteClick(NotificationData notificationData) {

                notificationViewModel.delete(notificationData);
                Toast.makeText(getContext(), "Notification deleted", Toast.LENGTH_SHORT).show();
            }
        });


        binding.recyclerViewNotifications.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewNotifications.setAdapter(adapter);


        MyDividerItemDecoration dividerItemDecoration = new MyDividerItemDecoration(getContext());
        binding.recyclerViewNotifications.addItemDecoration(dividerItemDecoration);


        notificationViewModel.getAllNotifications().observe(getViewLifecycleOwner(), notifications -> {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Collections.sort(notifications, Comparator.comparing(NotificationData::getNotificationTime).reversed());
            }
            adapter.setNotifications(notifications);


            System.out.println("Observed notifications: " + notifications.size());
            System.out.println("Notifications updated. Count: " + notifications.size());
            for (NotificationData notification : notifications) {
                System.out.println("Notification: " + notification.getNotificationMessage() + " at " + notification.getNotificationTime());
            }
        });
    }

    @Override
    public void onBackClicked() {
        Navigation.findNavController(requireView()).navigate(R.id.action_notificationSettingsFragment_to_viewNotificationsFragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
