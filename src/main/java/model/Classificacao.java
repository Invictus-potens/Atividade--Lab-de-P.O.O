package model;

public class Classificacao {
    private String nomeParticipante;
    private int totalPontos;

    public Classificacao(String nomeParticipante, int totalPontos) {
        this.nomeParticipante = nomeParticipante;
        this.totalPontos = totalPontos;
    }

    public String getNomeParticipante() { 
        return nomeParticipante; }

    public int getTotalPontos() { 
        return totalPontos; }
}