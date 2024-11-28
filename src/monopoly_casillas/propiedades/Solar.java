package monopoly_casillas.propiedades;

import monopoly_juego.Juego;
import monopoly_jugador.Jugador;
import monopoly_tablero.Grupo;
import monopoly_tablero.Tablero;
import monopoly_tablero.Valor;

import java.util.ArrayList;

public class Solar extends Propiedad {

    private Grupo grupo; //Grupo al que pertenece la casilla (si es solar).

    public Solar(String nombre, String tipo, int posicion, float valor, Jugador duenho) {
        super(nombre, tipo, posicion, valor, duenho);
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public float getPrecioOriginal() {
        float precio_original = switch (grupo.getColorGrupo()) {
            case "negro" -> Valor.VALOR_GRUPO_NEGRO;
            case "cyan" -> Valor.VALOR_GRUPO_AZUL;
            case "rosa" -> Valor.VALOR_GRUPO_ROSA;
            case "amarillo" -> Valor.VALOR_GRUPO_AMARELO;
            case "vermello" -> Valor.VALOR_GRUPO_VERMELLO;
            case "marron" -> Valor.VALOR_GRUPO_MARRON;
            case "verde" -> Valor.VALOR_GRUPO_VERDE;
            case "azul" -> Valor.VALOR_GRUPO_AZUL_OSCURO;
            default -> 0.f;
        };
        precio_original /= grupo.getMiembros().size();
        return precio_original;
    }

    @Override
    public void evaluarCasilla(Tablero tab, Jugador jugador, Jugador banca, ArrayList<Jugador> jugadores) {

        /* El jugador cayó una vez más en esta casilla */
        this.contarCaer[jugador.getIndice()]++;

        /* Si hay dueño */
        if (duenho != banca && duenho != jugador) {

            /* !!!! Calcular cuanto tiene que pagar !!!! */

            /* Alquiler inicial = 10% */
            float pago_alquiler = valor * 10.f;

            /* Sumar alquiler dependiendo de las edificaciones. */
            switch (this.getCasasN()) {
                case 1 -> pago_alquiler += pago_alquiler * 5;
                case 2 -> pago_alquiler += pago_alquiler * 15;
                case 3 -> pago_alquiler += pago_alquiler * 35;
                case 4 -> pago_alquiler += pago_alquiler * 50;
            }
            pago_alquiler += pago_alquiler * 70 * this.getHotelesN();
            pago_alquiler += pago_alquiler * 25 * this.getPiscinasN();
            pago_alquiler += pago_alquiler * 25 * this.getPistasN();

            /* Si el dueño del solar tiene el grupo, se dobla el valor. */
            if (this.grupo.esDuenhoGrupo(this.duenho)) {
                pago_alquiler *= 2;
            }

            /* Si no puede pagarlo */
            if (jugador.getFortuna() < pago_alquiler) {
                Juego.consola.imprimir("Dinero insuficiente. El jugador ahora tiene una deuda y debe solucionarla.");
                jugador.setDeuda(valor);
                return;
            }

            /* El jugador paga al propietario */
            jugador.sumarGastos(pago_alquiler);
            jugador.setPagoDeAlquileres(jugador.getPagoDeAlquileres() + pago_alquiler);
            jugador.sumarFortuna(-pago_alquiler);
            /* El propietario recibe */
            duenho.sumarFortuna(pago_alquiler);
            duenho.setCobroDeAlquileres(duenho.getCobroDeAlquileres() + pago_alquiler);

            Juego.consola.imprimir("El jugador " + jugador.getNombre() + " paga " + pago_alquiler + " € de alquiler.");

        }
        return;
    }
}
