package monopoly_avatares;

import monopoly_jugador.Jugador;
import monopoly_casillas.Casilla;
import monopoly_tablero.Tablero;
import monopoly_exception.avatares.AvatarNoValidoException;
import monopoly_exception.dados.DadosNoValidosException;
import monopoly_juego.Juego;

import java.util.ArrayList;

public class Pelota extends Avatar {

    private int debeContinuar;

    public Pelota(Jugador jugador, Casilla lugar, ArrayList<Avatar> avCreados) throws AvatarNoValidoException {
        super("Pelota", jugador, lugar, avCreados);
    }

    @Override
    public void moverEnAvanzado(Tablero tab, int tirada, ArrayList<Jugador> jugadores) {
        
        /* Movimiento Pelota */
        Juego.consola.imprimir("Moviendo como Pelota");

        ArrayList<ArrayList<Casilla>> pos = tab.getPosiciones();
        Casilla c = this.getLugar();

        /* Está en el proceso de avanzar por cada casilla */
        if (debeContinuar > 0) {

            debeContinuar-=2;
            /* Si le queda UN SOLO PASO */
            if (debeContinuar < 0) {
                tirada = 1;
                debeContinuar = 0;
            }
            else
                tirada = 2;
            try {
                Juego.consola.imprimir("Pasando a la siguiente casilla (modo Pelota)");
                Juego.consola.imprimir_sin_salto("El avatar " + this.getId() + " avanza " + tirada + " posiciones, desde " + c.getNombre());
                this.moverEnBasico(pos, tirada);
                c = this.getLugar();
                Juego.consola.imprimir(" hasta " + c.getNombre() + ".");
                c.evaluarCasilla(tab, this.getJugador(), tab.getBanca(), jugadores);
            } catch (DadosNoValidosException e) {
                Juego.consola.imprimir("Error: " + e.getMessage()); // Manejo del error
            }
            return;
        }

        /* Si es la primera tirada en modo Pelota */
        if (tirada > 4) {

            int tiradaPrimera;
            if (c.getPosicion() % 2 == 0)
                tiradaPrimera = 5;
            else
                tiradaPrimera = 4;

            try {
                Juego.consola.imprimir("Primer movimiento (modo Pelota)");
                Juego.consola.imprimir_sin_salto("El avatar " + this.getId() + " avanza " + tiradaPrimera + " posiciones, desde " + c.getNombre());
                this.moverEnBasico(pos, tiradaPrimera);
                c = this.getLugar();
                Juego.consola.imprimir(" hasta " + c.getNombre() + ".");
                c.evaluarCasilla(tab, this.getJugador(), tab.getBanca(), jugadores);
            } catch (DadosNoValidosException e) {
                Juego.consola.imprimir("Error: " + e.getMessage()); // Manejo del error
            }

            Juego.consola.imprimir("El jugador debe seguir avanzando aún");

            debeContinuar = tirada - tiradaPrimera;

        }
        else {

            int tiradaPrimera;
            if (c.getPosicion() % 2 == 0)
                tiradaPrimera = 5;
            else
                tiradaPrimera = 4;

            try {
                Juego.consola.imprimir("Primer y único movimiento (modo Pelota)");
                Juego.consola.imprimir_sin_salto("El avatar " + this.getId() + " avanza " + tiradaPrimera + " posiciones hacia atrás, desde " + c.getNombre());
                this.moverAvatarAtras(pos, tiradaPrimera);
                c = this.getLugar();
                Juego.consola.imprimir(" hasta " + c.getNombre() + ".");
                c.evaluarCasilla(tab, this.getJugador(), tab.getBanca(), jugadores);
            } catch (DadosNoValidosException e) {
                Juego.consola.imprimir("Error: " + e.getMessage()); // Manejo del error
            }

            debeContinuar = 0;
        }
    }
}