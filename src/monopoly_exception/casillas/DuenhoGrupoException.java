package monopoly_exception.casillas;

import monopoly_exception.AccionNoPermitidaException;

public class DuenhoGrupoException extends AccionNoPermitidaException {
    public DuenhoGrupoException(String mensaje){
        super(mensaje);
    }
}
