package com.example.inmobiliaria_android_mobile.ui.contratos;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.example.inmobiliaria_android_mobile.modelo.Contrato;
import com.example.inmobiliaria_android_mobile.modelo.Pago;
import com.example.inmobiliaria_android_mobile.request.ApiClient;

import java.util.ArrayList;

public class DetallePagosViewModel extends AndroidViewModel {

    private Context context;
    public DetallePagosViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

}