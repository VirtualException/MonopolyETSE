package monopoly;

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
    public boolean contruir() {

        /* Evaluar normas de edificación para el juagdor que quiere contruir */

        /* Tipo inválido */
        if (!"casa hotel piscina pista".contains(tipo)) {
            System.out.println("El edificio no es de un tipo válido.");
            return true;
        }

        /* Comprobar si alguna regla no se cumple */
        if (!casilla.getTipo().equals("Solar")) {
            System.out.println("La casilla no es un solar.");
            return true;
        }
        if (!grupo.esDuenhoGrupo(duenho)) {
            System.out.println("El jugador no es el dueño del grupo.");
            return true;
        }
        if (!casilla.haCaidoDosVeces(duenho)) {
            System.out.println("El jugador no ha caído dos veces aquí.");
            return true;
        }

        this.id = generarID(tipo);

        /* El multiplicador depende del tipo */
        float mult = switch (tipo) {
            case "casa" -> Valor.MULTIPLICADOR_CASA;
            case "hotel" -> Valor.MULTIPLICADOR_HOTEL;
            case "piscina" -> Valor.MULTIPLICADOR_PISCINA;
            case "pista" -> Valor.MULTIPLICADOR_PISTA_DE_DEPORTE;
            default -> 1.f;
        };

        this.coste = casilla.getValor() * mult;

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
    private String generarID(String tipo){

        int contadorTipo = 0;

        for(Edificio e : casilla.getEdificios()){
            if(e.getTipo().equals(tipo)){
                contadorTipo++;
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

    //public static ArrayList<Edificio> getEdificios() {
    //    return edificios;
    //}



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

    //public static void setEdificios(ArrayList<Edificio> edificios) {
    //    Edificio.edificios = edificios;
    //}
}