package monopoly;

import java.util.ArrayList;
import partida.*;


public class Casilla {

    //Atributos:
    private String nombre; //Nombre de la casilla
    private String tipo; //Tipo de casilla (Solar, Especial, Transporte, Servicios, Comunidad, Suerte y Impuesto).
    private float valor; //Valor de esa casilla (en la mayoría será valor de compra, en la casilla parking se usará como el bote).
    private int posicion; //Posición que ocupa la casilla en el tablero (entero entre 1 y 40).
    private Jugador duenho; //Dueño de la casilla (por defecto sería la banca).
    private Grupo grupo; //Grupo al que pertenece la casilla (si es solar).
    private float impuesto; //Cantidad a pagar por caer en la casilla: el alquiler en solares/servicios/transportes o impuestos.
    private float hipoteca; //Valor otorgado por hipotecar una casilla
    private ArrayList<Avatar> avatares; //Avatares que están situados en la casilla.

    //Constructores:
    public Casilla() {
    }//Parámetros vacíos

    /*Constructor para casillas tipo Solar, Servicios o Transporte:
     * Parámetros: nombre casilla, tipo (debe ser solar, serv. o transporte), posición en el tablero, valor y dueño.
     */
    public Casilla(String nombre, String tipo, int posicion, float valor, Jugador duenho) {
        this.avatares = new ArrayList<>();
        this.nombre = nombre;
        this.tipo = tipo;
        this.valor = valor;
        this.posicion = posicion;
        this.duenho = duenho;
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
    }



    //Método utilizado para añadir un avatar al array de avatares en casilla.
    public void anhadirAvatar(Avatar av) {
        if(avatares.contains(av)){
            System.out.println("Este avatar ya existe, elige otro distinto.");
        } else {
            avatares.add(av);
        }
    }




    //Método utilizado para eliminar un avatar del array de avatares en casilla.
    public void eliminarAvatar(Avatar av) {
        if(avatares.isEmpty()){
            System.out.println("ERROR.No hay ningún avatar.");
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
    public boolean evaluarCasilla(Tablero tab, Jugador jugador, Jugador banca, int tirada) {

        boolean solvente = true;
        String tipoCasilla = this.getTipo();

        /* Depende de donde caímos, hacer algo */
        if (tipoCasilla.equals("Solar")) {

            /* Si hay dueño */
            if (duenho != banca) {
                float pago = valor;
                /* Si no puede pagarlo */
                if (jugador.getFortuna() < pago) {
                    System.out.println("Dinero insuficiente.");
                    return false;
                }
                /* El jugador paga al propietario */
                jugador.sumarGastos(pago);
                jugador.sumarFortuna(-pago);
                /* El propietario recibe */
                duenho.sumarFortuna(pago);

                System.out.println("El juagdor " + jugador.getNombre() + " paga " + pago + " € de alquiler.");

                return true;
            }

        }
        else if (tipoCasilla.equals("Transporte")) {

        }
        else if (tipoCasilla.equals("Comunidad")) {

        }
        else if (tipoCasilla.equals("Servicio")) {

        }
        else if (tipoCasilla.equals("Suerte")) {

        }
        /* Tipo especial */
        else {
            /* Cae en cárcel */
            if (nombre.equals("IrCarcel")) {
                jugador.encarcelar(tab.getPosiciones());
            }
            /* Cae en cárcel */
            if (nombre.equals("Parking")) {
                float bote = this.getValor();
                jugador.sumarFortuna(bote);
                this.setValor(0);
            }
        }

        return solvente;
    }





    /*Método usado para comprar una casilla determinada. Parámetros:
     * - Jugador que solicita la compra de la casilla.
     * - Banca del monopoly (es el dueño de las casillas no compradas aún).*/
    public boolean  comprarCasilla(Jugador solicitante, Jugador banca) {
        
        if(!getTipo().equals("Solar") && !getTipo().equals("Transporte") && !getTipo().equals("Servicios")){
            System.out.println("ERROR. Esta casilla no se puede comprar.");
            return false;
        }

        if(!this.duenho.equals(banca)){
            System.out.println("ERROR. Esta casilla ya pertenece a otro jugador.");
            return false;
        }

        if(solicitante.getFortuna() < this.valor){
            System.out.println("ERROR. No tienes suficiente dinero para comprar esta casilla.");
            return false;
        }

        solicitante.sumarFortuna(-valor);
        solicitante.sumarGastos(valor);

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
            System.out.println("La cantidad a añadir debe ser un número positivo.");
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
            cadena +=("\tgrupo: " + getGrupo().getColorGrupo() + "," + "\n");
            cadena +=("\tpropietario: " + getDuenho().getNombre() + "," + "\n");
            cadena +=("\tvalor: " + getValor() + "," + "\n");
            cadena +=("\talquiler: " + alquiler + "," + "\n");
            cadena +=("\tvalor hotel: " + getValor()*0.6f + "," + "\n");
            cadena +=("\tvalor casa: " + getValor()*0.6f + "," + "\n");
            cadena +=("\tvalor piscina: " + getValor()*0.4f + "," + "\n");
            cadena +=("\tvalor pista de deporte: " + getValor()*1.25f + "," + "\n");
            cadena +=("\talquiler una casa: " + alquiler*5 + "," + "\n");
            cadena +=("\talquiler dos casas: " + alquiler*15 + "," + "\n");
            cadena +=("\talquiler tres casas: " + alquiler*35 + "," + "\n");
            cadena +=("\talquiler cuatro casas: " + alquiler*50 + "," + "\n");
            cadena +=("\talquiler hotel: " + alquiler*70 + "," + "\n");
            cadena +=("\talquiler piscina: " + alquiler*25 + "," + "\n");
            cadena +=("\talquiler pista de deporte: " + alquiler*25);
            cadena +=("\n},");
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
        if (getTipo().equals("Solar")){
            cadena = ("{\n");
            cadena += ("\ttipo: " + getTipo() + "," + "\n");
            cadena +=("\tgrupo: " + getGrupo().getColorGrupo() + "," + "\n");
            cadena +=("\tvalor: " + getValor());
            cadena +=("\n},");
        }  else if (getTipo().equals("Transporte")){
            cadena = ("{\n");
            cadena += ("\ttipo: " + getTipo() + "," + "\n");
            cadena +=("\tvalor: " + getValor() + "," + "\n");
            cadena +=("\n},");
        } else if (getTipo().equals("Servicio")){
            cadena = ("{\n");
            cadena += ("\ttipo: " + getTipo() + "," + "\n");
            cadena +=("\tvalor: " + getValor() + "," + "\n");
            cadena +=("\n},");
        }
        return cadena;
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

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
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
}