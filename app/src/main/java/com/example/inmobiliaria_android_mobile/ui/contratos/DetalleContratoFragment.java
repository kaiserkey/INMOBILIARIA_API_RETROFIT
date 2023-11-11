package com.example.inmobiliaria_android_mobile.ui.contratos;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inmobiliaria_android_mobile.R;;
import com.example.inmobiliaria_android_mobile.databinding.FragmentDetalleContratoBinding;
import com.example.inmobiliaria_android_mobile.modelo.Contrato;
import com.example.inmobiliaria_android_mobile.modelo.Inmueble;
import com.example.inmobiliaria_android_mobile.modelo.Inquilino;
import com.example.inmobiliaria_android_mobile.modelo.Pago;
import com.example.inmobiliaria_android_mobile.request.ApiClient;

import java.time.LocalDate;
import java.util.ArrayList;


public class DetalleContratoFragment extends Fragment {

    private FragmentDetalleContratoBinding binding;
    private Inmueble inmueble;
    private DetalleContratoViewModel mv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentDetalleContratoBinding.inflate(getLayoutInflater());
        mv = new ViewModelProvider(this).get(DetalleContratoViewModel.class);
        View root = binding.getRoot();


        Bundle bundle = getArguments();
        inmueble = (Inmueble) bundle.getSerializable("inmueble");
        inmueble.setImagenInmueble(null);

        mv.getContratoMutable().observe(getViewLifecycleOwner(), new Observer<Contrato>() {
            @Override
            public void onChanged(Contrato contrato) {

                Inquilino inquilino = contrato.getInquilino();
                Inmueble inmueble = contrato.getInmueble();
                Log.d("CONTRATO: ", contrato.toString());
                Log.d("INQUILINO: ", inquilino.toString());
                Log.d("INMUEBLE: ", inmueble.toString());

                binding.tvCodigo.setText(String.valueOf(contrato.getId()));
                binding.tvMontoMensual.setText(String.valueOf(contrato.getMontoAlquilerMensual()));

                String fechaInicio = contrato.getFechaInicio() != null ? contrato.getFechaInicio() : "9999-12-01T00:00:00";
                String fechaFinalizacion = contrato.getFechaFinalizacion() != null ? contrato.getFechaFinalizacion() : "9999-12-01T00:00:00";
                String fechaInicioFormateada = fechaInicio.substring(0, 10); // Extraer solo la parte de la fecha
                String fechaFinalizacionFormateada = fechaFinalizacion.substring(0, 10); // Extraer solo la parte de la fecha
                binding.tvFechaInicio.setText(fechaInicioFormateada);
                binding.tvFechaFinalizacion.setText(fechaFinalizacionFormateada);

                binding.tvInquilino.setText(inquilino.getNombre());
                binding.tvInmueble.setText(inmueble.getDireccion());
                binding.btnPagos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mv.getPagosMutable().observe(getViewLifecycleOwner(), new Observer<ArrayList<Pago>>() {
                            @Override
                            public void onChanged(ArrayList<Pago> pagos) {
                                NavController navController = Navigation.findNavController(v);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("pagosAsociados", pagos);
                                navController.navigate(R.id.nav_detallePagosFragment, bundle);
                            }
                        });

                        mv.obtenerPagosPorContrato(contrato.getId());
                    }
                });


            }
        });

        mv.obtenerContrato(inmueble);


        return root;
    }


}