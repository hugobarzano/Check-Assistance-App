package com.pdm.check_assistance_master;

/**
 * Created by hugo on 10/05/16.
 */
public class Alumno {
    private String nombre;
    private String dni;
    private String asistencia;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAsistencia() {
        return asistencia;
    }

    public void setAsistencia(String asistencia) {
        this.asistencia = asistencia;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }


    public Alumno(String nombre, String dni, String asistencia) {
        this.nombre = nombre;
        this.dni=dni;
        this.asistencia=asistencia;
    }

}
