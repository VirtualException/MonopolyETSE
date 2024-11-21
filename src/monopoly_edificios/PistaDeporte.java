package monopoly_edificios;

import monopoly.Valor;
import partida.Jugador;

public class PistaDeporte extends Edificio {

    final float pistaMultiplicador = Valor.MULTIPLICADOR_PISTA_DE_DEPORTE;

    public PistaDeporte(Jugador j) {
        super(j, "pista");
        super.setCoste(super.getCasilla().getPrecioOriginal() * pistaMultiplicador);
    }
}
