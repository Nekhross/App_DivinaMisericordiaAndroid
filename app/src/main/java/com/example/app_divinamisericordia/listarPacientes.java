package com.example.app_divinamisericordia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

public class listarPacientes extends AppCompatActivity implements View.OnClickListener {

    private ListView lstpacientes;
    private ProgressBar barraprogreso;
    private Button btncargar;

    private EditText etBuscar; // Agregar referencia al EditText
    private Spinner spnEstado; // Agregar referencia al Spinner
    private ArrayList<Paciente> listaCompleta; // Agregar lista completa de pacientes
    private PacienteAdapter adaptador;

    private String ip = "192.168.68.24";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);
        lstpacientes = findViewById(R.id.lstpacientes);
        barraprogreso = findViewById(R.id.barraprogreso);
        btncargar = findViewById(R.id.btncargar);

        etBuscar = findViewById(R.id.etBuscar); // Inicializar el EditText
        spnEstado = findViewById(R.id.spnEstado); // Inicializar el Spinner

        btncargar.setOnClickListener(this);
        llenarlista();

        // Configurar el Listener para filtrar la lista en tiempo real
        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Filtrar la lista en tiempo real
                String text = charSequence.toString().toLowerCase().trim();
                ArrayList<Paciente> filtrados = new ArrayList<>();

                if (text.isEmpty()) {
                    // Si el texto está vacío, restaurar la lista completa
                    filtrados.addAll(listaCompleta);
                } else {
                    // Filtrar por el texto ingresado
                    for (Paciente paciente : listaCompleta) {
                        if (paciente.getCedula().toLowerCase().contains(text)
                                || paciente.getNombres().toLowerCase().contains(text)
                                || paciente.getApellidos().toLowerCase().contains(text)) {
                            filtrados.add(paciente);
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

        // Configurar el Listener para el Spinner
        spnEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Obtener el estado seleccionado del Spinner
                String estadoSeleccionado = spnEstado.getSelectedItem().toString();

                // Filtrar la lista por el estado seleccionado
                filtrarListaPorEstado(estadoSeleccionado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void llenarlista() {
        try {
            barraprogreso.setVisibility(View.VISIBLE);

            JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                    (Request.Method.GET, "http://"+ip+"/demoDivina/public/api/pacientes", null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                barraprogreso.setVisibility(View.GONE);
                                listaCompleta = new ArrayList<>();
                                ArrayList<Paciente> listapaciente = new ArrayList<Paciente>();
                                int i;
                                for (i = 0; i < response.length(); i++) {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    String id = jsonObject.getString("id");
                                    String cedula = jsonObject.getString("cedula");
                                    String apellidos = jsonObject.getString("apellidos");
                                    String nombres = jsonObject.getString("nombres");
                                    String foto = jsonObject.getString("foto");
                                    Boolean estado = Boolean.valueOf(jsonObject.getString("estado"));

                                    listapaciente.add(new Paciente(id, cedula, apellidos, nombres, estado, foto));
                                }
                                if (i == 0) {
                                    Toast.makeText(getApplicationContext(), "No se encontraron pacientes", Toast.LENGTH_SHORT).show();
                                }

                                listaCompleta = new ArrayList<>(listapaciente);
                                adaptador = new PacienteAdapter(listarPacientes.this, listapaciente);
                                lstpacientes.setAdapter(adaptador);

                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(), "Error al procesar la respuesta JSON", Toast.LENGTH_SHORT).show();
                                barraprogreso.setVisibility(View.GONE);
                            }

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error de Conectividad (Actualizar Lista)", Toast.LENGTH_SHORT).show();
                            barraprogreso.setVisibility(View.GONE);
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    // Aquí puedes agregar encabezados si es necesario para la autenticación en tu API Laravel
                    // params.put("Authorization", "Bearer " + "token_de_autenticacion");
                    // params.put("Accept-Language", "fr");

                    return params;
                }
            };

            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsArrayRequest);


        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void filtrarListaPorEstado(String estadoSeleccionado) {
        if (adaptador == null) {
            // Si el adaptador es nulo, simplemente retornamos sin hacer nada
            return;
        }

        ArrayList<Paciente> filtrados = new ArrayList<>();

        if (estadoSeleccionado.equals("Todos")) {
            // Si se selecciona "Todos", mostrar la lista completa
            filtrados.addAll(listaCompleta);
        } else {
            // Filtrar por estado (Activo o Inactivo)
            for (Paciente paciente : listaCompleta) {
                if ((estadoSeleccionado.equals("Activos") && paciente.getEstado())
                        || (estadoSeleccionado.equals("Inactivos") && !paciente.getEstado())) {
                    filtrados.add(paciente);
                }
            }
        }

        adaptador.clear();
        adaptador.addAll(filtrados);
        adaptador.notifyDataSetChanged();
    }
    @Override
    protected void onResume() {
        super.onResume();

        llenarlista();
        // Verificar si listaCompleta está inicializada antes de usarla
        if (listaCompleta != null) {
            // Si está inicializada, llenar la lista y restaurar los valores
            llenarlista();

            EditText etBuscar = findViewById(R.id.etBuscar);
            etBuscar.setText("");

            Spinner spinnerEstado = findViewById(R.id.spnEstado);
            spinnerEstado.setSelection(0); // 0 es la posición para la opción "Todos"
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == findViewById(R.id.btncargar).getId()) {
            llenarlista();
        }
    }
}
