package com.example.utilization;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private HistoryAdapter adapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        View v = inflater.inflate(R.layout.fragment_history, container, false);

        recyclerView = v.findViewById(R.id.historyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        Button btnAdd = v.findViewById(R.id.btnAddWaste);
        btnAdd.setOnClickListener(view ->
                NavHostFragment
                        .findNavController(this)
                        .navigate(R.id.action_historyFragment_to_addWasteFragment)
        );

        adapter = new HistoryAdapter(history -> {
            Bundle args = new Bundle();
            args.putInt("historyId", history.id);

            NavHostFragment
                    .findNavController(this)
                    .navigate(R.id.action_historyFragment_to_givenFragment, args);
        });

        recyclerView.setAdapter(adapter);

        AppDataBase db = AppDataBase.get(requireContext());

        AuthViewModel authVM =
                new ViewModelProvider(requireActivity())
                        .get(AuthViewModel.class);

        authVM.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user == null) return;

            db.recyclingHistoryDao()
                    .getAllByUser(user.id)
                    .observe(getViewLifecycleOwner(), historyList -> {

                        if (historyList == null || historyList.isEmpty()) {
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            adapter.submitList(historyList);
                        }
                    });
        });

        return v;
    }
}