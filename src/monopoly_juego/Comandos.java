package monopoly_juego;

import monopoly_casillas.Casilla;
import monopoly_jugador.Jugador;

public interface Comandos {
    void crearJugador(String nombre, String tipo);
    void listarJugadores();
    void listarAvatares();
    void listarVenta();
    void listarEdificios();
    void acabarTurno();
    void comprar(String nombre);
    void venderEdificios(Jugador jugador, String tipoEdificio, String nombreCasila, int numEdificios);
    void edificar(String tipo);
    void lanzarDados(int a, int b);
    void incrementarSolares();
    void descAvatar(String id);
    void descJugador(String nombre);
    void descCasilla(String nombre);
    void descTurno();
    void salirCarcel();
    void hipotecarPropiedad(Jugador jugador, Casilla c);
    void deshipotecarPropiedad(Jugador jugador, Casilla c);
    void bancarrota(boolean pagarBanca);
    void estadisticasJugador(String jugador);
    void estadisticas();
    void listarTratos();
    void eliminarTrato(String id);
    void aceptarTrato(String id);
    void proponerTrato(String jugadorDestinatario, Casilla propiedadOfrecida, Casilla propiedadSolcitada, float cantidad1, float cantidad2);
} 