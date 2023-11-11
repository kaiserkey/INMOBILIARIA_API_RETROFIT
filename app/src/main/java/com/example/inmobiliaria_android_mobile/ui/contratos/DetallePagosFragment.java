package com.example.inmobiliaria_android_mobile.ui.contratos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inmobiliaria_android_mobile.R;
import com.example.inmobiliaria_android_mobile.databinding.FragmentDetallePagosBinding;
import com.example.inmobiliaria_android_mobile.modelo.Contrato;
import com.example.inmobiliaria_android_mobile.modelo.Inmueble;
import com.example.inmobiliaria_android_mobile.modelo.Inquilino;
import com.example.inmobiliaria_android_mobile.modelo.Pago;
import com.example.inmobiliaria_android_mobile.request.ApiClient;

import java.util.ArrayList;


public class DetallePagosFragment extends Fragment {

    private FragmentDetallePagosBinding binding;
    private ArrayList<Pago> pagos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentDetallePagosBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        Bundle bundle = getArguments();
        pagos = (ArrayList<Pago>) bundle.getSerializable("pagosAsociados");


        RecyclerView rv = binding.rvLista;
        GridLayoutManager grilla = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        rv.setLayoutManager(grilla);
        PagosAdapter adapter = new PagosAdapter(getContext(), pagos, getLayoutInflater());
        rv.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        return root;
    }
}