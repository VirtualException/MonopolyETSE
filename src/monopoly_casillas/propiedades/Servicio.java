package monopoly_casillas.propiedades;

import monopoly_juego.Juego;
import monopoly_jugador.Jugador;
import monopoly_tablero.Tablero;

import java.util.ArrayList;

public class Servicio extends Propiedad {

    public Servicio(String nombre, String tipo, int posicion, float valor, Jugador duenho) {
        super(nombre, tipo, posicion, valor, duenho);
    }

    @Override
    public void evaluarCasilla(Tablero tab, Jugador jugador, Jugador banca, ArrayList<Jugador> jugadores) {

        /* El jugador cayó una vez más en esta casilla */
        this.contarCaer[jugador.getIndice()]++;

        /* Si no hay dueño */
        if (duenho == banca || duenho == jugador) {
            return;
        }

        if (jugador.getFortuna() < valor) {
            Juego.consola.imprimir("El jugador " + jugador.getNombre() + " no tiene suficiente dinero para pagar el servicio. El jugador ahora tiene una deuda y debe solucionarla.");
            jugador.setDeuda(valor);
            return;
        }

        Juego.consola.imprimir("El jugador " + jugador.getNombre() + " paga el servicio por " + valor + "€.");
        jugador.sumarGastos(valor);
        jugador.sumarFortuna(-valor);
        jugador.setPagoTasasEImpuestos(jugador.getPagoTasasEImpuestos() + valor);
        duenho.sumarFortuna(valor);

    }
}
