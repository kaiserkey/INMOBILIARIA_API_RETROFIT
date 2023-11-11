package com.example.inmobiliaria_android_mobile.modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

public class Contrato implements Serializable {

    private int idContrato;
    private int idInquilino;
    private Inquilino inquilino;
    private int idInmueble;
    private Inmueble inmueble;
    private String fechaInicio;
    private String fechaFin;
    private double montoAlquilerMensual;
    private boolean activo;

    public Contrato() {
    }

    public Contrato(int idContrato, int idInquilino, Inquilino inquilino, int idInmueble, Inmueble inmueble, String fechaInicio, String fechaFin, double montoAlquilerMensual, boolean activo) {
        this.idContrato = idContrato;
        this.idInquilino = idInquilino;
        this.inquilino = inquilino;
        this.idInmueble = idInmueble;
        this.inmueble = inmueble;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.montoAlquilerMensual = montoAlquilerMensual;
        this.activo = activo;
    }

    public int getId() {
        return idContrato;
    }

    public void setId(int idContrato) {
        this.idContrato = idContrato;
    }

    public int getIdInquilino() {
        return idInquilino;
    }

    public void setIdInquilino(int idInquilino) {
        this.idInquilino = idInquilino;
    }

    public Inquilino getInquilino() {
        return inquilino;
    }

    public void setInquilino(Inquilino inquilino) {
        this.inquilino = inquilino;
    }

    public int getIdInmueble() {
        return idInmueble;
    }

    public void setIdInmueble(int idInmueble) {
        this.idInmueble = idInmueble;
    }

    public Inmueble getInmueble() {
        return inmueble;
    }

    public void setInmueble(Inmueble inmueble) {
        this.inmueble = inmueble;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFinalizacion() {
        return fechaFin;
    }

    public void setFechaFinalizacion(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public double getMontoAlquilerMensual() {
        return montoAlquilerMensual;
    }

    public void setMontoAlquilerMensual(double montoAlquilerMensual) {
        this.montoAlquilerMensual = montoAlquilerMensual;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }


    @Override
    public String toString() {
        return "Contrato{" +
                "idContrato=" + idContrato +
                ", idInquilino=" + idInquilino +
                ", inquilino=" + inquilino +
                ", idInmueble=" + idInmueble +
                ", inmueble=" + inmueble +
                ", fechaInicio='" + fechaInicio + '\'' +
                ", fechaFin='" + fechaFin + '\'' +
                ", montoAlquilerMensual=" + montoAlquilerMensual +
                ", activo=" + activo +
                '}';
    }
}
