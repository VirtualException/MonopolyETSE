package monopoly_exception.valores;

import monopoly_exception.AccionNoPermitidaException;

public class ValorNoPermitidoException extends AccionNoPermitidaException {
    public ValorNoPermitidoException(String mensaje){
        super(mensaje);
    }
}
