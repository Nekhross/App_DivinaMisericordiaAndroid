package com.example.app_divinamisericordia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity implements View.OnClickListener{


    private Button btnpacientes;
    private Button btnchequeo;

    private Button btnusuario;
    private Button btnsalir;

    private Button btninformativa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        btnpacientes = (Button) findViewById(R.id.btnpacientes);
        btnchequeo = (Button) findViewById(R.id.btnchequeos);
        btnusuario= (Button) findViewById(R.id.btnusuarios);
        btnsalir= (Button) findViewById(R.id.btnsalir);
        btninformativa= (Button) findViewById(R.id.btninformativa);


        btnpacientes.setOnClickListener(this);

        btnchequeo.setOnClickListener(this);
        btnusuario.setOnClickListener(this);
        btnsalir.setOnClickListener(this);
        btninformativa.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {

        if (view.getId() == findViewById(R.id.btnpacientes).getId()) {
            Intent actividad = new Intent(this, administrativaPacientes.class);
            startActivity(actividad);

        }
        if (view.getId() == findViewById(R.id.btnchequeos).getId()) {
            Intent actividad = new Intent(this, listarChequeos.class);
            startActivity(actividad);

        }
        if (view.getId() == findViewById(R.id.btnusuarios).getId()) {
            Intent actividad = new Intent(this, administrativaUsuarios.class);
            startActivity(actividad);

        }
        if (view.getId() == findViewById(R.id.btninformativa).getId()) {
            Intent actividad = new Intent(this, informativa.class);
            startActivity(actividad);

        }
        if (view.getId() == findViewById(R.id.btnsalir).getId()) {
           finish();
        }
    }
}