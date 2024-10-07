package partida;

import java.util.ArrayList;
import java.util.Random;
import monopoly.*;


public class Avatar {

    //Atributos
    private String id; //Identificador: una letra generada aleatoriamente.
    private String tipo; //Sombrero, Esfinge, Pelota, Coche
    private Jugador jugador; //Un jugador al que pertenece ese avatar.
    private Casilla lugar; //Los avatares se sitúan en casillas del tablero.

    private static final char[] letrasDisponibles = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static int letrasRestantes = letrasDisponibles.length;

    //Constructor vacío
    public Avatar() {
    }

    /*Constructor principal. Requiere éstos parámetros:
     * Tipo del avatar, jugador al que pertenece, lugar en el que estará ubicado, y un arraylist con los
     * avatares creados (usado para crear un ID distinto del de los demás avatares).
     */
    public Avatar(String tipo, Jugador jugador, Casilla lugar, ArrayList<Avatar> avCreados) {
        this.generarId(avCreados);  //Crea un ID nuevo para un avatar distinto de los creados anteriormente
        this.tipo = tipo;
        this.jugador = jugador;
        this.lugar = lugar;
    }

    //A continuación, tenemos otros métodos útiles para el desarrollo del juego.
    /*Método que permite mover a un avatar a una casilla concreta. Parámetros:
     * - Un array con las casillas del tablero. Se trata de un arrayList de arrayList de casillas (uno por lado).
     * - Un entero que indica el numero de casillas a moverse (será el valor sacado en la tirada de los dados).
     * EN ESTA VERSIÓN SUPONEMOS QUE valorTirada siemrpe es positivo.
     */
    public void moverAvatar(ArrayList<ArrayList<Casilla>> casillas, int valorTirada) {

        int nuevaPosicion = this.lugar.getPosicion() + valorTirada;

        if(nuevaPosicion > 40){
            jugador.setVueltas(jugador.getVueltas() + 1);
            jugador.sumarFortuna((float)Valor.SUMA_VUELTA);
            nuevaPosicion -= 40;    
        } 

        for(ArrayList<Casilla> arrayList : casillas){
            for(Casilla casilla : arrayList){
                if(casilla.getPosicion() == nuevaPosicion){
                    this.lugar = casilla;
                }
            }
        }
        
    }

    /*Método que permite generar un ID para un avatar. Sólo lo usamos en esta clase (por ello es privado).
     * El ID generado será una letra mayúscula. Parámetros:
     * - Un arraylist de los avatares ya creados, con el objetivo de evitar que se generen dos ID iguales.
     */
    /*private void generarId(ArrayList<Avatar> avCreados) {

        Random letraRandom = new Random();
        boolean existe;

        do {
            existe = false;
            this.id = String.valueOf(letraRandom.nextInt(26) + 'A');

            /* Si no existe en ningún avatar, salimos */
            /*for(Avatar avatar : avCreados){
                if(avatar.getId().equals(this.id)){
                    existe = true;
                }
            }
        } while(existe);
    }*/

    private void generarId(ArrayList<Avatar> avCreados) {
        Random random = new Random();
        boolean existe;

        do {
            existe = false;

            // Generamos un índice aleatorio para seleccionar una letra del arreglo
            int indiceAleatorio = random.nextInt(letrasRestantes); // letrasRestantes se actualiza
            this.id = String.valueOf(letrasDisponibles[indiceAleatorio]);

            /* Si no existe en ningún avatar, procedemos */
            for (Avatar avatar : avCreados) {
                if (avatar.getId().equals(this.id)) {
                    existe = true; // Si existe, repetimos el proceso
                    break; // Salimos del bucle si ya existe
                }
            }

            // Si no existe, eliminamos la letra del array y actualizamos letrasRestantes
            if (!existe) {
                // Eliminamos la letra seleccionada del array de letras disponibles
                for (int i = indiceAleatorio; i < letrasRestantes - 1; i++) {
                    letrasDisponibles[i] = letrasDisponibles[i + 1]; // Desplazamos las letras hacia la izquierda
                }
                letrasRestantes--; // Actualizamos el número de letras restantes
            }

        } while (existe);
    }




    //Getters y Setters


    public String getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public Casilla getLugar() {
        return lugar;
    }

    public void setLugar(Casilla lugar) {
        this.lugar = lugar;
    }
}