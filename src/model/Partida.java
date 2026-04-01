package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Partida {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private Clube casa;
    private Clube visitante;
    private LocalDateTime dataHora;
    private Campeonato campeonato;
    private ResultadoPartida resultado;

    public Partida() {}

    public Partida(Clube casa, Clube visitante, LocalDateTime dataHora, Campeonato campeonato) {
        this.casa = casa;
        this.visitante = visitante;
        this.dataHora = dataHora;
        this.campeonato = campeonato;
        this.resultado = null;
    }

    public boolean podeApostar() {
        return LocalDateTime.now().isBefore(dataHora.minusMinutes(20));
    }

    public boolean temResultado() {
        return resultado != null;
    }

    public Clube getCasa() { return casa; }
    public void setCasa(Clube casa) { this.casa = casa; }

    public Clube getVisitante() { return visitante; }
    public void setVisitante(Clube visitante) { this.visitante = visitante; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    public Campeonato getCampeonato() { return campeonato; }
    public void setCampeonato(Campeonato campeonato) { this.campeonato = campeonato; }

    public ResultadoPartida getResultado() { return resultado; }
    public void setResultado(ResultadoPartida resultado) { this.resultado = resultado; }

    @Override
    public String toString() {
        String status = temResultado()
                ? " [Resultado: " + resultado + "]"
                : " [Aguardando resultado]";
        return casa.getNome() + " vs " + visitante.getNome()
                + " - " + dataHora.format(FORMATTER) + status;
    }
}