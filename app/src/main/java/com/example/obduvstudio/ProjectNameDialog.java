package com.example.obduvstudio;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ProjectNameDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Настраиваем кастомный layout для диалога
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_project_name, null);

        EditText projectNameInput = dialogView.findViewById(R.id.projectNameInput);

        builder.setView(dialogView)
                .setTitle("Название проекта")
                .setPositiveButton("Создать", (dialog, id) -> {
                    String projectName = projectNameInput.getText().toString().trim();
                    if (projectName.isEmpty()) {
                        projectName = "Без названия";
                    }

                    // Переход к основной активности
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("PROJECT_NAME", projectName);
                    startActivity(intent);
                    getActivity().finish(); // Закрыть WelcomeActivity
                })
                .setNegativeButton("Отмена", (dialog, id) -> {
                    // Просто закрываем диалог
                    dialog.dismiss();
                });

        return builder.create();
    }
}