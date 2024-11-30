package monopoly_casillas;

import java.util.ArrayList;

import monopoly_casillas.propiedades.Solar;
import monopoly_edificios.*;
import monopoly_juego.Juego;
import monopoly_jugador.Jugador;
import monopoly_tablero.Tablero;
import monopoly_tablero.Valor;
import monopoly_avatares.Avatar;


public abstract class Casilla {

    //Atributos:
    protected String nombre; //Nombre de la casilla
    protected String tipo; //Tipo de casilla (Solar, Especial, Transporte, Servicios, Comunidad, Suerte y Impuesto).
    protected float valor; //Valor de esa casilla (en la mayoría será valor de compra, en la casilla parking se usará como el bote).
    protected int posicion; //Posición que ocupa la casilla en el tablero (entero entre 1 y 40).
    protected Jugador duenho; //Dueño de la casilla (por defecto sería la banca).
    protected float impuesto; //Cantidad a pagar por caer en la casilla: el alquiler en solares/servicios/transportes o impuestos.
    protected float hipoteca; //Valor otorgado por hipotecar una casilla
    protected ArrayList<Avatar> avatares; //Avatares que están situados en la casilla.
    protected boolean hipotecada; //Indica si una casilla está hipotecada o no.

    protected int[] contarCaer; // Cuenta las veces que un jugador cae en esta casilla. Index=Jugador
    protected ArrayList<Edificio> edificios;  //Edificios contruídos en esta casilla.


    /*Constructor para casillas tipo Solar, Servicios o Transporte:
     * Parámetros: nombre casilla, tipo (debe ser solar, serv. o transporte), posición en el tablero, valor y dueño.
     */
    public Casilla(String nombre, String tipo, int posicion, float valor, Jugador duenho) {
        this.avatares = new ArrayList<>();
        this.nombre = nombre;
        this.tipo = tipo;
        this.valor = valor;
        this.impuesto = valor;
        this.posicion = posicion;
        this.duenho = duenho;
        this.edificios = new ArrayList<>();
        this.contarCaer = new int[10];
        this.hipoteca = valor / 2f;
        this.hipotecada = false;
    }

    /*Constructor utilizado para inicializar las casillas de tipo IMPUESTOS.
     * Parámetros: nombre, posición en el tablero, impuesto establecido y dueño.
     */
    public Casilla(String nombre, int posicion, float impuesto, Jugador duenho) {
        this.avatares = new ArrayList<>();
        this.nombre = nombre;
        this.tipo = "Impuesto";
        this.posicion = posicion;
        this.duenho = duenho;// La banca es dueño de la casilla de impuestos
        this.impuesto = impuesto;
        this.hipotecada = false;
        this.edificios = new ArrayList<>();
        this.contarCaer = new int [10];
    }

    /*Constructor utilizado para crear las otras casillas (Suerte, Caja de comunidad y Especiales):
     * Parámetros: nombre, tipo de la casilla (será uno de los que queda), posición en el tablero y dueño.
     */
    public Casilla(String nombre, String tipo, int posicion, Jugador duenho) {
        this.avatares = new ArrayList<>();
        this.nombre = nombre;
        this.tipo = tipo;
        this.posicion = posicion;
        this.duenho = duenho;
        this.hipotecada = false;
        this.edificios = new ArrayList<>();
        this.contarCaer = new int [10];
    }

    //Método utilizado para añadir un avatar al array de avatares en casilla.
    public void anhadirAvatar(Avatar av) {
        if(avatares.contains(av)) {
            Juego.consola.imprimir("Este avatar ya existe, elige otro distinto.");
        } else {
            avatares.add(av);
        }
    }

    //Método utilizado para eliminar un avatar del array de avatares en casilla.
    public void eliminarAvatar(Avatar av) {
        if(avatares.isEmpty() || !avatares.contains(av)) {
            Juego.consola.imprimir("No hay ningún avatar.");
        } else {
            avatares.remove(av);
        }
    }

    /*Método para evaluar qué hacer en una casilla concreta. Parámetros:
     * - Jugador cuyo avatar está en esa casilla.
     * - La banca (para ciertas comprobaciones).
     * - El valor de la tirada: para determinar impuesto a pagar en casillas de servicios.
     * Valor devuelto: true en caso de ser solvente (es decir, de cumplir las deudas), y false
     * en caso de no cumplirlas.*/
    // Se implementa en cada subclase

