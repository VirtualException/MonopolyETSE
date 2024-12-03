package monopoly_casillas.acciones;

import monopoly_exception.dados.DadosNoValidosException;
import monopoly_juego.Juego;
import monopoly_jugador.Jugador;
import monopoly_tablero.Tablero;

import java.util.ArrayList;
import java.util.Scanner;

public class AccionCajaComunidad extends Accion {


    public AccionCajaComunidad(String nombre, String tipo, int posicion, Jugador banca) {
        super(nombre, tipo, posicion, banca);
    }

    @Override
    public void evaluarCasilla(Tablero tab, Jugador jugador, Jugador banca, ArrayList<Jugador> jugadores) throws DadosNoValidosException{

        /* El jugador cayó una vez más en esta casilla */
        this.contarCaer[jugador.getIndice()]++;

        Scanner scanner1 = new Scanner(System.in);
        int opcion1 = 0;
        Juego.consola.imprimir("Has caído en una casilla de Comunidad, por favor, escoge una carta");
        boolean numeroIncorrecto1 = true;
        while (numeroIncorrecto1) {
            Juego.consola.imprimir_sin_salto("Escoge un valor del 1 al 6: ");
            try {
                opcion1 = Integer.parseInt(scanner1.nextLine()); //hacemos un parse int
                if (opcion1 >= 1 && opcion1 <= 6) {
                    numeroIncorrecto1 = false; // Si el número está en el rango, sale del bucle
                } else {
                    throw new DadosNoValidosException("Valor erróneo. Debe ser un número entre 1 y 6.");
                }
            } catch (NumberFormatException e) {
                throw new DadosNoValidosException("Entrada no válida. Por favor, ingresa un número entre 1 y 6.");
            }
        }
        tab.getCartas().getGestorCartas().accion(this, jugador, banca, jugadores, tab, opcion1); //ejecutamos la funciona de las cartas
    }
}
