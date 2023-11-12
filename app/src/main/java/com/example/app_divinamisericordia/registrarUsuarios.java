package com.example.app_divinamisericordia;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class registrarUsuarios extends AppCompatActivity {
    private Button btn_almacenar;

    private EditText txt_usuario;
    private EditText txt_contraseña;
    private Spinner spinnerrol;
    private ProgressBar barraprogreso;

    private String[] roles;

    private String ip = "192.168.68.24";

    private  String url="http://"+ip+"/demoDivina/public/api/usuarios";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);
        txt_usuario = (EditText) findViewById(R.id.txtusuarios);
        txt_contraseña = (EditText) findViewById(R.id.txtcontrasenas);
        spinnerrol=(Spinner)findViewById(R.id.spinnerrolr);
        roles= getResources().getStringArray(R.array.rol);
        barraprogreso = (ProgressBar) findViewById(R.id.barraregistrousu);



        ArrayAdapter<String> rolesadapter=new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,roles);


        spinnerrol.setAdapter(rolesadapter);

        String rolesSeleccionado = getIntent().getStringExtra("roles");

        if (rolesSeleccionado != null &&  rolesSeleccionado.isEmpty()){
            int tipoIndex= rolesadapter.getPosition(rolesSeleccionado);
            spinnerrol.setSelection(tipoIndex);
        }
        btn_almacenar=(Button) findViewById(R.id.btnregistrar);
        btn_almacenar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String roleSeleccionado = roles[spinnerrol.getSelectedItemPosition()];
                String nombreusuario = txt_usuario.getText().toString().trim();
                String contrasena= txt_contraseña.getText().toString().trim();
                if(!nombreusuario.isEmpty() && !contrasena.isEmpty()){
                    guardarUsuario(roleSeleccionado,nombreusuario,contrasena);

                    spinnerrol.setSelection(0);

                    txt_usuario.setText("");
                    txt_contraseña.setText("");
                }else{
                    Toast.makeText(registrarUsuarios.this,"Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private  void guardarUsuario(String roleSeleccionado, String nombreusuario, String contrasena){

        JSONObject jsonObject= new JSONObject();
        barraprogreso.setVisibility(View.VISIBLE);

        try {

            jsonObject.put("usuario",nombreusuario);
            jsonObject.put("contrasena",contrasena);
            jsonObject.put("rol",roleSeleccionado);

        }catch (JSONException e){
            barraprogreso.setVisibility(View.INVISIBLE);
            e.printStackTrace();
        }

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        barraprogreso.setVisibility(View.INVISIBLE);
                        Toast.makeText(registrarUsuarios.this, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                barraprogreso.setVisibility(View.INVISIBLE);

                Toast.makeText(registrarUsuarios.this, "Error al registrar: " + error.toString(), Toast.LENGTH_SHORT).show();

            }
        });

        requestQueue.add(jsonObjectRequest);

    }
    }
