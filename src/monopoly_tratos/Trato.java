package monopoly_tratos;

import monopoly_casillas.Casilla;
import monopoly_juego.Juego;
import monopoly_jugador.Jugador;

import java.util.ArrayList;

public class Trato {

    private String id; //Id del trato
    private int numeroDeTrato;
    private Jugador proponente;
    private Jugador destinatario;
    private Casilla propiedadOfrecida;
    private Casilla propiedadSolicitada;
    private float cantidadOfrecida;
    private float cantidadSolicitada;
    private Boolean aceptado;


    public Trato(ArrayList<Trato> tratos, Jugador proponente, Jugador destinatario, Casilla propiedadOfrecida, Casilla propiedadSolicitada, float cantidadOfrecida, float cantidadSolicitada){
        this.id = crearId(tratos);
        this.proponente = proponente;
        this.destinatario = destinatario;
        this.propiedadOfrecida = propiedadOfrecida;
        this.propiedadSolicitada = propiedadSolicitada;
        this.cantidadOfrecida = cantidadOfrecida;
        this.cantidadSolicitada = cantidadSolicitada;
        this.aceptado = null;
    }


    // Método que crea el id de un trato
    private String crearId(ArrayList<Trato> tratos){
        String id = "";

        id = "trato" + String.valueOf(tratos.get(tratos.size() - 1).getNumeroDeTrato() + 1);
        this.numeroDeTrato = tratos.get(tratos.size() - 1).getNumeroDeTrato() + 1;
        
        return id;
    }


    // Método que verifica si el trato propuesto es válido o no
    public boolean verificarTrato(){
        
        if (propiedadOfrecida != null && !proponente.getPropiedades().contains(propiedadOfrecida)) {
            Juego.consola.imprimir("No se puede proponer el trato: " + propiedadOfrecida.getNombre() + " no pertenece a " + proponente.getNombre() + ".");
            return false;
        }

        if (propiedadSolicitada != null && !destinatario.getPropiedades().contains(propiedadSolicitada)) {
            Juego.consola.imprimir("No se puede proponer el trato: " + propiedadSolicitada.getNombre() + " no pertenece a " + destinatario.getNombre() + ".");
            return false;
        }
        return true;
    }


    // Método que imprime el trato propuesto
    public String mostrarTratoPropuesto(){
        String cadena = (destinatario.getNombre() + ", ¿te doy ");

        if (propiedadOfrecida != null) {
            cadena += (propiedadOfrecida.getNombre() + ",");
        } 

        if (cantidadOfrecida > 0) {
            cadena += (cantidadOfrecida + "€ ");
        }

        cadena += ("y tú me das ");

        if (propiedadSolicitada != null && cantidadSolicitada > 0) {
            cadena += (propiedadSolicitada.getNombre() + "y" + cantidadSolicitada + "€");
        }
        else {
            if (propiedadSolicitada != null) {
                cadena += (propiedadSolicitada.getNombre());
            }

            if (cantidadSolicitada > 0) {
                cadena += (cantidadSolicitada + "€");
            }
        }
        cadena += ("?");
        return cadena;
    }


    public void cambiarPropiedadPorPropiedad(Casilla propiedadOfrecida, Casilla propiedadSolicitada){
        this.propiedadOfrecida = propiedadSolicitada;
        this.propiedadSolicitada = propiedadOfrecida;

        if (!verificarTrato()) {
            return;
        }

        this.proponente.getPropiedades().remove(propiedadOfrecida);
        this.proponente.getPropiedades().add(propiedadSolicitada);
        this.destinatario.getPropiedades().remove(propiedadSolicitada);
        this.destinatario.getPropiedades().add(propiedadOfrecida);
    }


    public void cambiarPropiedadPorDinero(Casilla propiedadOfrecida, float cantidadSolicitada) {

        if (!this.proponente.getPropiedades().contains(propiedadOfrecida)) {
            Juego.consola.imprimir("ERROR. El jugador proponente " + this.proponente.getNombre() + " no posee la propiedad que ofrece.");
            return;
        } else if (this.destinatario.getFortuna() < cantidadSolicitada) {
            Juego.consola.imprimir("ERROR. El jugador destinatario " + this.destinatario.getNombre() + " no tiene el dinero suficiente solicitado. Se conserva el trato.");
            return;
        } else {
            this.proponente.sumarFortuna(cantidadSolicitada);
            this.proponente.getPropiedades().remove(propiedadOfrecida);
            this.destinatario.getPropiedades().add(propiedadOfrecida);
            this.destinatario.sumarFortuna(-cantidadSolicitada);
            this.destinatario.sumarGastos(cantidadSolicitada);
            return;
        }
    }


    public void cambiarDineroPorPropiedad(float cantidadOfrecida, Casilla propiedadSolicitada) {
        
        if (this.proponente.getFortuna() < cantidadOfrecida) {
            Juego.consola.imprimir("ERROR. El jugador proponente " + this.proponente.getNombre() + " no tiene el dinero suficiente ofrecido.");
            return;
        } else if (!this.destinatario.getPropiedades().contains(propiedadSolicitada)) {
            Juego.consola.imprimir("ERROR. El jugador destinatario " + this.destinatario.getNombre() + " no posee la propiedad solicitada.");
            return;
        } else {
            this.proponente.sumarFortuna(-cantidadOfrecida);
            this.proponente.sumarGastos(cantidadOfrecida);
            this.proponente.anhadirPropiedad(propiedadSolicitada);
            this.destinatario.eliminarPropiedad(propiedadSolicitada);
        }
    }


