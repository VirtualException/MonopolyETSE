package monopoly_exception.dados;

import monopoly_exception.AccionNoPermitidaException;

public class DadosNoValidosException extends AccionNoPermitidaException {
    public DadosNoValidosException(String mensaje){
        super(mensaje);
    }
}
