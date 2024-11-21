package monopoly_edificios;

import monopoly_tablero.Valor;
import monopoly_jugador.Jugador;

public class Piscina extends Edificio {

    final float piscinaMultiplicador = Valor.MULTIPLICADOR_HOTEL;

    public Piscina(Jugador j) {
        super(j, "piscina");
        super.setCoste(super.getCasilla().getPrecioOriginal() * piscinaMultiplicador);
    }
}
