package monopoly_tratos;

import monopoly_casillas.Casilla;
import monopoly_exception.propiedades.PropiedadException;
import monopoly_exception.tratos.TratoException;
import monopoly_juego.Juego;
import monopoly_jugador.Jugador;

import java.util.ArrayList;

public class Trato {

    private String id; //Id del trato
    private int numeroDeTrato;
    private Jugador proponente;
    private Jugador destinatario;
    private Casilla propiedadOfrecida;
    private Casilla propiedadSolicitada;
    private float cantidadOfrecida;
    private float cantidadSolicitada;
    private Boolean aceptado;


    public Trato(ArrayList<Trato> tratos, Jugador proponente, Jugador destinatario, Casilla propiedadOfrecida, Casilla propiedadSolicitada, float cantidadOfrecida, float cantidadSolicitada){
        this.id = crearId(tratos);
        this.proponente = proponente;
        this.destinatario = destinatario;
        this.propiedadOfrecida = propiedadOfrecida;
        this.propiedadSolicitada = propiedadSolicitada;
        this.cantidadOfrecida = cantidadOfrecida;
        this.cantidadSolicitada = cantidadSolicitada;
        this.aceptado = null;
    }


    // Método que crea el id de un trato
    private String crearId(ArrayList<Trato> tratos){
        if (tratos.isEmpty()) {
            this.numeroDeTrato = 1;
            return "trato1";
        }
        this.numeroDeTrato = tratos.get(tratos.size() - 1).getNumeroDeTrato() + 1;
        return id + this.numeroDeTrato;
    }


    // Método para añadir un trato al arraylist de tratos
    public void anhadirTrato(ArrayList<Trato> tratos, Trato trato) throws TratoException{
        if (tratos.contains(trato)) {
            throw new TratoException("ERROR. El trato ya existe.");
        } else {
            tratos.add(trato);
        }
    }


    // Método que verifica si el trato propuesto es válido o no
    public boolean verificarTrato(){
        
        if (propiedadOfrecida != null && !proponente.getPropiedades().contains(propiedadOfrecida)) {
            Juego.consola.imprimir("No se puede proponer el trato: " + propiedadOfrecida.getNombre() + " no pertenece a " + proponente.getNombre() + ".");
            return false;
        }

        if (propiedadSolicitada != null && !destinatario.getPropiedades().contains(propiedadSolicitada)) {
            Juego.consola.imprimir("No se puede proponer el trato: " + propiedadSolicitada.getNombre() + " no pertenece a " + destinatario.getNombre() + ".");
            return false;
        }
        return true;
    }


    // Método que imprime el trato propuesto
    public String mostrarTratoPropuesto(){  //Revisar
        String cadena = (destinatario.getNombre() + ", ¿te doy ");

        if (propiedadOfrecida != null) {
            cadena += (propiedadOfrecida.getNombre() + ",");
        } 

        if (cantidadOfrecida > 0) {
            cadena += (cantidadOfrecida + "€ ");
        }

        cadena += ("y tú me das ");

        if (propiedadSolicitada != null && cantidadSolicitada > 0) {
            cadena += (propiedadSolicitada.getNombre() + " y " + cantidadSolicitada + "€");
        }
        else {
            if (propiedadSolicitada != null) {
                cadena += (propiedadSolicitada.getNombre());
            }

            if (cantidadSolicitada > 0) {
                cadena += (cantidadSolicitada + "€");
            }
        }
        cadena += ("?");
        return cadena;
    }


    public void cambiarPropiedadPorPropiedad(){

        if (!this.proponente.getPropiedades().contains(propiedadOfrecida)) {
            Juego.consola.imprimir("El trato no se puede aceptar: El jugador proponente " + this.proponente.getNombre() + " no posee " + propiedadOfrecida.getNombre() + ".");
            return;
        } 
        if (!this.destinatario.getPropiedades().contains(propiedadSolicitada)) {
            Juego.consola.imprimir("El trato no se puede aceptar: El jugador destinatario " + this.destinatario.getNombre() + " no posee " + propiedadSolicitada.getNombre() + ".");
            return;
        }

        if(propiedadOfrecida != null && !propiedadOfrecida.getEdificios().isEmpty()){
            Juego.consola.imprimir("La propiedad ofrecida no puede tener edificios.");
            return;
        }

        if(propiedadSolicitada != null && !propiedadSolicitada.getEdificios().isEmpty()){
            Juego.consola.imprimir("La propiedad solicitada no puede tener edificios.");
            return;
        }


        try {
            this.proponente.eliminarPropiedad(propiedadOfrecida);
        } catch (PropiedadException e) {
            Juego.consola.imprimir("Error: " + e.getMessage()); // Manejo del error
        }
        this.proponente.anhadirPropiedad(propiedadSolicitada);
        try {
            this.destinatario.eliminarPropiedad(propiedadSolicitada);
        } catch (PropiedadException e) {
            Juego.consola.imprimir("Error: " + e.getMessage()); // Manejo del error
        }
        this.destinatario.anhadirPropiedad(propiedadOfrecida);

        Juego.consola.imprimir("Se ha aceptado el siguiente trato con " + this.proponente.getNombre() + ": le doy " + propiedadSolicitada.getNombre() + " y " + this.proponente.getNombre() + " me da " + propiedadOfrecida.getNombre() + ".");
    }


