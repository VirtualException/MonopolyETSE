package monopoly_tablero;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import monopoly_avatares.Avatar;
import monopoly_cartas.Cartas;
import monopoly_casillas.Casilla;
import monopoly_casillas.acciones.AccionCajaComunidad;
import monopoly_casillas.acciones.AccionSuerte;
import monopoly_casillas.especiales.Especial;
import monopoly_casillas.impuestos.Impuesto;
import monopoly_casillas.propiedades.Servicio;
import monopoly_casillas.propiedades.Solar;
import monopoly_casillas.propiedades.Transporte;
import monopoly_jugador.Jugador;


public class Tablero {
    //Atributos.
    private ArrayList<ArrayList<Casilla>> posiciones; //Posiciones del tablero: se define como un arraylist de arraylists de casillas (uno por cada lado del tablero).
    private HashMap<String, Grupo> grupos; //Grupos del tablero, almacenados como un HashMap con clave String (será el color del grupo).
    private Jugador banca; //Un jugador que será la banca.
    private Cartas cartas;


    //Constructor: únicamente le pasamos el jugador banca (que se creará desde el menú).
    public Tablero(Jugador banca) {
        this.posiciones = new ArrayList<>();
        this.grupos = new HashMap<>();
        this.banca = banca;
        this.cartas = new Cartas();
        generarCasillas();
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

        ladoNorte.add(new Especial("Parking", "Especial", 21, banca));
        Solar Solar12 = new Solar("Solar12", "Solar", 22, Valor.VALOR_GRUPO_VERMELLO / 3, banca);
        ladoNorte.add(Solar12);
        ladoNorte.add(new AccionSuerte("Suerte", "Suerte", 23, banca));
        Solar Solar13 = new Solar("Solar13", "Solar", 24, Valor.VALOR_GRUPO_VERMELLO / 3, banca);
        ladoNorte.add(Solar13);
        Solar Solar14 = new Solar("Solar14", "Solar", 25, Valor.VALOR_GRUPO_VERMELLO / 3, banca);
        ladoNorte.add(Solar14);
        ladoNorte.add(new Transporte("Trans3", "Transporte", 26, (float) Valor.SUMA_VUELTA, banca));
        Solar Solar15 = new Solar("Solar15", "Solar", 27, Valor.VALOR_GRUPO_MARRON / 3, banca);
        ladoNorte.add(Solar15);
        Solar Solar16 = new Solar("Solar16", "Solar", 28, Valor.VALOR_GRUPO_MARRON / 3, banca);
        ladoNorte.add(Solar16);
        ladoNorte.add(new Servicio("Serv2", "Servicio", 29, (float) (0.75f * Valor.SUMA_VUELTA), banca));
        Solar Solar17 = new Solar("Solar17", "Solar", 30, Valor.VALOR_GRUPO_MARRON / 3, banca);
        ladoNorte.add(Solar17);
        ladoNorte.add(new Especial("IrCarcel", "Especial", 31, banca));

        posiciones.add(ladoNorte);
        Grupo grupoRojo = new Grupo(Solar12, Solar13, Solar14, "rojo");
        Grupo grupoMarron = new Grupo(Solar15, Solar16, Solar17, "marron");
        grupos.put("rojo", grupoRojo);
        grupos.put("marron", grupoMarron);
        Solar12.setGrupo(grupos.get("rojo"));
        Solar13.setGrupo(grupos.get("rojo"));
        Solar14.setGrupo(grupos.get("rojo"));
        Solar15.setGrupo(grupos.get("marron"));
        Solar16.setGrupo(grupos.get("marron"));
        Solar17.setGrupo(grupos.get("marron"));
    }

