package partida;

import java.util.ArrayList;
import monopoly.*;


public class Jugador {

    //Atributos:
    private String nombre; //Nombre del jugador
    private Avatar avatar; //Avatar que tiene en la partida.
    private float fortuna; //Dinero que posee.
    private float gastos; //Gastos realizados a lo largo del juego.
    private boolean enCarcel; //Será true si el jugador está en la carcel
    private int tiradasCarcel; //Cuando está en la carcel, contará las tiradas sin éxito que ha hecho allí para intentar salir (se usa para limitar el numero de intentos).
    private int vueltas; //Cuenta las vueltas dadas al tablero.
    private int tiradas; // Cuenta las tiradas dobles. 1 = Puede tirar de nuevo. 0 = Primera vez que tira. -1 = Ya tiró
    private ArrayList<Casilla> propiedades; //Propiedades que posee el jugador.

    //Constructor vacío. Se usará para crear la banca.
    public Jugador() {
    }

    /*Constructor principal. Requiere parámetros:
     * Nombre del jugador, tipo del avatar que tendrá, casilla en la que empezará y ArrayList de
     * avatares creados (usado para dos propósitos: evitar que dos jugadores tengan el mismo nombre y
     * que dos avatares tengan mismo ID). Desde este constructor también se crea el avatar.
     */
    public Jugador(String nombre, String tipoAvatar, Casilla inicio, ArrayList<Avatar> avCreados) {
        this.nombre = nombre;
        if (tipoAvatar != "Banca") this.avatar = new Avatar(tipoAvatar, this, inicio, avCreados);  //Creación del avatar
        this.fortuna = (float) Valor.FORTUNA_INICIAL;
        this.gastos = 0.0f;
        this.enCarcel = false;
        this.tiradasCarcel = 0;
        this.vueltas = 0;
        this.propiedades = new ArrayList<>();
    }



    //Otros métodos:
    //Método para añadir una propiedad al jugador. Como parámetro, la casilla a añadir.
    public void anhadirPropiedad(Casilla casilla) {
        propiedades.add(casilla);
    }



    //Método para eliminar una propiedad del arraylist de propiedades de jugador.
    public void eliminarPropiedad(Casilla casilla) {
        if (propiedades.contains(casilla)) {
            propiedades.remove(casilla);
        } else {
            System.out.println("El jugador no es poseedor de la propiedad");
        }
    }



    //Método para añadir fortuna a un jugador
    //Como parámetro se pide el valor a añadir. Si hay que restar fortuna, se pasaría un valor negativo.
    public void sumarFortuna(float valor) {
        this.fortuna += valor;
    }



    //Método para sumar gastos a un jugador.
    //Parámetro: valor a añadir a los gastos del jugador (será el precio de un solar, impuestos pagados...).
    public void sumarGastos(float valor) {
        this.gastos += valor;
    }



    /*Método para establecer al jugador en la cárcel.
     * Se requiere disponer de las casillas del tablero para ello (por eso se pasan como parámetro).*/
    public void encarcelar(ArrayList<ArrayList<Casilla>> pos) {

        /* O xogador pode ir á carcel sen ter que caer na casilla */
        //if (this.avatar.getLugar().getNombre().equals("IrCarcel")){

            for(ArrayList<Casilla> arrayList : pos){
                for(Casilla casilla : arrayList) {
                    if(casilla.getNombre().equals("Carcel")){

                        this.avatar.getLugar().eliminarAvatar(this.avatar);
                        this.avatar.setLugar(casilla);
                        casilla.anhadirAvatar(this.avatar);
                        this.enCarcel = true;
                    }
                }
            }
        //}
    }

    /* Mover jugador de la casilla actual respecto al valor de la tirada*/
    public void moverJugador(Tablero tablero, int tirada) {

        ArrayList<ArrayList<Casilla>> pos = tablero.getPosiciones();

        Casilla c = this.getAvatar().getLugar();

        System.out.print("El avatar " + this.getAvatar().getId() + " avanza " + tirada + " posiciones, desde " + c.getNombre());
        this.getAvatar().moverAvatar(pos, tirada);
        c = this.getAvatar().getLugar();
        System.out.println(" hasta " + c.getNombre() + ".");

        boolean solvente = c.evaluarCasilla(tablero, this, tablero.getBanca(), tirada);

        if (!solvente) {
            System.out.println("Jugador en banca rota, se acaba la partida");
        }

    }



    //Getters y Setters


    public String getNombre(){
        return nombre;
    }

    public Avatar getAvatar(){
        return avatar;
    }

    public float getFortuna(){
        return fortuna;
    }

    public float getGastos(){
        return gastos;
    }

    public boolean isEnCarcel(){
        return enCarcel;
    }

    public int getTiradasCarcel(){
        return tiradasCarcel;
    }

    public int getVueltas() {
        return vueltas;
    }
    public void setVueltas(int vueltas) {
        this.vueltas = vueltas;
    }

    public ArrayList<Casilla> getPropiedades(){
        return propiedades;
    }

    public int getTiradas() {
        return tiradas;
    }

    public void setTiradas(int tiradas) {

        /* Limitar el valor */
        if (tiradas < -1 || tiradas > 1) {
            return;
        }

        this.tiradas = tiradas;
    }
}