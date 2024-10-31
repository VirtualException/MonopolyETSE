package monopoly;

import java.util.*;
import partida.*;

public class Cartas {
    private int id;
    private String tipo;
    private String mensaje;
    private ArrayList<Cartas> cartasSuerte;
    private ArrayList<Cartas> cartasComunidad;

    public Cartas(int id, String tipo, String mensaje) {
        this.id = id;
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.cartasSuerte = new ArrayList<>();
        this.cartasComunidad = new ArrayList<>();
    }

    public Cartas() {
        this.cartasSuerte = new ArrayList<>();
        this.cartasComunidad = new ArrayList<>();
        crearCartas();
    }

    public void crearCartas(){
        Cartas cartaSuerte1 = new Cartas(1, "Suerte", "Ve a Trans1 y coge un avion. Si pasas por la casilla de Salida, cobra la cantidad habitual");
        Cartas cartaSuerte2 = new Cartas(2, "Suerte", "Decides hacer un viaje de placer. Avanza hasta Solar15 directamente, sin pasar por la casilla de Salida y sin cobrar la cantidad habitual");
        Cartas cartaSuerte3 = new Cartas(3, "Suerte", "Vendes tu billete de avión para Solar13 en una subasta por Internet. Cobra 500K€");
        Cartas cartaSuerte4 = new Cartas(4, "Suerte", "Ve a Solar3. Si pasas por la casilla de Salida, cobra la cantidad habitual");
        Cartas cartaSuerte5 = new Cartas(5, "Suerte", "Los acreedores te persiguen por impago. Ve a la Cárcel. Ve directamente sin pasar por la casilla de Salida y sin cobrar la cantidad habitual");
        Cartas cartaSuerte6 = new Cartas(6, "Suerte", "¡Has ganado el bote de la lotería! Recibe 1M€");

        cartasSuerte.add(cartaSuerte1);
        cartasSuerte.add(cartaSuerte2);
        cartasSuerte.add(cartaSuerte3);
        cartasSuerte.add(cartaSuerte4);
        cartasSuerte.add(cartaSuerte5);
        cartasSuerte.add(cartaSuerte6);

        Cartas cartaComunidad1 = new Cartas(1, "Comunidad", "Paga 500K€ por un fin de semana en un balneario de 5 estrellas");
        Cartas cartaComunidad2 = new Cartas(2, "Comunidad", "Te investigan por fraude de identidad. Ve a la Cárcel. Ve directamente sin pasar por la casilla de Salida y sin cobrar la cantidad habitual");
        Cartas cartaComunidad3 = new Cartas(3, "Comunidad", "Colócate en la casilla de Salida. Cobra la cantidad habitual");
        Cartas cartaComunidad4 = new Cartas(4, "Comunidad", "Tu compañía de Internet obtiene beneficios. Recibe 2M€");
        Cartas cartaComunidad5 = new Cartas(5, "Comunidad", "Paga 1M€ por invitar a todos tus amigos a un viaje a Solar1");
        Cartas cartaComunidad6 = new Cartas(6, "Comunidad", "Alquilas a tus compañeros una villa en Solar7 durante una semana. Paga 200K€ a cada jugador");

        cartasComunidad.add(cartaComunidad1);
        cartasComunidad.add(cartaComunidad2);
        cartasComunidad.add(cartaComunidad3);
        cartasComunidad.add(cartaComunidad4);
        cartasComunidad.add(cartaComunidad5);
        cartasComunidad.add(cartaComunidad6);
    }

    public void hacerRandom(String tipo) {
        // Barajar las cartas de Suerte
        if (cartasSuerte != null && !cartasSuerte.isEmpty() && tipo.equals("Suerte")) {
            Collections.shuffle(cartasSuerte); // Baraja aleatoriamente el ArrayList de cartas de Suerte
        }

        // Barajar las cartas de Comunidad
        if (cartasComunidad != null && !cartasComunidad.isEmpty() && tipo.equals("Comunidad")) {
            Collections.shuffle(cartasComunidad); // Baraja aleatoriamente el ArrayList de cartas de Comunidad
        }
    }

