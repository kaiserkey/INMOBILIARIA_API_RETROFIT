package com.example.inmobiliaria_android_mobile.ui.inicio;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.inmobiliaria_android_mobile.request.ApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class InicioViewModel extends AndroidViewModel {

    private FusedLocationProviderClient fusedLocationClient;
    private MutableLiveData<Location> ubicacionActual = new MutableLiveData<>();
    private MutableLiveData<List<MarkerOptions>> marcadores = new MutableLiveData<>();
    private Context context;
    private ApiClient api;

    public InicioViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        api = ApiClient.getApi();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(application.getApplicationContext());
    }


    public LiveData<Location> getUbicacionActual() {
        return ubicacionActual;
    }
    public LiveData<List<MarkerOptions>> getMarcadores() {
        return marcadores;
    }

    public void actualizarUbicacion() {
        if (ContextCompat.checkSelfPermission(getApplication().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    ubicacionActual.setValue(location);
                    actualizarMarcadores(location);
                }
            });
        }
    }
    private void actualizarMarcadores(Location location) {
        List<MarkerOptions> listaMarcadores = new ArrayList<>();

        LatLng mercado1 = new LatLng(-33.289472, -66.336772);
        listaMarcadores.add(new MarkerOptions().position(mercado1).title("Inmobiliaria"));

        if (location != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            listaMarcadores.add(new MarkerOptions().position(latLng).title("Mi ubicación"));
        }

        marcadores.setValue(listaMarcadores);
    }
}