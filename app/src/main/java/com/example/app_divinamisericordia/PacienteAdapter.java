package com.example.app_divinamisericordia;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PacienteAdapter extends ArrayAdapter<Paciente> implements Filterable {

    private Context context;
    private List<Paciente> listaCompleta; // Lista con todos los elementos (lista original)
    private List<Paciente> pacientes; // Lista que se modifica al realizar el filtrado

    public PacienteAdapter(Context context, List<Paciente> pacientes) {
        super(context, 0, pacientes);
        this.context = context;
        this.listaCompleta = new ArrayList<>(pacientes); // Guardamos la lista original
        this.pacientes = pacientes; // Inicialmente, pacientes es la lista completa
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtener el paciente en la posición actual
        Paciente paciente = pacientes.get(position);

        // Verificar si la vista existe, de lo contrario, inflarla
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_paciente, parent, false);
        }

        // Referencias a los elementos del diseño de cada elemento de la lista
        ImageView imageViewFoto = convertView.findViewById(R.id.imageViewFoto);
        TextView textViewCedula = convertView.findViewById(R.id.txtcedula);
        TextView textViewNombre = convertView.findViewById(R.id.txtnombre);
        TextView textViewApellido = convertView.findViewById(R.id.txtapellido);
        TextView textViewEstado = convertView.findViewById(R.id.txtestado);
        Button btnActualizar = convertView.findViewById(R.id.btnactualizar);

        // Decodificar la foto desde Base64 y mostrarla en el ImageView
        if (paciente.getFoto() != null) {
            byte[] decodedBytes = Base64.decode(paciente.getFoto(), Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            imageViewFoto.setImageBitmap(decodedBitmap);
        }

        // Asignar los valores del paciente a los elementos de la vista
        textViewCedula.setText(paciente.getCedula());
        textViewNombre.setText(paciente.getNombres());
        textViewApellido.setText(paciente.getApellidos());
        textViewEstado.setText(paciente.getEstado() ? "Activo" : "Inactivo");

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear un Intent para abrir la actividad actualizarPaciente
                Intent intent = new Intent(context, actualizarPaciente.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Agregar el flag aquí
                intent.putExtra("id", paciente.getId());
                intent.putExtra("cedula", paciente.getCedula());
                intent.putExtra("nombres", paciente.getNombres());
                intent.putExtra("apellidos", paciente.getApellidos());
                intent.putExtra("estado", paciente.getEstado());
                String fotoBase64 = paciente.getFoto();
                if (fotoBase64 != null && !fotoBase64.isEmpty()) {
                    intent.putExtra("foto", fotoBase64);
                }
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<Paciente> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(listaCompleta); // Si no hay texto de búsqueda, mostrar la lista completa
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Paciente paciente : listaCompleta) {
                        if (paciente.getCedula().toLowerCase().contains(filterPattern)
                                || paciente.getNombres().toLowerCase().contains(filterPattern)
                                || paciente.getApellidos().toLowerCase().contains(filterPattern)) {
                            filteredList.add(paciente);
                        }
                    }
                }

                results.values = filteredList;
                results.count = filteredList.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                pacientes.clear();
                pacientes.addAll((ArrayList<Paciente>) results.values);
                notifyDataSetChanged();
            }
        };
    }
}
