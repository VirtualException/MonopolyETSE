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

    // Constructor vacío
    public Edificio() {
    }


    // Constructor edificio
    public Edificio(Jugador duenho, Casilla casilla, Grupo grupo, String tipo, float coste){
        this.id = generarID(tipo);
        this.duenho = duenho;
        this.casilla = casilla;
        this.grupo = grupo;
        this.coste = coste;
        this.tipo = tipo;
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



    /* Los edificios se deberían listar desde la casilla no???? */
    // Método que devuelve una cadena con información de un edificio.
    public String imprimirEdificio(){
        String cadena = "";
        cadena = ("{\n");
        cadena += ("\tid: " + id + "," + "\n");
        cadena += ("\tpropietario: " + duenho.getNombre() + "," + "\n");
        cadena += ("\tcasilla: " + casilla.getNombre() + "," + "\n");
        cadena += ("\tgrupo: " + grupo.getColorGrupo() + "," + "\n");
        cadena += ("\tcoste: " + coste + "\n");
        cadena += ("},\n");

        return cadena;
    }




    public void construirEdificio(String nombreEdificio){



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