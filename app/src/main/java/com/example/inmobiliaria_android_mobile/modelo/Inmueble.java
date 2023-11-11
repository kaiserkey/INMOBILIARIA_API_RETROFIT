package com.example.inmobiliaria_android_mobile.modelo;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Objects;

public class Inmueble implements Serializable {

    private int idInmueble;
    private int idPropietario;
    private Propietario propietario;
    private String direccion;
    private String uso;
    private String tipo;
    private int ambientes;
    private String coordenadas;
    private double precio;
    private boolean activo;
    private String foto;

    //imagen de bytes
    private Bitmap fotoFile;


    public Inmueble(int idInmueble, int idPropietario, Propietario propietario, String direccion, String uso, String tipo, int ambientes, String coordenadas, double precio, boolean activo, String foto) {
        this.idInmueble = idInmueble;
        this.idPropietario = idPropietario;
        this.propietario = propietario;
        this.direccion = direccion;
        this.uso = uso;
        this.tipo = tipo;
        this.ambientes = ambientes;
        this.coordenadas = coordenadas;
        this.precio = precio;
        this.activo = activo;
        this.foto = foto;
    }

    public int getId() {
        return idInmueble;
    }

    public void setId(int id) {
        this.idInmueble = id;
    }

    public int getIdPropietario() {
        return idPropietario;
    }

    public void setIdPropietario(int idPropietario) {
        this.idPropietario = idPropietario;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Propietario getPropietario() {
        return propietario;
    }

    public void setPropietario(Propietario propietario) {
        this.propietario = propietario;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getUso() {
        return uso;
    }

    public void setUso(String uso) {
        this.uso = uso;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getCantidadAmbientes() {
        return ambientes;
    }

    public void setCantidadAmbientes(int cantidadAmbientes) {
        this.ambientes = cantidadAmbientes;
    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }

    public double getPrecioInmueble() {
        return precio;
    }

    public void setPrecioInmueble(double precioInmueble) {
        this.precio = precioInmueble;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Bitmap getImagenInmueble() {
        return fotoFile;
    }

    public void setImagenInmueble(Bitmap imagenInmueble) {
        this.fotoFile = imagenInmueble;
    }

    @Override
    public String toString() {
        return "Inmueble{" +
                "idInmueble=" + idInmueble +
                ", idPropietario=" + idPropietario +
                ", propietario=" + propietario +
                ", direccion='" + direccion + '\'' +
                ", uso='" + uso + '\'' +
                ", tipo='" + tipo + '\'' +
                ", ambientes=" + ambientes +
                ", coordenadas='" + coordenadas + '\'' +
                ", precioInmueble=" + precio +
                ", activo='" + activo + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inmueble inmueble = (Inmueble) o;
        return idInmueble == inmueble.idInmueble;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idInmueble);
    }
}
