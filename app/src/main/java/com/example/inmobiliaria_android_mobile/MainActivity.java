package com.example.inmobiliaria_android_mobile;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.inmobiliaria_android_mobile.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainActivityViewModel mv;
    private SensorManager sensorManager;
    private Sensor accelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cargarConfig();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mv = new ViewModelProvider(this).get(MainActivityViewModel.class);

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mv.confirmaLogin(binding.etMail.getText().toString(), binding.etClave.getText().toString());
            }
        });

    }



    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }
    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float aceleracion = (float) Math.sqrt(x * x + y * y + z * z);

            if (aceleracion > 40) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:12345678"));
                startActivity(intent);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void cargarConfig(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Inmobiliaria");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorActionBar)));
    }

}