    public void cambiarPropiedadPorDinero() {

        if (!this.proponente.getPropiedades().contains(propiedadOfrecida)) {
            Juego.consola.imprimir("El trato no se puede aceptar: El jugador proponente " + this.proponente.getNombre() + " no posee " + propiedadOfrecida.getNombre() + ".");
            return;
        } 
        if (this.destinatario.getFortuna() < cantidadSolicitada) {
            Juego.consola.imprimir("El trato no puede ser aceptado: El jugador destinatario " + this.destinatario.getNombre() + " no tiene " + cantidadSolicitada + "€" + ". Se conserva el trato.");
            return;
        }

        if(propiedadOfrecida != null && !propiedadOfrecida.getEdificios().isEmpty()){
            Juego.consola.imprimir("La propiedad ofrecida no puede tener edificios.");
            return;
        }

        this.proponente.sumarFortuna(cantidadSolicitada);
        try {
            this.proponente.eliminarPropiedad(propiedadOfrecida);
        } catch (PropiedadException e) {
            Juego.consola.imprimir("Error: " + e.getMessage()); // Manejo del error
        }
        this.destinatario.anhadirPropiedad(propiedadOfrecida);
        this.destinatario.sumarFortuna(-cantidadSolicitada);
        this.destinatario.sumarGastos(cantidadSolicitada);

        Juego.consola.imprimir("Se ha aceptado el siguiente trato con " + this.proponente.getNombre() + ": le doy " + cantidadSolicitada + "€  y " + this.proponente.getNombre() + " me da " + propiedadOfrecida.getNombre() + ".");
    }


    public void cambiarDineroPorPropiedad() {
        
        if (this.proponente.getFortuna() < cantidadOfrecida) {
            Juego.consola.imprimir("El trato no se puede aceptar: El jugador proponente " + this.proponente.getNombre() + " no tiene " + cantidadOfrecida + "€.");
            return;
        } 
        if (!this.destinatario.getPropiedades().contains(propiedadSolicitada)) {
            Juego.consola.imprimir("El trato no se puede aceptar: El jugador destinatario " + this.destinatario.getNombre() + " no posee " + propiedadSolicitada.getNombre() + ".");
            return;
        }

        if(propiedadSolicitada != null && !propiedadSolicitada.getEdificios().isEmpty()){
            Juego.consola.imprimir("La propiedad solicitada no puede tener edificios.");
            return;
        }

        this.proponente.sumarFortuna(-cantidadOfrecida);
        this.proponente.sumarGastos(cantidadOfrecida);
        this.proponente.anhadirPropiedad(propiedadSolicitada);
        try {
            this.destinatario.eliminarPropiedad(propiedadSolicitada);
        } catch (PropiedadException e) {
            Juego.consola.imprimir("Error: " + e.getMessage()); // Manejo del error
        }
        this.destinatario.sumarFortuna(cantidadOfrecida);

        Juego.consola.imprimir("Se ha aceptado el siguiente trato con " + this.proponente.getNombre() + ": le doy " + propiedadSolicitada.getNombre() + " y " + this.proponente.getNombre() + " me da " + cantidadOfrecida + "€.");
    }


    public void cambiarPropiedadPorPropiedadYDinero() {
        if (!this.proponente.getPropiedades().contains(propiedadOfrecida)) {
            Juego.consola.imprimir("El trato no se puede aceptar: El jugador proponente " + this.proponente.getNombre() + " no posee " + propiedadOfrecida.getNombre() + ".");
            return;
        } 
        if (!this.destinatario.getPropiedades().contains(propiedadSolicitada)) {
            Juego.consola.imprimir("El trato no se puede aceptar: El jugador destinatario " + this.destinatario.getNombre() + " no posee " + propiedadSolicitada.getNombre() + ".");
            return;
        }
        if (this.destinatario.getFortuna() < cantidadSolicitada) {
            Juego.consola.imprimir("El trato no se puede aceptar: El jugador destinatario " + this.destinatario.getNombre() + " no tiene " + cantidadSolicitada + "€. Se conserva el trato.");
            return;
        }

        if(propiedadOfrecida != null && !propiedadOfrecida.getEdificios().isEmpty()){
            Juego.consola.imprimir("La propiedad ofrecida no puede tener edificios.");
            return;
        }

        if(propiedadSolicitada != null && !propiedadSolicitada.getEdificios().isEmpty()){
            Juego.consola.imprimir("La propiedad solicitada no puede tener edificios.");
            return;
        }


        try {
            this.proponente.eliminarPropiedad(propiedadOfrecida);
        } catch (PropiedadException e) {
            Juego.consola.imprimir("Error: " + e.getMessage()); // Manejo del error
        }
        this.proponente.anhadirPropiedad(propiedadSolicitada);
        this.proponente.sumarFortuna(cantidadSolicitada);
        try {
            this.destinatario.eliminarPropiedad(propiedadSolicitada);
        } catch (PropiedadException e) {
            Juego.consola.imprimir("Error: " + e.getMessage()); // Manejo del error
        }
        this.destinatario.anhadirPropiedad(propiedadOfrecida);
        this.destinatario.sumarFortuna(-cantidadSolicitada);
        this.destinatario.sumarGastos(cantidadSolicitada);

        Juego.consola.imprimir("Se ha aceptado el siguiente trato con " + this.proponente.getNombre() + ": le doy " + propiedadSolicitada.getNombre() + " y " + cantidadSolicitada +  "€, " + this.proponente.getNombre() + " me da " + propiedadOfrecida.getNombre() + ".");
    }


