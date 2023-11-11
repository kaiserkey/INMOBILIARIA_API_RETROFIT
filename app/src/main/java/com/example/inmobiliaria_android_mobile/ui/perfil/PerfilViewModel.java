package com.example.inmobiliaria_android_mobile.ui.perfil;

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

import com.example.inmobiliaria_android_mobile.MainActivity;
import com.example.inmobiliaria_android_mobile.R;
import com.example.inmobiliaria_android_mobile.modelo.Propietario;
import com.example.inmobiliaria_android_mobile.request.ApiClient;
import com.example.inmobiliaria_android_mobile.request.ApiClientRetrofit;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilViewModel extends AndroidViewModel {

    private Context context;
    private ApiClient api;
    private MutableLiveData<Propietario> dataPropietarioMutable;
    private MutableLiveData<String> valorBotonMutable;
    private MutableLiveData<Bitmap> imagenMutable;

    public PerfilViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        api = ApiClient.getApi();

    }

    public LiveData<Bitmap> getImagenMutable() {
        if(imagenMutable == null){
            imagenMutable = new MutableLiveData<>();
        }
        return imagenMutable;
    }
    public LiveData<String> getValorBotonMutable() {
        if (valorBotonMutable == null) {
            valorBotonMutable = new MutableLiveData<>();
        }
        return valorBotonMutable;
    }
    public LiveData<Propietario> getDataPropietarioMutable() {
        if (dataPropietarioMutable == null) {
            dataPropietarioMutable = new MutableLiveData<>();
        }
        return dataPropietarioMutable;
    }
    public void cargarPropietarioLogueado() {
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
                Log.d("RESPONSE", response.message());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        dataPropietarioMutable.setValue(response.body());
                        obtenerImagenPropietario();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Propietario> call, @NonNull Throwable t) {
                Toast.makeText(context, "Error al obtener usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void cambiarTextoBoton(String estadoBtn, String dni, String nombre, String apellido, String email, String clave, String telefono) {
        if (valorBotonMutable == null) {
            valorBotonMutable = new MutableLiveData<>();
        }

        try {
            if (estadoBtn.equals("EDITAR")) {
                valorBotonMutable.setValue("GUARDAR");
            } else {
                // Validamos que los campos no estén vacíos o nulos
                if (dni == null || nombre == null || apellido == null || email == null || clave == null || telefono == null || dni.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || clave.isEmpty() || telefono.isEmpty()) {
                    throw new IllegalArgumentException("Por favor, complete todos los campos");
                }

                // Validamos que el valor del campo "dni" sea un número válido y positivo
                long dniLong = Long.parseLong(dni);
                if (dniLong <= 0) {
                    throw new IllegalArgumentException("Por favor, ingrese un DNI válido");
                }

                Propietario propietario = dataPropietarioMutable.getValue();
                propietario.setDni(dni);
                propietario.setNombre(nombre);
                propietario.setApellido(apellido);
                propietario.setEmail(email);
                propietario.setClave(clave);
                propietario.setTelefono(telefono);

                actualizarPerfil(propietario);
                valorBotonMutable.setValue("EDITAR");
            }
        } catch (NumberFormatException e) {
            Toast.makeText(context, "Por favor, ingrese un DNI válido", Toast.LENGTH_SHORT).show();
        } catch (IllegalArgumentException e) {
            Toast.makeText(context, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
        }
    }
    public void actualizarPerfil(Propietario propietario){
        Log.d("UPDATE PROPIETARIO: ", propietario.toString());
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        ApiClientRetrofit.EndPointInmobiliaria end = ApiClientRetrofit.getEndPointInmobiliaria();
        Call<Propietario> call = end.updateUsuario(token,propietario);

        call.enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(@NonNull Call<Propietario> call, @NonNull Response<Propietario> response) {
                Log.d("UPDATE RESPONSE: ", response.message());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        dataPropietarioMutable.setValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Propietario> call, @NonNull Throwable t) {
                Toast.makeText(context, "Error al actualizar usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void obtenerImagenPropietario() {
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






