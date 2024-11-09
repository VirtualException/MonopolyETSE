package monopoly; 

import java.util.ArrayList;
import partida.*;


public class Grupo {

    //Atributos
    private ArrayList<Casilla> miembros; //Casillas miembros del grupo.
    private String colorGrupo; //Color del grupo
    private int numCasillas; //Número de casillas del grupo.

    //Constructor vacío.
    public Grupo() {
    }

    /*Constructor para cuando el grupo está formado por DOS CASILLAS:
     * Requiere como parámetros las dos casillas miembro y el color del grupo.
     */
    public Grupo(Casilla cas1, Casilla cas2, String colorGrupo) {
        this.miembros = new ArrayList<>();
        anhadirCasilla(cas1);
        anhadirCasilla(cas2);
        this.colorGrupo = colorGrupo;
    }

    /*Constructor para cuando el grupo está formado por TRES CASILLAS:
     * Requiere como parámetros las tres casillas miembro y el color del grupo.
     */
    public Grupo(Casilla cas1, Casilla cas2, Casilla cas3, String colorGrupo) {
        this.miembros = new ArrayList<>();
        anhadirCasilla(cas1);
        anhadirCasilla(cas2);
        anhadirCasilla(cas3);
        this.colorGrupo = colorGrupo;
    }

    /* Método añade una casilla al array de casillas miembro de un grupo.
     * Parámetro: casilla que se quiere añadir.
     */
    public final void anhadirCasilla(Casilla miembro) {
        this.miembros.add(miembro);
        this.numCasillas++;
    }

    /*Método que comprueba si el jugador pasado tiene en su haber todas las casillas del grupo:
     * Parámetro: jugador que se quiere evaluar.
     * Valor devuelto: true si es dueño de todas las casillas del grupo, false en otro caso.
     */
    public boolean esDuenhoGrupo(Jugador jugador) {
        for(Casilla casilla : miembros){
            if(!jugador.getPropiedades().contains(casilla) || jugador.getHipotecas().contains(casilla)){   //Comprueba que el jugador no tenga en su propiedad alguna de las casillas del grupo
                return false;
            }
        }
        return true;
    }




// Método que devuelve una cadena con información de los edificios de un grupo
    public void stringEdificosGrupo(){
        StringBuilder cadena = new StringBuilder();
        
        for(Casilla c : miembros){
            cadena.append("{\n");
            cadena.append("\tpropiedad: ").append(c.getNombre()).append(",\n");
            cadena.append("\thoteles: ");
            if(c.getEdificios() != null){
                cadena.append("[");
                for(Edificio e : c.getEdificios()){
                    if(e.getTipo().equals("hotel")){
                        cadena.append(e.getId()).append(", ");  
                    } 
                }
                cadena.append("],\n");
            } else {
                cadena.append("-,\n");
            }

            cadena.append("\tcasas: ");
            if(c.getEdificios() != null){
                cadena.append("[");
                for(Edificio e : c.getEdificios()){
                    if(e.getTipo().equals("casa")){
                        cadena.append(e.getId()).append(", ");  
                    } 
                }
                cadena.append("],\n");
            } else {
                cadena.append("-,\n");
            }

            cadena.append("\tpiscinas: ");
            if(c.getEdificios() != null){
                cadena.append("[");
                for(Edificio e : c.getEdificios()){
                    if(e.getTipo().equals("piscina")){
                        cadena.append(e.getId()).append(", ");  
                    } 
                }
                cadena.append("],\n");
            } else {
                cadena.append("-,\n");
            }

            cadena.append("\tpistasDeDeporte: ");
            if(c.getEdificios() != null){
                cadena.append("[");
                for(Edificio e : c.getEdificios()){
                    if(e.getTipo().equals("pista")){
                        cadena.append(e.getId()).append(", ");  
                    } 
                }
                cadena.append("],\n");
            } else {
                cadena.append("-,\n");
            }

            cadena.append("\talquiler: ").append(c.getValor()*0.10f).append("\n");
            cadena.append("},\n");
        }
        System.out.println(cadena.toString());
        mensajeFinal();
    }



    // Método que imprime el mensaje final de StringEdificiosGrupo
    public void mensajeFinal(){

        int numPistasEdificables = numEdificiosEdificablesGrupo("pista");
        int numPiscinasEdificables = numEdificiosEdificablesGrupo("piscina");
        int numHotelesEdificables = numEdificiosEdificablesGrupo("hotel");
        int numCasasEdificables = numEdificiosEdificablesGrupo("casa");

        String edificiosAConstruir = "Aún se pueden edificar ";
        String edificiosNoEdificables = "Ya no se pueden construir ";

        if (numPistasEdificables > 0){
            edificiosAConstruir += numPistasEdificables + " pista(s),";
        }
        else {
            edificiosNoEdificables += " pistas,";
        }
       
        if (numPiscinasEdificables > 0){
            edificiosAConstruir += numPiscinasEdificables + " piscina(s),";
        }
        else {
            edificiosNoEdificables += " piscinas,";
        }

        if (numHotelesEdificables > 0){
            edificiosAConstruir += numHotelesEdificables + " hotel(es),";
        }
        else {
            edificiosNoEdificables += " hoteles,";
        }
                
        if (numCasasEdificables > 0){
            edificiosAConstruir += numCasasEdificables + " casa(s)";
        }
        else {
            edificiosNoEdificables += " casas";
        }

        String mensajeFinal = "";

        if (numPistasEdificables > 0 || numPiscinasEdificables > 0 || numHotelesEdificables > 0 || numCasasEdificables > 0){
            mensajeFinal += edificiosAConstruir + ". ";
        } else {
            mensajeFinal = "No se puede construír ningún edificio.";
        }

        if (numPistasEdificables == 0 || numPiscinasEdificables == 0 || numHotelesEdificables == 0 || numCasasEdificables == 0){
            mensajeFinal += edificiosNoEdificables + ".";
        }

        System.out.println(mensajeFinal);
    }



    // Método para calcular el número de edificios de un tipo que aún se pueden construír
    public int numEdificiosEdificablesGrupo(String tipo){
        int maxEdificios = this.numCasillas;

        int numEdificios = 0;
        int numHoteles = 0;

        for (Casilla casilla : this.miembros){
            switch(tipo){
                case "casa":
                    numEdificios += casilla.getCasasN();
                    numHoteles += casilla.getHotelesN();
                break;
                case "hotel":
                    numEdificios += casilla.getHotelesN();
                break;

                case "piscina":
                    numEdificios += casilla.getPiscinasN();
                break;

                case "pista":
                    numEdificios += casilla.getPistasN();
                break;
                
                default: 
                    break;
            }
        }

        if (tipo.equals("casa") && numHoteles < maxEdificios){
            maxEdificios *= 4;
        }

        return maxEdificios - numEdificios;
    }

    //Getters y Setters


    public float getValorTotal() {
        float sum = 0;
        for (Casilla c : miembros) {
            sum += c.getValor();
        }
        return sum;
    }

    public ArrayList<Casilla> getMiembros() {
        return miembros;
    }

    public void setMiembros(ArrayList<Casilla> miembros) {
        this.miembros = miembros;
    }

    public String getColorGrupo() {
        return colorGrupo;
    }

    public void setColorGrupo(String colorGrupo) {
        this.colorGrupo = colorGrupo;
    }

    public int getNumCasillas() {
        return numCasillas;
    }

    public void setNumCasillas(int numCasillas) {
        this.numCasillas = numCasillas;
    }
}