    //Método para insertar las casillas del lado sur.
    private void insertarLadoSur() {
        ArrayList<Casilla> ladoSur = new ArrayList<>();

        ladoSur.add(new Especial("Salida", "Especial", 1, banca));
        Solar Solar1 = new Solar("Solar1", "Solar", 2, Valor.VALOR_GRUPO_NEGRO / 2, banca);
        ladoSur.add(Solar1);
        ladoSur.add(new AccionCajaComunidad("Caja", "Comunidad", 3, banca));
        Solar Solar2 = new Solar("Solar2", "Solar", 4, Valor.VALOR_GRUPO_NEGRO / 2, banca);
        ladoSur.add(Solar2);
        ladoSur.add(new Impuesto("Imp1", "Impuesto", 5, (float) Valor.SUMA_VUELTA, banca));
        ladoSur.add(new Transporte("Trans1", "Transporte", 6, (float) Valor.SUMA_VUELTA, banca));
        Solar Solar3 = new Solar("Solar3", "Solar", 7, Valor.VALOR_GRUPO_AZUL / 3, banca);
        ladoSur.add(Solar3);
        ladoSur.add(new AccionSuerte("Suerte", "Suerte", 8, banca));
        Solar Solar4 = new Solar("Solar4", "Solar", 9, Valor.VALOR_GRUPO_AZUL / 3, banca);
        ladoSur.add(Solar4);
        Solar Solar5 = new Solar("Solar5", "Solar", 10, Valor.VALOR_GRUPO_AZUL / 3, banca);
        ladoSur.add(Solar5);
        ladoSur.add(new Especial("Carcel", "Especial", 11, banca));

        posiciones.add(ladoSur);
        Grupo grupoNegro = new Grupo(Solar1, Solar2, "negro");
        Grupo grupoCyan = new Grupo(Solar3, Solar4, Solar5, "cyan");
        grupos.put("negro", grupoNegro);
        grupos.put("cyan", grupoCyan);
        Solar1.setGrupo(grupos.get("negro"));
        Solar2.setGrupo(grupos.get("negro"));
        Solar3.setGrupo(grupos.get("cyan"));
        Solar4.setGrupo(grupos.get("cyan"));
        Solar5.setGrupo(grupos.get("cyan"));
    }

    //Método que inserta casillas del lado oeste.
    private void insertarLadoOeste() {
        ArrayList<Casilla> ladoOeste = new ArrayList<>();

        Solar Solar6 = new Solar("Solar6", "Solar", 12, Valor.VALOR_GRUPO_ROSA / 3, banca);
        ladoOeste.add(Solar6);
        ladoOeste.add(new Servicio("Serv1", "Servicio", 13, (float) (0.75f * Valor.SUMA_VUELTA), banca));
        Solar Solar7 = new Solar("Solar7", "Solar", 14, Valor.VALOR_GRUPO_ROSA / 3, banca);
        ladoOeste.add(Solar7);
        Solar Solar8 = new Solar("Solar8", "Solar", 15, Valor.VALOR_GRUPO_ROSA / 3, banca);
        ladoOeste.add(Solar8);
        ladoOeste.add(new Transporte("Trans2", "Transporte", 16, (float) Valor.SUMA_VUELTA, banca));
        Solar Solar9 = new Solar("Solar9", "Solar", 17, Valor.VALOR_GRUPO_AMARELO / 3, banca);
        ladoOeste.add(Solar9);
        ladoOeste.add(new AccionCajaComunidad("Caja", "Comunidad", 18, banca));
        Solar Solar10 = new Solar("Solar10", "Solar", 19, Valor.VALOR_GRUPO_AMARELO / 3, banca);
        ladoOeste.add(Solar10);
        Solar Solar11 = new Solar("Solar11", "Solar", 20, Valor.VALOR_GRUPO_AMARELO / 3, banca);
        ladoOeste.add(Solar11);

        posiciones.add(ladoOeste);
        Grupo grupoMorado = new Grupo(Solar6, Solar7, Solar8, "morado");
        Grupo grupoAmarillo = new Grupo(Solar9, Solar10, Solar11, "amarillo");
        grupos.put("morado", grupoMorado);
        grupos.put("amarillo", grupoAmarillo);
        Solar6.setGrupo(grupos.get("morado"));
        Solar7.setGrupo(grupos.get("morado"));
        Solar8.setGrupo(grupos.get("morado"));
        Solar9.setGrupo(grupos.get("amarillo"));
        Solar10.setGrupo(grupos.get("amarillo"));
        Solar11.setGrupo(grupos.get("amarillo"));
    }

