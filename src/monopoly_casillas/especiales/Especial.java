package monopoly_casillas.especiales;

import monopoly_casillas.Casilla;
import monopoly_juego.Juego;
import monopoly_jugador.Jugador;
import monopoly_tablero.Tablero;

import java.util.ArrayList;

public class Especial extends Casilla {
    public Especial(String nombre, String tipo, int posicion, Jugador banca) {
        super(nombre, tipo, posicion, banca);
    }

    @Override
    public void evaluarCasilla(Tablero tab, Jugador jugador, Jugador banca, ArrayList<Jugador> jugadores) {

        String tipoCasilla = this.getTipo();

        /* El jugador cayó una vez más en esta casilla */
        this.contarCaer[jugador.getIndice()]++;

        Juego.consola.imprimir("Evaluando tipo especial");
        /* Cae en IrCárcel */
        if (nombre.equals("IrCarcel")) {
            jugador.encarcelar(tab.getPosiciones());
            jugador.incrementarVecesEnCarcel();
        }
        /* Cae en Parking */
        else if (nombre.equals("Parking")) {
            float bote = this.getValor();
            jugador.sumarFortuna(bote);
            jugador.setPremiosInversionesOBote(jugador.getPremiosInversionesOBote() + bote);
            this.setValor(0);
        }
    }
}
