package com.example.utilization;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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

        TextInputEditText email = v.findViewById(R.id.etPasswordReset);
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

        Button btnRegister = v.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(view -> showRegisterDialog());

        Button btnReset = v.findViewById(R.id.btnResetPassword);
        btnReset.setOnClickListener(view -> showResetPasswordDialog());

        Button btnResetPassword = v.findViewById(R.id.btnResetPassword);
        btnResetPassword.setOnClickListener(v1 -> showResetPasswordDialog());

        return v;
    }
    private void showRegisterDialog() {
        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_register, null);

        TextInputEditText etName = dialogView.findViewById(R.id.etName);
        TextInputEditText etEmail = dialogView.findViewById(R.id.etPasswordReset);
        TextInputEditText etPassword = dialogView.findViewById(R.id.etPassword);
        TextInputEditText etConfirm = dialogView.findViewById(R.id.etConfirmPassword);

        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Регистрация")
                .setView(dialogView)
                .setPositiveButton("Зарегистрироваться", (d, w) -> {
                    viewModel.register(
                            etName.getText().toString(),
                            etEmail.getText().toString(),
                            etPassword.getText().toString(),
                            etConfirm.getText().toString()
                    );
                })
                .setNegativeButton("Отмена", null)
                .show();
    }
    private void showResetPasswordDialog() {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View dialogView = inflater.inflate(R.layout.dialog_file_reset, null);

        TextInputEditText etEmail = dialogView.findViewById(R.id.etEmail);
        TextInputEditText etPasswordReset = dialogView.findViewById(R.id.etPasswordReset);

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Сброс пароля")
                .setView(dialogView)
                .setPositiveButton("Сменить пароль", (dialog, which) -> {
                    String email = etEmail.getText().toString().trim();
                    String newPassword = etPasswordReset.getText().toString();

                    if (email.isEmpty() || newPassword.isEmpty()) {
                        Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        Toast.makeText(requireContext(), "Введите корректный email", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (newPassword.length() < 6) {
                        Toast.makeText(requireContext(), "Пароль должен содержать минимум 6 символов", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    viewModel.resetPassword(email, newPassword)
                            .observe(getViewLifecycleOwner(), success -> {
                                if (success != null && success) {
                                    Toast.makeText(requireContext(),
                                            "Пароль успешно изменен", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(requireContext(),
                                            "Ошибка изменения пароля", Toast.LENGTH_SHORT).show();
                                }
                            });
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

}
