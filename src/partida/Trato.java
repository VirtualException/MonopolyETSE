package partida;

import monopoly.*;

public class Trato {

    private String nombre; //Id del trato
    private Jugador jugadorPropone;
    private String trato;
    private boolean aceptado;

    public Trato(String nombre, Jugador jugadorPropone, String trato){
        this.nombre = nombre;
        this.jugadorPropone = jugadorPropone;
        this.trato = trato;
        this.aceptado = false;
    }
    private String crearId(){

    }

    public void proponerTrato(String nombrePropiedad, int cantidadDinero){

    }


    public void aceptarTrato(){

    }


    // Función para listar la información de los tratos de un jugador 
    public void listarTratos(Jugador jugador){
        StringBuilder cadena = new StringBuilder();

        for (Trato trato: jugador.getTratos()){
            cadena.append("{\n");
            cadena.append("\tid: " + trato.getNombre() + "\n");
            cadena.append("\tjugadorPropone: " + trato.getJugadorPropone() + ",\n");
            cadena.append("\ttrato: cambiar " + trato.getTrato() + "\n");
            cadena.append("},\n");
        }
    }



    // GETTERS Y SETTERS

    public String getNombre(){
        return this.nombre;
    }

    public Jugador getJugadorPropone(){
        return this.jugadorPropone;
    }

    public String getTrato(){
        return this.trato;
    }
}



