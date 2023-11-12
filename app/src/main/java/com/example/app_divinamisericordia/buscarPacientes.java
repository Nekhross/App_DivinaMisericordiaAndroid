package com.example.app_divinamisericordia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class buscarPacientes extends AppCompatActivity {

    private EditText txtBuscarCedula;
    private Button btnBuscar;
    private TextView tvId;
    private TextView tvCedula;
    private TextView tvNombre;
    private TextView tvApellidos;
    private TextView tvEstado;
    private ImageView imgFoto;
    private Button btnActualizar;

    private ProgressBar barraProgreso;

    private String ip = "192.168.68.24";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);

        txtBuscarCedula = findViewById(R.id.txtBuscarCedula);
        btnBuscar = findViewById(R.id.btnBuscar);
        tvId = findViewById(R.id.ttxid);
        tvCedula = findViewById(R.id.tvCedula);
        tvNombre = findViewById(R.id.tvNombre);
        tvApellidos = findViewById(R.id.tvApellidos);
        tvEstado = findViewById(R.id.tvEstado);
        imgFoto = findViewById(R.id.imgFoto);
        btnActualizar = findViewById(R.id.btnActualizar);
        barraProgreso = findViewById(R.id.barraBuscar);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barraProgreso.setVisibility(View.VISIBLE);
                buscarPacientePorCedula();
            }
        });
    }

    private void buscarPacientePorCedula() {
        String cedula = txtBuscarCedula.getText().toString();

        String url = "http://"+ip+"/demoDivina/public/api/pacientes/buscarPorCedula"; // Reemplaza con la URL de tu API para la búsqueda por cédula

        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("cedula", cedula);
        } catch (JSONException e) {
            barraProgreso.setVisibility(View.INVISIBLE);
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                barraProgreso.setVisibility(View.INVISIBLE);
                                // Paciente encontrado, mostrar los datos en la interfaz
                                JSONObject paciente = response.getJSONObject("paciente");

                                String id = paciente.getString("id");
                                String cedula = paciente.getString("cedula");
                                String nombres = paciente.getString("nombres");
                                String apellidos = paciente.getString("apellidos");
                                boolean estado = paciente.getBoolean("estado");
                                String estadoString = estado ? "Activo" : "Inactivo";

                                // Obtener la imagen codificada en base64 desde la respuesta
                                String fotoBase64 = paciente.getString("foto");
                                byte[] fotoBytes = Base64.decode(fotoBase64, Base64.DEFAULT);
                                Bitmap fotoBitmap = BitmapFactory.decodeByteArray(fotoBytes, 0, fotoBytes.length);

                                // Mostrar los datos en los TextView e ImageView correspondientes
                                tvId.setText("ID: " + id);
                                tvCedula.setText("Cédula: " + cedula);
                                tvNombre.setText("Nombres: " + nombres);
                                tvApellidos.setText("Apellidos: " + apellidos);
                                tvEstado.setText("Estado: " + estadoString);

                                // Mostrar la imagen en el ImageView
                                imgFoto.setImageBitmap(fotoBitmap);

                                // Cambiar la visibilidad de los TextView e ImageView a VISIBLE
                                tvId.setVisibility(View.VISIBLE);
                                tvCedula.setVisibility(View.VISIBLE);
                                tvNombre.setVisibility(View.VISIBLE);
                                tvApellidos.setVisibility(View.VISIBLE);
                                tvEstado.setVisibility(View.VISIBLE);
                                imgFoto.setVisibility(View.VISIBLE);

                                // Cambiar la visibilidad del botón "Actualizar" a VISIBLE
                                btnActualizar.setVisibility(View.VISIBLE);

                                // Asignar el OnClickListener al botón "Actualizar"
                                btnActualizar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        // Obtener los datos del paciente que se mostraron en la interfaz
                                        String id = null;
                                        try {
                                            id = paciente.getString("id");
                                            String cedula = paciente.getString("cedula");
                                            String nombres = paciente.getString("nombres");
                                            String apellidos = paciente.getString("apellidos");
                                            boolean estado = paciente.getBoolean("estado");
                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }

                                        // Convertir la imagen del ImageView a base64 si es necesario
                                        String fotoBase64 = obtenerBase64DeImageView(imgFoto);

                                        // Crear un Intent para abrir la actividad ActualizarPaciente
                                        Intent intent = new Intent(buscarPacientes.this, actualizarPaciente.class);
                                        intent.putExtra("id", id);
                                        intent.putExtra("cedula", cedula);
                                        intent.putExtra("nombres", nombres);
                                        intent.putExtra("apellidos", apellidos);
                                        intent.putExtra("estado", estado);
                                        intent.putExtra("foto", fotoBase64);
                                        startActivity(intent);


                                    }
                                });
                            } else {
                                barraProgreso.setVisibility(View.INVISIBLE);
                                // Paciente no encontrado, mostrar mensaje de error
                                Toast.makeText(buscarPacientes.this, "Paciente no encontrado", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            barraProgreso.setVisibility(View.INVISIBLE);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                barraProgreso.setVisibility(View.INVISIBLE);
                // Manejar el error de la solicitud
                Toast.makeText(buscarPacientes.this, "Error en la solicitud", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonObjectRequest);
    }

    // Método para convertir una imagen del ImageView a base64
    private String obtenerBase64DeImageView(ImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    protected void onResume() {
        super.onResume();

        // Limpiar los campos de texto
        txtBuscarCedula.setText("");
        tvId.setText("");
        tvCedula.setText("");
        tvNombre.setText("");
        tvApellidos.setText("");
        tvEstado.setText("");
        imgFoto.setVisibility(View.INVISIBLE);


        // Ocultar el botón de actualizar
        btnActualizar.setVisibility(View.INVISIBLE);
    }
}