    /*Método usado para comprar una casilla determinada. Parámetros:
     * - Jugador que solicita la compra de la casilla.
     * - Banca del monopoly (es el dueño de las casillas no compradas aún).*/
    public boolean comprarCasilla(Jugador solicitante, Jugador banca) {

        if(!getTipo().equals("Solar") && !getTipo().equals("Transporte") && !getTipo().equals("Servicio")){
            Juego.consola.imprimir("Esta casilla no se puede comprar.");
            return false;
        }

        if(!this.duenho.equals(banca)){
            Juego.consola.imprimir("Esta casilla ya pertenece a un jugador.");
            return false;
        }

        if(solicitante.getFortuna() < this.valor){
            Juego.consola.imprimir("No tienes suficiente dinero para comprar esta casilla.");
            return false;
        }

        solicitante.sumarFortuna(-valor);
        solicitante.sumarGastos(valor);
        solicitante.setDineroInvertido(solicitante.getDineroInvertido() + valor);

        this.duenho = solicitante;
        solicitante.anhadirPropiedad(this);

        return true;
    }





    /*Método para añadir valor a una casilla. Utilidad:
     * - Sumar valor a la casilla de parking.
     * - Sumar valor a las casillas de solar al no comprarlas tras cuatro vueltas de todos los jugadores.
     * Este método toma como argumento la cantidad a añadir del valor de la casilla.*/
    public void sumarValor(float suma) {
        if(suma < 0){
            Juego.consola.imprimir("La cantidad a añadir debe ser un número positivo.");
        } else {
            this.valor += suma;
        }
    }




    /*Método para mostrar información sobre una casilla.
     * Devuelve una cadena con información específica de cada tipo de casilla.*/
    public String infoCasilla() {
        String cadena = "";
        if (getTipo().equals("Solar")){
            float alquiler = getValor()*0.1f;
            cadena = ("{\n");
            cadena += ("\ttipo: " + getTipo() + "," + "\n");
            cadena +=("\tgrupo: " + ((Solar) this).getGrupo().getColorGrupo() + "," + "\n");
            cadena +=("\tpropietario: " + getDuenho().getNombre() + "," + "\n");
            cadena +=("\tvalor: " + getValor() + "," + "\n");
            cadena +=("\talquiler: " + alquiler + "," + "\n");
            cadena +=("\tvalor hotel: " + getValor()* Valor.MULTIPLICADOR_HOTEL + "," + "\n");
            cadena +=("\tvalor casa: " + getValor()*Valor.MULTIPLICADOR_CASA + "," + "\n");
            cadena +=("\tvalor piscina: " + getValor()*Valor.MULTIPLICADOR_PISCINA + "," + "\n");
            cadena +=("\tvalor pista de deporte: " + getValor()*Valor.MULTIPLICADOR_PISTA_DE_DEPORTE + "," + "\n");
            cadena +=("\talquiler una casa: " + alquiler*Valor.ALQUILER_UNA_CASA + "," + "\n");
            cadena +=("\talquiler dos casas: " + alquiler*Valor.ALQUILER_DOS_CASA + "," + "\n");
            cadena +=("\talquiler tres casas: " + alquiler*Valor.ALQUILER_TRES_CASA + "," + "\n");
            cadena +=("\talquiler cuatro casas: " + alquiler*Valor.ALQUILER_CUATRO_CASA + "," + "\n");
            cadena +=("\talquiler hotel: " + alquiler*Valor.ALQULER_HOTEL + "," + "\n");
            cadena +=("\talquiler piscina: " + alquiler*Valor.ALQUILER_PISCINA + "," + "\n");
            cadena +=("\talquiler pista de deporte: " + alquiler*Valor.ALQUILER_PISTA_DE_DEPORTE + "," + "\n");
            cadena +=("\tedificios construídos en esta casilla: [");
            if(!edificios.isEmpty()){
                for (Edificio e : edificios){
                    cadena +=(e.getId() + ", ");
                }
            } else {
                cadena +=("");
            }
            cadena+=("]");
            cadena+=("\n},");
        } else if (getNombre().equals("Imp1") || getNombre().equals("Imp2")){
            cadena =("{\n");
            cadena +=("\ttipo: " + getTipo()  + "," + "\n");
            cadena +=("\ta pagar: " + getImpuesto());
            cadena +=("\n},");
        } else if (getTipo().equals("Transporte")){
            float alquiler1 = getValor()*0.1f;
            cadena = ("{\n");
            cadena +=("\ttipo: " + getTipo() + "," + "\n");
            cadena +=("\tpropietario: " + getDuenho() + "," + "\n");
            cadena +=("\tvalor: " + getValor() + "," + "\n");
            cadena +=("\talquiler: " + alquiler1);
            cadena +=("\n},");
        } else if (getTipo().equals("Servicio")){
            float alquiler2 = getValor()*0.1f;
            cadena = ("{\n");
            cadena += ("\ttipo: " + getTipo()  + "," + "\n");
            cadena +=("\tpropietario: " + getDuenho() + "," + "\n");
            cadena +=("\tvalor: " + getValor() + "," + "\n");
            cadena +=("\talquiler: " + alquiler2);
            cadena +=("\n},");
        } else if (getNombre().equals("Parking")){
            cadena = ("{\n");
            cadena +=("\tbote: " + getValor() + "," + "\n");
            cadena +=("\tjugadores: [");
            if(getAvatares() != null){
                for (Avatar a : getAvatares()){
                    cadena +=(a.getJugador().getNombre() + ", ");
                }
            } else {
                cadena +=("");
            }
            cadena+=("]");
            cadena+=("\n},");
        } else if (getNombre().equals("Carcel")){
            cadena =("{\n");
            cadena +=("\tsalir: " + (Valor.SUMA_VUELTA*0.25)  + "," + "\n");
            cadena +=("\tjugadores: ");
            if (getAvatares() != null){
                for (Avatar a : getAvatares()){
                    String nombreJugador = a.getJugador().getNombre();
                    cadena += ("[" + nombreJugador + "]");
                }
            } else {
                cadena += ("[]");
            }
            cadena +=("\n},");
        }
        return cadena;
    }

