package com.example.utilization;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AuthFragment extends Fragment {
    private AuthViewModel authViewModel;
    private TextInputEditText etEmail, etPassword;
    private TextInputLayout emailLayout, passwordLayout;
    private Button btnLogin;
    private ProgressBar progressBar;
    private TextView tvError, tvRegister, tvForgotPassword;

    private void initViews(View view) {
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        btnLogin = view.findViewById(R.id.btnLogin);
        progressBar = view.findViewById(R.id.progressBar);
        tvError = view.findViewById(R.id.tvError);
        tvRegister = view.findViewById(R.id.tvRegister);
        tvForgotPassword = view.findViewById(R.id.tvForgotPassword);
    }

    private void setupViewModel() {
        AuthRepository authRepository = new AuthRepository(requireContext());
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        observeViewModel();
    }

    private void observeViewModel() {
        authViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            btnLogin.setEnabled(!isLoading);
            btnLogin.setText(isLoading ? "Загрузка..." : "Войти");
        });

        authViewModel.getAuthResult().observe(getViewLifecycleOwner(), result -> {
            if (result.isSuccess()) {
                navigateToMain();
            } else {
                showError(result.getError());
            }
        });

        authViewModel.getPasswordResetResult().observe(getViewLifecycleOwner(), success -> {
            if (success != null && success) {
                showMessage("Ссылка для восстановления отправлена на email");
            } else if (success != null) {
                showError("Ошибка отправки email");
            }
        });
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(v -> attemptLogin());

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                clearErrors();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                clearErrors();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void attemptLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        clearErrors();

        if (validateInputs(email, password)) {
            authViewModel.login(email, password);
        }
    }

    private boolean validateInputs(String email, String password) {
        boolean isValid = true;

        if (email.isEmpty()) {
            emailLayout.setError("Введите email");
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Введите корректный email");
            isValid = false;
        }

        if (password.isEmpty()) {
            passwordLayout.setError("Введите пароль");
            isValid = false;
        } else if (password.length() < 6) {
            passwordLayout.setError("Пароль должен содержать минимум 6 символов");
            isValid = false;
        }

        return isValid;
    }

    private void clearErrors() {
        emailLayout.setError(null);
        passwordLayout.setError(null);
        tvError.setVisibility(View.GONE);
    }

    private void showError(String error) {
        tvError.setText(error);
        tvError.setVisibility(View.VISIBLE);
    }

    private void showMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
    }

    private void navigateToMain() {
        Intent intent = new Intent(requireContext(), MainActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

    private boolean validateRegistration(String name, String email, String password, String confirmPassword) {
        if (name.isEmpty()) {
            showError("Введите имя");
            return false;
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError("Введите корректный email");
            return false;
        }

        if (password.length() < 6) {
            showError("Пароль должен содержать минимум 6 символов");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            showError("Пароли не совпадают");
            return false;
        }

        return true;
    }
}