    public void accion(Casilla c, Jugador jugador, Jugador banca, ArrayList<Jugador> jugadores, Tablero tablero) {

        if (c.getTipo().equals("Suerte")) {

            hacerRandom(c.getTipo());
            int opcion1;
            for (Cartas carta1 : this.cartasSuerte) {

                do {
                    hacerRandom(c.getTipo()); // Baraja las cartas en cada intento
                    opcion1 = carta1.getId(); // Selecciona el ID de la primera carta barajada

                    switch (opcion1) {
                        case 1:
                            System.out.println(cartasSuerte.get(0).getMensaje());
                            jugador.moverJugador(tablero, (40 - jugador.getAvatar().getLugar().getPosicion()) + tablero.encontrar_casilla("Trans1").getPosicion());
                            break;
                        case 2:
                            System.out.println(cartasSuerte.get(0).getMensaje());
                            jugador.teleportJugador(tablero, tablero.encontrar_casilla("Solar15"));
                            break;
                        case 3:
                            System.out.println(cartasSuerte.get(0).getMensaje());
                            jugador.sumarFortuna(300000.0f);
                            break;
                        case 4:
                            System.out.println(cartasSuerte.get(0).getMensaje());
                            jugador.moverJugador(tablero, (40 - jugador.getAvatar().getLugar().getPosicion()) + tablero.encontrar_casilla("Solar3").getPosicion());
                            break;
                        case 5:
                            System.out.println(cartasSuerte.get(0).getMensaje());
                            jugador.encarcelar(tablero.getPosiciones());
                            break;
                        case 6:
                            System.out.println(cartasSuerte.get(0).getMensaje());
                            jugador.sumarFortuna(1000000.0f);
                            break;
                        default:
                            // Si no es un caso válido, el bucle continúa
                            break;
                    }

                } while (opcion1 < 1 || opcion1 > 6);
            }
        }

        if (c.getTipo().equals("Comunidad")) {

            hacerRandom(c.getTipo());
            int opcion2;

            for (Cartas carta2 : this.cartasComunidad) {
                do {
                    hacerRandom(c.getTipo()); // Baraja las cartas en cada intento
                    opcion2 = carta2.getId(); // Selecciona el ID de la primera carta barajada

                    switch (opcion2) {
                        case 1:
                            System.out.println(cartasComunidad.get(0).getMensaje());
                            jugador.sumarFortuna(-500000.0f);
                            jugador.sumarGastos(500000.0f);
                            banca.sumarFortuna(500000.0f);
                            break;
                        case 2:
                            System.out.println(cartasComunidad.get(0).getMensaje());
                            jugador.encarcelar(tablero.getPosiciones());
                            break;
                        case 3:
                            System.out.println(cartasComunidad.get(0).getMensaje());
                            jugador.moverJugador(tablero, (40 - jugador.getAvatar().getLugar().getPosicion()) + tablero.encontrar_casilla("Salida").getPosicion());
                            break;
                        case 4:
                            System.out.println(cartasComunidad.get(0).getMensaje());
                            jugador.sumarFortuna(2000000.0f);
                            break;
                        case 5:
                            System.out.println(cartasComunidad.get(0).getMensaje());
                            jugador.sumarFortuna(-1000000.0f);
                            jugador.sumarGastos(1000000.0f);
                            banca.sumarFortuna(1000000.0f);
                            break;
                        case 6:
                            System.out.println(cartasComunidad.get(0).getMensaje());
                            for (Jugador j : jugadores) {
                                if (!j.equals(jugador)) {
                                    jugador.sumarFortuna(-200000.0f);
                                    jugador.sumarGastos(200000.0f);
                                    j.sumarFortuna(200000.0f);
                                }
                            }
                            break;
                        default:
                            // No hace nada, el bucle se repite si no es uno de los casos válidos.
                            break;
                    }

                } while (opcion2 < 1 || opcion2 > 6); // Repite hasta que el ID esté entre 1 y 6
            }
        }
    }





    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public ArrayList<Cartas> getCartasSuerte() {
        return cartasSuerte;
    }

    public void setCartasSuerte(ArrayList<Cartas> cartasSuerte) {
        this.cartasSuerte = cartasSuerte;
    }

    public ArrayList<Cartas> getCartasComunidad() {
        return cartasComunidad;
    }

    public void setCartasComunidad(ArrayList<Cartas> cartasComunidad) {
        this.cartasComunidad = cartasComunidad;
    }

}
