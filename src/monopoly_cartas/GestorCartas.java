package monopoly_cartas;

import monopoly_casillas.Casilla;
import monopoly_casillas.acciones.AccionCajaComunidad;
import monopoly_casillas.acciones.AccionSuerte;
import monopoly_juego.Juego;
import monopoly_jugador.Jugador;
import monopoly_tablero.Tablero;

import java.util.ArrayList;
import java.util.Collections;

public class GestorCartas {
    private ArrayList<Cartas> cartasSuerte;
    private ArrayList<Cartas> cartasComunidad;
    public GestorCartas() {
        this.cartasSuerte = new ArrayList<>();
        this.cartasComunidad = new ArrayList<>();
        crearCartas();
    }

    public void crearCartas(){
        Cartas cartaSuerte1 = new Suerte(1, "Ve a Trans1 y coge un avion. Si pasas por la casilla de Salida, cobra la cantidad habitual");
        Cartas cartaSuerte2 = new Suerte(2, "Decides hacer un viaje de placer. Avanza hasta Solar15 directamente, sin pasar por la casilla de Salida y sin cobrar la cantidad habitual");
        Cartas cartaSuerte3 = new Suerte(3, "Vendes tu billete de avión para Solar13 en una subasta por Internet. Cobra 500K€");
        Cartas cartaSuerte4 = new Suerte(4, "Ve a Solar3. Si pasas por la casilla de Salida, cobra la cantidad habitual");
        Cartas cartaSuerte5 = new Suerte(5, "Los acreedores te persiguen por impago. Ve a la Cárcel. Ve directamente sin pasar por la casilla de Salida y sin cobrar la cantidad habitual");
        Cartas cartaSuerte6 = new Suerte(6, "¡Has ganado el bote de la lotería! Recibe 1M€");

        cartasSuerte.add(cartaSuerte1);
        cartasSuerte.add(cartaSuerte2);
        cartasSuerte.add(cartaSuerte3);
        cartasSuerte.add(cartaSuerte4);
        cartasSuerte.add(cartaSuerte5);
        cartasSuerte.add(cartaSuerte6);

        Cartas cartaComunidad1 = new Comunidad(1, "Paga 500K€ por un fin de semana en un balneario de 5 estrellas");
        Cartas cartaComunidad2 = new Comunidad(2, "Te investigan por fraude de identidad. Ve a la Cárcel. Ve directamente sin pasar por la casilla de Salida y sin cobrar la cantidad habitual");
        Cartas cartaComunidad3 = new Comunidad(3, "Colócate en la casilla de Salida. Cobra la cantidad habitual");
        Cartas cartaComunidad4 = new Comunidad(4, "Tu compañía de Internet obtiene beneficios. Recibe 2M€");
        Cartas cartaComunidad5 = new Comunidad(5, "Paga 1M€ por invitar a todos tus amigos a un viaje a Solar1");
        Cartas cartaComunidad6 = new Comunidad(6, "Alquilas a tus compañeros una villa en Solar7 durante una semana. Paga 200K€ a cada jugador");

        cartasComunidad.add(cartaComunidad1);
        cartasComunidad.add(cartaComunidad2);
        cartasComunidad.add(cartaComunidad3);
        cartasComunidad.add(cartaComunidad4);
        cartasComunidad.add(cartaComunidad5);
        cartasComunidad.add(cartaComunidad6);
    }

    public void hacerRandom(String tipo) { //NO SE IMPLEMENTA PARA EL EXAMEN
        // Barajar las cartas de Suerte
        if (cartasSuerte != null && !cartasSuerte.isEmpty() && tipo.equals("Suerte")) {
            Collections.shuffle(cartasSuerte); // Baraja aleatoriamente el ArrayList de cartas de Suerte
        }

        // Barajar las cartas de Comunidad
        if (cartasComunidad != null && !cartasComunidad.isEmpty() && tipo.equals("Comunidad")) {
            Collections.shuffle(cartasComunidad); // Baraja aleatoriamente el ArrayList de cartas de Comunidad
        }
    }

