package com.example.utilization;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

//адаптер для отображения категорий отходов
public class WasteCategoryAdapter extends RecyclerView.Adapter<WasteCategoryAdapter.ViewHolder> {

    //список категорий
    private List<WasteCategory> list = new ArrayList<>();

    //обновление данных адаптера
    public void setItems(List<WasteCategory> items) {
        this.list = items;
        notifyDataSetChanged();
    }

    //создание ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_waste_category, parent, false);
        return new ViewHolder(v);
    }

    //привязка данныъ к элементу списка
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WasteCategory item = list.get(position);
        holder.title.setText(item.wasteName);
        holder.desc.setText(item.description);
    }

    //количество элементов списка
    @Override
    public int getItemCount() {
        return list.size();
    }

    //ViewGolder для категории отходов
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, desc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.categoryName);
            desc = itemView.findViewById(R.id.categoryDesc);
        }
    }
}