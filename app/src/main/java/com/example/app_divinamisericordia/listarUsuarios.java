package com.example.app_divinamisericordia;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class listarUsuarios extends AppCompatActivity implements View.OnClickListener  {

    private ListView lstusuarios;
    private ProgressBar barraprogreso;
    private Button btncargar;

    private EditText etBuscar; // Agregar referencia al EditText
    private Spinner spnEstado; // Agregar referencia al Spinner
    private ArrayList<Usuario> listaCompleta; // Agregar lista completa de usuarios
    private UsuarioAdapter adaptador;

    private String ip = "192.168.68.24";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_usuarios);
        lstusuarios = findViewById(R.id.lstusuarios);
        barraprogreso = findViewById(R.id.barraprogreso);
        btncargar = findViewById(R.id.btncargar);

        etBuscar = findViewById(R.id.etBuscarusu); // Inicializar el EditText
        spnEstado = findViewById(R.id.spnEstadousu); // Inicializar el Spinner

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
                ArrayList<Usuario> filtrados = new ArrayList<>();

                if (text.isEmpty()) {
                    // Si el texto está vacío, restaurar la lista completa
                    filtrados.addAll(listaCompleta);
                } else {
                    // Filtrar por el texto ingresado
                    for (Usuario usuario : listaCompleta) {
                        if (usuario.getUsuario().toLowerCase().contains(text)
                                || usuario.getRol().toLowerCase().contains(text)) {
                            filtrados.add(usuario);
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
                    (Request.Method.GET, "http://" + ip + "/demoDivina/public/api/usuarios", null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                barraprogreso.setVisibility(View.GONE);

                                ArrayList<Usuario> listausuario = new ArrayList<>();
                                int i;
                                for (i = 0; i < response.length(); i++) {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    String id = jsonObject.getString("id");
                                    String usuario = jsonObject.getString("usuario");
                                    String contrasena = jsonObject.getString("contrasena");
                                    String rol = jsonObject.getString("rol");
                                    Boolean estado = Boolean.valueOf(jsonObject.getString("estado"));

                                    listausuario.add(new Usuario(id, usuario, contrasena, rol, estado));
                                }
                                if (i == 0) {
                                    Toast.makeText(getApplicationContext(), "No se encontraron usuarios", Toast.LENGTH_SHORT).show();
                                }

                                listaCompleta = new ArrayList<>(listausuario);
                                adaptador = new UsuarioAdapter(listarUsuarios.this, listausuario);
                                lstusuarios.setAdapter(adaptador);

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

        ArrayList<Usuario> filtrados = new ArrayList<>();

        if (estadoSeleccionado.equals("Todos")) {
            // Si se selecciona "Todos", mostrar la lista completa
            filtrados.addAll(listaCompleta);
        } else {
            // Filtrar por estado (Activo o Inactivo)
            for (Usuario usuario : listaCompleta) {
                if ((estadoSeleccionado.equals("Activos") && usuario.getEstado())
                        || (estadoSeleccionado.equals("Inactivos") && !usuario.getEstado())) {
                    filtrados.add(usuario);
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

            EditText etBuscar = findViewById(R.id.etBuscarusu);
            etBuscar.setText("");

            Spinner spinnerEstado = findViewById(R.id.spnEstadousu);
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