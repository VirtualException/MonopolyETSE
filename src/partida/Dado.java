package partida;


import java.util.Random;

public class Dado {
    //El dado solo tiene un atributo en nuestro caso: su valor.
    private int valor;
    private final Random numRandom;
    
    public Dado(){
        this.valor = 0;
        this.numRandom = new Random(); 
    }

    //Metodo para simular lanzamiento de un dado: devolverá un valor aleatorio entre 1 y 6.
    public int hacerTirada() {
        this.valor = numRandom.nextInt(6) + 1;
        return this.valor;
    }


    //Getters

    public int getValor(){
        return valor;
    }
}