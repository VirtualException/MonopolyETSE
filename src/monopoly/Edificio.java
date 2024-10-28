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

        /* Evaluar normas de edificación para el juagdor que quiere contruir */

        /* Tipo inválido */
        if (!"casa hotel piscina pista".contains(edificio.tipo)) {
            System.out.println("El edificio no es de un tipo válido.");
            return true;
        }

        /* Comprobar si alguna regla no se cumple */
        if (!edificio.casilla.getTipo().equals("Solar")) {
            System.out.println("La casilla no es un solar.");
            return true;
        }
        if (!edificio.grupo.esDuenhoGrupo(edificio.duenho)) {
            System.out.println("El jugador no es el dueño del grupo.");
            return true;
        }
        /*if (!edificio.casilla.haCaidoDosVeces(edificio.duenho)) {
            System.out.println("El jugador no ha caído dos veces aquí.");
            return true;
        }*/

        System.out.println("Contruyendo " + edificio.tipo + ".");

        /* (!) Contruye el edificio del tipo correspondiente si hay 4 del anterior tipo. */
        float mult;
        switch (edificio.tipo) {
            /* SI EL EDIFICIO ES CASA */
            case "casa":
                edificio.coste = edificio.casilla.getValor() * Valor.MULTIPLICADOR_CASA;
                if (edificio.casilla.getCasasN() == 4) {
                    System.out.println("Ya hay 4 contrucciones del mismo tipo.");
                    return true;
                }
                break;

            /* SI EL EDIFICIO ES HOTEL */
            case "hotel":
                edificio.coste = edificio.casilla.getValor() * Valor.MULTIPLICADOR_HOTEL;
                if (edificio.casilla.getHotelesN() == 4) {
                    System.out.println("Ya hay 4 contrucciones del tipo hotel.");
                    return true;
                }
                if (edificio.casilla.getCasasN() == 4) {
                    /* Eliminar casas */
                    edificio.casilla.getEdificios().removeIf(e -> e.tipo.equals("casa"));
                }
                else {
                    System.out.println("No hay suficientes casas para un hotel.");
                    return true;
                }
                break;

            /* SI EL EDIFICIO ES PISCINA */
            case "piscina":
                edificio.coste = edificio.casilla.getValor() * Valor.MULTIPLICADOR_PISCINA;
                if (edificio.casilla.getPiscinasN() == 4) {
                    System.out.println("Ya hay 4 contrucciones del tipo piscina.");
                    return true;
                }
                if (edificio.casilla.getHotelesN() == 4) {
                    /* Eliminar hoteles */
                    edificio.casilla.getEdificios().removeIf(e -> e.tipo.equals("hotel"));
                }
                else {
                    System.out.println("No hay suficientes hoteles para una piscina.");
                    return true;
                }
                break;

            /* SI EL EDIFICIO ES PISTA DE DEPORTE */
            case "pista":
                edificio.coste = edificio.casilla.getValor() * Valor.MULTIPLICADOR_PISTA_DE_DEPORTE;
                if (edificio.casilla.getPiscinasN() == 4) {
                    System.out.println("Ya hay 4 contrucciones del tipo pista de deporte.");
                    return true;
                }
                if (edificio.casilla.getPiscinasN() == 4) {
                    /* Eliminar piscinas */
                    edificio.casilla.getEdificios().removeIf(e -> e.tipo.equals("piscina"));
                }
                else {
                    System.out.println("No hay suficientes piscinas para una pista de deporte.");
                    return true;
                }
                break;

            /* TIPO INVÁLIDO */
            default:
                System.out.println("Tipo de contrucción inválido.");
                return true; /* Salir de la función con código de error */
        }

        edificio.id = edificio.generarID(edificio.tipo, tablero);

        /* El jugador gasta dinero */

        if (edificio.coste > edificio.duenho.getFortuna()) {
            System.out.println("Fortuna insuficiente para edificar.");
            return true;
        }

        System.out.println(edificio.id + " contruído.");

        edificio.duenho.setGastos(edificio.coste);
        edificio.duenho.sumarFortuna(-edificio.coste);
        edificio.duenho.setDineroInvertido(edificio.duenho.getDineroInvertido() + edificio.coste);

        /* Todo correcto */
        return false;
    }


    // Método para añadir un edificio al ArrayList de edificios.
    //public void anhadirEdificio(Edificio edificio){
    //    if(!edificios.contains(edificio)){
    //        edificios.add(edificio);
    //    } else {
    //        System.out.println("ERROR. Este edificio ya ha sido construído.");
    //    }
    //}



    // Método para eliminar un edificio del ArrayList de edificios.
    //public void eliminarEdificio(Edificio edificio){
    //    if(edificios.isEmpty() || !edificios.contains(edificio)){
    //        System.out.println("ERROR. No ha sido posible eliminar el edificio");
    //    } else {
    //        edificios.remove(edificio);
    //    }
    //}



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