    //Método que inserta las casillas del lado este.
    private void insertarLadoEste() {
        ArrayList<Casilla> ladoEste = new ArrayList<>();

        Solar Solar18 = new Solar("Solar18", "Solar", 32, Valor.VALOR_GRUPO_VERDE / 3, banca);
        ladoEste.add(Solar18);
        Solar Solar19 = new Solar("Solar19", "Solar", 33, Valor.VALOR_GRUPO_VERDE / 3, banca);
        ladoEste.add(Solar19);
        ladoEste.add(new AccionCajaComunidad("Caja", "Comunidad", 34, banca));
        Solar Solar20 = new Solar("Solar20", "Solar", 35, Valor.VALOR_GRUPO_VERDE / 3, banca);
        ladoEste.add(Solar20);
        ladoEste.add(new Transporte("Trans4", "Transporte", 36, (float) Valor.SUMA_VUELTA, banca));
        ladoEste.add(new AccionSuerte("Suerte", "Suerte", 37, banca));
        Solar Solar21 = new Solar("Solar21", "Solar", 38, Valor.VALOR_GRUPO_AZUL_OSCURO / 2, banca);
        ladoEste.add(Solar21);
        ladoEste.add(new Impuesto("Imp2", "Impuesto", 39, (float) (0.5f * Valor.SUMA_VUELTA), banca));
        Solar Solar22 = new Solar("Solar22", "Solar", 40, Valor.VALOR_GRUPO_AZUL_OSCURO / 2, banca);
        ladoEste.add(Solar22);

        posiciones.add(ladoEste);
        Grupo grupoVerde = new Grupo(Solar18, Solar19, Solar20, "verde");
        Grupo grupoAzul = new Grupo(Solar21, Solar22, "azul");
        grupos.put("verde", grupoVerde);
        grupos.put("azul", grupoAzul);
        Solar18.setGrupo(grupos.get("verde"));
        Solar19.setGrupo(grupos.get("verde"));
        Solar20.setGrupo(grupos.get("verde"));
        Solar21.setGrupo(grupos.get("azul"));
        Solar22.setGrupo(grupos.get("azul"));
    }



    public String casillaToColorString(int casilla, String nombre) {

        /* Hay que convertir palabras en los valores de "Valor.ALGO" */
        String representacion;
        StringBuilder avataresConAmpersand = new StringBuilder();
        if (!encontrar_casilla(nombre).getAvatares().isEmpty()){
            avataresConAmpersand.append("&");
        }
        for (Avatar av: encontrar_casilla(nombre).getAvatares()){
            avataresConAmpersand.append(av.getId());
        }

        Casilla cas = encontrar_casilla(nombre);
        String scolor = "";
        if (cas instanceof Solar)
            if (((Solar)cas).getGrupo() != null) scolor = ((Solar)cas).getGrupo().getColorGrupo();
        else
            return "";

        switch (scolor) {
            case "rojo":
                representacion = String.format(Valor.RED, nombre, avataresConAmpersand);
                break;
            case "marron":
                representacion = String.format(Valor.BROWN, nombre, avataresConAmpersand);
                break;
            case "negro":
                representacion = String.format(Valor.BLACK, nombre, avataresConAmpersand);
                break;
            case "cyan":
                representacion = String.format(Valor.CYAN, nombre, avataresConAmpersand);
                break;
            case "amarillo":
                representacion = String.format(Valor.YELLOW, nombre, avataresConAmpersand);
                break;
            case "morado":
                representacion = String.format(Valor.PURPLE, nombre, avataresConAmpersand);
                break;
            case "verde":
                representacion = String.format(Valor.GREEN, nombre, avataresConAmpersand);
                break;
            case "azul":
                representacion = String.format(Valor.BLUE, nombre, avataresConAmpersand);
                break;
            default:
                /* por defecto */
                representacion = String.format(Valor.WHITE, nombre, avataresConAmpersand);
                break;
        }

        return representacion;
    }

