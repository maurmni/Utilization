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

    //выпадающие списки категорий и пунктов приема
    private Spinner spinnerCategory;
    private Spinner spinnerPoint;
    private EditText editWeight; //поле для ввода веса

    //списки из бд
    private List<WasteCategory> categories = new ArrayList<>();
    private List<RecyclingPoint> points = new ArrayList<>();

    //адаптеры для отображения названий spiner
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

        //загрузка layout-файла фрагмента
        View v = inflater.inflate(R.layout.fragment_add_waste, container, false);

        //инициализация элементов интерфейса
        spinnerCategory = v.findViewById(R.id.spinnerCategory);
        spinnerPoint = v.findViewById(R.id.spinnerPoint);
        editWeight = v.findViewById(R.id.editWeight);
        Button btnAdd = v.findViewById(R.id.btnAddWaste);

        //получение ViewModel
        viewModel = new ViewModelProvider(this)
                .get(WasteGivenViewModel.class);

        //настройка адаптера категорий
        catAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                new ArrayList<>());
        spinnerCategory.setAdapter(catAdapter);

        //настройка адаптера пунктов приема
        pointAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                new ArrayList<>());
        spinnerPoint.setAdapter(pointAdapter);

        //получние экземпляра бд
        AppDataBase db = AppDataBase.get(requireContext());

        //при изменеии данных в бд Spinner автоматически обновится
        db.wasteCategoryDao().getAllLive().observe(getViewLifecycleOwner(), categoriesList -> {
            categories.clear();
            categories.addAll(categoriesList);

            //преобразование сущности в список названий
            List<String> names = new ArrayList<>();
            for (WasteCategory c : categories) {
                names.add(c.wasteName);
            }

            catAdapter.clear();
            catAdapter.addAll(names);
            catAdapter.notifyDataSetChanged();
        });

        //наблюдение за списком пунктов приема
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

        //получение historyId
        historyId = getArguments() != null
                ? getArguments().getInt("historyId", -1)
                : -1;

        //ViewModel авторизация для получения текущего пользователя
        AuthViewModel authVM =
                new ViewModelProvider(requireActivity())
                        .get(AuthViewModel.class);

        //после получения авторизованного пользователя активируется кнопка для добавления отходов
        authVM.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user == null) return;

            btnAdd.setOnClickListener(view -> {

                //проверка ввода веса
                String weightStr = editWeight.getText().toString().trim();
                if (weightStr.isEmpty()) {
                    Toast.makeText(getContext(), "Введите вес", Toast.LENGTH_SHORT).show();
                    return;
                }

                float weight = Float.parseFloat(weightStr);

                //получение выбранной категории
                int categoryIndex = spinnerCategory.getSelectedItemPosition();
                if (categoryIndex < 0) return;

                int categoryId = categories.get(categoryIndex).id;
                int pointId = points.get(spinnerPoint.getSelectedItemPosition()).id;

                //формирование текущей даты
                String date = new SimpleDateFormat(
                        "dd.MM.yyyy", Locale.getDefault()
                ).format(new Date());

                //сохранеие данных через ViewModel
                viewModel.insertWithHistory(
                        user.id,
                        categoryId,
                        weight,
                        pointId,
                        date
                );

                //возврат на предыдущий экран
                NavHostFragment.findNavController(this).navigateUp();
            });
        });

        //кнопка отмены
        Button btnFinish = v.findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(view ->
                NavHostFragment
                        .findNavController(this)
                        .navigateUp()
        );

        //логи для отладки наполненности бд
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