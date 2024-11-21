package monopoly_edificios;

import monopoly_tablero.Valor;
import monopoly_jugador.Jugador;

public class Hotel extends Edificio {

    final float hotelMultiplicador = Valor.MULTIPLICADOR_HOTEL;

    public Hotel(Jugador j) {
        super(j, "hotel");
        super.setCoste(super.getCasilla().getPrecioOriginal() * hotelMultiplicador);
    }
}
