package monopoly_exception.casillas;

import monopoly_exception.AccionNoPermitidaException;

public class TipoCasillaException extends AccionNoPermitidaException {
    public TipoCasillaException(String mensaje){
        super(mensaje);
    }
}
