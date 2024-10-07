package monopoly;

import java.util.ArrayList;
import java.util.Objects;
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
            Jugador jugador = new Jugador(comandos_args[2], comandos_args[3], tablero.encontrar_casilla("Salida"), avatares);
            Avatar avatar = new Avatar(jugador.getAvatar().getId(), jugador, tablero.encontrar_casilla("Salida"), avatares);
            avatares.add(avatar);
            jugadores.add(jugador);
            avatares.add(avatar);

            tablero.encontrar_casilla("Salida").anhadirAvatar(avatar);

            System.out.println("{");
            System.out.println("\tnombre: " + jugador.getNombre() + ",");
            System.out.println("\tavatar: " + jugador.getAvatar().getId());
            System.out.println("}");


        }

        /* Crear jugador, junto a su avatar */
        else if (comandos_args[0].equals("listar") && comandos_args[1].equals("jugadores") && num_args == 2) {

            for (Jugador j : jugadores) {
                System.out.println("{");
                System.out.println("\tnombre: " + j.getNombre() + ",");
                System.out.println("\tavatar: " + j.getAvatar().getId());
                System.out.println("\tfortuna: " + j.getFortuna());
                System.out.print("\tpropiedades: [");
                for (Casilla p : j.getPropiedades()) {
                    System.out.print(p.getNombre() + ", ");
                }
                System.out.println("]");
                System.out.print("\thipotecas: [");
                for (Casilla p : j.getPropiedades()) {
                    System.out.print(p.getNombre() + ", ");
                }
                System.out.println("]");
                System.out.print("\tedificios: [");
                for (Casilla p : j.getPropiedades()) {
                    System.out.print(p.getNombre() + ", ");
                }
                System.out.println("]");
                System.out.println("\n},");
            }

        } else if (comandos_args[0].equals("listar") && comandos_args[1].equals("avatares") && num_args == 2) {

            for (Avatar a : avatares) {
                System.out.println("{");
                System.out.println("\tid: " + a.getId() + ",");
                System.out.println("\ttipo: " + a.getTipo());
                System.out.println("\tcasilla: " + a.getLugar().getNombre());
                System.out.print("\tjugador: " + a.getJugador().getNombre());
                System.out.println("\n},");
            }

        } else if (comandos_args[0].equals("ver") && comandos_args[1].equals("tablero") && num_args == 2) {

            System.out.println(this.tablero);

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
    private void descJugador(String[] partes) {
    }

    /*Metodo que realiza las acciones asociadas al comando 'describir avatar'.
    * Parámetro: id del avatar a describir.
    */
    private void descAvatar(String ID) {
    }

    /* Metodo que realiza las acciones asociadas al comando 'describir nombre_casilla'.
    * Parámetros: nombre de la casilla a describir.
    */
    private void descCasilla(String nombre) {
    }

    //Metodo que ejecuta todas las acciones relacionadas con el comando 'lanzar dados'.
    private void lanzarDados() {
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
    }

    // Metodo que realiza las acciones asociadas al comando 'listar avatares'.
    private void listarAvatares() {
    }

    // Metodo que realiza las acciones asociadas al comando 'acabar turno'.
    private void acabarTurno() {
    }
}