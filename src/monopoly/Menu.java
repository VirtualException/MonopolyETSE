package monopoly;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;
import partida.*;

public class Menu {

    //Atributos
    private ArrayList<Jugador> jugadores; //Jugadores de la partida.
    private ArrayList<Avatar> avatares; //Avatares en la partida.
    private int turno = 0; //Índice correspondiente a la posición en el arrayList del jugador (y el avatar) que tienen el turno
    private int lanzamientos; //Variable para contar el número de lanzamientos de un jugador en un turno.
    private Tablero tablero; //Tablero en el que se juega.
    private Dado dado1; //Dos dados para lanzar y avanzar casillas.
    private Dado dado2;
    private Jugador banca; //El jugador banca.
    private boolean tirado; //Booleano para comprobar si el jugador que tiene el turno ha tirado o no.
    private boolean solvente; //Booleano para comprobar si el jugador que tiene el turno es solvente, es decir, si ha pagado sus deudas.


    public Menu() {
        iniciarPartida();
    }


    // Metodo para inciar una partida: crea los jugadores y avatares.
    private void iniciarPartida() {

        /* Crear banca y tablero */
        avatares = new ArrayList<Avatar>();
        jugadores = new ArrayList<Jugador>();
        banca = new Jugador("Banca", "Banca", null, avatares);
        tablero = new Tablero(banca);

        dado1 = new Dado();
        dado2 = new Dado();


        System.out.println(this.tablero);

        Scanner scan = new Scanner(System.in);
        boolean sair = false;

        while (!sair) {
            System.out.print("$> ");
            sair = this.analizarComando(scan.nextLine());
        }
    }
    
    /*Metodo que interpreta el comando introducido y toma la accion correspondiente.
    * Parámetro: cadena de caracteres (el comando).
    */
    private boolean analizarComando(String comando) {

        String[] comandos_args = new String[5];
        String[] split = comando.split(" ");
        int num_args = split.length;
        System.arraycopy(split, 0, comandos_args, 0, num_args);

        /* Crear jugador, junto a su avatar */
        if (comandos_args[0].equals("crear") && comandos_args[1].equals("jugador") && num_args == 4) {

            jugadores.add(new Jugador(comandos_args[2], comandos_args[3], tablero.encontrar_casilla("Salida"), avatares));
            avatares.add(jugadores.getLast().getAvatar());

            tablero.encontrar_casilla("Salida").anhadirAvatar(avatares.getLast());

            System.out.println("{");
            System.out.println("\tnombre: " + jugadores.getLast().getNombre() + ",");
            System.out.println("\tavatar: " + jugadores.getLast().getAvatar().getId());
            System.out.println("}");

        }
        /* Comandos de listar */
        else if (comandos_args[0].equals("listar") && comandos_args[1].equals("jugadores") && num_args == 2) {
            listarJugadores();
        }
        else if (comandos_args[0].equals("listar") && comandos_args[1].equals("avatares") && num_args == 2) {
            listarAvatares();
        }
        else if (comandos_args[0].equals("listar") && comandos_args[1].equals("enventa") && num_args == 2) {
            listarVenta();
        }
        /* Ver tablero */
        else if (comandos_args[0].equals("ver") && comandos_args[1].equals("tablero") && num_args == 2) {
            System.out.println(this.tablero);
        }
        /* Acciones jugador */
        else if (comandos_args[0].equals("acabar") && comandos_args[1].equals("turno") && num_args == 2) {
            acabarTurno();
        }
        else if (comandos_args[0].equals("lanzar") && comandos_args[1].equals("dados") && num_args == 2) {
            lanzarDados();
        }
        /* Describir */
        else if (comandos_args[0].equals("describir") && comandos_args[1].equals("avatar") && num_args == 3) {
            if (jugadores.isEmpty()){
                System.out.println("No hay avatares registrados");
            } else{
                for (Avatar a : avatares){
                    if (a.getId().equals(comandos_args[2])){
                        descAvatar(comandos_args[2]);
                    } else {
                        System.out.println("El avatar indicado no existe");
                    }
                }
            }

        }
        else if (comandos_args[0].equals("describir") && comandos_args[1].equals("jugador") && num_args == 3) {
            if (jugadores.isEmpty()){
                System.out.println("No hay jugadores registrados");
            } else{
                for (Jugador j : jugadores){
                    if (j.getNombre().equals(comandos_args[2])){
                        descJugador(comandos_args[2]);
                    } else {
                        System.out.println("El jugador indicado no existe");
                    }
                }

            }
        }
        /* Describir casilla */
        else if (comandos_args[0].equals("describir") && num_args == 2) {
            descCasilla(comandos_args[1]);
        }


        /* Comando salida */
        else if (comandos_args[0].equals("exit")) {
            return true;
        }

        else {
            System.out.println("Comando no reconocido.");
        }

        return false;
    }

