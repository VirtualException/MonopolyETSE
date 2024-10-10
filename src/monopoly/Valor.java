package monopoly;


public class Valor {
    //Se incluyen una serie de constantes útiles para no repetir valores.
    public static final float FORTUNA_BANCA = 500000; // Cantidad que tiene inicialmente la Banca
    public static final double FORTUNA_INICIAL = 9543076.28f; // Cantidad que recibe cada jugador al comenzar la partida
    public static final double SUMA_VUELTA = 1301328.584f; // Cantidad que recibe un jugador al pasar pos la Salida


    ////////////////////////// VALORES DE CADA GRUPO /////////////////////////////////
    public static final float VALOR_GRUPO_NEGRO = 1200000;
    public static final float VALOR_GRUPO_AZUL = (VALOR_GRUPO_NEGRO*1.3f);

    public static final float VALOR_GRUPO_ROSA = (VALOR_GRUPO_AZUL*1.3f);

    public static final float VALOR_GRUPO_AMARELO = (VALOR_GRUPO_ROSA*1.3f);

    public static final float VALOR_GRUPO_VERMELLO = (VALOR_GRUPO_AMARELO*1.3f);

    public static final float VALOR_GRUPO_MARRON = (VALOR_GRUPO_VERMELLO*1.3f);

    public static final float VALOR_GRUPO_VERDE = (VALOR_GRUPO_MARRON*1.3f);

    public static final float VALOR_GRUPO_AZUL_OSCURO = (VALOR_GRUPO_VERDE*1.3f);


    //Colores del texto:
    public static final String RESET = "\u001B[0m%s"; // Se añadió %s
    public static final String BLACK = "\u001B[30m%-8s %-7s\u001B[0m"; // Se añadió %s
    public static final String RED = "\u001B[31m%-8s %-7s\u001B[0m"; // Se añadió %s
    public static final String GREEN = "\u001B[32m%-8s %-7s\u001B[0m"; // Se añadió %s
    public static final String YELLOW = "\u001B[33m%-8s %-7s\u001B[0m"; // Se añadió %s
    public static final String BLUE = "\u001B[34m%-8s %-7s\u001B[0m"; // Se añadió %s
    public static final String PURPLE = "\u001B[35m%-8s %-7s\u001B[0m"; // Se añadió %s
    public static final String CYAN = "\u001B[36m%-8s %-7s\u001B[0m"; // Se añadió %s
    public static final String WHITE = "\u001B[37m%-8s %-7s\u001B[0m"; // Se añadió %s
    public static final String BROWN = "\u001B[38;5;94m%-8s %-7s\u001B[0m"; // Se añadió %s
    public static final String ROSA = "\u001B[35m%-8s %-7s\u001B[0m"; // Se añadió %s


    public static final float MULTIPLICADOR_CASA = (0.6f);
    public static final float MULTIPLICADOR_HOTEL = (0.6f);
    public static final float MULTIPLICADOR_PISCINA = (0.4f);
    public static final float MULTIPLICADOR_PISTA_DE_DEPORTE = (1.25f);


    public static final int ALQUILER_UNA_CASA = (5);
    public static final int ALQUILER_DOS_CASA = (15);
    public static final int ALQUILER_TRES_CASA = (35);
    public static final int ALQUILER_CUATRO_CASA = (50);
    public static final int ALQULER_HOTEL = (70);
    public static final int ALQUILER_PISCINA = (25);
    public static final int ALQUILER_PISTA_DE_DEPORTE = (25);

}