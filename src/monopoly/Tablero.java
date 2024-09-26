package monopoly;

import partida.*;
import java.util.ArrayList;
import java.util.HashMap;


public class Tablero {
    //Atributos.
    private ArrayList<ArrayList<Casilla>> posiciones; //Posiciones del tablero: se define como un arraylist de arraylists de casillas (uno por cada lado del tablero).
    private HashMap<String, Grupo> grupos; //Grupos del tablero, almacenados como un HashMap con clave String (será el color del grupo).
    private Jugador banca; //Un jugador que será la banca.

    //Constructor: únicamente le pasamos el jugador banca (que se creará desde el menú).
    public Tablero(Jugador banca) {
    }


    //Método para crear todas las casillas del tablero. Formado a su vez por cuatro métodos (1/lado).
    private void generarCasillas() {
        this.insertarLadoSur();
        this.insertarLadoOeste();
        this.insertarLadoNorte();
        this.insertarLadoEste();
    }

    //Método para insertar las casillas del lado norte.
    private void insertarLadoNorte() {
        ArrayList<Casilla> ladoNorte = new ArrayList<>();
        ladoNorte.add(new Casilla("Solar12","Solar",22,(2.2f*(float)Valor.FORTUNA_INICIAL),banca));
        ladoNorte.add(new Casilla("Suerte","Suerte",23,banca));
        ladoNorte.add(new Casilla("Solar13","Solar",24,(2.2f*(float)Valor.FORTUNA_INICIAL),banca));
        ladoNorte.add(new Casilla("Solar14","Solar",25,(2.2f*(float)Valor.FORTUNA_INICIAL),banca));
        ladoNorte.add(new Casilla("Trans3","Transporte",26,(float)Valor.SUMA_VUELTA,banca));
        ladoNorte.add(new Casilla("Solar15","Solar",27,(2.5f*(float)Valor.FORTUNA_INICIAL),banca));
        ladoNorte.add(new Casilla("Solar16","Solar",28,(2.5f*(float)Valor.FORTUNA_INICIAL),banca));
        ladoNorte.add(new Casilla("Serv2","Servicio",29,(0.75f*(float)Valor.SUMA_VUELTA),banca));
        ladoNorte.add(new Casilla("Solar17","Solar",30,(2.5f*(float)Valor.FORTUNA_INICIAL),banca));
        posiciones.add(ladoNorte);
    }

    //Método para insertar las casillas del lado sur.
    private void insertarLadoSur() {
        ArrayList<Casilla> ladoSur = new ArrayList<>();
        ladoSur.add(new Casilla("Salida","Especial", 1, banca));
        ladoSur.add(new Casilla("Solar1","Solar",2,(float)Valor.FORTUNA_INICIAL,banca));
        ladoSur.add(new Casilla("Caja","Comunidad",3,banca));
        ladoSur.add(new Casilla("Solar2","Solar",4,(float)Valor.FORTUNA_INICIAL,banca));
        ladoSur.add(new Casilla("Imp1",5,(float)Valor.SUMA_VUELTA,banca));
        ladoSur.add(new Casilla("Trans1","Transporte",6,(float)Valor.SUMA_VUELTA,banca));
        ladoSur.add(new Casilla("Solar3","Solar",7,(1.3f*((float)Valor.FORTUNA_INICIAL)),banca));
        ladoSur.add(new Casilla("Suerte","Suerte",8,banca));
        ladoSur.add(new Casilla("Solar4","Solar",9,(1.3f*((float)Valor.FORTUNA_INICIAL)),banca));
        ladoSur.add(new Casilla("Solar5","Solar",10,(1.3f*((float)Valor.FORTUNA_INICIAL)),banca));
        posiciones.add(ladoSur);
    }

    //Método que inserta casillas del lado oeste.
    private void insertarLadoOeste() {
        ArrayList<Casilla> ladoOeste = new ArrayList<>();
        ladoOeste.add(new Casilla("Carcel","Especial",11,banca));
        ladoOeste.add(new Casilla("Solar6","Solar",12,(1.6f*(float)Valor.FORTUNA_INICIAL),banca));
        ladoOeste.add(new Casilla("Serv1","Servicio",13,(0.75f*(float)Valor.SUMA_VUELTA),banca));
        ladoOeste.add(new Casilla("Solar7","Solar",14,(1.6f*(float)Valor.FORTUNA_INICIAL),banca));
        ladoOeste.add(new Casilla("Solar8","Solar",15,(1.6f*(float)Valor.FORTUNA_INICIAL),banca));
        ladoOeste.add(new Casilla("Trans2","Transporte",16,(float)Valor.SUMA_VUELTA,banca));
        ladoOeste.add(new Casilla("Solar9","Solar",17,(1.9f*(float)Valor.FORTUNA_INICIAL),banca));
        ladoOeste.add(new Casilla("Caja","Comunidad",18,banca));
        ladoOeste.add(new Casilla("Solar10","Solar",19,(1.9f*(float)Valor.FORTUNA_INICIAL),banca));
        ladoOeste.add(new Casilla("Solar11","Solar",20,(1.9f*(float)Valor.FORTUNA_INICIAL),banca));
        ladoOeste.add(new Casilla("Parking","Especial",21,banca));
        posiciones.add(ladoOeste);
    }

    //Método que inserta las casillas del lado este.
    private void insertarLadoEste() {
        ArrayList<Casilla> ladoEste = new ArrayList<>();
        ladoEste.add(new Casilla("IrCarcel","Especial",31,banca));
        ladoEste.add(new Casilla("Solar19","Solar",32,(2.8f*(float)Valor.FORTUNA_INICIAL),banca));
        ladoEste.add(new Casilla("Solar20","Solar",33,(2.8f*(float)Valor.FORTUNA_INICIAL),banca));
        ladoEste.add(new Casilla("Caja","Comunidad",34,banca));
        ladoEste.add(new Casilla("Solar21","Solar",35,(2.8f*(float)Valor.FORTUNA_INICIAL),banca));
        ladoEste.add(new Casilla("Trans3","Transporte",36,(float)Valor.SUMA_VUELTA,banca));
        ladoEste.add(new Casilla("Suerte","Suerte",37,banca));
        ladoEste.add(new Casilla("Solar22","Solar",38,(3.1f*(float)Valor.FORTUNA_INICIAL),banca));
        ladoEste.add(new Casilla("Imp2",39,(0.5f*(float)Valor.SUMA_VUELTA),banca));
        ladoEste.add(new Casilla("Solar23","Solar",40,(3.1f*(float)Valor.FORTUNA_INICIAL),banca));
        posiciones.add(ladoEste);
    }

    //Para imprimir el tablero, modificamos el método toString().
    @Override
    public String toString() {

        System.out.println();


    }

    //Método usado para buscar la casilla con el nombre pasado como argumento:
    public Casilla encontrar_casilla(String nombre){
    }
}