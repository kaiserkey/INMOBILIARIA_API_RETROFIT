package com.example.inmobiliaria_android_mobile.ui.perfil;

import static java.lang.Integer.parseInt;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.inmobiliaria_android_mobile.databinding.FragmentPerfilBinding;
import com.example.inmobiliaria_android_mobile.modelo.Propietario;

public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;
    private PerfilViewModel mv;

    public static PerfilFragment newInstance() {
        return new PerfilFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mv = new ViewModelProvider(this).get(PerfilViewModel.class);
        mv.cargarPropietarioLogueado();

        mv.getImagenMutable().observe(getViewLifecycleOwner(), new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap imageBytes) {
                    Glide.with(requireContext())
                            .load(imageBytes)
                            .transform(new CircleCrop())
                            .into(binding.ivAvatar);
            }
        });
        mv.getDataPropietarioMutable().observe(getViewLifecycleOwner(), new Observer<Propietario>() {
            @Override
            public void onChanged(Propietario propietario) {
                binding.tvNombre.setText(propietario.getNombre());
                binding.tvApellido.setText(propietario.getApellido());
                binding.tvCodigo.setText(String.valueOf(propietario.getIdPropietario()));
                binding.tvDni.setText(String.valueOf(propietario.getDni()));
                binding.tvMail.setText(propietario.getEmail());
                //binding.tvClave.setText(propietario.getClave());
                binding.tvPerfilTelefono.setText(propietario.getTelefono());
            }
        });

        binding.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String estadoBtn = binding.btnEditar.getText().toString();
                String dniString = binding.tvDni.getText().toString();
                String nombre = binding.tvNombre.getText().toString();
                String apellido = binding.tvApellido.getText().toString();
                String email = binding.tvMail.getText().toString();
                String contrasenia = binding.tvClave.getText().toString();
                String telefono = binding.tvPerfilTelefono.getText().toString();

                mv.cambiarTextoBoton(estadoBtn, dniString, nombre, apellido, email, contrasenia, telefono);
            }
        });


        mv.getValorBotonMutable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String value) {
                binding.btnEditar.setText(value);
            }
        });



    }

}