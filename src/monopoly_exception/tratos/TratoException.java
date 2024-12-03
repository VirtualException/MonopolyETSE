package monopoly_exception.tratos;

import monopoly_exception.AccionNoPermitidaException;

public class TratoException extends AccionNoPermitidaException {
    public TratoException(String mensaje){
        super(mensaje);
    }
}
