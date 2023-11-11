package com.example.inmobiliaria_android_mobile.request;

import com.example.inmobiliaria_android_mobile.R;
import com.example.inmobiliaria_android_mobile.modelo.*;

import java.util.ArrayList;


public class ApiClient {
    private ArrayList<Propietario> propietarios = new ArrayList<>();
    private ArrayList<Inquilino> inquilinos = new ArrayList<>();
    private ArrayList<Inmueble> inmuebles = new ArrayList<>();
    private ArrayList<Contrato> contratos = new ArrayList<>();
    private ArrayList<Pago> pagos = new ArrayList<>();
    private static Propietario usuarioActual = null;
    private static ApiClient api = null;

    private ApiClient() {
        //Nos conectamos a nuestra "Base de Datos"
        cargaDatos();
    }

    //Método para crear una instancia de ApiClient
    public static ApiClient getApi() {
        if (api == null) {
            api = new ApiClient();
        }
        return api;

    }


    //Servicios
    //Para que pueda iniciar sesion
    public Propietario login(String mail, final String password) {
        for (Propietario propietario : propietarios) {
            if (propietario.getEmail().equals(mail) && propietario.getClave().equals(password)) {
                usuarioActual = propietario;
                return propietario;
            }
        }
        return null;
    }


    //Retorna el usuario que inició Sesión
    public Propietario obtenerUsuarioActual() {
        return usuarioActual;
    }

    //Retorna todas las propiedades del usuario propietario logueado
    public ArrayList<Inmueble> obtnerPropiedades() {
        ArrayList<Inmueble> temp = new ArrayList<>();
        for (Inmueble inmueble : inmuebles) {
            if (inmueble.getPropietario().equals(usuarioActual)) {
                temp.add(inmueble);
            }
        }
        return temp;
    }

    //Lista de inmuebles alquilados actualmente del propietario logueado.
    public ArrayList<Inmueble> obtenerPropiedadesAlquiladas() {
        ArrayList<Inmueble> temp = new ArrayList<>();
        for (Contrato contrato : contratos) {
            if (contrato.getInmueble().getPropietario().equals(usuarioActual)) {
                temp.add(contrato.getInmueble());
            }
        }
        return temp;
    }


//Dado un inmueble retorna el contrato activo de dicho inmueble

    public Contrato obtenerContratoVigente(Inmueble inmueble) {

        for (Contrato contrato : contratos) {
            if (contrato.getInmueble().equals(inmueble)) {
                return contrato;
            }
        }
        return null;
    }

    //Dado un inmueble, retorna el inquilino del ultimo contrato activo de ese inmueble.
    public Inquilino obtenerInquilino(Inmueble inmueble) {
        for (Contrato contrato : contratos) {
            if (contrato.getInmueble().equals(inmueble)) {
                return contrato.getInquilino();
            }
        }
        return null;
    }

    //Dado un Contrato, retorna los pagos de dicho contrato
    public ArrayList<Pago> obtenerPagos(Contrato contratoVer) {
        ArrayList<Pago> temp = new ArrayList<>();
        for (Contrato contrato : contratos) {
            if (contrato.equals(contratoVer)) {
                for (Pago pago : pagos) {
                    if (pago.getContrato().equals(contrato)) {
                        temp.add(pago);
                    }
                }
            }
            break;
        }
        return temp;
    }

    //Actualizar Perfil
    public void actualizarPerfil(Propietario propietario) {
        int posición = propietarios.indexOf(propietario);
        if (posición != -1) {
            propietarios.set(posición, propietario);
        }
    }

    //ActualizarInmueble
    public void actualizarInmueble(Inmueble inmueble) {
        int posicion = inmuebles.indexOf(inmueble);
        if (posicion != -1) {
            inmuebles.set(posicion, inmueble);
        }
    }

    private void cargaDatos() {



    }
}
