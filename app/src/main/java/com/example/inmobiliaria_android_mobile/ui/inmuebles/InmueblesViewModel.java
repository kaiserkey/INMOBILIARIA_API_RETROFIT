package com.example.inmobiliaria_android_mobile.ui.inmuebles;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.inmobiliaria_android_mobile.MainActivity;
import com.example.inmobiliaria_android_mobile.R;
import com.example.inmobiliaria_android_mobile.modelo.Inmueble;
import com.example.inmobiliaria_android_mobile.modelo.Propietario;
import com.example.inmobiliaria_android_mobile.request.ApiClient;
import com.example.inmobiliaria_android_mobile.request.ApiClientRetrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmueblesViewModel extends AndroidViewModel {

    private Context context;
    private ApiClient api;
    private MutableLiveData<ArrayList<Inmueble>> inmueblesMutable;


    public InmueblesViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        api = ApiClient.getApi();
    }

    public LiveData<ArrayList<Inmueble>> getInmuebles() {
        if (inmueblesMutable == null) {
            inmueblesMutable = new MutableLiveData<>();
        }
        return inmueblesMutable;
    }


    public void obtenerPropiedades() {
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");

        if (token.isEmpty()) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
            return;
        }

        ApiClientRetrofit.EndPointInmobiliaria end = ApiClientRetrofit.getEndPointInmobiliaria();
        Call<ArrayList<Inmueble>> call = end.obtnerInmuebles(token);

        call.enqueue(new Callback<ArrayList<Inmueble>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Inmueble>> call, @NonNull Response<ArrayList<Inmueble>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        // Llamar a obtenerImagenInmuebleConImagen con un listener de devolución de llamada
                        obtenerImagenInmuebleConImagen(response.body(), new OnImagenesCargadasListener() {
                            @Override
                            public void onImagenesCargadas(ArrayList<Inmueble> inmuebles) {
                                // Notificar al MutableLiveData con la lista de inmuebles con las imágenes cargadas
                                inmueblesMutable.setValue(inmuebles);
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Inmueble>> call, @NonNull Throwable t) {
                Toast.makeText(context, "Error al obtener propiedades", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface OnImagenesCargadasListener {
        void onImagenesCargadas(ArrayList<Inmueble> inmuebles);
    }

    public void obtenerImagenInmuebleConImagen(ArrayList<Inmueble> inmueble, OnImagenesCargadasListener listener) {
        AtomicInteger count = new AtomicInteger(0);
        for (int i = 0; i<inmueble.size(); i++) {
            SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
            String token = sp.getString("token", "");

            ApiClientRetrofit.EndPointInmobiliaria end = ApiClientRetrofit.getEndPointInmobiliaria();
            Call<ResponseBody> call = end.obtenerImagenInmueble(token, inmueble.get(i).getId());

            int finalI = i;
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            byte[] imageBytes = response.body().bytes();

                            // Convertir los bytes de la imagen a un objeto Bitmap
                            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                            // Guardar el objeto Bitmap en el inmueble correspondiente
                            if (bitmap != null) {
                                // La imagen se obtuvo correctamente, guardarla en el inmueble
                                inmueble.get(finalI).setImagenInmueble(bitmap);
                            } else {
                                // La imagen es nula, cargar la imagen por defecto desde la carpeta drawable
                                Drawable defaultImage = context.getResources().getDrawable(R.drawable.propiedad);
                                Bitmap defaultBitmap = ((BitmapDrawable) defaultImage).getBitmap();
                                inmueble.get(finalI).setImagenInmueble(defaultBitmap);
                            }
                            Log.d("Cargo la Imagen?: ", inmueble.get(finalI).getImagenInmueble() != null ? "Si" : "No");

                            // Incrementar el contador de imágenes cargadas
                            if (count.incrementAndGet() == inmueble.size()) {
                                // Todas las imágenes se han cargado, notificar al listener
                                listener.onImagenesCargadas(inmueble);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error al obtener imagen", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    Log.d("error", t.getMessage());
                    Toast.makeText(context, "Error al obtener imagen", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}