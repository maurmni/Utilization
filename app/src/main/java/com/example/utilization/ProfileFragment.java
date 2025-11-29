package com.example.utilization;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProfileFragment extends Fragment {

    private ProfileViewModel viewModel;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView tvDate = v.findViewById(R.id.tvRegistrationDate);

        viewModel = new ViewModelProvider(this)
                .get(ProfileViewModel.class);

        viewModel.getUser()
                .observe(getViewLifecycleOwner(), user -> {
                    if (user == null) return;

                    Date date = new Date(user.registrationDate);
                    SimpleDateFormat sdf =
                            new SimpleDateFormat(
                                    "dd.MM.yyyy",
                                    Locale.getDefault()
                            );

                    tvDate.setText(
                            "Зарегистрирован: " + sdf.format(date)
                    );
                });

        return v;
    }
}

