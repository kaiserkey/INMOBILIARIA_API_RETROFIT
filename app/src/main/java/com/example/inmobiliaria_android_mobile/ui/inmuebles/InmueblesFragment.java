package com.example.inmobiliaria_android_mobile.ui.inmuebles;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.inmobiliaria_android_mobile.R;
import com.example.inmobiliaria_android_mobile.databinding.FragmentInmueblesBinding;
import com.example.inmobiliaria_android_mobile.modelo.Inmueble;
import com.example.inmobiliaria_android_mobile.modelo.Propietario;
import com.example.inmobiliaria_android_mobile.request.ApiClient;

import java.util.ArrayList;

public class InmueblesFragment extends Fragment {

    private InmueblesViewModel mv;
    private FragmentInmueblesBinding binding;
    public static InmueblesFragment newInstance() {
        return new InmueblesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentInmueblesBinding.inflate(inflater, container, false);
        mv = new ViewModelProvider(this).get(InmueblesViewModel.class);
        View root = binding.getRoot();

        mv.getInmuebles().observe(getViewLifecycleOwner(), new Observer<ArrayList<Inmueble>>() {
            @Override
            public void onChanged(ArrayList<Inmueble> inmuebles) {

                RecyclerView rv = binding.rvLista;
                GridLayoutManager grilla = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
                rv.setLayoutManager(grilla);
                InmueblesAdapter adapter = new InmueblesAdapter(getContext(), inmuebles, getLayoutInflater());
                rv.setAdapter(adapter);

            }
        });

        mv.obtenerPropiedades();

        Button btnNuevoInmueble = root.findViewById(R.id.btnNuevo);
        btnNuevoInmueble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el NavController
                NavController navController = Navigation.findNavController(v);
                // Navegar hacia el fragmento de nuevo inmueble
                navController.navigate(R.id.action_nav_inmuebles_to_nuevoInmuebleFragment);
            }
        });

        return root;
    }

}