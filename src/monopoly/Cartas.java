package monopoly;

import partida.Jugador;

import java.util.*;

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
        Cartas cartaSuerte1 = new Cartas(1, "suerte", "Ve a Trans1 y coge un avion. Si pasas por la casilla de Salida, cobra la cantidad habitual");
        Cartas cartaSuerte2 = new Cartas(2, "suerte", "Decides hacer un viaje de placer. Avanza hasta Solar15 directamente, sin pasar por la casilla de Salida y sin cobrar la cantidad habitual");
        Cartas cartaSuerte3 = new Cartas(3, "suerte", "Vendes tu billete de avión para Solar13 en una subasta por Internet. Cobra 500K€");
        Cartas cartaSuerte4 = new Cartas(4, "suerte", "Ve a Solar3. Si pasas por la casilla de Salida, cobra la cantidad habitual");
        Cartas cartaSuerte5 = new Cartas(5, "suerte", "Los acreedores te persiguen por impago. Ve a la Cárcel. Ve directamente sin pasar por la casilla de Salida y sin cobrar la cantidad habitual");
        Cartas cartaSuerte6 = new Cartas(6, "suerte", "¡Has ganado el bote de la lotería! Recibe 1M€");

        cartasSuerte.add(cartaSuerte1);
        cartasSuerte.add(cartaSuerte2);
        cartasSuerte.add(cartaSuerte3);
        cartasSuerte.add(cartaSuerte4);
        cartasSuerte.add(cartaSuerte5);
        cartasSuerte.add(cartaSuerte6);

        Cartas cartaComunidad1 = new Cartas(1, "comunidad", "Paga 500K€ por un fin de semana en un balneario de 5 estrellas");
        Cartas cartaComunidad2 = new Cartas(2, "comunidad", "Te investigan por fraude de identidad. Ve a la Cárcel. Ve directamente sin pasar por la casilla de Salida y sin cobrar la cantidad habitual");
        Cartas cartaComunidad3 = new Cartas(3, "comunidad", "Colócate en la casilla de Salida. Cobra la cantidad habitual");
        Cartas cartaComunidad4 = new Cartas(4, "comunidad", "Tu compañía de Internet obtiene beneficios. Recibe 2M€");
        Cartas cartaComunidad5 = new Cartas(5, "comunidad", "Paga 1M€ por invitar a todos tus amigos a un viaje a Solar1");
        Cartas cartaComunidad6 = new Cartas(6, "comunidad", "Alquilas a tus compañeros una villa en Solar7 durante una semana. Paga 200K€ a cada jugador");

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
        Casilla casilla;

        if (c.getTipo().equals("Suerte")) {

            hacerRandom(c.getTipo());

            for (Cartas casillas : this.cartasSuerte){
                if (casillas.id == 1){
                    System.out.println(cartasSuerte.get(1).getMensaje());
                    jugador.moverJugador(tablero, (40 - jugador.getAvatar().getLugar().getPosicion()) + tablero.encontrar_casilla("Trans1").getPosicion());
                } else if (casillas.id == 2){
                    System.out.println(cartasSuerte.get(2).getMensaje());
                    jugador.teleportJugador(tablero, tablero.encontrar_casilla("Solar15"));
                } else if (casillas.id == 3){
                    System.out.println(cartasSuerte.get(3).getMensaje());
                    jugador.sumarFortuna(300000.0f);
                } else if (casillas.id == 4){
                    System.out.println(cartasSuerte.get(4).getMensaje());
                    jugador.moverJugador(tablero, (40 - jugador.getAvatar().getLugar().getPosicion()) + tablero.encontrar_casilla("Solar3").getPosicion());
                } else if (casillas.id == 5){
                    System.out.println(cartasSuerte.get(5).getMensaje());
                    jugador.encarcelar(tablero.getPosiciones());
                } else if (casillas.id == 6){
                    System.out.println(cartasSuerte.get(6).getMensaje());
                    jugador.sumarFortuna(1000000.0f);
                }
            }

            for (Cartas casillas : this.cartasComunidad){
                if (casillas.id == 1){
                    System.out.println(cartasComunidad.get(1).getMensaje());
                    jugador.sumarFortuna(-500000.0f);
                    jugador.sumarGastos(500000.0f);
                    banca.sumarFortuna(500000.0f);
                } else if (casillas.id == 2){
                    System.out.println(cartasComunidad.get(2).getMensaje());
                    jugador.encarcelar(tablero.getPosiciones());
                } else if (casillas.id == 3){
                    System.out.println(cartasComunidad.get(3).getMensaje());
                    jugador.moverJugador(tablero, (40 - jugador.getAvatar().getLugar().getPosicion()) + tablero.encontrar_casilla("Salida").getPosicion());
                } else if (casillas.id == 4){
                    System.out.println(cartasComunidad.get(4).getMensaje());
                    jugador.sumarFortuna(2000000.0f);
                } else if (casillas.id == 5){
                    System.out.println(cartasComunidad.get(5).getMensaje());
                    jugador.sumarFortuna(-1000000.0f);
                    jugador.sumarGastos(1000000.0f);
                    banca.sumarFortuna(1000000.0f);
                } else if (casillas.id == 6){
                    System.out.println(cartasComunidad.get(6).getMensaje());
                    for (Jugador j : jugadores){
                        if (!j.equals(jugador)){
                            jugador.sumarFortuna(-200000.0f);
                            jugador.sumarGastos(200000.0f);
                            j.sumarFortuna(200000.0f);
                        }
                    }
                }
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
