package monopoly;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import partida.*;

public class Menu {

    //Atributos
    private ArrayList<Jugador> jugadores; //Jugadores de la partida.
    private ArrayList<Avatar> avatares; //Avatares en la partida.
    //private ArrayList<Edificio> edificios;  //Edificios en la partida.
    private int turno = 0; //Índice correspondiente a la posición en el arrayList del jugador (y el avatar) que tienen el turno
    //private int lanzamientos; //Variable para contar el número de lanzamientos de un jugador en un turno.
    private Tablero tablero; //Tablero en el que se juega.
    private Dado dado1; //Dos dados para lanzar y avanzar casillas.
    private Dado dado2;
    private Jugador banca; //El jugador banca.
    private boolean tirado; //Booleano para comprobar si el jugador que tiene el turno ha tirado o no.
    private boolean solvente; //Booleano para comprobar si el jugador que tiene el turno es solvente, es decir, si ha pagado sus deudas.


    public Menu() {
        iniciarPartida();
    }


    // Método para inciar una partida: crea los jugadores y avatares.
    private void iniciarPartida() {

        /* Crear banca y tablero */
        avatares = new ArrayList<>();
        jugadores = new ArrayList<>();
        banca = new Jugador("Banca", "Banca", null, null, -1); /* No tiene casilla ni avatar asociado */
        tablero = new Tablero(banca);

        dado1 = new Dado();
        dado2 = new Dado();

        tirado = false;
        solvente = false;

        System.out.println(this.tablero);

        Scanner scan = new Scanner(System.in);
        boolean sair = false;

        while (!sair) {
            System.out.print("$> ");
            sair = this.analizarComando(scan.nextLine());
        }
        scan.close();
    }

    
    /*Método que interpreta el comando introducido y toma la accion correspondiente.
    * Parámetro: cadena de caracteres (el comando).
    */
    private boolean analizarComando(String comando) {

        String[] comandos_args = new String[5];
        String[] split = comando.split(" ");
        int num_args = split.length;
        System.arraycopy(split, 0, comandos_args, 0, num_args);

        /* Crear jugador, junto a su avatar */
        if (comandos_args[0].equals("crear") && comandos_args[1].equals("jugador") && num_args == 4) {

            jugadores.add(new Jugador(comandos_args[2], comandos_args[3], tablero.encontrar_casilla("Salida"), avatares, jugadores.size()));
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
        } else if (comandos_args[0].equals("listar") && comandos_args[1].equals("edificios") && num_args == 2){
        listarEdificios();
        }
        /* Ver tablero */
        else if (comandos_args[0].equals("ver") && comandos_args[1].equals("tablero") && num_args == 2) {
            System.out.println(this.tablero);
        }
        /* Acciones jugador */
        else if (comandos_args[0].equals("acabar") && comandos_args[1].equals("turno") && num_args == 2) {
            acabarTurno();
        }
        else if (comandos_args[0].equals("comprar") && num_args == 2){
            comprar(comandos_args[1]);
        }
        else if (comandos_args[0].equals("edificar") && num_args == 2){
            edificar(comandos_args[1]);
        }
        else if (comandos_args[0].equals("lanzar") && comandos_args[1].equals("dados") && num_args == 2) {
            lanzarDados(-1, -1); // Modo aleatorio
        }
        else if (comandos_args[0].equals("lanzar") && comandos_args[1].equals("dados") && num_args == 4) {
            lanzarDados(Integer.parseInt(comandos_args[2]), Integer.parseInt(comandos_args[3])); // Modo manual
        }
        /* Describir */
        else if (comandos_args[0].equals("describir") && comandos_args[1].equals("avatar") && num_args == 3) {
            if (jugadores.isEmpty()){
                System.out.println("No hay avatares registrados");
                return false;
            }
            for (Avatar a : avatares){
                if (a.getId().equals(comandos_args[2])){
                    descAvatar(comandos_args[2]);
                    return false;  /* Ya imprimimos, así que salimos */
                }
            }
        } else if (comandos_args[0].equals("describir") && comandos_args[1].equals("jugador") && num_args == 3) {
            if (jugadores.isEmpty()){
                System.out.println("No hay jugadores registrados");
                return false;
            }
            for (Jugador j : jugadores){
                if (j.getNombre().equals(comandos_args[2])){
                    descJugador(comandos_args[2]);
                    return false; /* Ya imprimimos, así que salimos */
                }
            }
        } else if (comandos_args[0].equals("describir") && num_args == 2) {
            descCasilla(comandos_args[1]);
        } else if (comandos_args[0].equals("jugador") && num_args == 1) {
            descTurno();
        } else if (comandos_args[0].equals("salir") && comandos_args[1].equals("carcel") && num_args == 2) {
            if (jugadores.get(turno).isEnCarcel()) {
                salirCarcel();
            } else {
                System.out.println("El jugador no se encuentra en la cárcel.");
            }
        } else if (comandos_args[0].equals("bancarrota") && num_args == 1){
            bancarrota();
        } else if (comandos_args[0].equals("estadisticas") && num_args == 2){
            String nombreJugador = comandos_args[1];
            estadisticasJugador(nombreJugador);

        }
        /* Comando salida */
        else if (comandos_args[0].equals("exit") && num_args == 1) {
            return true;
        }

        else {
            System.out.println("Comando no reconocido.");
        }

        return false;
    }


