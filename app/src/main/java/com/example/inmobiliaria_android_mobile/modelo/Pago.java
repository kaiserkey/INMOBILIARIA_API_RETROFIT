package com.example.inmobiliaria_android_mobile.modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

public class Pago implements Serializable {

    private int idPago;
    private int idContrato;
    private Contrato contrato;
    private int numeroPago;
    private String fecha;
    private double importe;

    public Pago() {
    }

    public Pago(int idPago, int idContrato, Contrato contrato, int numeroPago, String fecha, double importe) {
        this.idPago = idPago;
        this.idContrato = idContrato;
        this.contrato = contrato;
        this.numeroPago = numeroPago;
        this.fecha = fecha;
        this.importe = importe;
    }

    public int getId() {
        return idPago;
    }

    public void setId(int id) {
        this.idPago = id;
    }

    public int getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(int idContrato) {
        this.idContrato = idContrato;
    }

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }

    public int getNumDePago() {
        return numeroPago;
    }

    public void setNumDePago(int numDePago) {
        this.numeroPago = numDePago;
    }

    public String getFechaDePago() {
        return fecha;
    }

    public void setFechaDePago(String fecha) {
        this.fecha = fecha;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    @Override
    public String toString() {
        return "Pago{" +
                "idPago=" + idPago +
                ", idContrato=" + idContrato +
                ", contrato=" + contrato +
                ", numeroPago=" + numeroPago +
                ", fecha='" + fecha + '\'' +
                ", importe=" + importe +
                '}';
    }
}
