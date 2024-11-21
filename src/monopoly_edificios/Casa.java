package monopoly_edificios;

import monopoly_tablero.Valor;
import monopoly_jugador.Jugador;

public class Casa extends Edificio {

    final float casaMultiplicador = Valor.MULTIPLICADOR_CASA;

    public Casa(Jugador j) {
        super(j, "casa");
        super.setCoste(super.getCasilla().getPrecioOriginal() * casaMultiplicador);
    }
}
