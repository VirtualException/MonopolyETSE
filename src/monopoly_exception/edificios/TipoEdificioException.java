package monopoly_exception.edificios;



public class TipoEdificioException extends EdificioNoValidoException {
    public TipoEdificioException(String mensaje){
        super(mensaje);
    }
}