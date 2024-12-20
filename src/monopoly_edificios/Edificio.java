package monopoly_edificios;

import java.util.ArrayList;

import monopoly_casillas.Casilla;
import monopoly_casillas.propiedades.Solar;
import monopoly_exception.casillas.DuenhoGrupoException;
import monopoly_exception.edificios.*;
import monopoly_exception.valores.ValorNoPermitidoException;
import monopoly_juego.Juego;
import monopoly_jugador.Jugador;
import monopoly_tablero.Grupo;
import monopoly_tablero.Tablero;
import monopoly_tablero.Valor;

public class Edificio {

    protected String id;
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
    static public Edificio construir(String tipo, Jugador j, Tablero tablero) throws TipoEdificioException, DuenhoGrupoException, MaximoEdificiosException, EdificarHotelException, EdificarPiscinaException, EdificarPistaException{

        Edificio edificio = null;
        Solar casilla = (Solar) j.getAvatar().getLugar();
        Grupo grupo = casilla.getGrupo();
        float coste = casilla.getPrecioOriginal();

        /* Evaluar normas de edificación para el jugador que quiere construir */

        /* Tipo inválido */
        if (!"casa hotel piscina pista".contains(tipo)) {
            throw new TipoEdificioException("El edificio no es de un tipo válido.");
        }

        /* Comprobar si alguna regla no se cumple */
        if (!casilla.getTipo().equals("Solar")) {
            throw new TipoEdificioException("La casilla no es un solar, no se puede contruir.");
        }
        
        boolean puedeConstruir = grupo.esDuenhoGrupo(j) || casilla.haCaidoMasDosVeces(j);
        if (!puedeConstruir) {
            throw new DuenhoGrupoException("El jugador no es el dueño del grupo o no ha caído más de 2 veces en la casilla.");
        }

        int numPistasEdificables = grupo.numEdificiosEdificablesGrupo("pista");
        int numPiscinasEdificables = grupo.numEdificiosEdificablesGrupo("piscina");
        int numHotelesEdificables = grupo.numEdificiosEdificablesGrupo("hotel");
        int numCasasEdificables = grupo.numEdificiosEdificablesGrupo("casa");

        if (numCasasEdificables == 0 &&
            numHotelesEdificables == 0 &&
            numPiscinasEdificables == 0 &&
            numPistasEdificables == 0) {
            throw new MaximoEdificiosException("Máximo de edificios alcanzados!");
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
                    throw new MaximoEdificiosException("Máximo de edificio alcanzado.");
                }
                edificio = new Casa(j);
                edificio.setCoste(precio_original * Valor.MULTIPLICADOR_CASA);
                break;

            /* SI EL EDIFICIO ES HOTEL */
            case "hotel":
                /* Comprobar máximo de edificios. */
                if (numHotelesEdificables == 0) {
                    throw new MaximoEdificiosException("Máximo de edificio alcanzado.");
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
                    throw new EdificarHotelException("No hay suficientes casas para un hotel.");
                }
                break;

            /* SI EL EDIFICIO ES PISCINA */
            case "piscina":
                /* Comprobar máximo de edificios. */
                if (numPiscinasEdificables == 0) {
                    throw new MaximoEdificiosException("Máximo de edificio alcanzado.");
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
                    throw new EdificarPiscinaException("No hay suficientes hoteles para una piscina.");
                }
                break;

            /* SI EL EDIFICIO ES PISTA DE DEPORTE */
            case "pista":
                if (numPistasEdificables == 0) {
                    throw new MaximoEdificiosException("Ya hay 2 contrucciones del tipo pista de deporte. Máximo de edificio alcanzado.");
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
                    throw new EdificarPistaException("No hay suficientes hoteles para una pista de deporte.");
                }
                break;

            /* TIPO INVÁLIDO */
            default:
            throw new TipoEdificioException("Tipo de contrucción inválido.");
        }

        edificio.setId(edificio.generarID(edificio.tipo, tablero));

        /* El jugador gasta dinero */

        if (coste > j.getFortuna()) {
            throw new ValorNoPermitidoException("Fortuna insuficiente para edificar.");
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