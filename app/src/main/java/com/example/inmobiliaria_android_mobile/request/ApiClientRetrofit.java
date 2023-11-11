package com.example.inmobiliaria_android_mobile.request;

import com.example.inmobiliaria_android_mobile.modelo.Contrato;
import com.example.inmobiliaria_android_mobile.modelo.Inmueble;
import com.example.inmobiliaria_android_mobile.modelo.Inquilino;
import com.example.inmobiliaria_android_mobile.modelo.Pago;
import com.example.inmobiliaria_android_mobile.modelo.Propietario;
import com.example.inmobiliaria_android_mobile.modelo.Usuario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public class ApiClientRetrofit {

    private static final String PATH = "http://192.168.1.139:5200/api/";
    private static EndPointInmobiliaria endPointInmobiliaria;

    public static EndPointInmobiliaria getEndPointInmobiliaria() {
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PATH)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        endPointInmobiliaria = retrofit.create(EndPointInmobiliaria.class);

        return endPointInmobiliaria;
    }


    public interface EndPointInmobiliaria {

        //login propietario
        @POST("Propietario/login")
        Call<String> login(@Body Usuario usuario);

        //perfil del propietario
        @GET("Propietario/getUsuario")
        Call<Propietario> obtenerUsuario(@Header("Authorization") String token);

        //actualizar perfil
        @POST("Inmueble/updateUsuario")
        Call<Propietario> updateUsuario(@Header("Authorization") String token, @Body Propietario propietario);

        //mis propiedades
        @GET("Inmueble/getInmuebles")
        Call<ArrayList<Inmueble>> obtnerInmuebles(@Header("Authorization") String token);
        @GET("Inmueble/inmueblesConContratoVigente/{idPropietario}")
        Call<ArrayList<Inmueble>> inmueblesConContratoVigente(@Header("Authorization") String token, @Path("idPropietario") int idPropietario);
        //actualizar inmueble
        @POST("Inmueble/updateInmueble")
        Call<Inmueble> actualizarInmueble(@Header("Authorization") String token, @Body Inmueble inmueble);
        //mis propiedades
        @GET("Inmueble/inmueblesAlquilados")
        Call<ArrayList<Inmueble>> obtnerInmueblesAlquilados(@Header("Authorization") String token);

        //Dado un inmueble, retorna el inquilino del ultimo contrato activo de ese inmueble
        @POST("Inmueble/getUltimoInquilino")
        Call<Inquilino> obtnerUltimoInquilino(@Header("Authorization") String token, @Body Inmueble inmueble);

        //contratos activos por inmueble
        @POST("Inmueble/getContratoActivo")
        Call<Contrato> obtenerContratoActivo(@Header("Authorization") String token, @Body Inmueble inmueble);

        //pagos del contrato
        @GET("Inmueble/getPagoContrato/{contratoId}")
        Call<ArrayList<Pago>> obtenerPagos(@Header("Authorization") String token, @Path("contratoId") int contratoId);

        //imagen del propietario
        @GET("Propietario/imagen")
        Call<ResponseBody> obtenerImagenPropietario(@Header("Authorization") String token);

        //imagen del inmueble
        @GET("Inmueble/imagen/{id}")
        Call<ResponseBody> obtenerImagenInmueble(@Header("Authorization") String token, @Path("id") int id);

        @Multipart
        @POST("Inmueble/nuevoInmuebleConImagen")
        Call<Inmueble> nuevoInmuebleConImagen(
                @Header("Authorization") String token,
                @Part("idPropietario") RequestBody idPropietario,
                @Part("direccion") RequestBody direccion,
                @Part("ambientes") RequestBody ambientes,
                @Part("precio") RequestBody precio,
                @Part("uso") RequestBody uso,
                @Part("tipo") RequestBody tipo,
                @Part MultipartBody.Part imageFile
        );
    }
}