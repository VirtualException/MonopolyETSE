package monopoly_exception.avatares;

import monopoly_exception.AccionNoPermitidaException;

public class AvatarNoValidoException extends AccionNoPermitidaException {
    public AvatarNoValidoException(String mensaje){
        super(mensaje);
    }
}