    private void crearEdificio(){
        // Edificio = new Edificio ( ...)
        // casilla.addEdificio(edificio)
        // this.edificios.add(edificio)
        // 
    }

    /*Método que realiza las acciones asociadas al comando 'describir jugador'.
    * Parámetro: comando introducido
     */
    private void descJugador(String nombre) {

        for (Jugador i : jugadores) {
            if (Objects.equals(i.getNombre(), nombre)) {
                System.out.print("{\n");
                System.out.print("\tnombre: " + i.getNombre()  + "," + "\n");
                System.out.print("\tavatar: " + i.getAvatar().getId() + "," + "\n");
                System.out.print("\tfortuna: " + i.getFortuna() + "," + "\n");
                System.out.print("\tpropiedades: [");
                for (Casilla p : i.getPropiedades()) {
                    System.out.print(p.getNombre() + ", ");
                }
                System.out.println("]");
                System.out.print("\thipotecas: [");
                System.out.println("]");
                System.out.print("\tedificios: [");
                for (Edificio e : i.getEdificios()) {
                    System.out.print(e.getId() + ", ");
                }
                System.out.print("]\n},\n");
            }
        }
    }

    /*Método que realiza las acciones asociadas al comando 'describir avatar'.
    * Parámetro: id del avatar a describir.
    */
    private void descAvatar(String ID) {
        for (Avatar i : avatares) {
            if (Objects.equals(i.getId(), ID)) {
                System.out.print("{\n");
                System.out.print("\tid: " + i.getId() + "," + "\n");
                System.out.print("\ttipo: " + i.getTipo() + "," + "\n");
                System.out.print("\tcasilla: " + i.getLugar().getNombre() + "," + "\n");
                System.out.print("\tjugador: " + i.getJugador().getNombre());
                System.out.print("\n},\n");
            }
        }
    }

    private void descTurno() {

        if (jugadores.isEmpty()) {
            System.out.println("No hay jugadores.");
            return;
        }

        String nombre = jugadores.get(turno).getNombre();
        String avatar = jugadores.get(turno).getAvatar().getId();
        System.out.println("{\n");
        System.out.print("\tnombre: " + nombre  + "," + "\n");
        System.out.print("\tavatar: " + avatar);
        System.out.print("\n}\n");
    }

    /* Método que realiza las acciones asociadas al comando 'describir nombre_casilla'.
    * Parámetros: nombre de la casilla a describir.
    */
    private void descCasilla(String nombre) {
        Casilla c = tablero.encontrar_casilla(nombre);
        if (c == null) {
            System.out.println("Esa casilla no existe.");
            return;
        }
        System.out.println(c.infoCasilla());
    }

    // Método que ejecuta todas las acciones relacionadas con el comando 'lanzar dados'.
    private void lanzarDados(int a, int b) {

        if (jugadores.isEmpty()) {
            System.out.println("No hay jugadores!");
            return;
        }
        Jugador j = jugadores.get(turno);
        if (this.tirado) {
            System.out.println("El jugador ya tiró.");
            return;
        }
        System.out.print("El jugador " + j.getNombre() + " tira los dados. ");

        if (a == -1)
            dado1.hacerTirada();
        else
            dado1.hacerTirada(a);
        if (b == -1)
            dado2.hacerTirada();
        else
            dado2.hacerTirada(b);

        System.out.println("La tirada es: " + dado1.getValor() + ", " + dado2.getValor() + ".");
        /* El jugador no puede tirar de nuevo */
        this.tirado = true;
        /* Comprobar si las tiradas son iguales. Se usa Override en la clase Dado */
        if (dado1.equals(dado2)) {

            System.out.println("Doble!");

            /* Sale de la cárcel */
            if(j.isEnCarcel()){
                j.setTiradasCarcel(j.getTiradasCarcel() + 1);
                if (j.getTiradasCarcel() >= 3) {
                    System.out.println("El jugador debe pagar para salír de la cárcel.");
                    return;
                }
                System.out.println("El jugador sale de la cárcel, puede tirar de nuevo");
                j.setEnCarcel(false);
                this.tirado = false;
                return;
            }

            /* Simplemente saca doble */
            j.setTiradasDobles(j.getTiradasDobles() + 1);

            /* Ir a la cárcel porque tira 3 dobles seguidos */
            if (j.getTiradasDobles() >= 3) {
                System.out.println("A la cárcel!");
                j.encarcelar(tablero.getPosiciones());
                j.setTiradasDobles(0);
                this.acabarTurno();
            }

            /* Puede tirar de nuevo */
            this.tirado = false;

        }

        if(!j.isEnCarcel()){
            /* mover jugador, etc.. */
            System.out.println("Moviendo jugador...");
            j.moverJugador(tablero, dado1.getValor() + dado2.getValor());
        }
        else {
            System.out.println("El jugador sigue en la cárcel.");
        }

        System.out.println(tablero);
    }


