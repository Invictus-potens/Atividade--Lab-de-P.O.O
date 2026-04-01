package model;

public class ResultadoPartida {

    private int golsCasa;
    private int golsVisitante;

    public ResultadoPartida() {}

    public ResultadoPartida(int golsCasa, int golsVisitante) {
        this.golsCasa = golsCasa;
        this.golsVisitante = golsVisitante;
    }

    public int getGolsCasa() { return golsCasa; }
    public void setGolsCasa(int golsCasa) { this.golsCasa = golsCasa; }

    public int getGolsVisitante() { return golsVisitante; }
    public void setGolsVisitante(int golsVisitante) { this.golsVisitante = golsVisitante; }

    public String getVencedor() {
        if (golsCasa > golsVisitante) return "CASA";
        if (golsVisitante > golsCasa) return "VISITANTE";
        return "EMPATE";
    }

    @Override
    public String toString() {
        return golsCasa + " x " + golsVisitante;
    }
}