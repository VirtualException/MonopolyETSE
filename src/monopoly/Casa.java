package monopoly;

import partida.Jugador;

public class Casa extends Edificio {

    final float casaMultiplicador = Valor.MULTIPLICADOR_CASA;

    public Casa(Jugador j) {
        super(j, "casa");
        super.setCoste(super.getCasilla().getPrecioOriginal() * casaMultiplicador);
    }
}
