package com.example.healthtracker.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.healthtracker.R;
import com.example.healthtracker.data.local.model.NotificationData;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private List<NotificationData> notifications;
    private final OnItemClickListener listener;

    public NotificationAdapter(OnItemClickListener listener) {

        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(NotificationData notificationData);
        void onDeleteClick(NotificationData notificationData);
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationData currentNotification = notifications.get(position);
        holder.bind(currentNotification, listener);
    }

    @Override
    public int getItemCount() {

        return notifications != null ? notifications.size() : 0;
    }

    public void setNotifications(List<NotificationData> notifications) {
        this.notifications = notifications;
        notifyDataSetChanged();
        Log.d("NotificationAdapter", "Set " + notifications.size() + " notifications and notified data set changed");
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewNotificationMessage;
        private final TextView textViewNotificationTime;
        private final Button deleteButton;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNotificationMessage = itemView.findViewById(R.id.notification_message);
            textViewNotificationTime = itemView.findViewById(R.id.notification_time);
            deleteButton = itemView.findViewById(R.id.buttonDeleteNotification);
        }

        void bind(final NotificationData notification, final OnItemClickListener listener) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            textViewNotificationTime.setText(sdf.format(notification.getNotificationTime()));
            textViewNotificationMessage.setText(notification.getNotificationMessage());
            itemView.setOnClickListener(v -> listener.onItemClick(notification));
            deleteButton.setOnClickListener(v -> listener.onDeleteClick(notification));
        }
    }
}
