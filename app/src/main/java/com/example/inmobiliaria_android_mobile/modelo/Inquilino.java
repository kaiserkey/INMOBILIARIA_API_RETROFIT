package com.example.inmobiliaria_android_mobile.modelo;

import java.io.Serializable;
import java.util.Date;

public class Inquilino implements Serializable {

    private int idInquilino;
    private String dni;
    private String nombre;
    private String apellido;
    private String lugarDeTrabajo;
    private String email;
    private String telefono;
    private String fechaNac;


    public Inquilino() {}

    public Inquilino(int idInquilino, String dni, String nombre, String apellido, String email, String telefono, String fechaNac) {
        this.idInquilino = idInquilino;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.fechaNac = fechaNac;

    }

    public int getId() {
        return idInquilino;
    }

    public void setId(int id) {
        this.idInquilino = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getLugarDeTrabajo() {
        return lugarDeTrabajo;
    }

    public void setLugarDeTrabajo(String lugarDeTrabajo) {
        this.lugarDeTrabajo = lugarDeTrabajo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(String fechaNac) {
        this.fechaNac = fechaNac;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

}
