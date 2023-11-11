package com.example.inmobiliaria_android_mobile.ui.contratos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inmobiliaria_android_mobile.R;
import com.example.inmobiliaria_android_mobile.modelo.Contrato;
import com.example.inmobiliaria_android_mobile.modelo.Inmueble;
import com.example.inmobiliaria_android_mobile.request.ApiClient;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ContratosAdapter extends RecyclerView.Adapter<ContratosAdapter.ViewHolder> {

    private Context context;
    private List<Inmueble> inmueblesAlquilados;
    private LayoutInflater inflater;

    public ContratosAdapter(Context context, List<Inmueble> inmuebles, LayoutInflater inflater) {
        this.context = context;
        this.inmueblesAlquilados = inmuebles;
        this.inflater = inflater;
    }


    @NonNull
    @Override
    public ContratosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = inflater.inflate(R.layout.item_inmueble_alquilado, parent, false);
        return new ContratosAdapter.ViewHolder(root);
    }


    @Override
    public void onBindViewHolder(@NonNull ContratosAdapter.ViewHolder holder, int position) {
        holder.direccion.setText(inmueblesAlquilados.get(position).getDireccion());
        Glide.with(context).load(inmueblesAlquilados.get(position).getImagenInmueble()).into(holder.fotoInmueble);
    }

    @Override
    public int getItemCount() {
        return inmueblesAlquilados.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView fotoInmueble;
        TextView direccion;
        Button btnVerInquilino;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fotoInmueble = itemView.findViewById(R.id.ivInmueble);
            direccion = itemView.findViewById(R.id.tvDireccion);
            btnVerInquilino = itemView.findViewById(R.id.btnVerInquilino);

            btnVerInquilino.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {

                Inmueble inmueble = inmueblesAlquilados.get(position);

                NavController navController = Navigation.findNavController(v);

                Bundle bundle = new Bundle();
                bundle.putSerializable("inmueble", inmueble);
                navController.navigate(R.id.nav_detalleContratoFragment, bundle);

            }
        }


    }


}