package com.example.utilization;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class WasteCategoryAdapter extends RecyclerView.Adapter<WasteCategoryAdapter.ViewHolder> {

    private List<WasteCategory> list = new ArrayList<>();

    public void setItems(List<WasteCategory> items) {
        this.list = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_waste_category, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WasteCategory item = list.get(position);
        holder.title.setText(item.wasteName);
        holder.desc.setText(item.description);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, desc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.categoryName);
            desc = itemView.findViewById(R.id.categoryDesc);
        }
    }
}