    /*Metodo que realiza las acciones asociadas al comando 'describir jugador'.
    * Parámetro: comando introducido
     */
    private void descJugador(String nombre) {

        for (Jugador i : jugadores) {
            if (Objects.equals(i.getNombre(), nombre)) {
                System.out.println("{");
                System.out.println("\tnombre: " + i.getNombre() + ",");
                System.out.println("\tavatar: " + i.getAvatar().getId());
                System.out.println("\tfortuna: " + i.getFortuna());
                System.out.print("\tpropiedades: [");
                for (Casilla p : i.getPropiedades()) {
                    System.out.print(p.getNombre() + ", ");
                }
                System.out.println("]");
                System.out.print("\thipotecas: [");
                for (Casilla p : i.getPropiedades()) {
                    System.out.print(p.getNombre() + ", ");
                }
                System.out.println("]");
                System.out.print("\tedificios: [");
                for (Casilla p : i.getPropiedades()) {
                    System.out.print(p.getNombre() + ", ");
                }
                System.out.println("]");
                System.out.println("\n},");
            }
        }
    }

    /*Metodo que realiza las acciones asociadas al comando 'describir avatar'.
    * Parámetro: id del avatar a describir.
    */
    private void descAvatar(String ID) {
        for (Avatar i : avatares) {
            if (Objects.equals(i.getId(), ID)) {
                System.out.println("{");
                System.out.println("\tid: " + i.getId() + ",");
                System.out.println("\ttipo: " + i.getTipo());
                System.out.println("\tcasilla: " + i.getLugar().getNombre());
                System.out.print("\tjugador: " + i.getJugador().getNombre());
                System.out.println("\n},");
            }
        }
    }

    /* Metodo que realiza las acciones asociadas al comando 'describir nombre_casilla'.
    * Parámetros: nombre de la casilla a describir.
    */
    private void descCasilla(String nombre) {
    }

    //Metodo que ejecuta todas las acciones relacionadas con el comando 'lanzar dados'.
    private void lanzarDados() {

        if (jugadores.isEmpty()) {
            System.out.println("No hay jugadores!");
            return;
        }

        Jugador j = jugadores.get(turno);

        if (j.getTiradas() == -1) {
            System.out.println("El jugador ya tiró.");
        }

        System.out.print("El jugador " + j.getNombre() + " tira los dados.");

        dado1.hacerTirada();
        dado2.hacerTirada();

        System.out.println("La tirada es: " + dado1.getValor() + ", " + dado2.getValor() + ".");

        if (dado1.getValor() == dado2.getValor()) {
            System.out.println("Doble!");
            if (j.getTiradas() == 1) {
                System.out.println("A la cárcel!");
                /* IR A LA CARCEL */
            }
            /* El jugador puede tirar de nuevo */
            j.setTiradas(1);
        }

        /* El jugador no puede tirar de nuevo */
        j.setTiradas(-1);

    }

    /*Metodo que ejecuta todas las acciones realizadas con el comando 'comprar nombre_casilla'.
    * Parámetro: cadena de caracteres con el nombre de la casilla.
     */
    private void comprar(String nombre) {
    }

    //Metodo que ejecuta todas las acciones relacionadas con el comando 'salir carcel'.
    private void salirCarcel() {
    }

    // Metodo que realiza las acciones asociadas al comando 'listar enventa'.
    private void listarVenta() {
    }

    // Metodo que realiza las acciones asociadas al comando 'listar jugadores'.
    private void listarJugadores() {
        if (jugadores.isEmpty()){
            System.out.println("No hay jugadores resgitrados");
        } else {
            for (Jugador j : jugadores) {
                descJugador(j.getNombre());
            }
        }
    }

    // Metodo que realiza las acciones asociadas al comando 'listar avatares'.
    private void listarAvatares() {

        if (jugadores.isEmpty()){
            System.out.println("No hay avatares resgitrados");
        } else {
            for (Avatar a : avatares) {
                descAvatar(a.getId());
            }
        }
    }

    // Metodo que realiza las acciones asociadas al comando 'acabar turno'.
    private void acabarTurno() {

        Jugador j = jugadores.get(turno);

        j.setTiradas(0);

        turno++;
        turno = turno % jugadores.size();

        j = jugadores.get(turno);

        System.out.println("El jugador actual es " + j.getNombre() + ".");

    }
}