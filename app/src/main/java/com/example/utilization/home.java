package com.example.utilization;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class home extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        BottomNavigationView bottomNav = v.findViewById(R.id.bottom_navigation);

        NavController navController =
                NavHostFragment.findNavController(this);

        NavigationUI.setupWithNavController(bottomNav, navController);

        return v;
    }
}