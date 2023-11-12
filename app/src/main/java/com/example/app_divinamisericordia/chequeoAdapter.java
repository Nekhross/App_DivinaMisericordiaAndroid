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

public class chequeoAdapter extends ArrayAdapter<chequeoMedico> {
    private Context context;
    private ArrayList<chequeoMedico> chequeoMedicos;

    public chequeoAdapter(Context context, ArrayList<chequeoMedico> chequeoMedico) {
        super(context, 0, chequeoMedico);
        this.context = context;
        this.chequeoMedicos = chequeoMedico;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtener el paciente en la posición actual
        chequeoMedico chequeomedico = chequeoMedicos.get(position);

        // Verificar si la vista existe, de lo contrario, inflarla
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_chequeo, parent, false);
        }

        // Referencias a los elementos del diseño de cada elemento de la lista

        TextView textViewCedula = convertView.findViewById(R.id.txtcedula);
        TextView textViewNombre = convertView.findViewById(R.id.txtnombre);
        TextView textViewApellido = convertView.findViewById(R.id.txtapellido);

        Button btnActualizar = convertView.findViewById(R.id.btnmedicamento);


        // Asignar los valores del paciente a los elementos de la vista
        textViewCedula.setText(chequeomedico.getCedula());
        textViewNombre.setText(chequeomedico.getNombres());
        textViewApellido.setText(chequeomedico.getApellidos());




        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear un Intent para abrir la actividad actualizarPaciente
                Intent intent = new Intent(context, revisionChequeo.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Agregar el flag aquí
                intent.putExtra("id", chequeomedico.getId());
                intent.putExtra("fecha", chequeomedico.getFecha());
                intent.putExtra("hora", chequeomedico.getHora());
                intent.putExtra("nota", chequeomedico.getNota());
                intent.putExtra("diagnostico", chequeomedico.getDiagnostico());


                context.startActivity(intent);


            }
        });


        return convertView;
    }
}