package com.example.healthtracker.ui.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.healthtracker.R;
import com.example.healthtracker.data.local.model.EducationalContent;

import java.util.List;

public class EducationalContentAdapter extends RecyclerView.Adapter<EducationalContentAdapter.EducationalContentViewHolder> {

    private List<EducationalContent> contentList;

    public EducationalContentAdapter(List<EducationalContent> contentList) {
        this.contentList = contentList;
    }

    @NonNull
    @Override
    public EducationalContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_educational_content, parent, false);
        return new EducationalContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EducationalContentViewHolder holder, int position) {
        EducationalContent content = contentList.get(position);
        holder.titleTextView.setText(content.getTitle());
        holder.contentTextView.setText(content.getContent());
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    static class EducationalContentViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView contentTextView;

        public EducationalContentViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
        }
    }
}