    public void cambiarPropiedadYDineroPorPropiedad() {
        if (!this.proponente.getPropiedades().contains(propiedadOfrecida)) {
            Juego.consola.imprimir("El trato no puede ser aceptado: El jugador proponente " + this.proponente.getNombre() + " no posee " + propiedadOfrecida.getNombre() + ".");
            return;
        }
        if (this.proponente.getFortuna() < cantidadOfrecida) {
            Juego.consola.imprimir("El trato no puede ser aceptado: El jugador proponente " + this.proponente.getNombre() + " no tiene " + cantidadOfrecida + "€.");
            return;
        }
        if (!this.destinatario.getPropiedades().contains(propiedadSolicitada)) {
            Juego.consola.imprimir("El trato no puede ser aceptado: El jugador destinatario " + this.destinatario.getNombre() + " no posee " + propiedadSolicitada.getNombre() + ".");
            return;
        }

        if(propiedadOfrecida != null && !propiedadOfrecida.getEdificios().isEmpty()){
            Juego.consola.imprimir("La propiedad ofrecida no puede tener edificios.");
            return;
        }

        if(propiedadSolicitada != null && !propiedadSolicitada.getEdificios().isEmpty()){
            Juego.consola.imprimir("La propiedad solicitada no puede tener edificios.");
            return;
        }


        try {
            this.proponente.eliminarPropiedad(propiedadOfrecida);
        } catch (PropiedadException e) {
            Juego.consola.imprimir("Error: " + e.getMessage()); // Manejo del error
        }
        this.proponente.sumarFortuna(-cantidadOfrecida);
        this.proponente.sumarGastos(cantidadOfrecida);
        this.proponente.anhadirPropiedad(propiedadSolicitada);
        try {
            this.destinatario.eliminarPropiedad(propiedadSolicitada);
        } catch (PropiedadException e) {
            Juego.consola.imprimir("Error: " + e.getMessage()); // Manejo del error
        }
        this.destinatario.sumarFortuna(cantidadOfrecida);
        this.destinatario.anhadirPropiedad(propiedadOfrecida);

        Juego.consola.imprimir("Se ha aceptado el siguiente trato con " + this.proponente.getNombre() + ": le doy " + propiedadSolicitada.getNombre() + " y " + this.proponente.getNombre() + " me da " + propiedadOfrecida.getNombre() + " y " + cantidadOfrecida + "€.");
    }





    // GETTERS Y SETTERS

    public String getId(){
        return this.id;
    }

    public int getNumeroDeTrato(){
        return this.numeroDeTrato;
    }

    public Jugador getProponente(){
        return this.proponente;
    }

    public Jugador getDestinatario(){
        return this.destinatario;
    }

    public Casilla getPropiedadOfrecida(){
        return this.propiedadOfrecida;
    }

    public Casilla getPropiedadSolicitada(){
        return this.propiedadSolicitada;
    }

    public float getCantidadOfrecida(){
        return this.cantidadOfrecida;
    }

    public float getCantidadSolicitada(){
        return this.cantidadSolicitada;
    }

    public Boolean getAceptado(){
        return this.aceptado;
    }

    public void setAceptado(Boolean aceptado){  
        this.aceptado = aceptado;
    }

    public void setPropiedadOfrecida(Casilla propiedadOfrecida){
        this.propiedadOfrecida = propiedadOfrecida;
    }

    public void setPropiedadSolicitada(Casilla propiedadSolicitada){
        this.propiedadSolicitada = propiedadSolicitada;
    }   

    public void setCantidadOfrecida(float cantidadOfrecida){
        this.cantidadOfrecida = cantidadOfrecida;
    }

    public void setCantidadSolicitada(float cantidadSolicitada){
        this.cantidadSolicitada = cantidadSolicitada;
    }

    public void setProponente(Jugador proponente){
        this.proponente = proponente;
    }

    public void setDestinatario(Jugador destinatario){
        this.destinatario = destinatario;
    }

    public void setNumeroDeTrato(int numeroDeTrato){
        this.numeroDeTrato = numeroDeTrato;
    }

    public void setId(String id){
        this.id = id;
    }
}