    /* Ya no hace falta */
    //private void lanzarDadosManual(int a, int b)

    /*Método que ejecuta todas las acciones realizadas con el comando 'comprar nombre_casilla'.
    * Parámetro: cadena de caracteres con el nombre de la casilla.
     */
    private void comprar(String nombre) {

        Jugador jugadorActual = jugadores.get(turno);
        Avatar av = jugadorActual.getAvatar();
        Casilla casillaActual = tablero.encontrar_casilla(nombre); 

        //Comprobamos que el nombre de la casilla que queremos comprar existe.
        if (casillaActual == null) {
            System.out.println("Esta casilla no existe.");
            return;
        }
        //Comprobamos que la casilla en la que se encuentra el avatar es la casilla que se quiere comprar.
        if(!av.getLugar().equals(casillaActual)){
            System.out.println("No se puede comprar esta casilla porque no estás en ella.");
            return;
        }

        boolean compraExitosa = casillaActual.comprarCasilla(jugadorActual, banca);

        if(compraExitosa){
            System.out.println("El jugador " + jugadorActual.getNombre() + " compra la casilla " + nombre + " por " +
            casillaActual.getValor() + "€. Su fortuna actual es " + jugadorActual.getFortuna() + "€.");
        }
    }

    /* Contruir edificio */
    private void edificar(String tipo) {
        Jugador j = jugadores.get(turno);

        /* El edificio es el encargado de comprobar las reglas, y en caso de éxito, se añade a la lista de edificios. */
        Edificio e = new Edificio(j, tipo);
        if (!Edificio.contruir(e, tablero)) {
            /* Si todo va bien, el edificio se añade a la lista de edificios  */
            ArrayList<Edificio> edificios = j.getEdificios();
            edificios.add(e);
            System.out.println("Añadiendo edificio a la lista de la casilla...");
            j.getAvatar().getLugar().setEdificios(edificios);
        }
        else {
            System.out.println("El edificio no se contruyó.");
        }
    }

    // Método que ejecuta todas las acciones relacionadas con el comando 'salir carcel'.
    private void salirCarcel() {
        System.out.println(jugadores.get(turno).getNombre() + " paga " + Valor.SUMA_VUELTA*0.25f + " y sale de la cárcel. Puede lanzar los dados.");
        tirado = false;
        jugadores.get(turno).setEnCarcel(false);
        banca.sumarFortuna((float) (Valor.SUMA_VUELTA*0.25f));
        jugadores.get(turno).sumarGastos((float) (Valor.SUMA_VUELTA*0.25f));
        jugadores.get(turno).sumarFortuna((float) (Valor.SUMA_VUELTA*(-0.25f)));
    }

    // Método que realiza las acciones asociadas al comando 'listar enventa'.
    private void listarVenta() {
        for (ArrayList <Casilla> casillas : tablero.getPosiciones()){
            for (Casilla c : casillas){
                if (c.getDuenho().getNombre().equals("Banca")){
                    System.out.println(c.casEnVenta());
                }
            }
        }
    }

    // Método que realiza las acciones asociadas al comando 'listar jugadores'.
    private void listarJugadores() {
        if (jugadores.isEmpty()){
            System.out.println("No hay jugadores registrados");
        } else {
            for (Jugador j : jugadores) {
                descJugador(j.getNombre());
            }
        }
    }

    // Método que realiza las acciones asociadas al comando 'listar avatares'.
    private void listarAvatares() {

        if (jugadores.isEmpty()){
            System.out.println("No hay avatares registrados");
        } else {
            for (Avatar a : avatares) {
                descAvatar(a.getId());
            }
        }
    }



    // Método para listar los edificios construídos.
    private void listarEdificios(){
        for (Jugador j : jugadores) {
            for (Edificio e : j.getEdificios()) {
                System.out.println(e.stringEdificio());
            }
        }
    }


    
    // Método para imprimir las estadisticas de un jugador.
    private void estadisticasJugador(String nombreJugador){
        boolean encontrado = false;
        
        for(Jugador j : jugadores){
            if(j.getNombre().equals(nombreJugador)){
                encontrado = true;
                System.out.println(j.estadisticasJugador(j));
                break;
            }
        }

        if(!encontrado){
            System.out.println("ERROR. El jugador " + nombreJugador + " no existe.");
        }
    }



    // Método para declararse en bancarrota
    private void bancarrota(){
        Jugador jugadorActual = jugadores.get(turno);
    /* ? */
        solvente = jugadorActual.getAvatar().getLugar().evaluarCasilla(tablero, jugadorActual ,banca);
        jugadorActual.bancarrota(jugadores, banca, solvente);
    }



    // Método que realiza las acciones asociadas al comando 'acabar turno'.
    private void acabarTurno() {

        Jugador j = jugadores.get(turno);

        j.setTiradasDobles(0);
        this.tirado = false;

        turno++;
        turno = turno % jugadores.size();

        j = jugadores.get(turno);

        System.out.println("El jugador actual es " + j.getNombre() + ".");

    }
}