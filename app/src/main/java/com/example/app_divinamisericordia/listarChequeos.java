package com.example.app_divinamisericordia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class listarChequeos extends AppCompatActivity implements View.OnClickListener {

    private ListView lstpacientes;
    private ProgressBar barraprogreso;
    private Button btncargar;
    private EditText etchequeos;
    private chequeoAdapter adaptador;
    private ArrayList<chequeoMedico> listaCompleta;
    private String ip = "192.168.68.24";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_chequeos);

        lstpacientes = findViewById(R.id.lstchequeos);
        barraprogreso = findViewById(R.id.barraprogreso);
        btncargar = findViewById(R.id.btncargar);
        etchequeos = findViewById(R.id.etchequeos);

        btncargar.setOnClickListener(this);

        // Configurar el Listener para filtrar la lista en tiempo real
        etchequeos.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Filtrar la lista en tiempo real
                String text = charSequence.toString().toLowerCase().trim();
                ArrayList<chequeoMedico> filtrados = new ArrayList<>();

                if (text.isEmpty()) {
                    // Si el texto está vacío, restaurar la lista completa
                    filtrados.addAll(listaCompleta);
                } else {
                    // Filtrar por el texto ingresado
                    for (chequeoMedico chequeo : listaCompleta) {
                        if (chequeo.getCedula().toLowerCase().contains(text)
                                || chequeo.getNombres().toLowerCase().contains(text)
                                || chequeo.getApellidos().toLowerCase().contains(text)) {
                            filtrados.add(chequeo);
                        }
                    }
                }

                adaptador.clear();
                adaptador.addAll(filtrados);
                adaptador.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        llenarlista();
    }

    private void llenarlista() {
        try {
            barraprogreso.setVisibility(View.VISIBLE);

            JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                    (Request.Method.GET, "http://" + ip + "/demoDivina/public/api/chequeomedico", null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                barraprogreso.setVisibility(View.GONE);

                                // Actualizar la lista completa
                                listaCompleta = new ArrayList<>();

                                ArrayList<chequeoMedico> chequeoMedicos = new ArrayList<>();
                                int i;
                                for (i = 0; i < response.length(); i++) {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    String id = jsonObject.getString("id");
                                    String fecha = jsonObject.getString("fecha");
                                    String hora = jsonObject.getString("hora");
                                    String nota = jsonObject.getString("notapreocupacion");
                                    String idpaciente = jsonObject.getString("id_paciente");
                                    String diagnostico = jsonObject.getString("diagnostico");
                                    String nombres = jsonObject.getString("nombres");
                                    String apellidos = jsonObject.getString("apellidos");
                                    String cedula = jsonObject.getString("cedula");

                                    chequeoMedicos.add(new chequeoMedico(id, fecha, hora, nota, idpaciente, diagnostico, nombres, apellidos, cedula));
                                    listaCompleta.add(new chequeoMedico(id, fecha, hora, nota, idpaciente, diagnostico, nombres, apellidos, cedula));
                                }

                                if (i == 0) {
                                    Toast.makeText(getApplicationContext(), "No se encontraron chequeos medicos", Toast.LENGTH_SHORT).show();
                                }

                                adaptador = new chequeoAdapter(listarChequeos.this, chequeoMedicos);
                                lstpacientes.setAdapter(adaptador);

                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(), "Error al procesar la respuesta JSON", Toast.LENGTH_SHORT).show();
                                barraprogreso.setVisibility(View.GONE);
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error de Conectividad Chequeo del Paciente", Toast.LENGTH_SHORT).show();
                            barraprogreso.setVisibility(View.GONE);
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    return params;
                }
            };

            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsArrayRequest);

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        llenarlista();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == findViewById(R.id.btncargar).getId()) {
            llenarlista();
        }
    }
}