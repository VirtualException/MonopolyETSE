package monopoly_exception.compras;

import monopoly_exception.AccionNoPermitidaException;

public class CompraInvalidaException extends AccionNoPermitidaException {
    public CompraInvalidaException(String mensaje) {
        super(mensaje);
    }
}

