package com.example.app_divinamisericordia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class actualizarPaciente extends AppCompatActivity {


    private String ip = "192.168.68.24";

    private ProgressBar barraProgreso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_paciente);

        // Obtener los datos del paciente enviados desde PacienteAdapter
        Intent intent = getIntent();
        if (intent != null) {
            String id = intent.getStringExtra("id");
            String cedula = intent.getStringExtra("cedula");
            String apellidos = intent.getStringExtra("apellidos");
            String nombres = intent.getStringExtra("nombres");
            boolean estado = intent.getBooleanExtra("estado", false);
            String fotoBase64 = intent.getStringExtra("foto");
            Bitmap fotoBitmap = null;
            if (fotoBase64 != null && !fotoBase64.isEmpty()) {
                try {
                    // Decodificar la foto desde Base64 a un array de bytes
                    byte[] decodedBytes = Base64.decode(fotoBase64, Base64.DEFAULT);
                    // Convertir el array de bytes a un objeto Bitmap
                    fotoBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Obtener referencias de los EditText y el Spinner
            TextView txtid = findViewById(R.id.ttxid);
            EditText txtCedulaAct = findViewById(R.id.txtcedulaact);
            EditText txtApellidosAct = findViewById(R.id.txtapellidosact);
            EditText txtNombresAct = findViewById(R.id.txtnombresact);
            Spinner spinnerEstado = findViewById(R.id.spinnerestado);
            ImageView actfotopaciente = findViewById(R.id.actfotopaciente);
            Button btnActualizar = findViewById(R.id.btnactualizado);
            barraProgreso = findViewById(R.id.barraActualizar);

            txtCedulaAct.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});



            // Establecer los valores en los EditText
            txtCedulaAct.setText(cedula);
            txtApellidosAct.setText(apellidos);
            txtNombresAct.setText(nombres);
            txtid.setText(id);

            // Crear una lista de estados
            List<String> estados = new ArrayList<>();
            estados.add("Activo");
            estados.add("Inactivo");

            if (fotoBitmap != null) {
                actfotopaciente.setImageBitmap(fotoBitmap);
            }

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
                    // Obtener los valores ingresados en los campos de texto y Spinner
                    String id = txtid.getText().toString();
                    String nuevaCedula = txtCedulaAct.getText().toString();
                    String nuevoApellido = txtApellidosAct.getText().toString();
                    String nuevoNombre = txtNombresAct.getText().toString();
                    String nuevoEstado = spinnerEstado.getSelectedItemPosition() == 0 ? "Activo" : "Inactivo";

                    // Llamar al método actualizarDatos para enviar la solicitud al servidor
                    barraProgreso.setVisibility(View.VISIBLE);
                    actualizarDatos(id, nuevaCedula, nuevoApellido, nuevoNombre, nuevoEstado);
                }
            });
        }
    }
    private void actualizarDatos(String id, String nuevaCedula, String nuevoApellido, String nuevoNombre, String nuevoEstado) {
        String url = "http://"+ip+"/demoDivina/public/api/pacientes/" + id;

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
            datosActualizados.put("nombres", nuevoNombre);
            datosActualizados.put("apellidos", nuevoApellido);
            datosActualizados.put("cedula", nuevaCedula);
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
                        Toast.makeText(actualizarPaciente.this, "Paciente actualizado correctamente", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        barraProgreso.setVisibility(View.INVISIBLE);
                        // Error al actualizar el paciente, mostrar mensaje de error al usuario
                        Toast.makeText(actualizarPaciente.this, "Error al actualizar el paciente", Toast.LENGTH_SHORT).show();
                    }
                });

        // Agregar la solicitud a la cola de solicitudes de Volley
        MySingleton.getInstance(this).addToRequestQueue(request);
    }


    // Resto del código de la actividad...
}
