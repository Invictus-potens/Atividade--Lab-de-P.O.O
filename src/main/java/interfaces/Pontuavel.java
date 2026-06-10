package interfaces;

import model.ResultadoPartida;

public interface Pontuavel {

    int calcularPontos(ResultadoPartida resultadoReal);

    int getPontos();
}