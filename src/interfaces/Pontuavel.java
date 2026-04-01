package interfaces;

import model.ResultadoPartida;

/**
 * Interface que define o contrato para objetos que podem receber pontuação.
 * Implementa o conceito de polimorfismo via interface.
 */
public interface Pontuavel {

    /**
     * Calcula e retorna a pontuação obtida com base no resultado real da partida.
     * Regras: 10 pts = resultado + placar exato | 5 pts = apenas resultado | 0 pts = erro
     */
    int calcularPontos(ResultadoPartida resultadoReal);

    /**
     * Retorna a pontuação atual (após cálculo).
     */
    int getPontos();
}
