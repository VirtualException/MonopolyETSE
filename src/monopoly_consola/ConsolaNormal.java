package monopoly_consola;

public class ConsolaNormal implements Consola{
    @Override
    public void imprimir (String mensaje){
        System.out.println(mensaje);
    }

    @Override
    public String leer(String peticion) {
        return "nada";
    }

}
