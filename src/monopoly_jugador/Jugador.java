package monopoly_jugador;

import java.util.ArrayList;

import monopoly_avatares.Avatar;
import monopoly_casillas.Casilla;
import monopoly_casillas.propiedades.Solar;
import monopoly_edificios.Edificio;
import monopoly_exception.avatares.AvatarNoValidoException;
import monopoly_exception.dados.DadosNoValidosException;
import monopoly_exception.propiedades.PropiedadException;
import monopoly_exception.casillas.TipoCasillaException;
import monopoly_exception.edificios.TipoEdificioException;
import monopoly_juego.Juego;
import monopoly_tablero.Tablero;
import monopoly_tablero.Valor;


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
    private int indice; /* Index dentro del array de jugadores */
    private float deuda;
    private int tiradas;
    private int tiradas_turno;
    private boolean modo;
    private boolean pagarBanca;
    private int debeContinuar;

    /*Constructor principal. Requiere parámetros:
     * Nombre del jugador, tipo del avatar que tendrá, casilla en la que empezará y ArrayList de
     * avatares creados (usado para dos propósitos: evitar que dos jugadores tengan el mismo nombre y
     * que dos avatares tengan mismo ID). Desde este constructor también se crea el avatar.
     */
    public Jugador(String nombre, String tipoAvatar, Casilla inicio, ArrayList<Avatar> avCreados, int index) {
        this.nombre = nombre;
        if (!tipoAvatar.equals("Banca")) {
            try {
                this.avatar = new Avatar(tipoAvatar, this, inicio, avCreados); // Creación del avatar
            } catch (AvatarNoValidoException e) {
                System.out.println("Advertencia: " + e.getMessage());
                // Establece un avatar por defecto en caso de error
                this.avatar = new Avatar("Pelota", this, inicio, avCreados);
            }
        }
        this.fortuna = (float) Valor.FORTUNA_INICIAL;
        this.gastos = 0.0f;
        this.enCarcel = false;
        this.tiradasCarcel = 0;
        this.vueltas = 0;
        this.tiradasDobles = 0;
        this.tiradas_turno = 0;
        this.dineroInvertido = 0.0f;
        this.pagoTasasEImpuestos = 0.0f;
        this.pagoDeAlquileres = 0.0f;
        this.cobroDeAlquileres = 0.0f;
        this.pasarPorCasillaDeSalida = 0.0f;
        this.premiosInversionesOBote = 0.0f;
        this.vecesEnLaCarcel = 0;
        this.propiedades = new ArrayList<>();
        this.indice = index;
        this.pagarBanca = false;
        this.debeContinuar = 0;
    }



    //Otros métodos:
    //Método para añadir una propiedad al jugador. Como parámetro, la casilla a añadir.
    public void anhadirPropiedad(Casilla casilla) {
        propiedades.add(casilla);
    }



    //Método para eliminar una propiedad del arraylist de propiedades de jugador.
    public void eliminarPropiedad(Casilla casilla) throws PropiedadException{
        if (propiedades.contains(casilla)) {
            propiedades.remove(casilla);
        } else {
            throw new PropiedadException("El jugador no es poseedor de la propiedad");
        }
    }



    //Método para añadir fortuna a un jugador
    //Como parámetro se pide el valor a añadir. Si hay que restar fortuna, se pasaría un valor negativo.
    public void sumarFortuna(float valor) {

        /* Si hay deuda. */
        if (deuda > 0.f) {
            if (valor >= deuda) {
                this.fortuna += valor-deuda;
                deuda = 0.f;
            }
            else {
                this.deuda = this.deuda - valor;
            }
        }
        else {
            this.fortuna += valor;
        }
    }



    //Método para sumar gastos a un jugador.
    //Parámetro: valor a añadir a los gastos del jugador (será el precio de un solar, impuestos pagados...).
    public void sumarGastos(float valor) {
        this.gastos += valor;
    }



    /*Método para establecer al jugador en la cárcel.
     * Se requiere disponer de las casillas del tablero para ello (por eso se pasan como parámetro).*/
    public void encarcelar(ArrayList<ArrayList<Casilla>> pos) {
        
        for(ArrayList<Casilla> arrayList : pos){
            for(Casilla casilla : arrayList) {
                if(casilla.getNombre().equals("Carcel")){

                    try {
                        this.avatar.getLugar().eliminarAvatar(this.avatar);
                        this.avatar.setLugar(casilla);
                        casilla.anhadirAvatar(this.avatar);
                        this.enCarcel = true;
                        this.incrementarVecesEnCarcel();
                    } catch (AvatarNoValidoException e) {
                        Juego.consola.imprimir("Error: " + e.getMessage()); // Manejo del error
                    }
                    return;
                }
            }
        }
    }


    // Método para hipotecar una propiedad
    public void hipotecarPropiedad(Casilla c) throws TipoCasillaException{

        float precioHipoteca;

        // Verificamos que el jugador posee la propiedad
        if (!propiedades.contains(c)) {
            Juego.consola.imprimir(this.getNombre() + " no puede hipotecar " + c.getNombre() + ". No es una propiedad que le pertenece.");
            return;
        }

        // Verificamos que la propiedad no esté hipotecada
        if (c.getHipotecada()) {
            Juego.consola.imprimir(this.getNombre() + " no puede hipotecar " + c.getNombre() + ". Ya está hipotecada.");
            return;
        }

        switch (c.getTipo()) {
                case "Solar":

                    // Verificamos que el solar no tenga edificios
                    if (c.getEdificios() != null && c.getEdificios().isEmpty()) {
                        precioHipoteca = (((Solar)c).getPrecioOriginal() / 2);
                        this.sumarFortuna(precioHipoteca);
                        c.setHipotecada(true);
                        Juego.consola.imprimir(this.getNombre() + " recibe " + precioHipoteca + "€ por la hipoteca de " + c.getNombre() + ". No puede recibir alquileres ni edificar en el grupo " + ((Solar)c).getGrupo().getColorGrupo() + ".");
                    } else {
                        Juego.consola.imprimir("No puedes hipotecar " + c.getNombre() + " porque tiene edificios. Debes vender todos los edificios.");
                    }
                    break;

                case "Transporte":
                    precioHipoteca = (float) Valor.SUMA_VUELTA / 2;
                    this.sumarFortuna(precioHipoteca);
                    c.setHipotecada(true);
                    Juego.consola.imprimir(this.getNombre() + " recibe " + precioHipoteca + "€ por la hipoteca de " + c.getNombre() + ". No puede recibir alquileres en " + c.getNombre() + ".");
                    break;

                case "Servicio":
                    precioHipoteca = (float) (0.75f * Valor.SUMA_VUELTA) / 2;
                    this.sumarFortuna(precioHipoteca);
                    c.setHipotecada(true);
                    Juego.consola.imprimir(this.getNombre() + " recibe " + precioHipoteca + "€ por la hipoteca de " + c.getNombre() + ". No puede recibir alquileres en " + c.getNombre() + ".");
                    break;

                default:
                    throw new TipoCasillaException("Tipo de casilla no válido para hipotecar.");

            }


    }



    // Método para deshipotecar una propiedad
    public void deshipotecarPropiedad(Casilla c) throws TipoCasillaException{
        
        float precioDeshipoteca;

        // Verificamos que el jugador posee la propiedad
        if (!this.propiedades.contains(c)) {
            Juego.consola.imprimir(this.getNombre() + " no puede deshipotecar " + c.getNombre() + ". No es una propiedad que le pertenece.");
            return;
        }
        if (!c.getHipotecada()) {
            Juego.consola.imprimir(this.getNombre() + " no puede deshipotecar " + c.getNombre() + ". No está hipotecada.");
            return;
        }

        /* Proceder a deshipotecar. */
        switch (c.getTipo()) {
            case "Solar":
                precioDeshipoteca = (float) 1.1 * (((Solar)c).getPrecioOriginal() / 2);

                if (this.getFortuna() >= precioDeshipoteca) {
                    this.sumarFortuna(-precioDeshipoteca);
                    this.sumarGastos(precioDeshipoteca);
                    c.setHipotecada(false);
                    Juego.consola.imprimir(this.getNombre() + " paga " + precioDeshipoteca + "€ por deshipotecar " + c.getNombre() + ". Ahora puede recibir alquileres y edificar en el grupo " + ((Solar)c).getGrupo().getColorGrupo() + ".");
                } else {
                    Juego.consola.imprimir(this.getNombre() + " no tiene suficiente dinero para deshipotecar " + c.getNombre() + ".");
                }
                break;

            case "Transporte":
                precioDeshipoteca = (float) (1.1 * (Valor.SUMA_VUELTA / 2));

                if (this.fortuna >= precioDeshipoteca) {
                    this.sumarFortuna(-precioDeshipoteca);
                    this.sumarGastos(precioDeshipoteca);
                    c.setHipotecada(false);
                    Juego.consola.imprimir(this.getNombre() + " paga " + precioDeshipoteca + "€ por la hipoteca de " + c.getNombre() + ". Ahora puede recibir alquileres en " + c.getNombre() + ".");
                } else {
                    Juego.consola.imprimir(this.getNombre() + " no tiene suficiente dinero para deshipotecar " + c.getNombre() + ".");
                }
                break;

            case "Servicio":
                precioDeshipoteca = (float) (1.1 * ((0.75f * Valor.SUMA_VUELTA) / 2));

                if (this.fortuna >= precioDeshipoteca) {
                    this.sumarFortuna(-precioDeshipoteca);
                    this.sumarGastos(precioDeshipoteca);
                    c.setHipotecada(false);
                    Juego.consola.imprimir(this.getNombre() + " paga " + precioDeshipoteca + "€ por la hipoteca de " + c.getNombre() + ". Ahora puede recibir alquileres en " + c.getNombre() + ".");
                } else {
                    Juego.consola.imprimir(this.getNombre() + " no tiene suficiente dinero para deshipotecar " + c.getNombre() + ".");
                }
                break;

            default:
                throw new TipoCasillaException("Tipo de casilla no válido para deshipotecar.");
        }
    }



    // Método para vender edificios
    public void venderEdificios(String tipoEdificio, String nombreCasilla, int numEdificios) throws TipoEdificioException{

        Casilla propiedad = null;

        for(Casilla c : this.propiedades){
            if(c.getNombre().equals(nombreCasilla)){
                propiedad = c;
                break;
            }
        }

        if(propiedad == null){
            Juego.consola.imprimir("No se pueden vender " + tipoEdificio + " en " + nombreCasilla + ". Esta propiedad no pertenece a " + nombre);
            return;
        }

        int n_edificios_actuales;
        float multiplicador;

        switch(tipoEdificio) {
            case "casa":
                n_edificios_actuales = propiedad.getCasasN();
                multiplicador = Valor.MULTIPLICADOR_CASA;
                break;
            case "hotel":
                n_edificios_actuales = propiedad.getHotelesN();
                multiplicador = Valor.MULTIPLICADOR_HOTEL;
                break;
            case "piscina":
                n_edificios_actuales = propiedad.getPiscinasN();
                multiplicador = Valor.MULTIPLICADOR_PISCINA;
                break;
            case "pista":
                n_edificios_actuales = propiedad.getPistasN();
                multiplicador = Valor.MULTIPLICADOR_PISTA_DE_DEPORTE;
                break;
            default:
                throw new TipoEdificioException("Tipo de edificio no reconocido. Debe ser: 'casa', 'hotel', 'piscina' o 'pista'.");
        }

        float precioEdificio = numEdificios * ((propiedad.getValor() * multiplicador) / 2);

        if(n_edificios_actuales < numEdificios) {
            Juego.consola.imprimir("No hay "+ numEdificios + " " + tipoEdificio + "(s). Solamente se podrían vender " + n_edificios_actuales + ".");
            return;
        }

        this.sumarFortuna(precioEdificio);
        for (int i = 0; i < numEdificios; i++)
            propiedad.eliminarEdificio(tipoEdificio);
        Juego.consola.imprimir(nombre + " ha vendido " + numEdificios +  " " + tipoEdificio + "(s) en " + nombreCasilla + ", recibiendo " + precioEdificio + "€." + " En la propiedad queda(n) " + (n_edificios_actuales - numEdificios) + " " + tipoEdificio + "(s).");

    }

    
    /* Mover jugador de la casilla actual respecto al valor de la tirada*/
    public boolean moverJugador(Tablero tablero, int tirada, ArrayList<Jugador> jugadores) {

        ArrayList<ArrayList<Casilla>> pos = tablero.getPosiciones();

        Casilla c = this.getAvatar().getLugar();

        if (!this.isModo()) {
            try {
                Juego.consola.imprimir_sin_salto("El avatar " + this.getAvatar().getId() + " avanza " + tirada + " posiciones, desde " + c.getNombre());
                this.getAvatar().moverAvatar(pos, tirada);
                c = this.getAvatar().getLugar();
                Juego.consola.imprimir(" hasta " + c.getNombre() + ".");
                c.evaluarCasilla(tablero, this, tablero.getBanca(), jugadores); // Puede lanzar DadosNoValidosException
            } catch (DadosNoValidosException e) {
                Juego.consola.imprimir("Error: " + e.getMessage()); // Manejo del error
            }
            return true;
        } else {
            if (this.getAvatar().getTipo().equals("Pelota")) {

                /* Movimiento Pelota */
                Juego.consola.imprimir("Moviendo como Pelota");

                /* Está en el proceso de avanzar por cada casilla */
                if (debeContinuar > 0) {

                    debeContinuar-=2;
                    /* Si le queda UN SOLO PASO */
                    if (debeContinuar < 0) {
                        tirada = 1;
                        debeContinuar = 0;
                    }
                    else
                        tirada = 2;
                    try {
                        Juego.consola.imprimir("Pasando a la siguiente casilla (modo Pelota)");
                        Juego.consola.imprimir_sin_salto("El avatar " + this.getAvatar().getId() + " avanza " + tirada + " posiciones, desde " + c.getNombre());
                        this.getAvatar().moverAvatar(pos, tirada);
                        c = this.getAvatar().getLugar();
                        Juego.consola.imprimir(" hasta " + c.getNombre() + ".");
                        c.evaluarCasilla(tablero, this, tablero.getBanca(), jugadores);
                    } catch (DadosNoValidosException e) {
                        Juego.consola.imprimir("Error: " + e.getMessage()); // Manejo del error
                    }
                    return true;

                }

                /* Si es la primera tirada en modo Pelota */
                if (tirada > 4) {

                    int tiradaPrimera;
                    if (avatar.getLugar().getPosicion() % 2 == 0)
                        tiradaPrimera = 5;
                    else
                        tiradaPrimera = 4;

                    try {
                        Juego.consola.imprimir("Primer movimiento (modo Pelota)");
                        Juego.consola.imprimir_sin_salto("El avatar " + this.getAvatar().getId() + " avanza " + tiradaPrimera + " posiciones, desde " + c.getNombre());
                        this.getAvatar().moverAvatar(pos, tiradaPrimera);
                        c = this.getAvatar().getLugar();
                        Juego.consola.imprimir(" hasta " + c.getNombre() + ".");
                        c.evaluarCasilla(tablero, this, tablero.getBanca(), jugadores);
                    } catch (DadosNoValidosException e) {
                        Juego.consola.imprimir("Error: " + e.getMessage()); // Manejo del error
                    }

                    Juego.consola.imprimir("El jugador debe seguir avanzando aún");

                    debeContinuar = tirada - tiradaPrimera;

                }
                else {

                    int tiradaPrimera;
                    if (avatar.getLugar().getPosicion() % 2 == 0)
                        tiradaPrimera = 5;
                    else
                        tiradaPrimera = 4;

                    try {
                        Juego.consola.imprimir("Primer y único movimiento (modo Pelota)");
                        Juego.consola.imprimir_sin_salto("El avatar " + this.getAvatar().getId() + " avanza " + tiradaPrimera + " posiciones hacia atrás, desde " + c.getNombre());
                        this.getAvatar().moverAvatarAtras(pos, tiradaPrimera);
                        c = this.getAvatar().getLugar();
                        Juego.consola.imprimir(" hasta " + c.getNombre() + ".");
                        c.evaluarCasilla(tablero, this, tablero.getBanca(), jugadores);
                    } catch (DadosNoValidosException e) {
                        Juego.consola.imprimir("Error: " + e.getMessage()); // Manejo del error
                    }

                    debeContinuar = 0;
                }



            } else if (this.getAvatar().getTipo().equals("Coche")) {

                /* Movimiento Coche */
                Juego.consola.imprimir("Moviendo como Coche");

                if (tiradas_turno <= -3) {
                    Juego.consola.imprimir("El jugador que tira en modo Coche no puede los siguientes " + (-tiradas_turno) + " turnos.");
                    return true;
                }

                /* Si lleva tirando muchas veces, no se tira*/
                if (tiradas_turno > 3) {
                    tiradas_turno = 0;
                    Juego.consola.imprimir("El jugador que tira en modo Coche ya tiró 3 veces.");
                    return true;
                }

                if (tirada > 4) {
                    try {
                        Juego.consola.imprimir_sin_salto("El avatar " + this.getAvatar().getId() + " avanza " + tirada + " posiciones, desde " + c.getNombre());
                        this.getAvatar().moverAvatar(pos, tirada);
                        c = this.getAvatar().getLugar();
                        Juego.consola.imprimir(" hasta " + c.getNombre() + ".");
                        c.evaluarCasilla(tablero, this, tablero.getBanca(), jugadores);
                    } catch (DadosNoValidosException e) {
                        Juego.consola.imprimir("Error: " + e.getMessage()); // Manejo del error
                    }
                    return false;
                }

                // Si la tirada es menor o igual a 4, retroceder y deshabilitar tiradas.

                tiradas_turno = -3;
                try {
                    Juego.consola.imprimir_sin_salto("El avatar " + this.getAvatar().getId() + " avanza " + tirada + " posiciones hacia atrás, desde " + c.getNombre());
                    this.getAvatar().moverAvatarAtras(pos, tirada);
                    c = this.getAvatar().getLugar();
                    Juego.consola.imprimir(" hasta " + c.getNombre() + ".");
                    c.evaluarCasilla(tablero, this, tablero.getBanca(), jugadores);
                } catch (DadosNoValidosException e) {
                    Juego.consola.imprimir("Error: " + e.getMessage()); // Manejo del error
                }
                return true;
            }
        }
        return true;
    }



    public void teleportJugador(Tablero tablero, Casilla casilla) {

        ArrayList<ArrayList<Casilla>> pos = tablero.getPosiciones();

        Casilla c = this.getAvatar().getLugar();

        Juego.consola.imprimir_sin_salto("El avatar " + this.getAvatar().getId() + " avanza a " + casilla.getNombre() + " , desde " + c.getNombre());
        this.getAvatar().teleportAvatar(pos, casilla);
        c = this.getAvatar().getLugar();
        Juego.consola.imprimir(" hasta " + c.getNombre() + ".");
    }



    // Método que devuelve una cadena con las estadísticas del jugador.
    public String estadisticasJugador(Jugador jugador){
        String cadena;

        cadena = ("{\n");
        cadena += ("\tdineroInvertido: " + jugador.getDineroInvertido() + ",\n");
        cadena += ("\tpagoTasasEImpuestos: " + jugador.getPagoTasasEImpuestos() + ",\n");
        cadena += ("\tpagoDeAlquileres: " + jugador.getPagoDeAlquileres() + ",\n");
        cadena += ("\tcobroDeAlquileres: " + jugador.getCobroDeAlquileres() + ",\n");
        cadena += ("\tpasarPorCasillaDeSalida: " + jugador.getPasarPorCasillaDeSalida() + ",\n");
        cadena += ("\tpremiosInversionesOBote: " + jugador.getPremiosInversionesOBote() + ",\n");
        cadena += ("\tvecesEnLaCarcel: " + jugador.getVecesEnLaCarcel() + "\n");
        cadena += ("}\n");

        return cadena;
    }

    
     //Método para declararse en bancarrota
     public void bancarrota(ArrayList<Jugador> jugadores, Jugador banca, boolean pagarBanca) {

        Jugador jugador = this;
        Jugador propietario = avatar.getLugar().getDuenho();

        boolean solvente = this.deuda == 0.f;

        if(!solvente){
            if(propietario.equals(banca) && !pagarBanca){
                traspasarPropiedadesJugador(banca, jugador);
                Juego.consola.imprimir("El jugador " + this.nombre + " se ha declarado en bancarrota. Sus propiedades pasan a estar de nuevo en venta al precio al que estaban.");
       
            } else if (!propietario.equals(banca)){
                traspasarPropiedadesJugador(propietario, jugador);
                propietario.sumarFortuna(jugador.fortuna);
                jugador.setFortuna(0.0f);
                Juego.consola.imprimir("El jugador " + this.nombre + " se ha declarado en bancarrota. Sus propiedades y fortuna pasan al jugador " + propietario.getNombre());

            } else if (propietario.equals(banca) && pagarBanca){
                traspasarPropiedadesJugador(banca, jugador);
                banca.sumarFortuna(jugador.fortuna);
                jugador.setFortuna(0.0f);
                Juego.consola.imprimir("El jugador " + this.nombre + " se ha declarado en bancarrota. Sus propiedades y fortuna pasan a la banca");
            }
            eliminarJugador(jugadores, jugador);   
        } else {
            traspasarPropiedadesJugador(banca, jugador);
            Juego.consola.imprimir("El jugador " + this.nombre + " se ha declarado en bancarrota. Sus propiedades pasan a estar de nuevo en venta al precio al que estaban.");
            eliminarJugador(jugadores, jugador); 
        }
    }


    private void traspasarPropiedadesJugador(Jugador nuevoPropietario, Jugador jugador){
        ArrayList<Casilla> propiedadesACopiar = new ArrayList<>(jugador.propiedades);
        for(Casilla c : propiedadesACopiar) {
            try {
                c.setValor(((Solar)c).getPrecioOriginal()); //resetea el precio de la casilla a su precio inicial
                nuevoPropietario.anhadirPropiedad(c);
                c.setDuenho(nuevoPropietario);
                jugador.eliminarPropiedad(c);
            } catch (PropiedadException e) {
                Juego.consola.imprimir("Error: " + e.getMessage()); // Manejo del error
            }
        }
    }

    //Método para eliminar a un jugador de la partida
    private void eliminarJugador(ArrayList<Jugador> jugadores, Jugador jugador){
        for (int i = 0; i < jugadores.size(); i++){
            if (jugadores.get(i).equals(jugador)){
                jugadores.get(i).getAvatar().getLugar().eliminarAvatar(jugadores.get(i).getAvatar());
                jugadores.remove(i);
                break;
            }
        }
        Juego.consola.imprimir("El jugador" + jugador.getNombre() + " ha sido eliminado de la partida.");
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
        ArrayList<Casilla> propiedadesNoHipotecadas  = new ArrayList<>();

        for(Casilla c : this.propiedades){
            if(!c.getHipotecada()){
                propiedadesNoHipotecadas.add(c);
            }
        }
        return propiedadesNoHipotecadas;
    }


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


    public ArrayList<Casilla> getHipotecas(){
        ArrayList<Casilla> propiedadesHipotecadas = new ArrayList<>();

        for(Casilla c : this.propiedades){
            if(c.getHipotecada()){
                propiedadesHipotecadas.add(c);
            }
        }
        return propiedadesHipotecadas;
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

    public void incrementarVecesEnCarcel(){
        if(this.enCarcel){
            this.vecesEnLaCarcel++;
        }
    }

    public void setPropiedades(ArrayList<Casilla> propiedades) {
        this.propiedades = propiedades;
    }

    public int getIndice() {
        return indice;
    }

    public int getTiradas() {
        return tiradas;
    }

    public void setTiradas(int tiradas) {
        this.tiradas = tiradas;
    }

    public float getDeuda() {
        return deuda;
    }

    public void setDeuda(float deuda) {
        this.deuda = deuda;
    }

    public void sumarTirada_Turno() {
        tiradas_turno++;
    }

    //public void setHipotecas(ArrayList<Hipoteca> hipotecas) {
    //    this.hipotecas = hipotecas;
    //}

    //public void setEdificios(ArrayList<Edificio> edificios) {
    //    this.edificios = edificios;
    //}

    public boolean isModo() {
        return modo;
    }

    public void setModo(boolean modo) {
        this.modo = modo;
    }

    public boolean isPagarBanca() {
        return pagarBanca;
    }

    public void setPagarBanca(boolean pagarBanca) {
        this.pagarBanca = pagarBanca;
    }

    public int getDebeContinuar() {
        return debeContinuar;
    }

}