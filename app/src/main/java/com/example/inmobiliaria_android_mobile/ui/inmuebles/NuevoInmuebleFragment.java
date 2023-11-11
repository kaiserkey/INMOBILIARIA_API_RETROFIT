package com.example.inmobiliaria_android_mobile.ui.inmuebles;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.inmobiliaria_android_mobile.R;
import com.example.inmobiliaria_android_mobile.databinding.FragmentNuevoInmuebleBinding;
import com.example.inmobiliaria_android_mobile.modelo.Inmueble;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class NuevoInmuebleFragment extends Fragment {
    private NuevoInmuebleViewModel mv;
    private FragmentNuevoInmuebleBinding binding;
    private ImageView ivImagen;
    private static final int REQUEST_PERMISSIONS_CODE = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNuevoInmuebleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mv = new ViewModelProvider(this).get(NuevoInmuebleViewModel.class);

        Button btnCargarImagen = root.findViewById(R.id.btn_cargar_imagen);
        ivImagen = root.findViewById(R.id.ivImagen);

        btnCargarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkStoragePermissions()) {
                    mv.abrirSelectorImagen(NuevoInmuebleFragment.this);
                } else {
                    requestStoragePermissions();
                }
            }
        });

        Button btnCrearInmueble = root.findViewById(R.id.btn_crear_inmueble);
        btnCrearInmueble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarInmueble();
            }
        });
        Log.d("NuevoInmuebleFragment", "onCreateView");
        // Observar cambios en la URI de la imagen
        mv.getImagenUriLiveData().observe(getViewLifecycleOwner(), new Observer<Uri>() {
            @Override
            public void onChanged(Uri imagenUri) {
                Log.d("URI onChanged", "ENTRO");
                // Cargar la imagen en el ImageView utilizando Glide
                mostrarImagen(imagenUri);
            }
        });

        return root;
    }

    private boolean checkStoragePermissions() {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermissions() {
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mv.abrirSelectorImagen(this);
            } else {
                Toast.makeText(requireContext(), "No se han concedido permisos para acceder al almacenamiento", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void mostrarImagen(Uri imagenUri) {
        //ivImagen.setImageURI(imagenUri);
        Log.d("URI de la imagen:", imagenUri.toString());
        Log.d("IMAGEN: ", imagenUri.getPath());
        Glide.with(requireContext())
                .load(imagenUri)
                .into(ivImagen);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri imagenUri = data.getData();
                mv.procesarImagenSeleccionada(imagenUri);
            }
        }
    }

    public Bitmap obtenerBitmapDeImagen(Uri imagenUri) throws IOException {
        if (imagenUri == null) {
            // Si la URI es nula, cargar la imagen por defecto desde la carpeta drawable
            return BitmapFactory.decodeResource(getResources(), R.drawable.propiedad);
        }

        InputStream inputStream = requireContext().getContentResolver().openInputStream(imagenUri);
        return BitmapFactory.decodeStream(inputStream);
    }

    public Inmueble obtenerDatosInmueble() {
        EditText etDireccion = requireView().findViewById(R.id.etDireccion);
        EditText etAmbientes = requireView().findViewById(R.id.etAmbientes);
        EditText etPrecio = requireView().findViewById(R.id.etPrecio);
        EditText etUso = requireView().findViewById(R.id.etUso);
        EditText etTipo = requireView().findViewById(R.id.etTipo);

        String direccion = etDireccion.getText().toString().trim();
        String ambientesStr = etAmbientes.getText().toString().trim();
        int ambientes = ambientesStr.isEmpty() ? 0 : Integer.parseInt(ambientesStr);
        String precioStr = etPrecio.getText().toString().trim();
        double precio = precioStr.isEmpty() ? 0 : Double.parseDouble(precioStr);
        String uso = etUso.getText().toString().trim();
        String tipo = etTipo.getText().toString().trim();

        // Obtiene la Uri de la imagen seleccionada
        Uri imagenUri = mv.getImagenUriLiveData().getValue();

        // Convierte la imagen en un Bitmap
        Bitmap imagenBitmap = null;
        try {
            imagenBitmap = obtenerBitmapDeImagen(imagenUri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Crea el objeto Inmueble con los datos obtenidos del formulario
        Inmueble inmueble = new Inmueble(0, 0, null, direccion, uso, tipo, ambientes, "", precio, false, "");
        inmueble.setImagenInmueble(imagenBitmap);

        return inmueble;
    }

    public void enviarInmueble() {
        Inmueble inmueble = obtenerDatosInmueble();

        // Crear una instancia de MultipartBody.Part para la imagen
        MultipartBody.Part imagePart = prepareFilePart("file", mv.getImagenUriLiveData().getValue());

        mv.enviarInmuebleAlServidor(inmueble, imagePart);
    }

    // Método para preparar la parte del archivo
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        if (fileUri == null) {
            return null;
        }

        File file = new File(getRealPathFromURI(fileUri));
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    // Método para obtener la ruta real desde la Uri
    private String getRealPathFromURI(Uri contentUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, projection, null, null, null);

        if (cursor == null) {
            return null;
        }

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String filePath = cursor.getString(column_index);
        cursor.close();

        return filePath;
    }


}