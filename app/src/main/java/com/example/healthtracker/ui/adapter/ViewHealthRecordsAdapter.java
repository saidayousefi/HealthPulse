package com.example.healthtracker.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.healthtracker.R;
import com.example.healthtracker.data.local.model.RecordData;

public class ViewHealthRecordsAdapter extends ListAdapter<RecordData, ViewHealthRecordsAdapter.RecordDataViewHolder> {

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(RecordData recordData);
        void onDeleteClick(RecordData recordData);
    }

    public ViewHealthRecordsAdapter(OnItemClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<RecordData> DIFF_CALLBACK = new DiffUtil.ItemCallback<RecordData>() {
        @Override
        public boolean areItemsTheSame(@NonNull RecordData oldItem, @NonNull RecordData newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull RecordData oldItem, @NonNull RecordData newItem) {
            return oldItem.equals(newItem);
        }
    };

    @NonNull
    @Override
    public RecordDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record_data, parent, false);
        return new RecordDataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordDataViewHolder holder, int position) {
        RecordData recordData = getItem(position);
        holder.bind(recordData, listener);
    }

    public static class RecordDataViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewDateTime;
        private final TextView textViewSystolic;
        private final TextView textViewDiastolic;
        private final TextView textViewBloodSugar;
        private final Button buttonEdit;
        private final Button buttonDelete;

        public RecordDataViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDateTime = itemView.findViewById(R.id.textViewDateTime);
            textViewSystolic = itemView.findViewById(R.id.textViewSystolic);
            textViewDiastolic = itemView.findViewById(R.id.textViewDiastolic);
            textViewBloodSugar = itemView.findViewById(R.id.textViewBloodSugar);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }

        public void bind(final RecordData recordData, final OnItemClickListener listener) {
            textViewDateTime.setText("DateTime: " + recordData.getDate() + " " + recordData.getTime());
            textViewSystolic.setText("Systolic: " + recordData.getSystolic());
            textViewDiastolic.setText("Diastolic: " + recordData.getDiastolic());
            textViewBloodSugar.setText("Blood Sugar: " + recordData.getBloodSugar());

            buttonEdit.setOnClickListener(v -> listener.onItemClick(recordData));
            buttonDelete.setOnClickListener(v -> listener.onDeleteClick(recordData));
        }
    }
}
