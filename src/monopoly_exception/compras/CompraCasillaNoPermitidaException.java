package monopoly_exception.compras;
public class CompraCasillaNoPermitidaException extends CompraInvalidaException{
    public CompraCasillaNoPermitidaException(String mensaje) {
        super(mensaje);
    }
}