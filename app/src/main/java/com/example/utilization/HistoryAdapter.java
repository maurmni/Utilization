package com.example.utilization;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter
        extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    public interface OnItemClickListener {
        void onClick(RecyclingHistory history);
    }

    private List<RecyclingHistory> list;
    private OnItemClickListener listener;

    public HistoryAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<RecyclingHistory> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycling_history, parent, false);

        return new HistoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        RecyclingHistory history = list.get(position);
        holder.bind(history);
        holder.itemView.setOnClickListener(v -> listener.onClick(history));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {

        TextView tvPoint, tvDate;

        HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPoint = itemView.findViewById(R.id.tvPoint);
            tvDate = itemView.findViewById(R.id.tvDate);
        }

        void bind(RecyclingHistory h) {
            tvPoint.setText("Пункт: " + h.recyclingPoint);
            tvDate.setText(h.date);
        }
    }
}