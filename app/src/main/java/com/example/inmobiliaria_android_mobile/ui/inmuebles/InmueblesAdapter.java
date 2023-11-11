package com.example.inmobiliaria_android_mobile.ui.inmuebles;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.inmobiliaria_android_mobile.MenuActivity;
import com.example.inmobiliaria_android_mobile.R;
import com.example.inmobiliaria_android_mobile.modelo.Inmueble;
import com.squareup.picasso.Picasso;

import java.util.List;

public class InmueblesAdapter extends RecyclerView.Adapter<InmueblesAdapter.ViewHolder> {

    private Context context;
    private List<Inmueble> inmuebles;
    private LayoutInflater inflater;

    public InmueblesAdapter(Context context, List<Inmueble> inmuebles, LayoutInflater inflater) {
        this.context = context;
        this.inmuebles = inmuebles;
        this.inflater = inflater;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = inflater.inflate(R.layout.item_inmueble, parent, false);
        return new ViewHolder(root);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Inmueble inmueble = inmuebles.get(position);
        holder.direccion.setText(inmueble.getDireccion());
        holder.monto.setText(inmueble.getPrecioInmueble() + "");
        Glide.with(context).load(inmueble.getImagenInmueble()).into(holder.fotoInmueble);
    }

    @Override
    public int getItemCount() {
        return inmuebles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView fotoInmueble;
        TextView direccion, monto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            fotoInmueble = itemView.findViewById(R.id.ivInmueble);
            direccion = itemView.findViewById(R.id.tvDireccion);
            monto = itemView.findViewById(R.id.tvMontoMensual);
        }

        @Override
        public void onClick(View v) {
            NavController navController = Navigation.findNavController(v);
            Inmueble inmueble = inmuebles.get(getAdapterPosition());
            Bundle bundle = new Bundle();
            bundle.putSerializable("inmueble", inmueble);
            navController.navigate(R.id.nav_detalleInmuebleFragment, bundle);
        }

    }

}
