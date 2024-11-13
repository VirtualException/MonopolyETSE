package monopoly;

import java.util.ArrayList;
import partida.*;

public class Edificio {

    private String id;
    private Jugador duenho;
    private Casilla casilla;
    private Grupo grupo;
    private float coste;
    private String tipo;


    // Constructor edificio
    public Edificio(Jugador j, String tipo) {

        /* Solo inicializamos lo básico, el resto solo es necesario si el edificio se puede contruir */
        this.duenho = j;
        this.casilla = j.getAvatar().getLugar();
        this.grupo = casilla.getGrupo();
        this.tipo = tipo;
    }

    /* Construye edificio evaluando si se puede o no. Devuelve 1 si no se puede */
    static public boolean construir(Edificio edificio, Tablero tablero) {
        Grupo grupo = edificio.getGrupo();
        Casilla casilla = edificio.getCasilla();
        Jugador duenho = edificio.getDuenho();
        String tipo = edificio.getTipo();
        float coste = edificio.getCoste();

        /* Evaluar normas de edificación para el jugador que quiere construir */

        /* Tipo inválido */
        if (!"casa hotel piscina pista".contains(tipo)) {
            System.out.println("El edificio no es de un tipo válido.");
            return true;
        }

        /* Comprobar si alguna regla no se cumple */
        if (!casilla.getTipo().equals("Solar")) {
            System.out.println("La casilla no es un solar, no se puede contruir.");
            return true;
        }
        
        boolean puedeConstruir = grupo.esDuenhoGrupo(duenho) || casilla.haCaidoMasDosVeces(duenho);
        if (!puedeConstruir) {
            System.out.println("El jugador no es el dueño del grupo o no ha caído más de 2 veces en la casilla.");
            return true;
        }

        int numPistasEdificables = grupo.numEdificiosEdificablesGrupo("pista");
        int numPiscinasEdificables = grupo.numEdificiosEdificablesGrupo("piscina");
        int numHotelesEdificables = grupo.numEdificiosEdificablesGrupo("hotel");
        int numCasasEdificables = grupo.numEdificiosEdificablesGrupo("casa");

        if (numCasasEdificables == 0 &&
            numHotelesEdificables == 0 &&
            numPiscinasEdificables == 0 &&
            numPistasEdificables == 0) {
            System.out.println("Máximo de edificios alcanzados!");
            return true;
        }

        System.out.println("Contruyendo " + tipo + ".");

        /* Contruye el edificio del tipo correspondiente si hay 4 del anterior tipo. */

        /* Coste original */
        float precio_original = casilla.getPrecioOriginal();

        //float mult;
        switch (tipo) {
            /* SI EL EDIFICIO ES CASA */
            case "casa":
                /* Comprobar máximo de edificios. */
                if (numCasasEdificables == 0 || casilla.getCasasN() == 4) {
                    System.out.println("Máximo de edificio alcanzado.");
                }
                edificio.setCoste(precio_original * Valor.MULTIPLICADOR_CASA);
                break;

            /* SI EL EDIFICIO ES HOTEL */
            case "hotel":
                /* Comprobar máximo de edificios. */
                if (numHotelesEdificables == 0) {
                    System.out.println("Máximo de edificio alcanzado.");
                }
                if (casilla.getCasasN() == 4) {
                    /* Eliminar casas */
                    for(int i=0; i<4; i++){
                        casilla.eliminarEdificio("casa");
                    }
                    edificio.setCoste(precio_original * Valor.MULTIPLICADOR_HOTEL);
                }
                else {
                    System.out.println("No hay suficientes casas para un hotel.");
                    return true;
                }
                break;

            /* SI EL EDIFICIO ES PISCINA */
            case "piscina":
                /* Comprobar máximo de edificios. */
                if (numPiscinasEdificables == 0) {
                    System.out.println("Máximo de edificio alcanzado.");
                }
                if (casilla.getHotelesN() >= 1 && casilla.getCasasN() >= 2) {
                    /* Eliminar hoteles */
                    casilla.eliminarEdificio("hotel");
                    for(int i = 0; i < 2; i++){
                        casilla.eliminarEdificio("casa");
                    }
                    edificio.setCoste(precio_original * Valor.MULTIPLICADOR_PISCINA);
                }
                else {
                    System.out.println("No hay suficientes hoteles para una piscina.");
                    return true;
                }
                break;

            /* SI EL EDIFICIO ES PISTA DE DEPORTE */
            case "pista":
                if (numPistasEdificables == 0) {
                    System.out.println("Ya hay 2 contrucciones del tipo pista de deporte. Máximo de edificio alcanzado.");
                    return true;
                }
                if (casilla.getHotelesN() >= 2) {
                    /* Eliminar hoteles */
                    for(int i = 0; i < 2; i++) {
                        casilla.getEdificios().removeIf(e -> e.tipo.equals("hotel"));
                }
                    edificio.setCoste(precio_original * Valor.MULTIPLICADOR_PISTA_DE_DEPORTE);
                }
                else {
                    System.out.println("No hay suficientes hoteles para una pista de deporte.");
                    return true;
                }
                break;

            /* TIPO INVÁLIDO */
            default:
                System.out.println("Tipo de contrucción inválido.");
                return true; /* Salir de la función con código de error */
        }

        edificio.setId(edificio.generarID(edificio.tipo, tablero));

        /* El jugador gasta dinero */

        if (coste > duenho.getFortuna()) {
            System.out.println("Fortuna insuficiente para edificar.");
            return true;
        }

        System.out.println(edificio.id + " contruído.");

        duenho.setGastos(coste);
        duenho.sumarFortuna(-coste);
        duenho.setDineroInvertido(duenho.getDineroInvertido() + coste);

        /* Todo correcto */
        return false;
    }




    // Método para generar el ID del edificio.
    private String generarID(String tipo, Tablero tablero){

        int contadorTipo = 0;

        for(ArrayList<Casilla> arraylist : tablero.getPosiciones()){
            for(Casilla c : arraylist){
                for(Edificio e : c.getEdificios()){
                    if(e.getTipo().equals(tipo)){
                        contadorTipo++;
                    }
                }
            }
        }
        return tipo + "-" + (contadorTipo + 1);
    }


    // Método que devuelve una cadena con información de un edificio.
    public String stringEdificio(){
        String cadena;
        cadena = ("{\n");
        cadena += ("\tid: " + id + "," + "\n");
        cadena += ("\tpropietario: " + duenho.getNombre() + "," + "\n");
        cadena += ("\tcasilla: " + casilla.getNombre() + "," + "\n");
        cadena += ("\tgrupo: " + grupo.getColorGrupo() + "," + "\n");
        cadena += ("\tcoste: " + coste + "\n");
        cadena += ("},\n");

        return cadena;
    }



     
    // Getters y Setters


    public String getId() {
        return this.id;
    }

    public Jugador getDuenho() {
        return this.duenho;
    }

    public Casilla getCasilla() {
        return this.casilla;
    }

    public Grupo getGrupo() {
        return this.grupo;
    }

    public float getCoste() {
        return this.coste;
    }

    public String getTipo() {
        return this.tipo;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setDuenho(Jugador duenho) {
        this.duenho = duenho;
    }

    public void setCasilla(Casilla casilla) {
        this.casilla = casilla;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public void setCoste(float coste) {
        this.coste = coste;
    }

    public void setTipo(String tipo) {
        if (tipo.equals("casa") || tipo.equals("hotel") || tipo.equals("piscina") || tipo.equals("pista")) {
            this.tipo = tipo;
        }
    }
}