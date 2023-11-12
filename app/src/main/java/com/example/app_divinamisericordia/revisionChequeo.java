package com.example.app_divinamisericordia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class revisionChequeo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revision_chequeo);

        // Obtener los datos del chequeo enviados desde PacienteAdapter
        Intent intent = getIntent();
        if (intent != null) {
            String id = intent.getStringExtra("id");
            String fecha = intent.getStringExtra("fecha");
            String hora = intent.getStringExtra("hora");
            String nota = intent.getStringExtra("nota");
            String diagnostico = intent.getStringExtra("diagnostico");

            // Obtener referencias de los EditText y el Spinner
            TextView txtid = findViewById(R.id.txtid);
            TextView txtfecha = findViewById(R.id.lblfecha);
            TextView txthora = findViewById(R.id.lblhora);
            TextView txtnota = findViewById(R.id.lblnotaevo);
            TextView txtdiagnostico = findViewById(R.id.lbldiagnostico);
            Button btnActualizar = findViewById(R.id.btnregresarlis);

            // Establecer los valores en los EditText
            txtfecha.setText(fecha);
            txthora.setText(hora);
            txtnota.setText(nota);
            txtdiagnostico.setText(diagnostico);
            txtid.setText(id);


            btnActualizar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();


                }
            });
        }
    }
    }
