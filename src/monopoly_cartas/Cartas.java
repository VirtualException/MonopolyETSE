package monopoly_cartas;

import java.util.*;

import monopoly_casillas.Casilla;
import monopoly_casillas.acciones.AccionSuerte;
import monopoly_juego.Juego;
import monopoly_jugador.Jugador;
import monopoly_tablero.Tablero;

public class Cartas {
    private String mensaje;
    private GestorCartas gestorCartas;

    public Cartas(int id, String mensaje) {
        this.mensaje = mensaje;
    }
    public Cartas (){
        this.gestorCartas = new GestorCartas();
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }


    public GestorCartas getGestorCartas() {
        return gestorCartas;
    }

}
