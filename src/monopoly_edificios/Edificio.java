package monopoly_edificios;

import java.util.ArrayList;

import monopoly_casillas.Casilla;
import monopoly_casillas.propiedades.Solar;
import monopoly_juego.Juego;
import monopoly_jugador.Jugador;
import monopoly_tablero.Grupo;
import monopoly_tablero.Tablero;
import monopoly_tablero.Valor;

public class Edificio {

    private String id;
    private Jugador duenho;
    private Solar solar;
    private Grupo grupo;
    private float coste;
    private String tipo;


    // Constructor edificio
    public Edificio(Jugador j, String tipo) {

        /* Solo inicializamos lo básico, el resto solo es necesario si el edificio se puede contruir */
        this.duenho = j;
        this.solar = (Solar) j.getAvatar().getLugar();
        this.grupo = solar.getGrupo();
        this.tipo = tipo;
    }

    /* Construye edificio evaluando si se puede o no. Devuelve 1 si no se puede */
    static public Edificio construir(String tipo, Jugador j, Tablero tablero) {

        Edificio edificio = null;
        Solar casilla = (Solar) j.getAvatar().getLugar();
        Grupo grupo = casilla.getGrupo();
        float coste = casilla.getPrecioOriginal();

        /* Evaluar normas de edificación para el jugador que quiere construir */

        /* Tipo inválido */
        if (!"casa hotel piscina pista".contains(tipo)) {
            Juego.consola.imprimir("El edificio no es de un tipo válido.");
            return null;
        }

        /* Comprobar si alguna regla no se cumple */
        if (!casilla.getTipo().equals("Solar")) {
            Juego.consola.imprimir("La casilla no es un solar, no se puede contruir.");
            return null;
        }
        
        boolean puedeConstruir = grupo.esDuenhoGrupo(j) || casilla.haCaidoMasDosVeces(j);
        if (!puedeConstruir) {
            Juego.consola.imprimir("El jugador no es el dueño del grupo o no ha caído más de 2 veces en la casilla.");
            return null;
        }

        int numPistasEdificables = grupo.numEdificiosEdificablesGrupo("pista");
        int numPiscinasEdificables = grupo.numEdificiosEdificablesGrupo("piscina");
        int numHotelesEdificables = grupo.numEdificiosEdificablesGrupo("hotel");
        int numCasasEdificables = grupo.numEdificiosEdificablesGrupo("casa");

        if (numCasasEdificables == 0 &&
            numHotelesEdificables == 0 &&
            numPiscinasEdificables == 0 &&
            numPistasEdificables == 0) {
            Juego.consola.imprimir("Máximo de edificios alcanzados!");
            return null;
        }

        Juego.consola.imprimir("Contruyendo " + tipo + ".");

        /* Contruye el edificio del tipo correspondiente si hay 4 del anterior tipo. */

        /* Coste original */
        float precio_original = casilla.getPrecioOriginal();

        //float mult;
        switch (tipo) {
            /* SI EL EDIFICIO ES CASA */
            case "casa":
                /* Comprobar máximo de edificios. */
                if (numCasasEdificables == 0 || casilla.getCasasN() == 4) {
                    Juego.consola.imprimir("Máximo de edificio alcanzado.");
                    return null;
                }
                edificio = new Casa(j);
                edificio.setCoste(precio_original * Valor.MULTIPLICADOR_CASA);
                break;

            /* SI EL EDIFICIO ES HOTEL */
            case "hotel":
                /* Comprobar máximo de edificios. */
                if (numHotelesEdificables == 0) {
                    Juego.consola.imprimir("Máximo de edificio alcanzado.");
                    return null;
                }
                if (casilla.getCasasN() == 4) {
                    /* Eliminar casas */
                    for(int i=0; i<4; i++){
                        casilla.eliminarEdificio("casa");
                    }
                    edificio = new Hotel(j);
                    edificio.setCoste(precio_original * Valor.MULTIPLICADOR_HOTEL);
                }
                else {
                    Juego.consola.imprimir("No hay suficientes casas para un hotel.");
                    return null;
                }
                break;

            /* SI EL EDIFICIO ES PISCINA */
            case "piscina":
                /* Comprobar máximo de edificios. */
                if (numPiscinasEdificables == 0) {
                    Juego.consola.imprimir("Máximo de edificio alcanzado.");
                    return null;
                }
                if (casilla.getHotelesN() >= 1 && casilla.getCasasN() >= 2) {
                    /* Eliminar hoteles */
                    casilla.eliminarEdificio("hotel");
                    for(int i = 0; i < 2; i++){
                        casilla.eliminarEdificio("casa");
                    }
                    edificio = new Piscina(j);
                    edificio.setCoste(precio_original * Valor.MULTIPLICADOR_PISCINA);
                }
                else {
                    Juego.consola.imprimir("No hay suficientes hoteles para una piscina.");
                    return null;
                }
                break;

            /* SI EL EDIFICIO ES PISTA DE DEPORTE */
            case "pista":
                if (numPistasEdificables == 0) {
                    Juego.consola.imprimir("Ya hay 2 contrucciones del tipo pista de deporte. Máximo de edificio alcanzado.");
                    return null;
                }
                if (casilla.getHotelesN() >= 2) {
                    /* Eliminar hoteles */
                    for(int i = 0; i < 2; i++) {
                        casilla.getEdificios().removeIf(e -> e.tipo.equals("hotel"));
                    }
                    edificio = new PistaDeporte(j);
                    edificio.setCoste(precio_original * Valor.MULTIPLICADOR_PISTA_DE_DEPORTE);
                }
                else {
                    Juego.consola.imprimir("No hay suficientes hoteles para una pista de deporte.");
                    return null;
                }
                break;

            /* TIPO INVÁLIDO */
            default:
                Juego.consola.imprimir("Tipo de contrucción inválido.");
                return null; /* Salir de la función con código de error */
        }

        edificio.setId(edificio.generarID(edificio.tipo, tablero));

        /* El jugador gasta dinero */

        if (coste > j.getFortuna()) {
            Juego.consola.imprimir("Fortuna insuficiente para edificar.");
            return null;
        }

        Juego.consola.imprimir(edificio.id + " contruído.");

        j.setGastos(coste);
        j.sumarFortuna(-coste);
        j.setDineroInvertido(j.getDineroInvertido() + coste);

        /* Todo correcto */
        return edificio;
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
        cadena += ("\tcasilla: " + solar.getNombre() + "," + "\n");
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

    public Solar getCasilla() {
        return this.solar;
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

    public void setCasilla(Solar casilla) {
        this.solar = casilla;
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