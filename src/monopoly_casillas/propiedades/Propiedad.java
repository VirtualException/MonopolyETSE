package monopoly_casillas.propiedades;

import monopoly_casillas.Casilla;
import monopoly_jugador.Jugador;

public abstract class Propiedad extends Casilla {
    public Propiedad(String nombre, String tipo, int posicion, float valor, Jugador duenho) {
        super(nombre, tipo, posicion, valor, duenho);
    }
}
