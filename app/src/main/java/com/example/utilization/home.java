package com.example.utilization;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class home extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle b) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        BottomNavigationView nav = v.findViewById(R.id.bottom_navigation);

        nav.setOnItemSelectedListener(item -> {
            Fragment f;

            int id = item.getItemId();

            if (id == R.id.userNavi) {
                f = new ProfileFragment();
            } else if (id == R.id.garbagePoint) {
                f = new HomeContentFregment();
            } else {
                return false;
            }

            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.home_container, f)
                    .commit();

            return true;
        });
        nav.setSelectedItemId(R.id.garbagePoint);
        return v;
    }
}