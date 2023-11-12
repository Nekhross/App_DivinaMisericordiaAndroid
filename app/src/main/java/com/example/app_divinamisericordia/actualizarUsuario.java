package com.example.app_divinamisericordia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class actualizarUsuario extends AppCompatActivity {

    private String ip = "192.168.68.24";

    private ProgressBar barraProgreso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_usuario);

        // Obtener los datos del paciente enviados desde PacienteAdapter
        Intent intent = getIntent();
        if (intent != null) {
            String id = intent.getStringExtra("id");
            String usuario = intent.getStringExtra("usuario");
            String contrasena = intent.getStringExtra("contrasena");
            String rol = intent.getStringExtra("rol");
            boolean estado = intent.getBooleanExtra("estado", false);

            // Obtener referencias de los EditText y el Spinner
            TextView txtid = findViewById(R.id.ttxid);
            EditText txtusuarioact = findViewById(R.id.txtusuarios);
            EditText txtcontrasenaAct = findViewById(R.id.txtcontrasenas);
            Spinner spinnerrolact = findViewById(R.id.spinnerrolact);
            Spinner spinnerEstado = findViewById(R.id.spinnerestado);
            Button btnActualizar = findViewById(R.id.btnactualizado);
            barraProgreso = findViewById(R.id.barraActualizarusu);

            // Establecer los valores en los EditText
            txtusuarioact.setText(usuario);
            txtcontrasenaAct.setText(contrasena);
            ArrayAdapter<CharSequence> rolesAdapter = ArrayAdapter.createFromResource(this, R.array.rol, android.R.layout.simple_spinner_item);
            rolesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerrolact.setAdapter(rolesAdapter);
            int posicionRol = rolesAdapter.getPosition(rol);
            spinnerrolact.setSelection(posicionRol);
            txtid.setText(id);

            // Crear una lista de estados
            List<String> estados = new ArrayList<>();
            estados.add("Activo");
            estados.add("Inactivo");


            // Crear un ArrayAdapter para el Spinner
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, estados);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerEstado.setAdapter(adapter);


            // Establecer el valor del Spinner (estado)
            spinnerEstado.setSelection(estado ? 0 : 1); // Suponiendo que el estado "Activo" está en la posición 0 y "Inactivo" en la posición 1
            btnActualizar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    barraProgreso.setVisibility(View.VISIBLE);
                    // Obtener los valores ingresados en los campos de texto y Spinner
                    String id = txtid.getText().toString();
                    String nuevoUsuario = txtusuarioact.getText().toString();
                    String nuevaContrasena = txtcontrasenaAct.getText().toString();
                    String nuevoRol = spinnerrolact.getSelectedItem().toString();
                    String nuevoEstado = spinnerEstado.getSelectedItemPosition() == 0 ? "Activo" : "Inactivo";

                    // Llamar al método actualizarDatos para enviar la solicitud al servidor
                    actualizarDatos(id, nuevoUsuario, nuevaContrasena, nuevoRol, nuevoEstado);
                }
            });
        }

    }

    private void actualizarDatos(String id, String nuevoUsuario, String nuevaContrasena, String nuevoRol, String nuevoEstado) {
        String url = "http://"+ip+"/demoDivina/public/api/usuarios/" + id;

        // Convertir el valor de nuevoEstado a booleano
        boolean estadoBooleano;
        if (nuevoEstado.equals("Activo")) {
            estadoBooleano = true;
        } else {
            estadoBooleano = false;
        }

        // Crear un objeto JSON con los datos actualizados del paciente
        JSONObject datosActualizados = new JSONObject();
        try {
            datosActualizados.put("usuario", nuevoUsuario);
            datosActualizados.put("contrasena", nuevaContrasena);
            datosActualizados.put("rol", nuevoRol);
            datosActualizados.put("estado", estadoBooleano);
        } catch (JSONException e) {
            barraProgreso.setVisibility(View.INVISIBLE);
            e.printStackTrace();
        }

        // Resto del código de la función...

        // Realizar la solicitud POST o PUT utilizando Volley
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, datosActualizados,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        barraProgreso.setVisibility(View.INVISIBLE);
                        // Actualización exitosa, mostrar mensaje de éxito al usuario
                        Toast.makeText(actualizarUsuario.this, "Paciente actualizado correctamente", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        barraProgreso.setVisibility(View.INVISIBLE);
                        // Error al actualizar el paciente, mostrar mensaje de error al usuario
                        Toast.makeText(actualizarUsuario.this, "Error"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Agregar la solicitud a la cola de solicitudes de Volley
        MySingleton.getInstance(this).addToRequestQueue(request);
    }




}
