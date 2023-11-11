package com.example.inmobiliaria_android_mobile.ui.contratos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inmobiliaria_android_mobile.R;
import com.example.inmobiliaria_android_mobile.modelo.Contrato;
import com.example.inmobiliaria_android_mobile.modelo.Inmueble;
import com.example.inmobiliaria_android_mobile.modelo.Pago;
import com.example.inmobiliaria_android_mobile.request.ApiClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PagosAdapter extends RecyclerView.Adapter<PagosAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Pago> pagosAsociados;
    private LayoutInflater inflater;

    public PagosAdapter(Context context, ArrayList<Pago> pagosAsociados, LayoutInflater inflater) {
        this.context = context;
        this.pagosAsociados = pagosAsociados;
        this.inflater = inflater;
    }


    @NonNull
    @Override
    public PagosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = inflater.inflate(R.layout.item_pago, parent, false);
        return new PagosAdapter.ViewHolder(root);
    }


    @Override
    public void onBindViewHolder(@NonNull PagosAdapter.ViewHolder holder, int position) {
        holder.idPago.setText(String.valueOf(pagosAsociados.get(position).getId()));
        holder.numPago.setText(String.valueOf(pagosAsociados.get(position).getNumDePago()));
        holder.codigoContrato.setText(String.valueOf(pagosAsociados.get(position).getContrato().getId()));
        holder.importe.setText(String.valueOf(pagosAsociados.get(position).getImporte()));
        Log.d("PAGOS: ", pagosAsociados.get(position).toString());

        String fechaDePago = pagosAsociados.get(position).getFechaDePago() != null ? pagosAsociados.get(position).getFechaDePago() : "9999-12-01T00:00:00";
        String fechaDePagoFormateada = fechaDePago.substring(0, 10);
        holder.fechaDePago.setText(fechaDePagoFormateada);

    }

    @Override
    public int getItemCount() {
        return pagosAsociados.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView idPago,numPago, codigoContrato, importe, fechaDePago;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            idPago = itemView.findViewById(R.id.tvCodigoPago);
            numPago = itemView.findViewById(R.id.tvNumPago);
            codigoContrato = itemView.findViewById(R.id.tvCodigoContrato);
            importe = itemView.findViewById(R.id.tvImporte);
            fechaDePago = itemView.findViewById(R.id.tvFechaPago);

        }

    }

}
