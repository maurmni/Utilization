package com.example.utilization;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.textfield.TextInputEditText;

public class AuthFragment extends Fragment {

    private AuthViewModel viewModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle b) {
        View v = inflater.inflate(R.layout.fragment_auth, container, false);

        TextInputEditText email = v.findViewById(R.id.etEmail);
        TextInputEditText pass = v.findViewById(R.id.etPassword);
        TextView error = v.findViewById(R.id.tvError);

        Button login = v.findViewById(R.id.btnLogin);

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        viewModel.setAuthRepository(new AuthRepository(requireContext()));

        viewModel.getAuthResult().observe(getViewLifecycleOwner(), r -> {
            if (r.isSuccess()) {
                ((MainActivity) requireActivity()).openHomeFragment();
            } else {
                error.setText(r.getError());
                error.setVisibility(View.VISIBLE);
            }
        });

        login.setOnClickListener(v1 ->
                viewModel.login(
                        email.getText().toString(),
                        pass.getText().toString()
                ));

        return v;
    }
}