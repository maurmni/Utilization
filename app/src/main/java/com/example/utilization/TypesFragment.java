package com.example.utilization;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import androidx.lifecycle.Observer;

//фрагмент для отображения списка категорий
public class TypesFragment extends Fragment {

    private RecyclerView recyclerView;
    private WasteCategoryAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //звгрузка разметки фрагмента
        View v = inflater.inflate(R.layout.fragment_types, container, false);

        recyclerView = v.findViewById(R.id.typesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //создание и установка адаптера
        adapter = new WasteCategoryAdapter();
        recyclerView.setAdapter(adapter);

        //получение бд
        AppDataBase db = AppDataBase.get(requireContext());
        //наблюдение за списком категорий
        db.wasteCategoryDao().getAllLive().observe(getViewLifecycleOwner(), list -> {
            Log.d("TYPES", "categories size = " + list.size());
            adapter.setItems(list);
        });
        return v;
    }
}
