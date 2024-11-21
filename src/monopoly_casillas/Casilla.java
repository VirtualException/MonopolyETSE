package monopoly_casillas;

import java.util.ArrayList;
import java.util.Scanner;

import monopoly_jugador.Jugador;
import monopoly_tablero.Grupo;
import monopoly_tablero.Tablero;
import monopoly_tablero.Valor;
import monopoly_avatares.Avatar;
import monopoly_edificios.Edificio;


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
    private boolean hipotecada; //Indica si una casilla está hipotecada o no.

    private int[] contarCaer; // Cuenta las veces que un jugador cae en esta casilla. Index=Jugador
    private ArrayList<Edificio> edificios;  //Edificios contruídos en esta casilla.


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
            System.out.println("Este avatar ya existe, elige otro distinto.");
        } else {
            avatares.add(av);
        }
    }

    //Método utilizado para eliminar un avatar del array de avatares en casilla.
    public void eliminarAvatar(Avatar av) {
        if(avatares.isEmpty() || !avatares.contains(av)) {
            System.out.println("No hay ningún avatar.");
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
    public void evaluarCasilla(Tablero tab, Jugador jugador, Jugador banca, ArrayList<Jugador> jugadores) {

        String tipoCasilla = this.getTipo();

        /* El jugador cayó una vez más en esta casilla */
        this.contarCaer[jugador.getIndice()]++;

        /* Depende de donde caímos, hacer algo */
        switch (tipoCasilla) {
            case "Solar":

                /* Si hay dueño */
                if (duenho != banca && duenho != jugador) {

                    /* !!!! Calcular cuanto tiene que pagar !!!! */

                    /* Alquiler inicial = 10% */
                    float pago_alquiler = valor * 10.f;

                    /* Sumar alquiler dependiendo de las edificaciones. */
                    switch (this.getCasasN()) {
                        case 1 -> pago_alquiler += pago_alquiler * 5;
                        case 2 -> pago_alquiler += pago_alquiler * 15;
                        case 3 -> pago_alquiler += pago_alquiler * 35;
                        case 4 -> pago_alquiler += pago_alquiler * 50;
                    }
                    pago_alquiler += pago_alquiler * 70 * this.getHotelesN();
                    pago_alquiler += pago_alquiler * 25 * this.getPiscinasN();
                    pago_alquiler += pago_alquiler * 25 * this.getPistasN();

                    /* Si el dueño del solar tiene el grupo, se dobla el valor. */
                    if (this.grupo.esDuenhoGrupo(this.duenho)) {
                        pago_alquiler *= 2;
                    }

                    /* Si no puede pagarlo */
                    if (jugador.getFortuna() < pago_alquiler) {
                        System.out.println("Dinero insuficiente. El jugador ahora tiene una deuda y debe solucionarla.");
                        jugador.setDeuda(valor);
                        break;
                    }

                    /* El jugador paga al propietario */
                    jugador.sumarGastos(pago_alquiler);
                    jugador.setPagoDeAlquileres(jugador.getPagoDeAlquileres() + pago_alquiler);
                    jugador.sumarFortuna(-pago_alquiler);
                    /* El propietario recibe */
                    duenho.sumarFortuna(pago_alquiler);
                    duenho.setCobroDeAlquileres(duenho.getCobroDeAlquileres() + pago_alquiler);

                    System.out.println("El jugador " + jugador.getNombre() + " paga " + pago_alquiler + " € de alquiler.");

                }
                break;

            case "Transporte":
                /* Si no hay dueño */
                if (duenho == banca || duenho == jugador) {
                    break;
                }

                if (jugador.getFortuna() < valor) {
                    System.out.println("El jugador " + jugador.getNombre() + " no tiene suficiente dinero para pagar el transporte. El jugador ahora tiene una deuda y debe solucionarla.");
                    jugador.setDeuda(valor);
                    break;
                }

                System.out.println("El jugador " + jugador.getNombre() + " paga el transporte por " + valor + "€.");
                jugador.sumarGastos(valor);
                jugador.sumarFortuna(-valor);
                jugador.setPagoTasasEImpuestos(jugador.getPagoTasasEImpuestos() + valor);
                duenho.sumarFortuna(valor);

                break;

            case "Comunidad":
                Scanner scanner1 = new Scanner(System.in);
                int opcion1 = 0;
                System.out.println("Has caído en una casilla de Comunidad, por favor, escoge una carta");
                boolean numeroIncorrecto1 = true;
                while (numeroIncorrecto1) {
                    System.out.print("Escoge un valor del 1 al 6: ");
                    try {
                        opcion1 = Integer.parseInt(scanner1.nextLine()); //hacemos un parse int
                        if (opcion1 >= 1 && opcion1 <= 6) {
                            numeroIncorrecto1 = false; // Si el número está en el rango, sale del bucle
                        } else {
                            System.out.println("Valor erróneo. Debe ser un número entre 1 y 6.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Entrada no válida. Por favor, ingresa un número entre 1 y 6.");
                    }
                }
                tab.getCartas().accion(this, jugador, banca, jugadores, tab, opcion1); //ejecutamos la funciona de las cartas
                break;
            case "Suerte":
                Scanner scanner2 = new Scanner(System.in);
                int opcion2 = 0;
                boolean numeroIncorrecto2 = true;
                System.out.println("Has caído en una casilla de Suerte, por favor, escoge una carta");
                while (numeroIncorrecto2) {
                    System.out.print("Escoge un valor del 1 al 6: ");
                    try {
                        opcion2 = Integer.parseInt(scanner2.nextLine()); //hacemos un parse int
                        if (opcion2 >= 1 && opcion2 <= 6) {
                            numeroIncorrecto2 = false; // Si el número está en el rango, sale del bucle
                        } else {
                            System.out.println("Valor erróneo. Debe ser un número entre 1 y 6.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Entrada no válida. Por favor, ingresa un número entre 1 y 6.");
                    }
                }
                tab.getCartas().accion(this, jugador, banca, jugadores, tab, opcion2); //ejecutamos la funcion de las cartas
                break;
            case "Servicio":
                /* Si no hay dueño */
                if (duenho == banca || duenho == jugador) {
                    break;
                }

                if (jugador.getFortuna() < valor) {
                    System.out.println("El jugador " + jugador.getNombre() + " no tiene suficiente dinero para pagar el servicio. El jugador ahora tiene una deuda y debe solucionarla.");
                    jugador.setDeuda(valor);
                    break;
                }

                System.out.println("El jugador " + jugador.getNombre() + " paga el servicio por " + valor + "€.");
                jugador.sumarGastos(valor);
                jugador.sumarFortuna(-valor);
                jugador.setPagoTasasEImpuestos(jugador.getPagoTasasEImpuestos() + valor);
                duenho.sumarFortuna(valor);

                break;
            case "Impuesto":

                if (jugador.getFortuna() < impuesto) {
                    System.out.println("El jugador " + jugador.getNombre() + " no tiene suficiente dinero para pagar el impuesto. El jugador ahora tiene una deuda y debe solucionarla.");
                    jugador.setDeuda(impuesto);
                    break;
                }

                System.out.println("El jugador " + jugador.getNombre() + " paga un impuesto de " + impuesto + ".");
                jugador.sumarGastos(impuesto);
                jugador.sumarFortuna(-impuesto);
                jugador.setPagoTasasEImpuestos(jugador.getPagoTasasEImpuestos() + impuesto);
                banca.sumarFortuna(impuesto);
                /* Bote del parking */
                Casilla parking = tab.encontrar_casilla("Parking");
                parking.sumarValor(impuesto);
                break;

            default:
                System.out.println("Evaluando tipo especial");
                /* Cae en IrCárcel */
                if (nombre.equals("IrCarcel")) {
                    jugador.encarcelar(tab.getPosiciones());
                    jugador.incrementarVecesEnCarcel();
                }
                /* Cae en Parking */
                else if (nombre.equals("Parking")) {
                    float bote = this.getValor();
                    jugador.sumarFortuna(bote);
                    jugador.setPremiosInversionesOBote(jugador.getPremiosInversionesOBote() + bote);
                    this.setValor(0);
                }
                break;
        }
    }

    /*Método usado para comprar una casilla determinada. Parámetros:
     * - Jugador que solicita la compra de la casilla.
     * - Banca del monopoly (es el dueño de las casillas no compradas aún).*/
    public boolean comprarCasilla(Jugador solicitante, Jugador banca) {

        if(!getTipo().equals("Solar") && !getTipo().equals("Transporte") && !getTipo().equals("Servicio")){
            System.out.println("Esta casilla no se puede comprar.");
            return false;
        }

        if(!this.duenho.equals(banca)){
            System.out.println("Esta casilla ya pertenece a un jugador.");
            return false;
        }

        if(solicitante.getFortuna() < this.valor){
            System.out.println("No tienes suficiente dinero para comprar esta casilla.");
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
                cadena +=("\tgrupo: " + getGrupo().getColorGrupo() + "," + "\n");
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
            if (e.getTipo().equals("casa"))
                sum++;
        }
        return sum;
    }
    /* número de hoteles */
    public int getHotelesN() {
        int sum = 0;
        for (Edificio e : edificios) {
            if (e.getTipo().equals("hotel"))
                sum++;
        }
        return sum;
    }
    /* número de piscinas */
    public int getPiscinasN() {
        int sum = 0;
        for (Edificio e : edificios) {
            if (e.getTipo().equals("piscina"))
                sum++;
        }
        return sum;
    }

    /* número de pistas */
    public int getPistasN() {
        int sum = 0;
        for (Edificio e : edificios) {
            if (e.getTipo().equals("pista"))
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



    public float getPrecioOriginal() {
        float precio_original = switch (grupo.getColorGrupo()) {
            case "negro" -> Valor.VALOR_GRUPO_NEGRO;
            case "cyan" -> Valor.VALOR_GRUPO_AZUL;
            case "rosa" -> Valor.VALOR_GRUPO_ROSA;
            case "amarillo" -> Valor.VALOR_GRUPO_AMARELO;
            case "vermello" -> Valor.VALOR_GRUPO_VERMELLO;
            case "marron" -> Valor.VALOR_GRUPO_MARRON;
            case "verde" -> Valor.VALOR_GRUPO_VERDE;
            case "azul" -> Valor.VALOR_GRUPO_AZUL_OSCURO;
            default -> 0.f;
        };
        precio_original /= grupo.getMiembros().size();
        return  precio_original;
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
}