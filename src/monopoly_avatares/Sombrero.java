package monopoly_avatares;

import monopoly_casillas.Casilla;
import monopoly_exception.avatares.AvatarNoValidoException;
import monopoly_juego.Juego;
import monopoly_jugador.Jugador;
import monopoly_tablero.Tablero;

import java.util.ArrayList;

public class Sombrero extends Avatar{
    public Sombrero(Jugador jugador, Casilla lugar, ArrayList<Avatar> avCreados) throws AvatarNoValidoException {
        super("Sombrero", jugador, lugar, avCreados);
    }

    @Override
    public void moverEnAvanzado(Tablero tab, int tirada, ArrayList<Jugador> jugadores) {
        Juego.consola.imprimir("Ester avatar es una sombrero, por lo que no tiene movimiento avanzado");
    }
}
