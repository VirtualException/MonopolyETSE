package monopoly_exception.edificios;

import monopoly_exception.AccionNoPermitidaException;

public class EdificioNoValidoException extends AccionNoPermitidaException {
    public EdificioNoValidoException(String mensaje){
        super(mensaje);
    }
}
