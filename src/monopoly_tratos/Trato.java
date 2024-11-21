package monopoly_tratos;

import monopoly_casillas.Casilla;
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
        String id = "";

        id = "trato" + String.valueOf(tratos.get(tratos.size() - 1).getNumeroDeTrato() + 1);
        this.numeroDeTrato = tratos.get(tratos.size() - 1).getNumeroDeTrato() + 1;
        
        return id;
    }


    // Método que verifica si el trato propuesto es válido o no
    public boolean verificarTrato(){
        
        if (propiedadOfrecida != null && !proponente.getPropiedades().contains(propiedadOfrecida)) {
            System.out.println("No se puede proponer el trato: " + propiedadOfrecida.getNombre() + " no pertenece a " + proponente.getNombre() + ".");
            return false;
        }

        if (propiedadSolicitada != null && !destinatario.getPropiedades().contains(propiedadSolicitada)) {
            System.out.println("No se puede proponer el trato: " + propiedadSolicitada.getNombre() + " no pertenece a " + destinatario.getNombre() + ".");
            return false;
        }
        return true;
    }


    // Método que imprime el trato propuesto
    public String mostrarTratoPropuesto(){
        String cadena = (destinatario.getNombre() + ", ¿te doy ");

        if (propiedadOfrecida != null) {
            cadena += (propiedadOfrecida.getNombre() + ",");
        } 

        if (cantidadOfrecida > 0) {
            cadena += (cantidadOfrecida + "€ ");
        }

        cadena += ("y tú me das ");

        if (propiedadSolicitada != null) {
            cadena += (propiedadSolicitada.getNombre() + ",");
        }

        if (cantidadSolicitada > 0) {
            cadena += (cantidadSolicitada + "€");
        }

        cadena += ("?");
        return cadena;
    }


    public void aceptarTrato(){

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

    public float getCantidad1(){
        return this.cantidadOfrecida;
    }

    public float getCantidad2(){
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

    public void setCantidad1(float cantidadOfrecida){
        this.cantidadOfrecida = cantidadOfrecida;
    }

    public void setCantidad2(float cantidadSolicitada){
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