package com.example.app_divinamisericordia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import org.osmdroid.views.MapView;

public class informativa extends AppCompatActivity implements View.OnClickListener {

    private MapView mapView;
    private Button btndireccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informativa);
        btndireccion= (Button) findViewById(R.id.btndireccion);
        btndireccion.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        if (v.getId()==findViewById(R.id.btndireccion).getId()){
            double latitude = -3.282317;
            double longitude = -79.961385;

            // Crear la URI para la ubicación en Google Maps
            Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude + "?q=" + Uri.encode("Divina Misericordi Albergue para desamparados"));

            // Crear el Intent para abrir Google Maps
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            // Verificar si hay aplicaciones que pueden manejar el Intent
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
                Toast.makeText(this,"DIRIGIENDO A GOOGLE MAPS",Toast.LENGTH_SHORT).show();
            }

        }

    }
}