package monopoly_avatares;

import java.util.ArrayList;
import java.util.Random;

import monopoly_casillas.Casilla;
import monopoly_exception.avatares.AvatarNoValidoException;
import monopoly_juego.Juego;
import monopoly_tablero.Valor;
import monopoly_jugador.Jugador;


public class Avatar {

    //Atributos
    private String id; //Identificador: una letra generada aleatoriamente.
    private String tipo; //Sombrero, Esfinge, Pelota, Coche
    private Jugador jugador; //Un jugador al que pertenece ese avatar.
    private Casilla lugar; //Los avatares se sitúan en casillas del tablero.


    //Constructor vacío
    public Avatar() {
    }

    /*Constructor principal. Requiere éstos parámetros:
     * Tipo del avatar, jugador al que pertenece, lugar en el que estará ubicado, y un arraylist con los
     * avatares creados (usado para crear un ID distinto del de los demás avatares).
     */
    public Avatar(String tipo, Jugador jugador, Casilla lugar, ArrayList<Avatar> avCreados) throws AvatarNoValidoException{
        this.generarId(avCreados);  //Crea un ID nuevo para un avatar distinto de los creados anteriormente

        if ("Pelota Coche Esfinge Sombrero".contains(tipo)) {
            this.tipo = tipo;
        }
        else {
            this.tipo = "Pelota";
            throw new AvatarNoValidoException("Tipo de avatar no reconocido, se toma Pelota por defecto.");
        }

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

        if(nuevaPosicion > 40) {

            nuevaPosicion = nuevaPosicion % 40;

            if(!this.lugar.getNombre().equals("IrCarcel")){
                jugador.setVueltas(jugador.getVueltas() + 1);
                jugador.sumarFortuna((float) Valor.SUMA_VUELTA);
                jugador.setPasarPorCasillaDeSalida(jugador.getPasarPorCasillaDeSalida() + (float)Valor.SUMA_VUELTA);
            }

        } 

        for(ArrayList<Casilla> arrayList : casillas){
            for(Casilla casilla : arrayList) {
                if(casilla.getPosicion() == nuevaPosicion) {
                    if(casilla.getNombre().equals("IrCarcel") || casilla.getNombre().equals("Carcel")){
                        jugador.encarcelar(casillas);
                        return;
                    }

                    try {
                        this.lugar.eliminarAvatar(this);
                        this.lugar = casilla;
                        casilla.anhadirAvatar(this);
                    } catch (AvatarNoValidoException e) {
                        Juego.consola.imprimir("Error: " + e.getMessage()); // Manejo del error
                    }
                }
            }
        }
    }

    /*REVISAR IrCarcel / carcel */
    public void moverAvatarAtras(ArrayList<ArrayList<Casilla>> casillas, int valorTirada) {

        int nuevaPosicion = this.lugar.getPosicion() - Math.abs(valorTirada); // Resta el valor para moverse hacia atrás

        // Ajuste en caso de que la posición sea negativa
        if (nuevaPosicion < 0) {
            nuevaPosicion = 40 + (nuevaPosicion % 40); // Ajuste para el tablero circular

            if (!this.lugar.getNombre().equals("IrCarcel")) {
                jugador.setVueltas(jugador.getVueltas() - 1);
                jugador.sumarFortuna((float) Valor.SUMA_VUELTA * (-1));
                jugador.setPasarPorCasillaDeSalida(jugador.getPasarPorCasillaDeSalida() + (float)Valor.SUMA_VUELTA * (-1));
            }
        }

        // Mover el avatar a la nueva casilla
        for (ArrayList<Casilla> arrayList : casillas) {
            for (Casilla casilla : arrayList) {
                if (casilla.getPosicion() == nuevaPosicion) {
                    try {
                        this.lugar.eliminarAvatar(this);
                        this.lugar = casilla;
                        casilla.anhadirAvatar(this);
                    } catch (AvatarNoValidoException e) {
                        Juego.consola.imprimir("Error: " + e.getMessage()); // Manejo del error
                    }
                    return; // Salimos del bucle una vez encontrado
                }
            }
        }
    }

    public void teleportAvatar(ArrayList<ArrayList<Casilla>> casillas, Casilla c) {

        for(ArrayList<Casilla> arrayList : casillas){
            for(Casilla casilla : arrayList){
                if(casilla.getPosicion() == c.getPosicion()){
                    try {
                        this.lugar.eliminarAvatar(this);
                        this.lugar = casilla;
                        casilla.anhadirAvatar(this);
                    } catch (AvatarNoValidoException e) {
                        Juego.consola.imprimir("Error: " + e.getMessage()); // Manejo del error
                    }
                    return;
                }
            }
        }
    }

    /*Método que permite generar un ID para un avatar. Sólo lo usamos en esta clase (por ello es privado).
     * El ID generado será una letra mayúscula. Parámetros:
     * - Un arraylist de los avatares ya creados, con el objetivo de evitar que se generen dos ID iguales.
     */
    private void generarId(ArrayList<Avatar> avCreados) {
        Random random = new Random();
        boolean existe;

        do {

            existe = false;
            this.id = Character.toString((char)random.nextInt(26) + 'A');

            /* Por cada avatar que exista */
            for (Avatar avatar : avCreados) {
                if (avatar.getId().equals(this.id)) {
                    existe = true; // Si existe el id, repetimos el proceso
                    break;
                }
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

    public Jugador getJugador() {
        return jugador;
    }

    public Casilla getLugar() {
        return lugar;
    }

    public void setLugar(Casilla lugar) {
        this.lugar = lugar;
    }
}