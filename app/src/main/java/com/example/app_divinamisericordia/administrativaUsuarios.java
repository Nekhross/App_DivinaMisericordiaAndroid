package com.example.app_divinamisericordia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class administrativaUsuarios extends AppCompatActivity implements View.OnClickListener {

    private Button btnregresar;
    private Button btnregistrar;
    private Button btnlistar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrativa_usuarios);

        btnregresar = (Button) findViewById(R.id.btnregresar);
        btnregistrar = (Button) findViewById(R.id.btnregistrarusu);
        btnlistar = (Button) findViewById(R.id.btnlistar);

        btnregresar.setOnClickListener(this);

        btnregistrar.setOnClickListener(this);
        btnlistar.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        if (view.getId() == findViewById(R.id.btnregresar).getId()) {
            finish();
        }
        if (view.getId() == findViewById(R.id.btnregistrarusu).getId()) {
            Intent actividad = new Intent(this, registrarUsuarios.class);
            startActivity(actividad);
        }
        if (view.getId() == findViewById(R.id.btnlistar).getId()) {
            Intent actividad = new Intent(this, listarUsuarios.class);
            startActivity(actividad);
        }


    }

}