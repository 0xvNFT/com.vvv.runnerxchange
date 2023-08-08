package com.vvv.runnerxchange;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private final List<HistoryItem> historyItems;

    public HistoryAdapter(List<HistoryItem> historyItems) {
        this.historyItems = historyItems;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item_layout, parent, false);
        return new HistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        HistoryItem currentItem = historyItems.get(position);
        holder.dateTextView.setText(currentItem.getDateTime());
        holder.durationTextView.setText(currentItem.getDuration());
        holder.distanceTextView.setText(currentItem.getDistance());
        holder.caloriesTextView.setText(currentItem.getCaloriesBurned());
    }

    @Override
    public int getItemCount() {
        return historyItems.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView;
        TextView durationTextView;
        TextView distanceTextView;
        TextView caloriesTextView;

        HistoryViewHolder(View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.date_time);
            durationTextView = itemView.findViewById(R.id.duration_time);
            distanceTextView = itemView.findViewById(R.id.distance);
            caloriesTextView = itemView.findViewById(R.id.calories);
        }
    }
}

