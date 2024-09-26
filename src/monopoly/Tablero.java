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
        this.banca  = banca;
    }

    
    // Metodo para crear todas las casillas del tablero. Formado a su vez por cuatro métodos (1/lado).
    private void generarCasillas() {

        // 0 sur, 1 oeste, 2 norte, 3 este
        posiciones.add(new ArrayList<Casilla>());
        posiciones.add(new ArrayList<Casilla>());
        posiciones.add(new ArrayList<Casilla>());
        posiciones.add(new ArrayList<Casilla>());

        ArrayList<Casilla> sur = posiciones.get(0); // Parte sur
        sur.add(new Casilla("Salida", "Salida",     0, 0,           null));
        sur.add(new Casilla("Solar1", "Solar",      1,600_000,      null));
        sur.add(new Casilla("Caja", "Caja",         2, 0,           null));
        sur.add(new Casilla("Solar2", "Solar",      3, 600_000,     null));
        sur.add(new Casilla("Imp1", "Impuesto",     4, 0,           null));
        sur.add(new Casilla("Trans1", "Transporte", 5, 0,           null));
        sur.add(new Casilla("Solar3", "Solar",      6, 520_000,     null));
        sur.add(new Casilla("Suerte", "Suerte",     7, 0,           null));
        sur.add(new Casilla("Solar4", "Solar",      8, 520_000,     null));
        sur.add(new Casilla("Solar5", "Solar",      9, 520_000,     null));

        ArrayList<Casilla> oeste = posiciones.get(1); // Parte sur
        oeste.add(new Casilla("Cárcel", "Carcel",   0, 0,           null));
        oeste.add(new Casilla("Solar6", "Solar",    1, 676_000,     null));
        oeste.add(new Casilla("Serv1", "Servicios", 2, 0,           null));
        oeste.add(new Casilla("Solar7", "Solar",    3, 676_000,     null));
        oeste.add(new Casilla("Solar8", "Solar",    4, 676_000,     null));
        oeste.add(new Casilla("Trans2", "Transporte",5,0,           null));
        oeste.add(new Casilla("Solar9", "Solar",    6, 878_800,     null));
        oeste.add(new Casilla("Caja", "Caja",       7, 0,           null));
        oeste.add(new Casilla("Solar10", "Solar",   8, 878_800,     null));
        oeste.add(new Casilla("Solar11", "Solar",   9, 878_800,     null));

        ArrayList<Casilla> norte = posiciones.get(2); // Parte norte
        norte.add(new Casilla("Cárcel", "Carcel",   0, 0,           null));
        norte.add(new Casilla("Solar6", "Solar",    1,600_000,      null));
        norte.add(new Casilla("Serv1", "Servicios", 2, 0,           null));
        norte.add(new Casilla("Solar7", "Solar",    3, 600_000,     null));
        norte.add(new Casilla("Solar8", "Solar",    4, 0,           null));
        norte.add(new Casilla("Trans2", "Transporte",5, 0,          null));
        norte.add(new Casilla("Solar8", "Solar",    6, 1_560_000,   null));
        norte.add(new Casilla("Caja", "Caja",       7, 0,           null));
        norte.add(new Casilla("Solar10", "Solar",   8, 1_560_000,   null));
        norte.add(new Casilla("Solar11", "Solar",   9, 1_560_000,   null));

    }

    //Para imprimir el tablero, modificamos el método toString().
    @Override
    public String toString() {
        return "a";
    }
    
    //Método usado para buscar la casilla con el nombre pasado como argumento:
    public Casilla encontrar_casilla(String nombre) {
        return new Casilla();
    }
}
