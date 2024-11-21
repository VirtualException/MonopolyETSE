package monopoly_consola;

import java.util.Scanner;

public interface Consola {
    public void imprimir(String mensaje);

    public void imprimir_sin_salto(String mensaje);
    public String leer (String peticion);
}