    public void accion(Casilla c, Jugador jugador, Jugador banca, ArrayList<Jugador> jugadores, Tablero tablero, int opcion) {

        //hacerRandom(c.getTipo()); //NO SE IMPLEMENTA PARA EL EXAMEN

        boolean modo = jugador.isModo();
        jugador.setModo(!modo);

        if (c instanceof AccionSuerte) {

            Juego.consola.imprimir("Sacando carta de suerte...");

            switch (opcion) {
                case 1:
                    Juego.consola.imprimir(cartasSuerte.get(opcion - 1).getMensaje());
                    jugador.moverJugador(tablero, (40 - jugador.getAvatar().getLugar().getPosicion()) + tablero.encontrar_casilla("Trans1").getPosicion(), jugadores);
                    break;
                case 2:
                    Juego.consola.imprimir(cartasSuerte.get(opcion - 1).getMensaje());
                    jugador.teleportJugador(tablero, tablero.encontrar_casilla("Solar15"));
                    break;
                case 3:
                    Juego.consola.imprimir(cartasSuerte.get(opcion - 1).getMensaje());
                    jugador.sumarFortuna(300000.0f);
                    jugador.setPremiosInversionesOBote(jugador.getPremiosInversionesOBote() + 300000.0f);
                    break;
                case 4:
                    Juego.consola.imprimir(cartasSuerte.get(opcion - 1).getMensaje());
                    jugador.moverJugador(tablero, (40 - jugador.getAvatar().getLugar().getPosicion()) + tablero.encontrar_casilla("Solar3").getPosicion(), jugadores);
                    break;
                case 5:
                    Juego.consola.imprimir(cartasSuerte.get(opcion - 1).getMensaje());
                    jugador.encarcelar(tablero.getPosiciones());
                    break;
                case 6:
                    Juego.consola.imprimir(cartasSuerte.get(opcion - 1).getMensaje());
                    jugador.sumarFortuna(1000000.0f);
                    jugador.setPremiosInversionesOBote(jugador.getPremiosInversionesOBote() + 1000000.0f);
                    break;
                default:
                    // Si no es un caso válido, el bucle continúa
                    break;
            }

        }

        if (c instanceof AccionCajaComunidad) {

            Juego.consola.imprimir("Sacando carta de comunidad...");

            switch (opcion) {
                case 1:
                    Juego.consola.imprimir(cartasComunidad.get(opcion - 1).getMensaje());
                    if (jugador.getFortuna() < 500000.0f){
                        Juego.consola.imprimir_sin_salto("Tu fortuna es insuficiente. El jugador ahora tiene una deuda y debe solucionarla.");
                        jugador.setDeuda (500000.0f);
                        break;
                    }
                    jugador.sumarFortuna(-500000.0f);
                    jugador.sumarGastos(500000.0f);
                    jugador.setPagoTasasEImpuestos(jugador.getPagoTasasEImpuestos() + 500000.0f);
                    banca.sumarFortuna(500000.0f);
                    break;
                case 2:
                    Juego.consola.imprimir(cartasComunidad.get(opcion - 1).getMensaje());
                    jugador.encarcelar(tablero.getPosiciones());
                    break;
                case 3:
                    Juego.consola.imprimir(cartasComunidad.get(opcion - 1).getMensaje());
                    jugador.moverJugador(tablero, (40 - jugador.getAvatar().getLugar().getPosicion()) + tablero.encontrar_casilla("Salida").getPosicion(), jugadores);
                    break;
                case 4:
                    Juego.consola.imprimir(cartasComunidad.get(opcion - 1).getMensaje());
                    jugador.sumarFortuna(2000000.0f);
                    jugador.setPremiosInversionesOBote(jugador.getPremiosInversionesOBote() + 2000000.0f);
                    break;
                case 5:
                    Juego.consola.imprimir(cartasComunidad.get(opcion - 1).getMensaje());
                    if (jugador.getFortuna() < 1000000.0f){
                        Juego.consola.imprimir("Tu fortuna es insuficiente. El jugador ahora tiene una deuda y debe solucionarla.");
                        jugador.setDeuda (1000000.0f);
                        break;
                    }
                    jugador.sumarFortuna(-1000000.0f);
                    jugador.sumarGastos(1000000.0f);
                    jugador.setPagoTasasEImpuestos(jugador.getPagoTasasEImpuestos() + 1000000.0f);
                    banca.sumarFortuna(1000000.0f);
                    break;
                case 6:
                    Juego.consola.imprimir(cartasComunidad.get(opcion - 1).getMensaje());
                    if (jugador.getFortuna() < 200000.0f * (jugadores.size()-1)){
                        Juego.consola.imprimir("Tu fortuna es insuficiente. El jugador ahora tiene una deuda y debe solucionarla.");
                        jugador.setDeuda (200000.0f * jugadores.size());
                        jugador.setPagarBanca(true);
                        break;
                    }
                    for (Jugador j : jugadores) {
                        if (!j.equals(jugador)) {
                            jugador.sumarFortuna(-200000.0f);
                            jugador.sumarGastos(200000.0f);
                            jugador.setPagoTasasEImpuestos(jugador.getPagoTasasEImpuestos() + 200000.0f);
                            j.sumarFortuna(200000.0f);
                        }
                    }
                    break;
                default:
                    // No hace nada, el bucle se repite si no es uno de los casos válidos.
                    break;
            }
        }

        jugador.setModo(modo);
    }

}
