package interfaces;

import model.ResultadoPartida;

//Class genérica para calcular os pontos
public interface Pontuavel {

    int calcularPontos(ResultadoPartida resultadoReal);

    int getPontos();
}