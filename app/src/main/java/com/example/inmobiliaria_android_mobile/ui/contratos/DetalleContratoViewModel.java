package com.example.inmobiliaria_android_mobile.ui.contratos;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.inmobiliaria_android_mobile.MainActivity;
import com.example.inmobiliaria_android_mobile.modelo.Contrato;
import com.example.inmobiliaria_android_mobile.modelo.Inmueble;
import com.example.inmobiliaria_android_mobile.modelo.Pago;
import com.example.inmobiliaria_android_mobile.modelo.Propietario;
import com.example.inmobiliaria_android_mobile.request.ApiClient;
import com.example.inmobiliaria_android_mobile.request.ApiClientRetrofit;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleContratoViewModel extends AndroidViewModel {

    private Context context;
    private MutableLiveData<Contrato> contratoMutable;
    private MutableLiveData<ArrayList<Pago>> pagosMutable;
    public DetalleContratoViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public LiveData<ArrayList<Pago>> getPagosMutable() {
        if(pagosMutable == null){
            pagosMutable = new MutableLiveData<>();
        }
        return pagosMutable;
    }
    public LiveData<Contrato> getContratoMutable() {
        if(contratoMutable == null){
            contratoMutable = new MutableLiveData<>();
        }
        return contratoMutable;
    }

    public void obtenerContrato(Inmueble inmueble) {

        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");

        if (token.isEmpty()) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
            return;
        }

        ApiClientRetrofit.EndPointInmobiliaria end = ApiClientRetrofit.getEndPointInmobiliaria();
        Call<Contrato> call = end.obtenerContratoActivo(token, inmueble);

        call.enqueue(new Callback<Contrato>() {
            @Override
            public void onResponse(@NonNull Call<Contrato> call, @NonNull Response<Contrato> response) {
                Log.d("CONTRATO: ", response.message());
                if (response.isSuccessful()) {
                    Contrato contrato = response.body();
                    contratoMutable.postValue(contrato);
                } else {
                    Toast.makeText(context, "Error al obtener el contrato", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Contrato> call, @NonNull Throwable t) {
                Toast.makeText(context, "Error al obtener el contrato", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void obtenerPagosPorContrato(int contratoId) {

        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");

        if (token.isEmpty()) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
            return;
        }

        ApiClientRetrofit.EndPointInmobiliaria end = ApiClientRetrofit.getEndPointInmobiliaria();
        Call<ArrayList<Pago>> call = end.obtenerPagos(token, contratoId);

        call.enqueue(new Callback<ArrayList<Pago>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Pago>> call, @NonNull Response<ArrayList<Pago>> response) {
                if (response.isSuccessful()) {
                    ArrayList<Pago> pagos = response.body();
                    pagosMutable.setValue(pagos);
                } else {
                    String error = "Error al obtener los pagos. Código: " + response.code();
                    try {
                        error += "\n" + response.errorBody().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d("error", error);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Pago>> call, @NonNull Throwable t) {
                String error = "Error al obtener los pagos: " + t.getMessage();
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
            }
        });
    }





}