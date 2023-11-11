package com.example.inmobiliaria_android_mobile;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.inmobiliaria_android_mobile.modelo.Propietario;
import com.example.inmobiliaria_android_mobile.modelo.Usuario;
import com.example.inmobiliaria_android_mobile.request.ApiClient;
import com.example.inmobiliaria_android_mobile.request.ApiClientRetrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityViewModel extends AndroidViewModel {

    private Context context;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();

    }


    public void confirmaLogin(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Campo vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        Usuario usuario = new Usuario(email, password);
        ApiClientRetrofit.EndPointInmobiliaria end = ApiClientRetrofit.getEndPointInmobiliaria();
        Call<String> call = end.login(usuario);
        Log.d("CALL" , call.toString());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                Log.d("response", response.body() + response.toString());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String token = "Bearer " + response.body();
                        Log.d("TOKEN", token); // Mostrar el token en el log
                        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("token", token);
                        editor.commit();

                        Intent intent = new Intent(context, MenuActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
                    }
                } else {
                    if (response.code() == 404) {
                        Toast.makeText(context, "Datos incorrectos", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Error en la respuesta: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Toast.makeText(context, "Error al llamar al servicio de login", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
