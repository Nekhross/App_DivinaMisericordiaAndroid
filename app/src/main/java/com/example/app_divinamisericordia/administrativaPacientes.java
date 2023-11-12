package com.example.app_divinamisericordia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class administrativaPacientes extends AppCompatActivity implements View.OnClickListener {

    private Button btnregresar;
    private Button btnbuscar;
    private Button btnlistar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrativa);

        btnregresar = (Button) findViewById(R.id.btnregresar);
        btnbuscar = (Button) findViewById(R.id.btnregistrarusu);
        btnlistar = (Button) findViewById(R.id.btnlistar);

        btnregresar.setOnClickListener(this);

        btnbuscar.setOnClickListener(this);
        btnlistar.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        if (view.getId() == findViewById(R.id.btnregresar).getId()) {
            finish();
        }
        if (view.getId() == findViewById(R.id.btnregistrarusu).getId()) {
            Intent actividad = new Intent(this, buscarPacientes.class);
            startActivity(actividad);
        }
        if (view.getId() == findViewById(R.id.btnlistar).getId()) {
            Intent actividad = new Intent(this, listarPacientes.class);
            startActivity(actividad);
        }


    }

}