package com.example.inmobiliaria_android_mobile;

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

import com.example.inmobiliaria_android_mobile.modelo.Propietario;
import com.example.inmobiliaria_android_mobile.request.ApiClientRetrofit;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivityViewModel extends AndroidViewModel {

    private Context context;
    private MutableLiveData<Propietario> propietarioMutable;
    private MutableLiveData<Bitmap> imagenMutable;
    public MenuActivityViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public LiveData<Bitmap> getImagenMutable() {
        if(imagenMutable == null){
            imagenMutable = new MutableLiveData<>();
        }
        return imagenMutable;
    }

    public LiveData<Propietario> getPropietarioMutable() {
        if(propietarioMutable == null){
            propietarioMutable = new MutableLiveData<>();
        }
        return propietarioMutable;
    }

    public void obtenerPropietario() {
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
                        propietarioMutable.setValue(response.body());

                        // Obtener la imagen del propietario
                        obtenerImagenPropietario(response.body().getIdPropietario());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Propietario> call, @NonNull Throwable t) {
                Toast.makeText(context, "Error al obtener usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void obtenerImagenPropietario(int id) {
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");

        ApiClientRetrofit.EndPointInmobiliaria end = ApiClientRetrofit.getEndPointInmobiliaria();
        Call<ResponseBody> call = end.obtenerImagenPropietario(token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        byte[] imageBytes = response.body().bytes();

                        // Convertir los bytes de la imagen a un objeto Bitmap
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                        // Guardar el objeto Bitmap en un MutableLiveData
                        if (bitmap != null) {
                            // La imagen se obtuvo correctamente, guardarla en el MutableLiveData
                            imagenMutable.setValue(bitmap);
                        } else {
                            // La imagen es nula, cargar la imagen por defecto desde la carpeta drawable
                            Drawable defaultImage = context.getResources().getDrawable(R.drawable.default_user_profile);
                            Bitmap defaultBitmap = ((BitmapDrawable) defaultImage).getBitmap();
                            imagenMutable.setValue(defaultBitmap);
                        }

                        // Utilizar el MutableLiveData como sea necesario
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