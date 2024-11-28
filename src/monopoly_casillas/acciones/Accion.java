package monopoly_casillas.acciones;

import monopoly_casillas.Casilla;
import monopoly_jugador.Jugador;

public abstract class Accion extends Casilla {
    public Accion(String nombre, String tipo, int posicion, Jugador banca) {
        super(nombre, tipo, posicion, banca);
    }
}
