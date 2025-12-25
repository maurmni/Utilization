package com.example.utilization;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

//фрагментп профиля пользвателя
public class ProfileFragment extends Fragment {

    private ProfileViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        //элементы интерфейса
        Button btnSave = v.findViewById(R.id.btnSaveProfile);
        Button btnEdit = v.findViewById(R.id.btnEditProfile);
        TextInputEditText etName = v.findViewById(R.id.etName);
        TextInputEditText etEmail = v.findViewById(R.id.etEmail);
        TextInputEditText etPassword = v.findViewById(R.id.etPassword);
        TextView tvDate = v.findViewById(R.id.tvRegistrationDate);

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        //наблюдение за данными пользователями
        viewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user == null) return;

            etName.setText(user.name);
            etEmail.setText(user.email);
            etPassword.setText(user.password);

            Date date = new Date(user.registrationDate);
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            tvDate.setText("Зарегистрирован: " + sdf.format(date));
        });

        //сохранение изменений
        btnSave.setOnClickListener(view -> {
            String newName = etName.getText().toString();
            String newEmail = etEmail.getText().toString();
            String newPassword = etPassword.getText().toString();

            viewModel.updateUser(newName, newEmail, newPassword);
        });

        //обновление диалога редактирования
        btnEdit.setOnClickListener(view -> {
            UserEntity user = viewModel.getUser().getValue();
            if (user != null) {
                showEditDialog(user);
            }
        });

        //выход из аккаунта
        Button btnLogout = v.findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(view -> logout());
        return v;
    }
    //диалог редактирования профиля
    private void showEditDialog(UserEntity user) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_profile, null);
        TextInputEditText etEmail = dialogView.findViewById(R.id.etEmail);
        TextInputEditText etPassword = dialogView.findViewById(R.id.etPasswordReset);

        etEmail.setText(user.email);

        new AlertDialog.Builder(requireContext())
                .setTitle("Редактировать профиль")
                .setView(dialogView)
                .setPositiveButton("Сохранить", (d, which) -> {
                    String newEmail = etEmail.getText().toString();
                    String newPassword = etPassword.getText().toString();

                    viewModel.updateProfile(user.id, newEmail, newPassword)
                            .observe(getViewLifecycleOwner(), success -> {
                                if (success != null && success) {
                                    Toast.makeText(requireContext(),
                                            "Профиль обновлен", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(requireContext(),
                                            "Ошибка обновления", Toast.LENGTH_SHORT).show();
                                }
                            });
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    //выход из аккаунта
    private void logout() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Выход из аккаунта")
                .setMessage("Вы уверены, что хотите выйти?")
                .setPositiveButton("Да", (dialog, which) -> {
                    android.content.SharedPreferences prefs = requireActivity()
                            .getSharedPreferences("auth", android.content.Context.MODE_PRIVATE);
                    prefs.edit()
                            .clear()
                            .apply();
                    android.content.Intent intent = new android.content.Intent(requireContext(), MainActivity.class);
                    intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK | android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    requireActivity().finish();
                })
                .setNegativeButton("Нет", (dialog, which) -> dialog.dismiss())
                .show();
    }

}
