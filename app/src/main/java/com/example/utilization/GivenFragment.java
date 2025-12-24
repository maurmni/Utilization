package com.example.utilization;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

public class GivenFragment extends Fragment {

    private int historyId;
    private WasteGivenViewModel viewModel;
    private GivenAdapter adapter;
    private AppDataBase db;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_given, container, false);
        TextView tvEmpty = v.findViewById(R.id.tvEmpty);
        historyId = getArguments().getInt("historyId");
        RecyclerView rv = v.findViewById(R.id.rvGiven);

        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

        viewModel = new ViewModelProvider(this)
                .get(WasteGivenViewModel.class);

        adapter = new GivenAdapter();
        rv.setAdapter(adapter);

        db = AppDataBase.get(requireContext());

        AuthViewModel authVM =
                new ViewModelProvider(requireActivity())
                        .get(AuthViewModel.class);

        authVM.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user == null) return;

            db.wasteGivenDao()
                    .getByHistoryAndUser(historyId, user.id)
                    .observe(getViewLifecycleOwner(), givens -> {

                        if (givens == null || givens.isEmpty()) {
                            NavHostFragment
                                    .findNavController(GivenFragment.this)
                                    .navigateUp();
                            return;
                        }

                        tvEmpty.setVisibility(View.GONE);
                        rv.setVisibility(View.VISIBLE);
                        adapter.submit(givens);
                    });
        });

        adapter.setListener(new WasteGivenListener() {

            @Override
            public void onEdit(WasteGiven item) {
                showEditDialog(item);
            }

            @Override
            public void onDelete(WasteGiven item) {
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Удалить запись?")
                        .setMessage("Вы уверены?")
                        .setPositiveButton("Да", (d, w) ->
                                viewModel.delete(item))
                        .setNegativeButton("Отмена", null)
                        .show();
            }
        });

        return v;
    }
    private void showEditDialog(WasteGiven item) {
        View dialog = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_edit_waste_given, null);

        EditText etWeight = dialog.findViewById(R.id.etWeight);
        EditText etDate = dialog.findViewById(R.id.etDate);

        etWeight.setText(String.valueOf(item.wasteWeight));
        etDate.setText(item.givingDate);

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Редактировать")
                .setView(dialog)
                .setPositiveButton("Сохранить", (d, w) -> {
                    item.wasteWeight =
                            Float.parseFloat(etWeight.getText().toString());
                    item.givingDate = etDate.getText().toString();

                    viewModel.update(item);
                })
                .setNegativeButton("Отмена", null)
                .show();
    }
}
