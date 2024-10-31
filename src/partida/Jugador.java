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
    private int tiradasDobles; // Cuenta las tiradas dobles.
    private float dineroInvertido;
    private float pagoTasasEImpuestos;
    private float pagoDeAlquileres;
    private float cobroDeAlquileres;
    private float pasarPorCasillaDeSalida;
    private float premiosInversionesOBote;
    private int vecesEnLaCarcel;
    private ArrayList<Casilla> propiedades; //Propiedades que posee el jugador.
    //private ArrayList<Hipoteca> hipotecas;
    //private ArrayList<Edificio> edificios;
    private int indice; /* Index dentro del array de jugadores */

    //Constructor vacío. Se usará para crear la banca.
    public Jugador() {
    }

    /*Constructor principal. Requiere parámetros:
     * Nombre del jugador, tipo del avatar que tendrá, casilla en la que empezará y ArrayList de
     * avatares creados (usado para dos propósitos: evitar que dos jugadores tengan el mismo nombre y
     * que dos avatares tengan mismo ID). Desde este constructor también se crea el avatar.
     */
    public Jugador(String nombre, String tipoAvatar, Casilla inicio, ArrayList<Avatar> avCreados, int index) {
        this.nombre = nombre;
        if (!tipoAvatar.equals("Banca")) this.avatar = new Avatar(tipoAvatar, this, inicio, avCreados);  //Creación del avatar
        this.fortuna = (float) Valor.FORTUNA_INICIAL;
        this.gastos = 0.0f;
        this.enCarcel = false;
        this.tiradasCarcel = 0;
        this.vueltas = 0;
        this.tiradasDobles = 0;
        this.dineroInvertido = 0.0f;
        this.pagoTasasEImpuestos = 0.0f;
        this.pagoDeAlquileres = 0.0f;
        this.cobroDeAlquileres = 0.0f;
        this.pasarPorCasillaDeSalida = 0.0f;
        this.premiosInversionesOBote = 0.0f;
        this.vecesEnLaCarcel = 0;
        this.propiedades = new ArrayList<>();
        //this.hipotecas = new ArrayList<>();
        //this.edificios = new ArrayList<>();
        this.indice = index;
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

                        return;
                    }
                }
            }
        //}
    }


    
    /* Mover jugador de la casilla actual respecto al valor de la tirada*/
    public void moverJugador(Tablero tablero, int tirada, ArrayList<Jugador> jugadores) {

        ArrayList<ArrayList<Casilla>> pos = tablero.getPosiciones();

        Casilla c = this.getAvatar().getLugar();

        System.out.print("El avatar " + this.getAvatar().getId() + " avanza " + tirada + " posiciones, desde " + c.getNombre());
        this.getAvatar().moverAvatar(pos, tirada);
        c = this.getAvatar().getLugar();
        System.out.println(" hasta " + c.getNombre() + ".");

        c.evaluarCasilla(tablero, this, tablero.getBanca(), jugadores);
    }

    public void teleportJugador(Tablero tablero, Casilla casilla) {

        ArrayList<ArrayList<Casilla>> pos = tablero.getPosiciones();

        Casilla c = this.getAvatar().getLugar();

        System.out.print("El avatar " + this.getAvatar().getId() + " avanza a " + casilla.getNombre() + " , desde " + c.getNombre());
        this.getAvatar().teleportAvatar(pos, casilla);
        c = this.getAvatar().getLugar();
        System.out.println(" hasta " + c.getNombre() + ".");
    }



    // Método que devuelve una cadena con las estadísticas del jugador.
    public String estadisticasJugador(Jugador jugador){
        String cadena;

        cadena = ("{\n");
        cadena += ("\tdineroInvertido: " + jugador.getDineroInvertido() + "," + "\n");
        cadena += ("\tpagoTasasEImpuestos: " + jugador.getPagoTasasEImpuestos() + "," + "\n");
        cadena += ("\tpagoDeAlquileres: " + jugador.getPagoDeAlquileres() + "," + "\n");
        cadena += ("\tcobroDeAlquileres: " + jugador.getCobroDeAlquileres() + "," + "\n");
        cadena += ("\tpasarPorCasillaDeSalida: " + jugador.getPasarPorCasillaDeSalida() + "," + "\n");
        cadena += ("\tpremiosInversionesOBote: " + jugador.getPremiosInversionesOBote() + "," + "\n");
        cadena += ("\tvecesEnLaCarcel: " + jugador.getVecesEnLaCarcel() + "\n");
        cadena += ("}\n");

        return cadena;
    }

    


    //Método para declararse en bancarrota
    public void bancarrota(ArrayList<Jugador> jugadores, Jugador banca, boolean solvente){

        Jugador jugador = this;
        Jugador propietario = avatar.getLugar().getDuenho();

        //COMPROBAR SI HAI QUE RESETEAR PRECIOS PROPIEDADES
        if(!solvente){
            if(propietario.equals(banca)){
                traspasarPropiedadesJugador(banca, jugador);
                System.out.println("El jugador " + this.nombre + " se ha declarado en bancarrota. Sus propiedades pasan a estar de nuevo en venta al precio al que estaban.");
       
            } else {
                traspasarPropiedadesJugador(propietario, jugador);
                propietario.sumarFortuna(jugador.fortuna);
                jugador.setFortuna(0);
                System.out.println("El jugador " + this.nombre + " se ha declarado en bancarrota. Sus propiedades y fortuna pasan al jugador " + propietario.getNombre());
            }
            solvente = true;  
            eliminarJugador(jugadores, jugador);   
        } else {
            traspasarPropiedadesJugador(banca, jugador);
            System.out.println("El jugador " + this.nombre + " se ha declarado en bancarrota. Sus propiedades pasan a estar de nuevo en venta al precio al que estaban.");
            eliminarJugador(jugadores, jugador); 
        }
    }

    private void traspasarPropiedadesJugador(Jugador nuevoPropietario, Jugador jugador){
        for(Casilla c : jugador.propiedades){
            nuevoPropietario.anhadirPropiedad(c);
            c.setDuenho(nuevoPropietario);
            jugador.eliminarPropiedad(c);
        }
         
    }

    //Método para eliminar a un jugador de la partida
    private void eliminarJugador(ArrayList<Jugador> jugadores, Jugador jugador){
        for(Jugador j : jugadores){
            if(j.equals(jugador)){
                j.getAvatar().getLugar().eliminarAvatar(j.getAvatar());
                jugadores.remove(j);
            }
        }
        System.out.println("El jugador" + jugador.getNombre() + " ha sido eliminado de la partida.");
    }



    //Getters y Setters


    public String getNombre() {
        return nombre;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public float getFortuna() {
        return fortuna;
    }

    public float getGastos() {
        return gastos;
    }

    public boolean isEnCarcel() {
        return enCarcel;
    }

    public int getTiradasCarcel() {
        return tiradasCarcel;
    }

    public int getVueltas() {
        return vueltas;
    }

    public int getTiradasDobles() {
        return tiradasDobles;
    }

    public float getDineroInvertido() {
        return dineroInvertido;
    }

    public float getPagoTasasEImpuestos() {
        return pagoTasasEImpuestos;
    }

    public float getPagoDeAlquileres() {
        return pagoDeAlquileres;
    }

    public float getCobroDeAlquileres() {
        return cobroDeAlquileres;
    }

    public float getPasarPorCasillaDeSalida() {
        return pasarPorCasillaDeSalida;
    }

    public float getPremiosInversionesOBote() {
        return premiosInversionesOBote;
    }

    public int getVecesEnLaCarcel() {
        return vecesEnLaCarcel;
    }

    public ArrayList<Casilla> getPropiedades() {
        return propiedades;
    }

    //public ArrayList<Hipoteca> getHipotecas() {
    //    return hipotecas;
    //}

    public ArrayList<Edificio> getEdificios() {
        ArrayList<Edificio> edificios = new ArrayList<>();

        /* Casillas del jugador */
        for (Casilla c : this.propiedades) {
            /* Añadir todos los edificios de las casillas del jugador */
            for (Edificio e : c.getEdificios()) {
                edificios.add(e);
            }
        }

        return edificios;
    }

 


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public void setFortuna(float fortuna) {
        this.fortuna = fortuna;
    }

    public void setGastos(float gastos) {
        this.gastos = gastos;
    }

    public void setEnCarcel(boolean enCarcel) {
        this.enCarcel = enCarcel;
    }

    public void setTiradasCarcel(int tiradasCarcel) {
        this.tiradasCarcel = tiradasCarcel;
    }

    public void setVueltas(int vueltas) {
        this.vueltas = vueltas;
    }

    public void setTiradasDobles(int tiradasDobles) {
        this.tiradasDobles = tiradasDobles;
    }

    public void setDineroInvertido(float dineroInvertido) {
        this.dineroInvertido = dineroInvertido;
    }

    public void setPagoTasasEImpuestos(float pagoTasasEImpuestos) {
        this.pagoTasasEImpuestos = pagoTasasEImpuestos;
    }

    public void setPagoDeAlquileres(float pagoDeAlquileres) {
        this.pagoDeAlquileres = pagoDeAlquileres;
    }

    public void setCobroDeAlquileres(float cobroDeAlquileres) {
        this.cobroDeAlquileres = cobroDeAlquileres;
    }

    public void setPasarPorCasillaDeSalida(float pasarPorCasillaDeSalida) {
        this.pasarPorCasillaDeSalida = pasarPorCasillaDeSalida;
    }

    public void setPremiosInversionesOBote(float premiosInversionesOBote) {
        this.premiosInversionesOBote = premiosInversionesOBote;
    }

    public void setVecesEnLaCarcel(int vecesEnLaCarcel) {
        this.vecesEnLaCarcel = vecesEnLaCarcel;
    }

    public void setPropiedades(ArrayList<Casilla> propiedades) {
        this.propiedades = propiedades;
    }

    public int getIndice() {
        return indice;
    }

    //public void setHipotecas(ArrayList<Hipoteca> hipotecas) {
    //    this.hipotecas = hipotecas;
    //}

    //public void setEdificios(ArrayList<Edificio> edificios) {
    //    this.edificios = edificios;
    //}
}