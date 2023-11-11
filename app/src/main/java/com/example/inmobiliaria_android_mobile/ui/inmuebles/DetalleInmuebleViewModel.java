package com.example.inmobiliaria_android_mobile.ui.inmuebles;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.inmobiliaria_android_mobile.modelo.Contrato;
import com.example.inmobiliaria_android_mobile.modelo.Inmueble;
import com.example.inmobiliaria_android_mobile.modelo.Pago;
import com.example.inmobiliaria_android_mobile.modelo.Propietario;
import com.example.inmobiliaria_android_mobile.request.ApiClient;
import com.example.inmobiliaria_android_mobile.request.ApiClientRetrofit;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleInmuebleViewModel extends AndroidViewModel {

    private Context context;
    private MutableLiveData<Inmueble> dataInmuebleMutable;
    public DetalleInmuebleViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public LiveData<Inmueble> getDataInmuebleMutable() {
        if(dataInmuebleMutable == null){
            dataInmuebleMutable = new MutableLiveData<>();
        }
        return dataInmuebleMutable;
    }


    public void actualizarInmueble(Inmueble inmueble) {
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");

        ApiClientRetrofit.EndPointInmobiliaria end = ApiClientRetrofit.getEndPointInmobiliaria();
        Call<Inmueble> call = end.actualizarInmueble(token, inmueble);

        call.enqueue(new Callback<Inmueble>() {
            @Override
            public void onResponse(@NonNull Call<Inmueble> call, @NonNull Response<Inmueble> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        dataInmuebleMutable = (dataInmuebleMutable != null) ? dataInmuebleMutable : new MutableLiveData<>();
                        dataInmuebleMutable.setValue(response.body());

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Inmueble> call, @NonNull Throwable t) {
                Toast.makeText(context, "Error al actualizar inmueble", Toast.LENGTH_SHORT).show();
            }
        });
    }





}