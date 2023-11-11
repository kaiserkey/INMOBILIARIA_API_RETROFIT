package com.example.inmobiliaria_android_mobile.ui.inquilinos;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inmobiliaria_android_mobile.R;
import com.example.inmobiliaria_android_mobile.databinding.FragmentInmueblesBinding;
import com.example.inmobiliaria_android_mobile.databinding.FragmentInquilinosBinding;
import com.example.inmobiliaria_android_mobile.modelo.Inmueble;
import com.example.inmobiliaria_android_mobile.request.ApiClient;
import com.example.inmobiliaria_android_mobile.ui.inmuebles.InmueblesAdapter;
import com.example.inmobiliaria_android_mobile.ui.inmuebles.InmueblesViewModel;

import java.util.ArrayList;

public class InquilinosFragment extends Fragment {

    private InquilinosViewModel mv;
    private FragmentInquilinosBinding binding;
    public static InquilinosFragment newInstance() {
        return new InquilinosFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentInquilinosBinding.inflate(inflater, container, false);
        mv = new ViewModelProvider(this).get(InquilinosViewModel.class);
        View root = binding.getRoot();
        mv.getInmueblesAlquilados().observe(getViewLifecycleOwner(), new Observer<ArrayList<Inmueble>>() {
            @Override
            public void onChanged(ArrayList<Inmueble> inmuebles) {

                RecyclerView rv = binding.rvLista;
                GridLayoutManager grilla = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
                rv.setLayoutManager(grilla);
                InquilinosAdapter adapter = new InquilinosAdapter(getContext(), inmuebles, getLayoutInflater());
                rv.setAdapter(adapter);

            }
        });

        mv.obtenerPropiedadesAlquiladas();


        return root;
    }

}