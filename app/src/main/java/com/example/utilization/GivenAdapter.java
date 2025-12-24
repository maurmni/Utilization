package com.example.utilization;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GivenAdapter
        extends RecyclerView.Adapter<GivenAdapter.GivenVH> {

    private List<WasteGiven> list = new ArrayList<>();
    private WasteGivenListener listener;

    public void setListener(WasteGivenListener listener) {
        this.listener = listener;
    }

    public void submit(List<WasteGiven> items) {
        list.clear();
        list.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GivenVH onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_given, parent, false);

        return new GivenVH(v);
    }

    @Override
    public void onBindViewHolder(
            @NonNull GivenVH holder,
            int position) {

        WasteGiven item = list.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class GivenVH extends RecyclerView.ViewHolder {

        TextView txt;
        Button btnEdit, btnDelete;

        GivenVH(@NonNull View itemView) {
            super(itemView);

            txt = itemView.findViewById(R.id.txtGiven);
            btnEdit = itemView.findViewById(R.id.btnEditItem);
            btnDelete = itemView.findViewById(R.id.btnDelItem);
        }
        void bind(WasteGiven item) {

            txt.setText(
                    "Категория: " + item.wasteCategory +
                            "\nВес: " + item.wasteWeight +
                            "\nДата: " + item.givingDate
            );

            btnEdit.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEdit(item);
                }
            });

            btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDelete(item);
                }
            });
        }
    }
}
