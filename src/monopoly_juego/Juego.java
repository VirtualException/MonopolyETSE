package monopoly_juego;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import monopoly_avatares.Avatar;
import monopoly_casillas.Casilla;
import monopoly_consola.Consola;
import monopoly_consola.ConsolaNormal;
import monopoly_dados.Dado;
import monopoly_edificios.Edificio;
import monopoly_jugador.Jugador;
import monopoly_tablero.Grupo;
import monopoly_tablero.Tablero;
import monopoly_tablero.Valor;
import monopoly_tratos.Trato;

public class Juego implements Comandos{

    //Atributos
    private ArrayList<Jugador> jugadores; //Jugadores de la partida.
    private ArrayList<Avatar> avatares; //Avatares en la partida.
    private int turno; //Índice correspondiente a la posición en el arrayList del jugador (y el avatar) que tienen el turno
    //private int lanzamientos; //Variable para contar el número de lanzamientos de un jugador en un turno.
    private Tablero tablero; //Tablero en el que se juega.
    private Dado dado1; //Dos dados para lanzar y avanzar casillas.
    private Dado dado2;
    private Jugador banca; //El jugador banca.
    private boolean tirado; //Booleano para comprobar si el jugador que tiene el turno ha tirado o no.
    public static ConsolaNormal CONSOLA = new ConsolaNormal();
    private ArrayList<Trato> tratos;
    
    public static Consola consola;

    public Juego() {
        iniciarPartida();
    }


