package monopoly_avatares;

import monopoly_jugador.Jugador;
import monopoly_casillas.Casilla;
import monopoly_tablero.Tablero;
import monopoly_exception.avatares.AvatarNoValidoException;
import monopoly_exception.dados.DadosNoValidosException;
import monopoly_juego.Juego;

import java.util.ArrayList;

public class Coche extends Avatar {
    public Coche(Jugador jugador, Casilla lugar, ArrayList<Avatar> avCreados) throws AvatarNoValidoException {
        super("Coche", jugador, lugar, avCreados);
    }

    @Override
    public void moverEnAvanzado(Tablero tab, int tirada, ArrayList<Jugador> jugadores) {

        Jugador jugadorActual = this.getJugador();

        /* Movimiento Coche */
        Juego.consola.imprimir("Moviendo como Coche");

        if (jugadorActual.getTiradasTurno() <= -3) {
            Juego.consola.imprimir("El jugador que tira en modo Coche no puede los siguientes " + (-jugadorActual.getTiradasTurno()) + " turnos.");
            return;
        }

        /* Si lleva tirando muchas veces, no se tira*/
        if (jugadorActual.getTiradasTurno() > 3) {
            jugadorActual.setTiradas(0);
            Juego.consola.imprimir("El jugador que tira en modo Coche ya tiró 3 veces.");
            return;
        }

        if (tirada > 4) {
            try {
                Juego.consola.imprimir_sin_salto("El avatar " + this.getId() + " avanza " + tirada + " posiciones, desde " + this.getLugar().getNombre());
                this.moverEnBasico(tab.getPosiciones(), tirada);
                Casilla c = this.getLugar();
                Juego.consola.imprimir(" hasta " + c.getNombre() + ".");
                c.evaluarCasilla(tab, this.getJugador(), tab.getBanca(), jugadores);
            } catch (DadosNoValidosException e) {
                Juego.consola.imprimir("Error: " + e.getMessage()); // Manejo del error
            }
            return;
        }

        // Si la tirada es menor o igual a 4, retroceder y deshabilitar tiradas.
        jugadorActual.setTiradas(-3);

        try {
            Juego.consola.imprimir_sin_salto("El avatar " + this.getId() + " avanza " + tirada + " posiciones hacia atrás, desde " + this.getLugar().getNombre());
            this.moverAvatarAtras(tab.getPosiciones(), tirada);
            Casilla c = this.getLugar();
            Juego.consola.imprimir(" hasta " + c.getNombre() + ".");
            c.evaluarCasilla(tab, this.getJugador(), tab.getBanca(), jugadores);
        } catch (DadosNoValidosException e) {
            Juego.consola.imprimir("Error: " + e.getMessage()); // Manejo del error
        }
    }
}

