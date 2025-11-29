package com.example.utilization;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import com.google.android.material.textfield.TextInputEditText;

public class AuthFragment extends Fragment {

    private AuthViewModel viewModel;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        View v = inflater.inflate(R.layout.fragment_auth, container, false);

        TextInputEditText email = v.findViewById(R.id.etEmail);
        TextInputEditText pass = v.findViewById(R.id.etPassword);
        Button btnLogin = v.findViewById(R.id.btnLogin);
        TextView error = v.findViewById(R.id.tvError);

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        viewModel.setAuthRepository(new AuthRepository(requireContext()));

        btnLogin.setOnClickListener(view ->
                viewModel.login(
                        email.getText().toString(),
                        pass.getText().toString()
                ));

        viewModel.getAuthResult().observe(getViewLifecycleOwner(), r -> {
            if (r.isSuccess()) {
                NavHostFragment
                        .findNavController(this)
                        .navigate(R.id.action_auth_to_home);
            } else {
                error.setText(r.getError());
                error.setVisibility(View.VISIBLE);
            }
        });

        return v;
    }
}