    public void cambiarPropiedadPorPropiedadYDinero(Casilla propiedadOfrecida, Casilla propiedadSolicitada, float cantidadSolicitada) {
        if (!this.proponente.getPropiedades().contains(propiedadOfrecida)) {
            Juego.consola.imprimir("ERROR. El jugador proponente " + this.proponente.getNombre() + " no posee la propiedad que ofrece.");
            return;
        } else if (!this.destinatario.getPropiedades().contains(propiedadSolicitada)) {
            Juego.consola.imprimir("ERROR. El jugador destinatario " + this.destinatario.getNombre() + " no posee la propiedad que ofrece.");
            return;
        } else if (this.destinatario.getFortuna() < cantidadSolicitada) {
            Juego.consola.imprimir("ERROR. El jugador destinatario " + this.destinatario.getNombre() + " no tiene el dinero suficiente solicitado. Se conserva el trato.");
            return;
        } else {
            this.proponente.eliminarPropiedad(propiedadOfrecida);
            this.proponente.anhadirPropiedad(propiedadSolicitada);
            this.proponente.sumarFortuna(cantidadSolicitada);
            this.destinatario.eliminarPropiedad(propiedadSolicitada);
            this.destinatario.sumarFortuna(-cantidadSolicitada);
            this.destinatario.sumarGastos(cantidadSolicitada);
        }
    }


    public void cambiarPropiedadYDineroPorPropiedad(Casilla propiedadOfrecida, float cantidadOfrecida, Casilla propiedadSolicitada) {
        if (!this.proponente.getPropiedades().contains(propiedadOfrecida)) {
            Juego.consola.imprimir("ERROR. El jugador proponente " + this.proponente.getNombre() + " no posee la propiedad que ofrece.");
            return;
        } else if (this.proponente.getFortuna() < cantidadOfrecida) {
            Juego.consola.imprimir("ERROR. El jugador proponente " + this.proponente.getNombre() + " no tiene el dinero suficiente ofrecido.");
        } else if (!this.destinatario.getPropiedades().contains(propiedadSolicitada)) {
            Juego.consola.imprimir("ERROR. El jugador destinatario " + this.destinatario.getNombre() + " no posee la propiedad solicitada.");
            return;
        } else {
            this.proponente.eliminarPropiedad(propiedadOfrecida);
            this.proponente.sumarFortuna(-cantidadOfrecida);
            this.proponente.sumarGastos(cantidadOfrecida);
            this.proponente.anhadirPropiedad(propiedadSolicitada);
            this.destinatario.eliminarPropiedad(propiedadSolicitada);
        }
    }


    // Función para listar la información de los tratos de un jugador 
    private void listarTratos(ArrayList<Trato> tratos, Jugador jugadorActual){

        StringBuilder cadena = new StringBuilder();    
        for (Trato trato: tratos){
            if (trato.getDestinatario().getNombre().equals(jugadorActual.getNombre())){
                cadena.append("{\n");
                cadena.append("\tid: " + trato.getId() + "\n");
                cadena.append("\tjugadorPropone: " + trato.getProponente() + ",\n");
                cadena.append("\ttrato: " + trato.mostrarTratoPropuesto() + "\n");
                cadena.append("},\n");
            }
        }
    }



    private void eliminarTrato(ArrayList<Trato> tratos, Jugador jugadorActual, String id){
        Trato tratoAEliminar = null;

        for (Trato trato : tratos) {
            if (trato.getId().equals(id)){
                tratoAEliminar = trato;
            } 
        }
        
        if (tratoAEliminar == null){
            System.out.println("ERROR. Este trato no existe");
        }
        else {
            String nombreJugadorActual = jugadorActual.getNombre();
            if (tratoAEliminar.getProponente().getNombre().equals(nombreJugadorActual)){
                System.out.println("ERROR. El trato no le pertenece al jugador");
            }
            else if (tratoAEliminar.getAceptado() == Boolean.TRUE){
                System.out.println("ERROR. El trato fue aceptado, no se puede eliminar");
            }
            else {
                tratos.remove(tratoAEliminar);
                System.out.println("Se ha eliminado el trato " + id + ".");
            }
        }
    }


    // GETTERS Y SETTERS

    public String getId(){
        return this.id;
    }

    public int getNumeroDeTrato(){
        return this.numeroDeTrato;
    }

    public Jugador getProponente(){
        return this.proponente;
    }

    public Jugador getDestinatario(){
        return this.destinatario;
    }

    public Casilla getPropiedadOfrecida(){
        return this.propiedadOfrecida;
    }

    public Casilla getPropiedadSolicitada(){
        return this.propiedadSolicitada;
    }

    public float getCantidad1(){
        return this.cantidadOfrecida;
    }

    public float getCantidad2(){
        return this.cantidadSolicitada;
    }

    public Boolean getAceptado(){
        return this.aceptado;
    }

    public void setAceptado(Boolean aceptado){  
        this.aceptado = aceptado;
    }

    public void setPropiedadOfrecida(Casilla propiedadOfrecida){
        this.propiedadOfrecida = propiedadOfrecida;
    }

    public void setPropiedadSolicitada(Casilla propiedadSolicitada){
        this.propiedadSolicitada = propiedadSolicitada;
    }   

    public void setCantidad1(float cantidadOfrecida){
        this.cantidadOfrecida = cantidadOfrecida;
    }

    public void setCantidad2(float cantidadSolicitada){
        this.cantidadSolicitada = cantidadSolicitada;
    }

    public void setProponente(Jugador proponente){
        this.proponente = proponente;
    }

    public void setDestinatario(Jugador destinatario){
        this.destinatario = destinatario;
    }

    public void setNumeroDeTrato(int numeroDeTrato){
        this.numeroDeTrato = numeroDeTrato;
    }

    public void setId(String id){
        this.id = id;
    }
}