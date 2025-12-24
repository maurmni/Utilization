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

        viewModel = new ViewModelProvider(this)
                .get(AuthViewModel.class);

        btnLogin.setOnClickListener(view -> {
            error.setVisibility(View.GONE);

            String emailStr = email.getText().toString().trim();
            String passStr = pass.getText().toString().trim();

            if (emailStr.isEmpty() || passStr.isEmpty()) {
                Toast.makeText(requireContext(),
                        "Введите email и пароль", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.login(emailStr, passStr)
                    .observe(getViewLifecycleOwner(), result -> {
                        if (result == null) return;

                        if (result.isSuccess()) {
                            NavHostFragment.findNavController(this)
                                    .navigate(R.id.homeContentFragment);
                        } else {
                            error.setText(result.getError());
                            error.setVisibility(View.VISIBLE);
                        }
                    });
        });

        v.findViewById(R.id.btnRegister)
                .setOnClickListener(view -> showRegisterDialog());

        v.findViewById(R.id.btnResetPassword)
                .setOnClickListener(view -> showResetPasswordDialog());

        return v;
    }

    private void showRegisterDialog() {
        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_register, null);

        TextInputEditText etName = dialogView.findViewById(R.id.etName);
        TextInputEditText etEmail = dialogView.findViewById(R.id.etPasswordReset);
        TextInputEditText etPassword = dialogView.findViewById(R.id.etPassword);
        TextInputEditText etConfirm = dialogView.findViewById(R.id.etConfirmPassword);

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Регистрация")
                .setView(dialogView)
                .setPositiveButton("Зарегистрироваться", (d, w) -> {

                    String name = etName.getText().toString().trim();
                    String email = etEmail.getText().toString().trim();
                    String password = etPassword.getText().toString();
                    String confirm = etConfirm.getText().toString();

                    if (name.isEmpty() || email.isEmpty()
                            || password.isEmpty() || confirm.isEmpty()) {
                        Toast.makeText(requireContext(),
                                "Заполните все поля", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!password.equals(confirm)) {
                        Toast.makeText(requireContext(),
                                "Пароли не совпадают", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    viewModel.register(name, email, password)
                            .observe(getViewLifecycleOwner(), result -> {
                                if (result == null) return;

                                if (result.isSuccess()) {
                                    Toast.makeText(requireContext(),
                                            "Регистрация успешна", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(requireContext(),
                                            result.getError(), Toast.LENGTH_SHORT).show();
                                }
                            });
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void showResetPasswordDialog() {
        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_file_reset, null);

        TextInputEditText etEmail = dialogView.findViewById(R.id.etEmail);
        TextInputEditText etPassword = dialogView.findViewById(R.id.etPasswordReset);

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Сброс пароля")
                .setView(dialogView)
                .setPositiveButton("Сменить пароль", (d, w) -> {

                    String email = etEmail.getText().toString().trim();
                    String newPassword = etPassword.getText().toString();

                    if (email.isEmpty() || newPassword.isEmpty()) {
                        Toast.makeText(requireContext(),
                                "Заполните все поля", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    viewModel.resetPassword(email, newPassword)
                            .observe(getViewLifecycleOwner(), success -> {
                                if (success != null && success) {
                                    Toast.makeText(requireContext(),
                                            "Пароль изменён", Toast.LENGTH_SHORT).show();
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