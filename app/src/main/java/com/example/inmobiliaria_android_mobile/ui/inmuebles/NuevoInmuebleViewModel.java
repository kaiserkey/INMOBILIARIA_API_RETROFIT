package com.example.inmobiliaria_android_mobile.ui.inmuebles;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria_android_mobile.MainActivity;
import com.example.inmobiliaria_android_mobile.modelo.Inmueble;
import com.example.inmobiliaria_android_mobile.modelo.Propietario;
import com.example.inmobiliaria_android_mobile.request.ApiClient;
import com.example.inmobiliaria_android_mobile.request.ApiClientRetrofit;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NuevoInmuebleViewModel extends AndroidViewModel {
    private MutableLiveData<Uri> imagenUriLiveData = new MutableLiveData<>();
    private Context context;
    private ApiClient api;
    private MutableLiveData<ArrayList<Inmueble>> inmueblesMutable;
    private Fragment fragment;

    public NuevoInmuebleViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        api = ApiClient.getApi();
    }



    public LiveData<Uri> getImagenUriLiveData() {
        Log.d("getImagenUriLiveData", "ENTRO");
        if (imagenUriLiveData == null) {
            imagenUriLiveData = new MutableLiveData<>();
        }
        return imagenUriLiveData;
    }

    public void procesarImagenSeleccionada(Uri imagenUri) {
        imagenUriLiveData.setValue(imagenUri);
    }

    public void abrirSelectorImagen(Fragment fragment) {
        this.fragment = fragment;
        // Abre el selector de imágenes
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        fragment.startActivityForResult(intent, 1);
    }

    public interface OnPropietarioObtenidoListener {
        void onPropietarioObtenido(Propietario propietario);
    }

    public void obtenerPropietarioDelServidor(String token, OnPropietarioObtenidoListener listener) {
        ApiClientRetrofit.EndPointInmobiliaria end = ApiClientRetrofit.getEndPointInmobiliaria();
        Call<Propietario> call = end.obtenerUsuario(token);

        call.enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(@NonNull Call<Propietario> call, @NonNull Response<Propietario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("RESPONSE", response.message());
                    Propietario propietario = response.body();
                    // Llamar al método de la interfaz para notificar al listener
                    listener.onPropietarioObtenido(propietario);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Propietario> call, @NonNull Throwable t) {
                Toast.makeText(context, "Error al obtener usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void enviarInmuebleAlServidor(Inmueble inmueble,MultipartBody.Part imagePart) {

        // Validar los datos del inmueble antes de enviarlos
        if (inmueble.getDireccion().isEmpty()) {
            Toast.makeText(context, "La dirección del inmueble no puede estar vacía", Toast.LENGTH_SHORT).show();
            return;
        }

        if (inmueble.getCantidadAmbientes() <= 0) {
            Toast.makeText(context, "La cantidad de ambientes debe ser mayor a cero", Toast.LENGTH_SHORT).show();
            return;
        }

        if (inmueble.getPrecioInmueble() <= 0) {
            Toast.makeText(context, "El precio del inmueble debe ser mayor a cero", Toast.LENGTH_SHORT).show();
            return;
        }

        String usoInmueble = inmueble.getUso().toLowerCase(); // Convertir a minúsculas para hacer la comparación más flexible

        if (!usoInmueble.equals("residencial") && !usoInmueble.equals("comercial")) {
            Toast.makeText(context, "El uso del inmueble debe ser 'residencial' o 'comercial'", Toast.LENGTH_SHORT).show();
            return;
        }

        if (inmueble.getTipo().isEmpty()) {
            Toast.makeText(context, "El tipo del inmueble no puede estar vacío", Toast.LENGTH_SHORT).show();
            return;
        }


        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");

        if (token.isEmpty()) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
            return;
        }
        // Obtener el propietario del servidor
        obtenerPropietarioDelServidor(token, new OnPropietarioObtenidoListener() {
            @Override
            public void onPropietarioObtenido(Propietario propietario) {
                if (propietario != null) {
                    // Establecer el ID del propietario en el inmueble
                    inmueble.setIdPropietario(propietario.getIdPropietario());
                    Log.d("INMUEBLE: ", inmueble.toString());

                    // Crear un RequestBody para el JSON
                    RequestBody idPropietario = RequestBody.create(MediaType.parse("application/json"), inmueble.getIdPropietario() + "");
                    RequestBody direccion = RequestBody.create(MediaType.parse("application/json"), inmueble.getDireccion());
                    RequestBody ambientes = RequestBody.create(MediaType.parse("application/json"), inmueble.getCantidadAmbientes() + "");
                    RequestBody precio = RequestBody.create(MediaType.parse("application/json"), inmueble.getPrecioInmueble() + "");
                    RequestBody uso = RequestBody.create(MediaType.parse("application/json"), inmueble.getUso());
                    RequestBody tipo = RequestBody.create(MediaType.parse("application/json"), inmueble.getTipo());

                    // Llamar al método para enviar el inmueble al servidor
                    ApiClientRetrofit.EndPointInmobiliaria end = ApiClientRetrofit.getEndPointInmobiliaria();
                    Call<Inmueble> call = end.nuevoInmuebleConImagen(token,
                                                                        idPropietario,
                                                                        direccion,
                                                                        ambientes,
                                                                        precio,
                                                                        uso,
                                                                        tipo,
                                                                        imagePart);

                    call.enqueue(new Callback<Inmueble>() {
                        @Override
                        public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                            Log.d("RESPONSE INSERT INMUEBLE: ", response.message());
                            if (response.isSuccessful()) {
                                // Mostrar Toast de éxito
                                Toast.makeText(context, "Inmueble cargado correctamente", Toast.LENGTH_SHORT).show();
                            } else {
                                // Mostrar Toast de error
                                Toast.makeText(context, "Error al cargar el inmueble", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Inmueble> call, Throwable t) {
                            // Mostrar Toast de error
                            Toast.makeText(context, "Error de red al cargar el inmueble", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Manejar el caso en el que no se pudo obtener el propietario
                    Toast.makeText(context, "Error al obtener el propietario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
