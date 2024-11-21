package monopoly_consola;

import java.util.Scanner;

public class ConsolaNormal implements Consola{

    public void imprimir (String mensaje){
        System.out.println(mensaje);
    }

    public void imprimir_sin_salto (String mensaje){
        System.out.print(mensaje);
    }


    public String leer(String peticion) {
        Scanner scan = new Scanner(System.in);
        return scan.nextLine();
    }


}
