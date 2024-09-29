package partida;


import java.util.Random;

public class Dado {
    //El dado solo tiene un atributo en nuestro caso: su valor.
    private int valor;

    //Metodo para simular lanzamiento de un dado: devolverá un valor aleatorio entre 1 y 6.
    public int hacerTirada() {
        Random numRandom = new Random();
        this.valor = numRandom.nextInt(6) + 1;
        return this.valor;
    }


    //Getters y Setters

    public int getValor(){
        return valor;
    }

    public void setValor(int valor){
        this.valor = valor;
    }
}