    // Método para inciar una partida: crea los jugadores y avatares.
    private void iniciarPartida() {

        /* Crear banca y tablero */
        avatares = new ArrayList<>();
        jugadores = new ArrayList<>();
        banca = new Jugador("Banca", "Banca", null, null, -1); /* No tiene casilla ni avatar asociado */
        tablero = new Tablero(banca);
        turno = 0;
        tratos = new ArrayList<>();
        consola = new ConsolaNormal();

        dado1 = new Dado();
        dado2 = new Dado();

        tirado = false;

        consola.imprimir(String.valueOf(this.tablero));

        Scanner scan = new Scanner(System.in);
        boolean sair = false;


        crearJugador("Pepe", "Pelota");
        //crearJugador("Ana", "Pelota");

        //lanzarDados(1, 2);
        //acabarTurno();

        //lanzarDados(5, 1);
        //acabarTurno();

        while (!sair) {
            consola.imprimir_sin_salto("$> ");
            sair = this.analizarComando(scan.nextLine());
        }
        scan.close();
    }

    
    /*Método que interpreta el comando introducido y toma la accion correspondiente.
    * Parámetro: cadena de caracteres (el comando).
    */
    public boolean analizarComando(String comando) {

        String[] comandos_args = new String[5];
        String[] split = comando.split(" ");
        int num_args = split.length;
        System.arraycopy(split, 0, comandos_args, 0, num_args);

        boolean tiene_deudas = false;
        if (!jugadores.isEmpty()) {
            Jugador j = jugadores.get(turno);
            tiene_deudas = j.getDeuda() > 0.f;
            if (tiene_deudas) {
                if (j.getDeuda() <= j.getFortuna()) {
                    j.sumarFortuna(-j.getDeuda());
                    j.setDeuda(0);
                    tiene_deudas = false;
                }
            }
        }
        if (tiene_deudas)
            consola.imprimir("Atención! Debes solucionar tu deuda de " + (jugadores.get(turno).getDeuda()) + "! Vende o hipoteca propiedades.");

        boolean avanzarPelota = jugadores.get(turno).getDebeContinuar() > 0;

        /* Crear jugador, junto a su avatar */
        if (comandos_args[0].equals("crear") && comandos_args[1].equals("jugador") && num_args == 4) {
            crearJugador(comandos_args[2], comandos_args[3]);
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
        } else if (comandos_args[0].equals("listar") && comandos_args[1].equals("edificios") && num_args == 3){
            String colorGrupo = comandos_args[2];
            Grupo grupo = tablero.getGrupos().get(colorGrupo);
            if(grupo != null){
                grupo.stringEdificosGrupo();
            }
        }
        /* Ver tablero */
        else if (comandos_args[0].equals("ver") && comandos_args[1].equals("tablero") && num_args == 2) {
            consola.imprimir(String.valueOf(this.tablero));
        }
        /* Acciones jugador */
        else if (comandos_args[0].equals("acabar") && comandos_args[1].equals("turno") && num_args == 2) {
            if (tiene_deudas || avanzarPelota) return false;
            acabarTurno();
        }
        else if (comandos_args[0].equals("comprar") && num_args == 2){
            if (tiene_deudas) return false;
            comprar(comandos_args[1]);
        } else if (comandos_args[0].equals("vender") && num_args == 4){
            String tipoEdificio = comandos_args[1];
            String nombreCasilla = comandos_args[2];
            int numEdificios = Integer.parseInt(comandos_args[3]);
            venderEdificios(jugadores.get(turno), tipoEdificio, nombreCasilla, numEdificios);
            /* Actualizar estado de deudas. */
            if (tiene_deudas) {
                float deuda = jugadores.get(turno).getDeuda();
                if (deuda >= 0.f) {
                    tiene_deudas = false;
                    consola.imprimir("El jugador ya no tiene deudas.");
                }
            }
        }
        else if (comandos_args[0].equals("edificar") && num_args == 2){
            if (tiene_deudas) return false;
            edificar(comandos_args[1]);
        }
        else if (comandos_args[0].equals("lanzar") && comandos_args[1].equals("dados") && num_args == 2) {
            if (tiene_deudas || avanzarPelota) return false;
            lanzarDados(-1, -1); // Modo aleatorio
        }
        else if (comandos_args[0].equals("lanzar") && comandos_args[1].equals("dados") && num_args == 4) {
            tirado = false; /* Puede tirar de nuevo, es modo manual. */
            lanzarDados(Integer.parseInt(comandos_args[2]), Integer.parseInt(comandos_args[3])); // Modo manual
            incrementarSolares();
        }
        /* Describir */
        else if (comandos_args[0].equals("describir") && comandos_args[1].equals("avatar") && num_args == 3) {
            if (jugadores.isEmpty()){
                consola.imprimir("No hay avatares registrados");
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
                consola.imprimir("No hay jugadores registrados");
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
            if (tiene_deudas) return false;
            if (jugadores.get(turno).isEnCarcel()) {
                salirCarcel();
            } else {
                consola.imprimir("El jugador no se encuentra en la cárcel.");
            }
        } else if (comandos_args[0].equals("hipotecar") && num_args == 2) {
            String nombreCasilla = comandos_args[1];
            hipotecarPropiedad(jugadores.get(turno), tablero.encontrar_casilla(nombreCasilla));
            /* Actualizar estado de deudas. */
            if (tiene_deudas) {
                float deuda = jugadores.get(turno).getDeuda();
                if (deuda >= 0.f) {
                    tiene_deudas = false;
                    consola.imprimir("El jugador ya no tiene deudas.");
                }
            }
        } else if (comandos_args[0].equals("deshipotecar") && num_args == 2){
            String nombreCasilla = comandos_args[1];
            deshipotecarPropiedad(jugadores.get(turno), tablero.encontrar_casilla(nombreCasilla));
            /* Actualizar estado de deudas. */
            if (tiene_deudas) {
                float deuda = jugadores.get(turno).getDeuda();
                if (deuda >= 0.f) {
                    tiene_deudas = false;
                    consola.imprimir("El jugador ya no tiene deudas.");
                }
            }
        } else if (comandos_args[0].equals("bancarrota") && num_args == 1) {
            if (avanzarPelota) return false;
            /* No funciona como debería. */
            bancarrota(jugadores.get(turno).isPagarBanca());
        } else if (comandos_args[0].equals("estadisticas") && num_args == 2) {
            String nombreJugador = comandos_args[1];
            estadisticasJugador(nombreJugador);
        } else if (comandos_args[0].equals("estadisticas") && num_args == 1) {
            estadisticas();
        } else if (comandos_args[0].equals("tratos") && num_args == 1) {
            listarTratos();
        } else if (comandos_args[0].equals("eliminar") && num_args == 2) {
            String nombreTratoAEliminar = comandos_args[1]; 
            eliminarTrato(nombreTratoAEliminar);
        } else if (comandos_args[0].equals("aceptar") && num_args == 2) {
            String nombreTrato = comandos_args[1];
            aceptarTrato(nombreTrato);
        } else if (comandos_args[0].equals("trato") && num_args == 5) {  //Ejemplo de uso: trato Luis: cambiar (Solar1, Solar2)
            String nombreDestinatario = comandos_args[1].replace(":", "");
            Casilla propiedadOfrecida = null;
            Casilla propiedadSolicitada = null;

            float cantidadOfrecida = 0f;
            float cantidadSolicitada = 0f;
            
            try{            
                cantidadOfrecida = Float.parseFloat(comandos_args[3].replaceAll("[(,]", ""));    
            }
            catch(NumberFormatException e){
                propiedadOfrecida = tablero.encontrar_casilla(comandos_args[3].replaceAll("[(,]", ""));
            }

            try{            
                cantidadSolicitada = Float.parseFloat(comandos_args[4].replace(")", ""));    
            }
            catch(NumberFormatException e){
                propiedadSolicitada = tablero.encontrar_casilla(comandos_args[4].replace(")", ""));
            }    

            proponerTrato(nombreDestinatario, propiedadOfrecida, propiedadSolicitada, cantidadOfrecida, cantidadSolicitada);
        } else if (comandos_args[0].equals("trato") && num_args == 7) {
            String nombreDestinatario = comandos_args[1].replaceAll(":", "");
            Casilla propiedadOfrecida = null;
            Casilla propiedadSolicitada = null;

            float cantidadOfrecida = 0f;
            float cantidadSolicitada = 0f;

            if (comandos_args[5].equals("y")) {
                propiedadOfrecida = tablero.encontrar_casilla(comandos_args[3].replaceAll("[(,]", ""));
                propiedadSolicitada = tablero.encontrar_casilla(comandos_args[4]);
                cantidadSolicitada = Float.parseFloat(comandos_args[6].replaceAll(")", ""));

                proponerTrato(nombreDestinatario, propiedadOfrecida, propiedadSolicitada, cantidadOfrecida, cantidadSolicitada);
            } else if (comandos_args[4].equals("y")) {
                propiedadOfrecida = tablero.encontrar_casilla(comandos_args[3].replaceAll("(", ""));
                cantidadOfrecida = Float.parseFloat(comandos_args[5].replaceAll(",", ""));
                propiedadSolicitada = tablero.encontrar_casilla(comandos_args[6].replaceAll(")", ""));

                proponerTrato(nombreDestinatario, propiedadOfrecida, propiedadSolicitada, cantidadOfrecida, cantidadSolicitada);
            } else {
                consola.imprimir("Error. Formato inválido");
            }
        }
        /* Comando salida */
        else if (comandos_args[0].equals("exit") && num_args == 1) {
            return true;
        }
        else if (comandos_args[0].equals("cero")) {
            jugadores.get(turno).setFortuna(0);
        }
        else if(comandos_args[0].equals("pelota")) {
            /* Seguir con elmovimiento en modo Pelota */
            if (avanzarPelota)
                jugadores.get(turno).moverJugador(tablero, 0, jugadores);
        }
        else if (comandos_args[0].equals("modo") && comandos_args[1].equals("avanzado") && num_args == 2) {
            if (avanzarPelota) return false;
            consola.imprimir("A partir de ahora, el jugador " + jugadores.get(turno).getAvatar().getId() + " de tipo " + jugadores.get(turno).getAvatar().getTipo() + ", se moverá en modo Avanzado");
            jugadores.get(turno).setModo(true);
        }
        else if (comandos_args[0].equals("modo") && comandos_args[1].equals("simple") && num_args == 2) {
            if (avanzarPelota) return false;
            consola.imprimir("A partir de ahora, el jugador " + jugadores.get(turno).getAvatar().getId() + " de tipo " + jugadores.get(turno).getAvatar().getTipo() + ", se moverá en modo Simple");
            jugadores.get(turno).setModo(false);
        } else {
            consola.imprimir("Comando no reconocido.");
        }

        return false;
    }

    @Override
    public void crearJugador(String nombre, String tipo) {
        jugadores.add(new Jugador(nombre, tipo, tablero.encontrar_casilla("Salida"), avatares, jugadores.size()));
        avatares.add(jugadores.getLast().getAvatar());

        tablero.encontrar_casilla("Salida").anhadirAvatar(avatares.getLast());

        consola.imprimir("{");
        consola.imprimir("\tnombre: " + jugadores.getLast().getNombre() + ",");
        consola.imprimir("\tavatar: " + jugadores.getLast().getAvatar().getId());
        consola.imprimir("}");
    }

    /*Método que realiza las acciones asociadas al comando 'describir jugador'.
    * Parámetro: comando introducido
     */
    @Override
    public void descJugador(String nombre) {

        for (Jugador j : jugadores) {
            if (Objects.equals(j.getNombre(), nombre)) {
                consola.imprimir_sin_salto("{\n");
                consola.imprimir_sin_salto("\tnombre: " + j.getNombre()  + "," + "\n");
                consola.imprimir_sin_salto("\tavatar: " + j.getAvatar().getId() + "," + "\n");
                consola.imprimir_sin_salto("\tfortuna: " + j.getFortuna() + "," + "\n");
                consola.imprimir_sin_salto("\tpropiedades: [");
                for (Casilla p : j.getPropiedades()) {
                    consola.imprimir_sin_salto(p.getNombre() + ", ");
                }
                consola.imprimir("]");

                if(j.getHipotecas().isEmpty()){
                    consola.imprimir("\thipotecas: -");
                } else {
                    consola.imprimir_sin_salto("\thipotecas: [");
                    for (Casilla c : j.getHipotecas()) {
                        consola.imprimir(c.getNombre() + ", ");
                    }
                    consola.imprimir_sin_salto("]");
                }
                
                if(j.getEdificios().isEmpty()){
                    consola.imprimir("\tedificos: -");
                    consola.imprimir("},");
                } else {
                    consola.imprimir_sin_salto("\tedificios: [");
                    for (Edificio e : j.getEdificios()) {
                        consola.imprimir_sin_salto(e.getId() + ", ");
                    }
                    consola.imprimir_sin_salto("]\n},\n");
                }
            }
        }
    }

    /*Método que realiza las acciones asociadas al comando 'describir avatar'.
    * Parámetro: id del avatar a describir.
    */

    @Override
    public void descAvatar(String ID) {
        for (Avatar i : avatares) {
            if (Objects.equals(i.getId(), ID)) {
                consola.imprimir_sin_salto("{\n");
                consola.imprimir_sin_salto("\tid: " + i.getId() + "," + "\n");
                consola.imprimir_sin_salto("\ttipo: " + i.getTipo() + "," + "\n");
                consola.imprimir_sin_salto("\tcasilla: " + i.getLugar().getNombre() + "," + "\n");
                consola.imprimir_sin_salto("\tjugador: " + i.getJugador().getNombre());
                consola.imprimir_sin_salto("\n},\n");
            }
        }
    }

    @Override
    public void descTurno() {

        if (jugadores.isEmpty()) {
            consola.imprimir("No hay jugadores.");
            return;
        }

        String nombre = jugadores.get(turno).getNombre();
        String avatar = jugadores.get(turno).getAvatar().getId();
        consola.imprimir("{\n");
        consola.imprimir_sin_salto("\tnombre: " + nombre  + "," + "\n");
        consola.imprimir_sin_salto("\tavatar: " + avatar);
        consola.imprimir_sin_salto("\n}\n");
    }

    /* Método que realiza las acciones asociadas al comando 'describir nombre_casilla'.
    * Parámetros: nombre de la casilla a describir.
    */
    public void descCasilla(String nombre) {
        Casilla c = tablero.encontrar_casilla(nombre);
        if (c == null) {
            consola.imprimir("Esa casilla no existe.");
            return;
        }
        consola.imprimir(c.infoCasilla());
    }

    // Método que ejecuta todas las acciones relacionadas con el comando 'lanzar dados'.
    @Override
    public void lanzarDados(int a, int b) {

        if (jugadores.isEmpty()) {
            consola.imprimir("No hay jugadores!");
            return;
        }
        Jugador j = jugadores.get(turno);
        if (this.tirado) {
            consola.imprimir("El jugador ya tiró.");
            return;
        }
        consola.imprimir_sin_salto("El jugador " + j.getNombre() + " tira los dados. ");
        j.setTiradas(j.getTiradas() + 1);

        if (a == -1)
            dado1.hacerTirada();
        else
            dado1.hacerTirada(a);
        if (b == -1)
            dado2.hacerTirada();
        else
            dado2.hacerTirada(b);

        consola.imprimir("La tirada es: " + dado1.getValor() + ", " + dado2.getValor() + ".");
        /* El jugador no puede tirar de nuevo */
        this.tirado = true;




        /* Comprobar si las tiradas son iguales */
        if (dado1.equals(dado2)) {

            consola.imprimir("Doble!");

            /* Sale de la cárcel */
            if(j.isEnCarcel()){
                j.setTiradasCarcel(j.getTiradasCarcel() + 1);
                consola.imprimir("El jugador sale de la cárcel, puede tirar de nuevo");
                j.setEnCarcel(false);
                this.tirado = false;
                return;
            }

            /* Simplemente saca doble */
            j.setTiradasDobles(j.getTiradasDobles() + 1);

            /* Ir a la cárcel porque tira 3 dobles seguidos */
            if (j.getTiradasDobles() >= 3) {
                consola.imprimir("A la cárcel!");
                j.encarcelar(tablero.getPosiciones());
                j.setTiradasDobles(0);
                this.acabarTurno();
            }

            /* Puede tirar de nuevo */
            this.tirado = false;

        } else {
            if (j.isEnCarcel()) {
                if (j.getTiradasCarcel() >= 3) {
                    consola.imprimir("El jugador debe pagar para salír de la cárcel.");

                    float pago = (float) Valor.SUMA_VUELTA * 0.25f;

                    if (pago > j.getFortuna()) {
                        consola.imprimir("Dinero insuficiente para salir de la cárcel. El jugador ahora tiene una deuda y debe solucionarla.");
                        j.setDeuda(pago);
                        return;
                    }

                    j.setGastos(pago);
                    j.setPagoTasasEImpuestos(j.getPagoTasasEImpuestos() + pago);
                    j.sumarFortuna(-pago);

                    return;
                }
                j.setTiradasDobles(0);
            }
        }

        if(!j.isEnCarcel()){
            /* mover jugador, etc.. */
            consola.imprimir("Moviendo jugador...");
            j.sumarTirada_Turno(); /* Tiró otra vez */
            tirado = j.moverJugador(tablero, dado1.getValor() + dado2.getValor(), jugadores);

        }
        else {
            consola.imprimir("El jugador sigue en la cárcel.");
        }

        consola.imprimir(String.valueOf(tablero));
    }


    /* Ya no hace falta */
    //private void lanzarDadosManual(int a, int b)

    /*Método que ejecuta todas las acciones realizadas con el comando 'comprar nombre_casilla'.
    * Parámetro: cadena de caracteres con el nombre de la casilla.
     */
    @Override
    public void comprar(String nombre) {

        Jugador jugadorActual = jugadores.get(turno);
        Avatar av = jugadorActual.getAvatar();
        Casilla casillaActual = tablero.encontrar_casilla(nombre); 

        //Comprobamos que el nombre de la casilla que queremos comprar existe.
        if (casillaActual == null) {
            consola.imprimir("Esta casilla no existe.");
            return;
        }
        //Comprobamos que la casilla en la que se encuentra el avatar es la casilla que se quiere comprar.
        if(!av.getLugar().equals(casillaActual)){
            consola.imprimir("No se puede comprar esta casilla porque no estás en ella.");
            return;
        }

        boolean compraExitosa = casillaActual.comprarCasilla(jugadorActual, banca);

        jugadorActual.setDineroInvertido(jugadorActual.getDineroInvertido() + casillaActual.getValor());

        if(compraExitosa){
            consola.imprimir("El jugador " + jugadorActual.getNombre() + " compra la casilla " + nombre + " por " +
            casillaActual.getValor() + "€. Su fortuna actual es " + jugadorActual.getFortuna() + "€.");
        }
    }

    /* Contruir edificio */
    @Override
    public void edificar(String tipo) {
        Jugador j = jugadores.get(turno);

        /* El edificio es el encargado de comprobar las reglas, y en caso de
        éxito, se añade a la lista de edificios. Además se suman los gastos. */

        Edificio e = Edificio.construir(tipo, j, tablero);

        if (e != null) {
            /* Si todo va bien, el edificio se añade a la lista de edificios  */
            ArrayList<Edificio> edificios = j.getEdificios();
            edificios.add(e);
            consola.imprimir("Añadiendo edificio a la lista de la casilla...");
            j.getAvatar().getLugar().setEdificios(edificios);
            consola.imprimir("Edificio de tipo \"" + e.getTipo() + "\" añadido.");
        }
        else {
            consola.imprimir("El edificio no se contruyó.");
        }
    }

    // Método que ejecuta todas las acciones relacionadas con el comando 'salir carcel'.
    @Override
    public void salirCarcel() {
        consola.imprimir(jugadores.get(turno).getNombre() + " paga " + Valor.SUMA_VUELTA*0.25f + " y sale de la cárcel. Puede lanzar los dados.");
        tirado = false;
        jugadores.get(turno).setEnCarcel(false);
        banca.sumarFortuna((float) (Valor.SUMA_VUELTA*0.25f));
        jugadores.get(turno).sumarGastos((float) (Valor.SUMA_VUELTA*0.25f));
        jugadores.get(turno).sumarFortuna((float) (Valor.SUMA_VUELTA*(-0.25f)));
        jugadores.get(turno).setPagoTasasEImpuestos(jugadores.get(turno).getPagoTasasEImpuestos() + (float) (Valor.SUMA_VUELTA*0.25f));
    }

    // Método que realiza las acciones asociadas al comando 'listar enventa'.
    @Override
    public void listarVenta() {
        for (ArrayList <Casilla> casillas : tablero.getPosiciones()){
            for (Casilla c : casillas){
                if (c.getDuenho().getNombre().equals("Banca")){
                    consola.imprimir(c.casEnVenta());
                }
            }
        }
    }

    // Método que realiza las acciones asociadas al comando 'listar jugadores'.
    @Override
    public void listarJugadores() {
        if (jugadores.isEmpty()){
            consola.imprimir("No hay jugadores registrados");
        } else {
            for (Jugador j : jugadores) {
                descJugador(j.getNombre());
            }
        }
    }

    // Método que realiza las acciones asociadas al comando 'listar avatares'.
    @Override
    public void listarAvatares() {

        if (jugadores.isEmpty()){
            consola.imprimir("No hay avatares registrados");
        } else {
            for (Avatar a : avatares) {
                descAvatar(a.getId());
            }
        }
    }



    // Método para listar los edificios construidos por cada jugador
    @Override 
    public void listarEdificios() {
        for (Jugador j : jugadores) {
            if (j.getEdificios().isEmpty()) {
                consola.imprimir("El jugador " + j.getNombre() + " no ha construido edificios.");
            } else {
                consola.imprimir("Edificios del jugador " + j.getNombre() + ":");
                for (Edificio e : j.getEdificios()) {
                    consola.imprimir(e.stringEdificio());
                }
            }
        }
    }



    // Método para vender edificios
    @Override
    public void venderEdificios(Jugador jugador, String tipoEdificio, String nombreCasila, int numEdificios){
        jugador.venderEdificios(tipoEdificio, nombreCasila, numEdificios);
    }


    
    // Método para imprimir las estadisticas de un jugador.
    @Override
    public void estadisticasJugador(String nombreJugador){
        boolean encontrado = false;
        
        for(Jugador j : jugadores){
            if(j.getNombre().equals(nombreJugador)){
                encontrado = true;
                consola.imprimir(j.estadisticasJugador(j));
                break;
            }
        }

        if(!encontrado){
            consola.imprimir("ERROR. El jugador " + nombreJugador + " no existe.");
        }
    }

    @Override
    public void estadisticas() {

        /* Casilla más rentable */
        float max = -1;
        Casilla casillaMasRentable = null;
        for (ArrayList<Casilla> lado : tablero.getPosiciones()) {
            for (Casilla c : lado) {
                if (c.getValor() > max) {
                    max = c.getValor();
                    casillaMasRentable = c;
                }
            }
        }
    
        /* Grupo más rentable */
        max = -1;
        Grupo grupoMasRentable = null;
        for (Grupo g : tablero.getGrupos().values()) {
            if (g.getValorTotal() > max) {
                max = g.getValorTotal();
                grupoMasRentable = g;
            }
        }
    
        /* Casilla más frecuentada */
        max = -1;
        Casilla casillaMasFrecuentada = null;
        for (ArrayList<Casilla> lado : tablero.getPosiciones()) {
            for (Casilla c : lado) {
                float frecuencia = 0;
                for (Jugador j : jugadores) {
                    frecuencia += c.getContarCaer(j);
                }
                if (frecuencia > max) {
                    max = frecuencia;
                    casillaMasFrecuentada = c;
                }
            }
        }
    
        /* Jugador con más vueltas */
        max = -1;
        Jugador jugadorMasVueltas = null;
        for (Jugador j : jugadores) {
            if (j.getVueltas() > max) {
                max = j.getVueltas();
                jugadorMasVueltas = j;
            }
        }
    
        /* Jugador con más lanzamientos de dados */
        max = -1;
        Jugador jugadorMasVecesDados = null;
        for (Jugador j : jugadores) {
            if (j.getTiradas() > max) {
                max = j.getTiradas();
                jugadorMasVecesDados = j;
            }
        }
    
        /* Jugador en cabeza por fortuna */
        max = -1;
        Jugador jugadorEnCabeza = null;
        for (Jugador j : jugadores) {
            if (j.getFortuna() > max) {
                max = j.getFortuna();
                jugadorEnCabeza = j;
            }
        }
    
        /* Comprobar si se encontraron resultados y mostrarlos */
        if (casillaMasRentable != null && grupoMasRentable != null && casillaMasFrecuentada != null &&
            jugadorMasVueltas != null && jugadorMasVecesDados != null && jugadorEnCabeza != null) {
            
            String cadena = "{\n";
            cadena += "\tcasillaMasRentable: " + casillaMasRentable.getNombre() + ",\n";
            cadena += "\tgrupoMasRentable: " + grupoMasRentable.getColorGrupo() + ",\n";
            cadena += "\tcasillaMasFrecuentada: " + casillaMasFrecuentada.getNombre() + ",\n";
            cadena += "\tjugadorMasVueltas: " + jugadorMasVueltas.getNombre() + ",\n";
            cadena += "\tjugadorMasVecesDados: " + jugadorMasVecesDados.getNombre() + ",\n";
            cadena += "\tjugadorEnCabeza: " + jugadorEnCabeza.getNombre() + "\n";
            cadena += "}";
    
            consola.imprimir(cadena);
        } else {
            consola.imprimir("No se pudieron calcular todas las estadísticas, verifica los datos.");
        }
    }
    



    // Método para declararse en bancarrota
    @Override
    public void bancarrota(boolean pagarBanca) {
        Jugador jugadorActual = jugadores.get(turno);
        jugadorActual.bancarrota(jugadores, banca, pagarBanca);
    }


    // Método para hipotecar una propiedad
    @Override
    public void hipotecarPropiedad(Jugador jugador, Casilla c) {
        jugador.hipotecarPropiedad(c);
    }



    // Método para deshipotecar una propiedad
    @Override
    public void deshipotecarPropiedad(Jugador jugador, Casilla c) {
        jugador.deshipotecarPropiedad(c);
    }



    // Método que realiza las acciones asociadas al comando 'acabar turno'.
    @Override
    public void acabarTurno() {

        Jugador j = jugadores.get(turno);

        j.setTiradasDobles(0);
        this.tirado = false;

        turno++;
        turno = turno % jugadores.size();

        j = jugadores.get(turno);

        consola.imprimir("El jugador actual es " + j.getNombre() + ". A " + j.getNombre() + " le han propuesto los siguientes tratos: ");
        listarTratos();
    }

    /* Método para incrementar el precio de los Solares un 5% si todos los jugadores han
     * completado 4 vueltas y los solares no han sido comprados previamente */
     @Override
    public void incrementarSolares() {
        boolean vueltasCompletadas = true;

        // Comprobamos que todos los jugadores han completado 4 vueltas

        for(Jugador j : jugadores){
            if(j.getVueltas() < 4){
                vueltasCompletadas = false;
                break;
            }
        }

        /* Si no se dieron 4 vueltas, salimos. */
        if (!vueltasCompletadas)
            return;

        /* Si se dieron, incrementamos. */

        // Aumentamos el precio de los Solares
        for (ArrayList<Casilla> lados : tablero.getPosiciones()) {
            for (Casilla c : lados) {
                if (!c.getAvatares().isEmpty() && c.getDuenho().getNombre().equals("Banca")) {
                    c.setValor(c.getValor() * 1.05f);
                }
            }
        }

        /* Reseteamos las vueltas de los jugadores */
        for (Jugador j : jugadores) {
            j.setVueltas(0);
        }
    }

    // Función para listar la información de los tratos de un jugador 
    @Override
    public void listarTratos(){

        StringBuilder cadena = new StringBuilder();
        String nombreJugador = this.jugadores.get(turno).getNombre();

        if (!tratos.isEmpty()) {
            for (Trato trato: this.tratos){
                if (trato.getDestinatario().getNombre().equals(nombreJugador)){
                    cadena.append("{\n");
                    cadena.append("\tid: " + trato.getId() + "\n");
                    cadena.append("\tjugadorPropone: " + trato.getProponente().getNombre() + ",\n");
                    cadena.append("\ttrato: " + descripcionTrato(trato) + "\n");
                    cadena.append("},\n");
                }
            }
        } else {
            consola.imprimir("No hay tratos propuestos para " + nombreJugador);
            return;
        }
    }


    private String descripcionTrato(Trato trato) {
        StringBuilder descripcion = new StringBuilder();

        if (trato.getPropiedadOfrecida() != null && trato.getPropiedadSolicitada() != null) {
            if (trato.getCantidadSolicitada() <= 0 && trato.getCantidadOfrecida() <= 0) {
                descripcion.append("cambiar (" + trato.getPropiedadOfrecida().getNombre() + ", " + trato.getPropiedadSolicitada().getNombre() + ")");
            }
            else {
                if (trato.getCantidadSolicitada() > 0 && trato.getCantidadOfrecida() <= 0) {
                    descripcion.append("cambiar (" + trato.getPropiedadOfrecida().getNombre() + ", " + trato.getPropiedadSolicitada().getNombre() + " y " + trato.getCantidadSolicitada() + "€)");
                } else if (trato.getCantidadSolicitada() <= 0 && trato.getCantidadOfrecida() > 0) {
                    descripcion.append("cambiar (" + trato.getPropiedadOfrecida().getNombre() + " y " + trato.getCantidadOfrecida() + "€, " + trato.getPropiedadSolicitada().getNombre() + ")");
                }
            }
        } else if (trato.getCantidadOfrecida() <= 0 && trato.getPropiedadOfrecida() != null && trato.getCantidadSolicitada() > 0) {
            descripcion.append("cambiar (" + trato.getPropiedadOfrecida().getNombre() + ", " + trato.getCantidadSolicitada() + "€)");
        } else if (trato.getCantidadOfrecida() > 0 && trato.getPropiedadSolicitada() != null && trato.getCantidadSolicitada() <= 0) {
            descripcion.append("cambiar (" + trato.getCantidadOfrecida() + "€, " + trato.getPropiedadSolicitada().getNombre() + ")");
        }

        return descripcion.toString();
    }


    @Override
    public void eliminarTrato(String id){
        Trato tratoAEliminar = null;

        for (Trato trato : this.tratos) {
            if (trato.getId().equals(id)){
                tratoAEliminar = trato;
            } 
        }
        
        if (tratoAEliminar == null){
            consola.imprimir("ERROR. Este trato no existe");
        }
        else {
            String nombreJugadorActual = this.jugadores.get(turno).getNombre();
            if (tratoAEliminar.getProponente().getNombre().equals(nombreJugadorActual)){
                consola.imprimir("ERROR. El trato no le pertenece al jugador");
            }
            else if (tratoAEliminar.getAceptado() == Boolean.TRUE){
                consola.imprimir("ERROR. El trato fue aceptado, no se puede eliminar");
            }
            else {
                this.tratos.remove(tratoAEliminar);
                consola.imprimir("Se ha eliminado el trato " + id + ".");
            }
        }
    }


    @Override
    public void aceptarTrato(String id) {
        Trato tratoAAceptar = null;

        for (Trato trato : this.tratos) {
            if (trato.getId().equals(id)){
                tratoAAceptar = trato;
            }
        }

        if (tratoAAceptar == null){
            consola.imprimir("ERROR. Este trato no existe");
            return;
        }

        // Verificamos si la propiedad ofrecida está hipotecada 
        boolean propiedadOfrecidaHipotecada = tratoAAceptar.getPropiedadOfrecida().getHipotecada();

        if (propiedadOfrecidaHipotecada){
            String respuesta = this.respuestaAceptarTrato();
            if (respuesta.equals("n")) {
                consola.imprimir("El trato se ha rechazado.");
                return;
            }
        }
        // Realizamos el intercambio

        if (tratoAAceptar.getPropiedadOfrecida() != null && tratoAAceptar.getPropiedadSolicitada() != null){
            if (tratoAAceptar.getCantidadSolicitada() <= 0 && tratoAAceptar.getCantidadOfrecida() <= 0){
                tratoAAceptar.cambiarPropiedadPorPropiedad();
            }
            else {
                if (tratoAAceptar.getCantidadSolicitada() > 0 && tratoAAceptar.getCantidadOfrecida() <= 0) {
                    tratoAAceptar.cambiarPropiedadPorPropiedadYDinero();
                } else if (tratoAAceptar.getCantidadSolicitada() <= 0 && tratoAAceptar.getCantidadOfrecida() > 0) {
                    tratoAAceptar.cambiarPropiedadYDineroPorPropiedad();
                }
            }
        } else if (tratoAAceptar.getCantidadOfrecida() <= 0 && tratoAAceptar.getPropiedadOfrecida() != null && tratoAAceptar.getCantidadSolicitada() > 0){
            tratoAAceptar.cambiarPropiedadPorDinero();
        } else if (tratoAAceptar.getCantidadOfrecida() > 0 && tratoAAceptar.getPropiedadSolicitada() != null && tratoAAceptar.getCantidadSolicitada() <= 0){
            tratoAAceptar.cambiarDineroPorPropiedad();
        }
    }


    private String respuestaAceptarTrato() {
        Scanner scanner = new Scanner(System.in);
        String respuesta = null;

        while(respuesta == null || !respuesta.equals("s") || !respuesta.equals("n")){
            consola.imprimir("La propiedad a recibir es una hipoteca, quieres aceptar el trato? (s/n): ");
            respuesta = scanner.nextLine();
        }

        scanner.close();
        return respuesta;
    }





    @Override
    public void proponerTrato(String jugadorDestinatario, Casilla propiedadOfrecida, Casilla propiedadSolcitada, float cantidad1, float cantidad2) {
        Jugador jugadorProponente = jugadores.get(turno);
        Jugador destinatario = null;

        for (Jugador j : this.jugadores) {
            if (j.getNombre().equals(jugadorDestinatario)) {
                destinatario = j;
                break;
            }
        }

        if(propiedadOfrecida == null && propiedadSolcitada == null){ //COMPROBACIONES

        }

        if (destinatario == null) {
            consola.imprimir("ERROR. El jugador " + jugadorDestinatario + " no existe.");
            return;
        }

        Trato trato = new Trato(this.tratos, jugadorProponente, destinatario, propiedadOfrecida, propiedadSolcitada, cantidad1, cantidad2);
        this.tratos.add(trato);

        if (!trato.verificarTrato()) {
            return;
        }

        consola.imprimir(trato.mostrarTratoPropuesto());
    }
}