    /* Método para mostrar información de una casilla en venta.
     * Valor devuelto: texto con esa información.
     */
    public String casEnVenta() {
        String cadena = "";
        switch (getTipo()) {
            case "Solar":
                cadena = ("{\n");
                cadena += ("\ttipo: " + getTipo() + "," + "\n");
                cadena +=("\tgrupo: " + ((Solar) this).getGrupo().getColorGrupo() + "," + "\n");
                cadena +=("\tvalor: " + getValor());
                cadena +=("\n},");
                break;
            case "Transporte":
                cadena = ("{\n");
                cadena += ("\ttipo: " + getTipo() + "," + "\n");
                cadena +=("\tvalor: " + getValor() + "," + "\n");
                cadena +=("\n},");
                break;
            case "Servicio":
                cadena = ("{\n");
                cadena += ("\ttipo: " + getTipo() + "," + "\n");
                cadena +=("\tvalor: " + getValor() + "," + "\n");
                cadena +=("\n},");
                break;
            default:
                break;
        }
        return cadena;
    }


    /* número de casas */
    public int getCasasN() {
        int sum = 0;
        for (Edificio e : edificios) {
            if (e instanceof Casa)
                sum++;
        }
        return sum;
    }
    /* número de hoteles */
    public int getHotelesN() {
        int sum = 0;
        for (Edificio e : edificios) {
            if (e instanceof Hotel)
                sum++;
        }
        return sum;
    }
    /* número de piscinas */
    public int getPiscinasN() {
        int sum = 0;
        for (Edificio e : edificios) {
            if (e instanceof Piscina)
                sum++;
        }
        return sum;
    }

    /* número de pistas */
    public int getPistasN() {
        int sum = 0;
        for (Edificio e : edificios) {
            if (e instanceof PistaDeporte)
                sum++;
        }
        return sum;
    }

    public boolean haCaidoMasDosVeces(Jugador j) {
        int veces = contarCaer[j.getIndice()];
        return veces > 2;
    }

    public int getContarCaer(Jugador j) {
        return contarCaer[j.getIndice()];
    }



    // Método para eliminar un edificio
    public void eliminarEdificio(String tipoEdificio){
        for(Edificio e : edificios){
            if(e.getTipo().equals(tipoEdificio)){
                edificios.remove(e);
                break;
            }
        }
    }




    //Getters y Setters


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public Jugador getDuenho() {
        return duenho;
    }

    public void setDuenho(Jugador duenho) {
        this.duenho = duenho;
    }

    public float getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(float impuesto) {
        this.impuesto = impuesto;
    }

    public float getHipoteca() {
        return hipoteca;
    }

    public void setHipoteca(float hipoteca) {
        this.hipoteca = hipoteca;
    }

    public ArrayList<Avatar> getAvatares() {
        return avatares;
    }

    public void setAvatares(ArrayList<Avatar> avatares) {
        this.avatares = avatares;
    }

    public ArrayList<Edificio> getEdificios() {
        return this.edificios;
    }
    public void setEdificios(ArrayList<Edificio> e) {
        this.edificios = e;
    }

    public boolean getHipotecada(){
        return hipotecada;
    }

    public void setHipotecada(boolean hipotecada){
        this.hipotecada = hipotecada;
    }

    public void evaluarCasilla(Tablero tablero, Jugador jugador, Jugador banca, ArrayList<Jugador> jugadores) {
    }
}