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
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    public static final String BROWN = "\u001B[38;5;94m";
}