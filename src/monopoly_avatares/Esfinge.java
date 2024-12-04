package monopoly_avatares;

import monopoly_casillas.Casilla;
import monopoly_exception.avatares.AvatarNoValidoException;
import monopoly_juego.Juego;
import monopoly_jugador.Jugador;
import monopoly_tablero.Tablero;
import monopoly_tablero.Valor;

import java.util.ArrayList;

public class Esfinge extends Avatar{
    public Esfinge(Jugador jugador, Casilla lugar, ArrayList<Avatar> avCreados) throws AvatarNoValidoException {
        super("Esfinge", jugador, lugar, avCreados);
    }

    @Override
    public void moverEnAvanzado(Tablero tab, int tirada, ArrayList<Jugador> jugadores) {
        Juego.consola.imprimir("Ester avatar es una esfinge, por lo que no tiene movimiento avanzado");
    }
}
