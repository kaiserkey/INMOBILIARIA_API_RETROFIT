package com.example.inmobiliaria_android_mobile.ui.inquilinos;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.inmobiliaria_android_mobile.MainActivity;
import com.example.inmobiliaria_android_mobile.modelo.Inmueble;
import com.example.inmobiliaria_android_mobile.modelo.Inquilino;
import com.example.inmobiliaria_android_mobile.modelo.Propietario;
import com.example.inmobiliaria_android_mobile.request.ApiClientRetrofit;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleInquilinoViewModel extends AndroidViewModel {

    private MutableLiveData<Inquilino> inquilinoMutable;
    private MutableLiveData<Propietario> propietarioGaranteMutable;
    private Context context;
    public DetalleInquilinoViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public LiveData<Propietario> getPropietarioGaranteMutable() {
        propietarioGaranteMutable = (propietarioGaranteMutable == null) ? new MutableLiveData<>() : propietarioGaranteMutable;
        return propietarioGaranteMutable;
    }


    public LiveData<Inquilino> getInquilinoMutable() {
        inquilinoMutable = (inquilinoMutable == null) ? new MutableLiveData<>() : inquilinoMutable;
        return inquilinoMutable;
    }

    public void obtenerInquilino(Inmueble inmueble){
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");

        if (token.isEmpty()) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
            return;
        }

        ApiClientRetrofit.EndPointInmobiliaria end = ApiClientRetrofit.getEndPointInmobiliaria();
        Call<Inquilino> call = end.obtnerUltimoInquilino(token, inmueble);

        call.enqueue(new Callback<Inquilino>() {
            @Override
            public void onResponse(@NonNull Call<Inquilino> call, @NonNull Response<Inquilino> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        inquilinoMutable.setValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Inquilino> call, @NonNull Throwable t) {
                Toast.makeText(context, "Error al obtener inquilino", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void obtenerPropietarioGarante() {
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");

        if (token.isEmpty()) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
            return;
        }

        ApiClientRetrofit.EndPointInmobiliaria end = ApiClientRetrofit.getEndPointInmobiliaria();
        Call<Propietario> call = end.obtenerUsuario(token);

        call.enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(@NonNull Call<Propietario> call, @NonNull Response<Propietario> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        propietarioGaranteMutable.setValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Propietario> call, @NonNull Throwable t) {
                Toast.makeText(context, "Error al obtener usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }


}