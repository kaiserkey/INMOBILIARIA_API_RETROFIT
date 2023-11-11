package com.example.inmobiliaria_android_mobile.ui.logout;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.example.inmobiliaria_android_mobile.MainActivity;
import com.example.inmobiliaria_android_mobile.request.ApiClient;

public class LogoutViewModel extends AndroidViewModel {

    private Context context;

    public LogoutViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public void mostrarDialogoDeConfirmacion(Fragment fragment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.requireContext());
        builder.setTitle("Cierre de sesión");
        builder.setMessage("¿Está seguro que desea cerrar la sesión?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Cierra la sesión y lleva al usuario de vuelta al MainActivity
                Intent intent = new Intent(fragment.requireContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                fragment.startActivity(intent);
                fragment.requireActivity().finish();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
