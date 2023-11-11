package com.example.inmobiliaria_android_mobile.ui.logout;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inmobiliaria_android_mobile.MainActivity;
import com.example.inmobiliaria_android_mobile.R;
import com.example.inmobiliaria_android_mobile.databinding.FragmentLogoutBinding;

public class LogoutFragment extends Fragment {

    private LogoutViewModel mv;
    private FragmentLogoutBinding binding;

    public static LogoutFragment newInstance() {
        return new LogoutFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLogoutBinding.inflate(inflater, container, false);
        mv = new ViewModelProvider(this).get(LogoutViewModel.class);
        mv.mostrarDialogoDeConfirmacion(this);
        return binding.getRoot();
    }
}
