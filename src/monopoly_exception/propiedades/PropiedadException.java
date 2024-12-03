package monopoly_exception.propiedades;

import monopoly_exception.AccionNoPermitidaException;

public class PropiedadException extends AccionNoPermitidaException {
    public PropiedadException(String mensaje){
        super(mensaje);
    }
}
