package com.example.app_divinamisericordia;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class UsuarioAdapter extends ArrayAdapter<Usuario> {
    private Context context;
    private ArrayList<Usuario> usuarios;

    public UsuarioAdapter(Context context, ArrayList<Usuario> usuarios) {
        super(context,0,usuarios);
        this.context = context;
        this.usuarios = usuarios;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Usuario usuario = usuarios.get(position);

        // Verificar si la vista existe, de lo contrario, inflarla
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_usuario,parent,false);
        }

        // Referencias a los elementos del diseño de cada elemento de la lista
        TextView textViewusuario = convertView.findViewById(R.id.ttx_usuario);
        TextView textViewrol = convertView.findViewById(R.id.ttx_rol);
        TextView textcontra= convertView.findViewById(R.id.txtcontrasenas);
        TextView textViewestado = convertView.findViewById(R.id.txtestado);
        Button btnActualizar = convertView.findViewById(R.id.btnactualizar);



        // Asignar los valores del paciente a los elementos de la vista
        textViewusuario.setText(usuario.getUsuario());
        textViewrol.setText(usuario.getRol());

        textViewestado.setText(usuario.getEstado() ? "Activo" : "Inactivo");

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear un Intent para abrir la actividad actualizarPaciente
                // Crear un Intent para abrir la actividad actualizarPaciente
                Intent intent = new Intent(context, actualizarUsuario.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Agregar el flag aquí
                intent.putExtra("id",usuario.getId());
                intent.putExtra("usuario", usuario.getUsuario());
                intent.putExtra("contrasena", usuario.getContrasena());
                intent.putExtra("rol", usuario.getRol());
                intent.putExtra("estado", usuario.getEstado());

                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
