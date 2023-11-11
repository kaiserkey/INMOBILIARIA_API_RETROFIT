package com.example.inmobiliaria_android_mobile.ui.inmuebles;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.inmobiliaria_android_mobile.R;
import com.example.inmobiliaria_android_mobile.databinding.FragmentDetalleInmuebleBinding;
import com.example.inmobiliaria_android_mobile.databinding.FragmentInmueblesBinding;
import com.example.inmobiliaria_android_mobile.modelo.Inmueble;
import com.example.inmobiliaria_android_mobile.request.ApiClient;
import com.example.inmobiliaria_android_mobile.ui.contratos.ContratosViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class DetalleInmuebleFragment extends Fragment {

    private DetalleInmuebleViewModel mv;
    private FragmentDetalleInmuebleBinding binding;
    private Inmueble inmueble;

    public static DetalleInmuebleFragment newInstance() {
        return new DetalleInmuebleFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetalleInmuebleBinding.inflate(inflater, container, false);
        mv = new ViewModelProvider(this).get(DetalleInmuebleViewModel.class);
        View root = binding.getRoot();

        Bundle bundle = getArguments();
        inmueble = (Inmueble) bundle.getSerializable("inmueble");

        binding.tvCodigo.setText(String.valueOf(inmueble.getId()));
        binding.tvAmbientes.setText(String.valueOf(inmueble.getCantidadAmbientes()));
        binding.tvDireccion.setText(inmueble.getDireccion());
        binding.tvPrecio.setText(String.valueOf(inmueble.getPrecioInmueble()));
        binding.tvUso.setText(inmueble.getUso());
        binding.tvTipo.setText(inmueble.getTipo());
        Glide.with(binding.getRoot().getContext()).load(inmueble.getImagenInmueble()).into(binding.imageView2);
        binding.cbEstado.setChecked(inmueble.getActivo());

        binding.cbEstado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inmueble.setActivo(binding.cbEstado.isChecked());
                mv.actualizarInmueble(inmueble);
            }
        });


        return root;
    }


}