package com.example.utilization;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//адаптер для отображения истории сдачи
public class HistoryAdapter
        extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    //интерфейс для обработки клика по элементу
    public interface OnItemClickListener {
        void onClick(RecyclingHistory history);
    }

    //список элементов истории
    private List<RecyclingHistory> list;
    private OnItemClickListener listener;

    //обработчик нажатий
    public HistoryAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    //обновление списка данных
    public void submitList(List<RecyclingHistory> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    //создание ViewHolder для 1 (одного(one)) элемента списка
    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycling_history, parent, false);

        return new HistoryViewHolder(v);
    }

    //привязка данных к элкменту списка
    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        RecyclingHistory history = list.get(position);
        holder.bind(history);
        //обработка клика по выбранному элементу
        holder.itemView.setOnClickListener(v -> listener.onClick(history));
    }

    //количество элеентво списка
    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    //ViewHolder хранящий ссылки на элементы интерфейса строки списка
    static class HistoryViewHolder extends RecyclerView.ViewHolder {

        TextView tvPoint, tvDate;

        HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPoint = itemView.findViewById(R.id.tvPoint);
            tvDate = itemView.findViewById(R.id.tvDate);
        }

        //заполнение элементов интерфейса данными
        void bind(RecyclingHistory h) {
            tvPoint.setText("Пункт: " + h.recyclingPoint);
            tvDate.setText(h.date);
        }
    }
}