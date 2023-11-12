package com.example.app_divinamisericordia;

public class chequeoMedico {
    private String id;
    private String fecha;
    private String hora;
    private String nota;
    private String idpaciente;
    private String diagnostico;
    private String nombres;
    private String apellidos;
    private String cedula;

    public chequeoMedico() {
    }

    public chequeoMedico(String id, String fecha, String hora, String nota, String idpaciente, String diagnostico, String nombres, String apellidos, String cedula) {
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.nota = nota;
        this.idpaciente = idpaciente;
        this.diagnostico = diagnostico;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.cedula = cedula;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getIdpaciente() {
        return idpaciente;
    }

    public void setIdpaciente(String idpaciente) {
        this.idpaciente = idpaciente;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
}
