package partida;

import monopoly.*;
import java.util.ArrayList;

public class Trato {

    private String id; //Id del trato
    private int numeroDeTrato;
    private Jugador jugadorPropone;
    private Jugador jugadorRecibe;
    private String trato;
    private Casilla propiedad1;
    private Casilla propiedad2;
    private float cantidad1;
    private float cantidad2;
    private Boolean aceptado;

    public Trato1(ArrayList<Trato> tratos, Jugador jugadorPropone, Jugador jugadorRecibe, String trato, Casilla propiedad1, Casilla propiedad2){
        this.id = crearId(tratos);
        this.jugadorPropone = jugadorPropone;
        this.jugadorRecibe = jugadorRecibe;
        this.trato = trato;
        this.aceptado = null;
    }
    private String crearId(ArrayList<Trato> tratos){
        String id = "";

        id = "trato" + String.valueOf(tratos.get(tratos.size() - 1).getNumeroDeTrato() + 1);
        this.numeroDeTrato = tratos.get(tratos.size() - 1).getNumeroDeTrato() + 1;
        
        return id;
    }

    public void proponerTrato(String nombrePropiedad, int cantidadDinero){

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

    public Jugador getJugadorPropone(){
        return this.jugadorPropone;
    }

    public Jugador getJugadorRecibe(){
        return this.jugadorRecibe;
    }

    public String getTrato(){
        return this.trato;
    }

    public Boolean getAceptado(){
        return this.aceptado;
    }
}



