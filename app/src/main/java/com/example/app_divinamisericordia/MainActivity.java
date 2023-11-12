package com.example.app_divinamisericordia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnlogin;
    private EditText txtusuario;
    private EditText txtclave;

    private ProgressBar barraProgreso;
    private String ip = "192.168.68.24";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnlogin = findViewById(R.id.btnlogin);
        txtusuario = findViewById(R.id.txtusuario);
        txtclave = findViewById(R.id.txtclave);
        barraProgreso = findViewById(R.id.progresoLogin);

        btnlogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnlogin) {
            String usuario = txtusuario.getText().toString();
            String contrasena = txtclave.getText().toString();

            // Verificar si los campos de inicio de sesión están vacíos
            if (usuario.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                barraProgreso.setVisibility(View.VISIBLE);
                login(usuario, contrasena);
            }
        }
    }

    private void login(String usuario, String contrasena) {
        String url = "http://"+ip+"/demoDivina/public/api/usuarios/login"; // Reemplaza con la URL de tu API para el login

        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("usuario", usuario);
            jsonBody.put("contrasena", contrasena);
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
                            String message = response.getString("message");

                            if (success) {
                                // Login exitoso, redirigir a la siguiente actividad
                                barraProgreso.setVisibility(View.INVISIBLE);
                                Intent actividad = new Intent(MainActivity.this, Menu.class);
                                txtusuario.setText("");
                                txtclave.setText("");
                                startActivity(actividad);
                            } else {
                                barraProgreso.setVisibility(View.INVISIBLE);
                                // Login fallido, mostrar mensaje de error
                                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainActivity.this, "Error en la solicitud reingrese los datos", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }
}
