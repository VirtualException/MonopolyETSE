package monopoly_casillas.impuestos;

import monopoly_casillas.Casilla;
import monopoly_juego.Juego;
import monopoly_jugador.Jugador;
import monopoly_tablero.Tablero;

import java.util.ArrayList;
import java.util.Scanner;

public class Impuesto extends Casilla {
    public Impuesto(String nombre, String tipo, int posicion, float valor, Jugador duenho) {
        super(nombre, tipo, posicion, valor, duenho);
        System.out.println("Creado impuesto " + nombre + " con valor: " + valor + "\n");
    }

    @Override
    public void evaluarCasilla(Tablero tab, Jugador jugador, Jugador banca, ArrayList<Jugador> jugadores) {

        String tipoCasilla = this.getTipo();

        /* El jugador cayó una vez más en esta casilla */
        this.contarCaer[jugador.getIndice()]++;

        if (jugador.getFortuna() < impuesto) {
            Juego.consola.imprimir("El jugador " + jugador.getNombre() + " no tiene suficiente dinero para pagar el impuesto. El jugador ahora tiene una deuda y debe solucionarla.");
            jugador.setDeuda(impuesto);
            return;
        }

        Juego.consola.imprimir("El jugador " + jugador.getNombre() + " paga un impuesto de " + impuesto + ".");
        jugador.sumarGastos(impuesto);
        jugador.sumarFortuna(-impuesto);
        jugador.setPagoTasasEImpuestos(jugador.getPagoTasasEImpuestos() + impuesto);
        banca.sumarFortuna(impuesto);
        /* Bote del parking */
        Casilla parking = tab.encontrar_casilla("Parking");
        parking.sumarValor(impuesto);
        return;

    }

}
