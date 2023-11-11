package com.example.inmobiliaria_android_mobile;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.inmobiliaria_android_mobile.modelo.Propietario;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.inmobiliaria_android_mobile.databinding.ActivityMenuBinding;

public class MenuActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMenuBinding binding;
    private MenuActivityViewModel mv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        mv = new ViewModelProvider(this).get(MenuActivityViewModel.class);
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMenu.toolbar);

        View headerView = binding.navView.getHeaderView(0);
        ImageView imageView = headerView.findViewById(R.id.imageView);
        TextView tvNombre = headerView.findViewById(R.id.tvNombre);
        TextView tvEmail = headerView.findViewById(R.id.tvMail);

        mv.obtenerPropietario();

        mv.getImagenMutable().observe(this, new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap imageBytes) {
                    Glide.with(getApplicationContext())
                            .load(imageBytes)
                            .transform(new CircleCrop())
                            .into(imageView);
            }
        });

        mv.getPropietarioMutable().observe(this, new Observer<Propietario>() {
            @Override
            public void onChanged(Propietario propietario) {
                tvNombre.setText(propietario.getNombre());
                tvEmail.setText(propietario.getEmail());
            }
        });

        binding.appBarMenu.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_inicio, R.id.nav_perfil, R.id.nav_inmuebles, R.id.nav_inquilinos, R.id.nav_contratos, R.id.nav_logout)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}