package com.example.utilization;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddWasteFragment extends Fragment {

    private Spinner spinnerCategory;
    private Spinner spinnerPoint;
    private EditText editWeight;

    private List<WasteCategory> categories = new ArrayList<>();
    private List<RecyclingPoint> points = new ArrayList<>();

    private ArrayAdapter<String> catAdapter;
    private ArrayAdapter<String> pointAdapter;
    private int historyId;
    private WasteGivenViewModel viewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {

        View v = inflater.inflate(R.layout.fragment_add_waste, container, false);

        spinnerCategory = v.findViewById(R.id.spinnerCategory);
        spinnerPoint = v.findViewById(R.id.spinnerPoint);
        editWeight = v.findViewById(R.id.editWeight);
        Button btnAdd = v.findViewById(R.id.btnAddWaste);

        viewModel = new ViewModelProvider(this)
                .get(WasteGivenViewModel.class);

        catAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                new ArrayList<>());
        spinnerCategory.setAdapter(catAdapter);

        pointAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                new ArrayList<>());
        spinnerPoint.setAdapter(pointAdapter);

        AppDataBase db = AppDataBase.get(requireContext());

        db.wasteCategoryDao().getAllLive().observe(getViewLifecycleOwner(), categoriesList -> {
            categories.clear();
            categories.addAll(categoriesList);

            List<String> names = new ArrayList<>();
            for (WasteCategory c : categories) {
                names.add(c.wasteName);
            }

            catAdapter.clear();
            catAdapter.addAll(names);
            catAdapter.notifyDataSetChanged();
        });

        db.recyclingPointDao().getAllLive().observe(getViewLifecycleOwner(), pointsList -> {
            points.clear();
            points.addAll(pointsList);

            List<String> names = new ArrayList<>();
            for (RecyclingPoint p : points) {
                names.add(p.pointName + " (" + p.address + ")");
            }

            pointAdapter.clear();
            pointAdapter.addAll(names);
            pointAdapter.notifyDataSetChanged();
        });

        historyId = getArguments() != null
                ? getArguments().getInt("historyId", -1)
                : -1;

        AuthViewModel authVM =
                new ViewModelProvider(requireActivity())
                        .get(AuthViewModel.class);

        authVM.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user == null) return;

            btnAdd.setOnClickListener(view -> {

                String weightStr = editWeight.getText().toString().trim();
                if (weightStr.isEmpty()) {
                    Toast.makeText(getContext(), "Введите вес", Toast.LENGTH_SHORT).show();
                    return;
                }

                float weight = Float.parseFloat(weightStr);
                int categoryIndex = spinnerCategory.getSelectedItemPosition();
                if (categoryIndex < 0) return;

                int categoryId = categories.get(categoryIndex).id;
                int pointId = points.get(spinnerPoint.getSelectedItemPosition()).id;

                String date = new SimpleDateFormat(
                        "dd.MM.yyyy", Locale.getDefault()
                ).format(new Date());

                viewModel.insertWithHistory(
                        user.id,
                        categoryId,
                        weight,
                        pointId,
                        date
                );

                NavHostFragment.findNavController(this).navigateUp();
            });
        });

        Button btnFinish = v.findViewById(R.id.btnFinish);

        btnFinish.setOnClickListener(view ->
                NavHostFragment
                        .findNavController(this)
                        .navigateUp()
        );

        db.wasteCategoryDao().getAllLive()
                .observe(getViewLifecycleOwner(), list ->
                        Log.d("DB_CHECK", "Категорий: " + list.size())
                );

        db.recyclingPointDao().getAllLive()
                .observe(getViewLifecycleOwner(), list ->
                        Log.d("DB_CHECK", "Пунктов: " + list.size())
                );

        return v;
    }
}