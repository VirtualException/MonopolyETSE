package monopoly;

public class Edificio {

    private String tipo;

    //Constructor vacío
    public Edificio() {
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        if (tipo.equals("casa") || tipo.equals("hotel") || tipo.equals("piscina") || tipo.equals("pista")) {
            this.tipo = tipo;
        }
    }
}