    //Para imprimir el tablero, modificamos el método toString().
    @Override
    public String toString() {

        // Separadores con color aplicado
        String separador = String.format(Valor.RESET, (" " + "—".repeat(16)).repeat(11) + "\n");
        String separador2 = String.format(Valor.RESET, " " + "—".repeat(16) +  " ".repeat(154) + "—".repeat(16) + "\n");

        String str = "";
        str += separador;

        /* Lado de arriba */
        for (int i = 0; i < posiciones.get(2).size(); i++) {
            String cnom = casillaToColorString(posiciones.get(2).get(i).getPosicion(), posiciones.get(2).get(i).getNombre());

            int padding = 16 - getRealLength(cnom); // Usar la longitud real
            if (padding < 0) padding = 0; // Asegura que padding no sea negativo
            str += String.format(Valor.RESET, "|") + cnom + " ".repeat(padding);
        }
        str += String.format(Valor.RESET, "|\n");
        str += separador;

        /* Ambos lados */
        for (int i = 0; i < 9; i++) {
            /* Lado izquierdo */
            String cnomIzq = casillaToColorString(posiciones.get(1).get(8 - i).getPosicion(), posiciones.get(1).get(8 - i).getNombre());
            int paddingIzq = 16 - getRealLength(cnomIzq); // Usar la longitud real
            if (paddingIzq < 0) paddingIzq = 0; // Asegura que padding no sea negativo

            /* Lado derecho */
            String cnomDer = casillaToColorString(posiciones.get(3).get(i).getPosicion(), posiciones.get(3).get(i).getNombre());
            int paddingDer = 16 - getRealLength(cnomDer); // Usar la longitud real
            if (paddingDer < 0) paddingDer = 0; // Asegura que padding no sea negativo

            // Concatenar las casillas con colores y espacios en ambos lados
            str += String.format(Valor.RESET, "|") + cnomIzq + " ".repeat(paddingIzq)
                    + String.format(Valor.RESET, "|") + " ".repeat(152)
                    + String.format(Valor.RESET, "|") + cnomDer + " ".repeat(paddingDer)
                    + String.format(Valor.RESET, "|\n");

            if (i != 8) {
                str += separador2;
            }
        }

        str += separador;

        /* Lado de abajo */
        for (int i = posiciones.get(0).size() - 1; i >= 0; i--) {
            String cnom = casillaToColorString(posiciones.get(0).get(i).getPosicion(), posiciones.get(0).get(i).getNombre());
            int padding = 16 - getRealLength(cnom); // Usar la longitud real
            if (padding < 0) padding = 0; // Asegura que padding no sea negativo
            str += String.format(Valor.RESET, "|") + cnom + " ".repeat(padding);
        }
        str += String.format(Valor.RESET, "|\n");
        str += separador;

        return str;
    }


    // Método para obtener la longitud real de una cadena sin los caracteres ANSI
    private int getRealLength(String str) {
        return str.replaceAll("\u001B\\[[;\\d]*m", "").length(); // Elimina los códigos ANSI
    }



    //Método usado para buscar la casilla con el nombre pasado como argumento:
    public Casilla encontrar_casilla(String nombre) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < posiciones.get(i).size(); j++) {
                if (Objects.equals(posiciones.get(i).get(j).getNombre(), nombre)) {
                    return posiciones.get(i).get(j);
                }
            }
        }
        /* No se encontró */
        return null;
    }



    //Getters y Setters


    public ArrayList<ArrayList<Casilla>> getPosiciones() {
        return posiciones;
    }

    public void setPosiciones(ArrayList<ArrayList<Casilla>> posiciones) {
        this.posiciones = posiciones;
    }

    public HashMap<String, Grupo> getGrupos() {
        return grupos;
    }

    public void setGrupos(HashMap<String, Grupo> grupos) {
        this.grupos = grupos;
    }

    public Jugador getBanca() {
        return banca;
    }

    public void setBanca(Jugador banca) {
        this.banca = banca;
    }

    public Cartas getCartas() {
        return cartas;
    }

    public void setCartas(Cartas cartas) {
        this.cartas = cartas;
    }
}
