package monopoly_dados;


import monopoly_juego.Juego;

import java.util.Random;
import java.util.Scanner;

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

    /* Tirada manual */
    public int hacerTirada(int a) {
        this.valor = a;
        return this.valor;
    }

    public int hacerTiradaManual() {
        Scanner c = new Scanner(System.in);
        Juego.consola.imprimir_sin_salto("Valor dado: ");
        this.valor = Integer.parseInt(c.nextLine());
        c.close();
        return this.valor;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        Dado d = (Dado) obj;
        return d.getValor() == this.getValor();
    }


    //Getters

    public int getValor(){
        return valor;
    }
}