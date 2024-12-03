package monopoly_casillas.acciones;

import monopoly_exception.dados.DadosNoValidosException;
import monopoly_juego.Juego;
import monopoly_jugador.Jugador;
import monopoly_tablero.Tablero;

import java.util.ArrayList;
import java.util.Scanner;

public class AccionSuerte extends Accion {
    public AccionSuerte(String nombre, String tipo, int posicion, Jugador banca) {
        super(nombre, tipo, posicion, banca);
    }

    @Override
    public void evaluarCasilla(Tablero tab, Jugador jugador, Jugador banca, ArrayList<Jugador> jugadores) throws DadosNoValidosException{

        /* El jugador cayó una vez más en esta casilla */
        this.contarCaer[jugador.getIndice()]++;

        Scanner scanner2 = new Scanner(System.in);
        int opcion2 = 0;
        boolean numeroIncorrecto2 = true;
        Juego.consola.imprimir("Has caído en una casilla de Suerte, por favor, escoge una carta");
        while (numeroIncorrecto2) {
            Juego.consola.imprimir_sin_salto("Escoge un valor del 1 al 6: ");
            try {
                opcion2 = Integer.parseInt(scanner2.nextLine()); //hacemos un parse int
                if (opcion2 >= 1 && opcion2 <= 6) {
                    numeroIncorrecto2 = false; // Si el número está en el rango, sale del bucle
                } else {
                    throw new DadosNoValidosException("Valor erróneo. Debe ser un número entre 1 y 6.");
                }
            } catch (NumberFormatException e) {
                throw new DadosNoValidosException("Entrada no válida. Por favor, ingresa un número entre 1 y 6.");
            }
        }
        tab.getCartas().accion(this, jugador, banca, jugadores, tab, opcion2); //ejecutamos la funcion de las cartas
    }
}
