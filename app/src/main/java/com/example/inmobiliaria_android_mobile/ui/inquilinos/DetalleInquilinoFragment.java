package com.example.inmobiliaria_android_mobile.ui.inquilinos;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.inmobiliaria_android_mobile.R;
import com.example.inmobiliaria_android_mobile.databinding.FragmentDetalleInmuebleBinding;
import com.example.inmobiliaria_android_mobile.databinding.FragmentDetalleInquilinoBinding;
import com.example.inmobiliaria_android_mobile.modelo.Inmueble;
import com.example.inmobiliaria_android_mobile.modelo.Inquilino;
import com.example.inmobiliaria_android_mobile.modelo.Propietario;
import com.example.inmobiliaria_android_mobile.request.ApiClient;

public class DetalleInquilinoFragment extends Fragment {

    private FragmentDetalleInquilinoBinding binding;
    private DetalleInquilinoViewModel mv;
    private Inmueble inmueble;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentDetalleInquilinoBinding.inflate(inflater, container, false);
        mv = new ViewModelProvider(this).get(DetalleInquilinoViewModel.class);
        View root = binding.getRoot();

        Bundle bundle = getArguments();
        inmueble = (Inmueble) bundle.getSerializable("inmueble");
        inmueble.setImagenInmueble(null);

        mv.getInquilinoMutable().observe(getViewLifecycleOwner(), new Observer<Inquilino>() {
            @Override
            public void onChanged(Inquilino inquilino) {
                    binding.tvCodigo.setText(String.valueOf(inquilino.getId()));
                    binding.tvDni.setText(String.valueOf(inquilino.getDni()));
                    binding.tvNombre.setText(inquilino.getNombre());
                    binding.tvApellido.setText(inquilino.getApellido());
                    binding.tvMail.setText(inquilino.getEmail());
                    binding.tvTelefono.setText(inquilino.getTelefono());
            }
        });

        mv.getPropietarioGaranteMutable().observe(getViewLifecycleOwner(), new Observer<Propietario>() {
            @Override
            public void onChanged(Propietario propietario) {
                binding.tvTelefonoGarante.setText(propietario.getTelefono());
                binding.tvGarante.setText(propietario.getNombre());

            }
        });

        mv.obtenerInquilino(inmueble);
        mv.obtenerPropietarioGarante();

        return root;
    }
}
