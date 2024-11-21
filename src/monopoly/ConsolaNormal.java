package monopoly;

import java.util.Scanner;

public class ConsolaNormal implements Consola{
    @Override
    public void imprimir (String mensaje){
        System.out.println(mensaje);
    }

    @Override
    public String leer(String peticion) {
        Scanner scan = new Scanner(System.in);
        return scan.nextLine();
    }


}
