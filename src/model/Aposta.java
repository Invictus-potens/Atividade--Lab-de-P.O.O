package model;

import interfaces.Pontuavel;

public class Aposta implements Pontuavel {

    private Usuario usuario;
    private Partida partida;
    private GrupoAposta grupo;
    private int golsCasaApostado;
    private int golsVisitanteApostado;
    private int pontos;
    private boolean calculada;

    public Aposta() {
        this.pontos = 0;
        this.calculada = false;
    }

    public Aposta(Usuario usuario, Partida partida, GrupoAposta grupo,
                  int golsCasa, int golsVisitante) {
        this();
        this.usuario = usuario;
        this.partida = partida;
        this.grupo = grupo;
        this.golsCasaApostado = golsCasa;
        this.golsVisitanteApostado = golsVisitante;
    }

    @Override
    public int calcularPontos(ResultadoPartida resultadoReal) {
        ResultadoPartida apostado = new ResultadoPartida(golsCasaApostado, golsVisitanteApostado);

        if (apostado.getVencedor().equals(resultadoReal.getVencedor())) {
            boolean placarExato = golsCasaApostado == resultadoReal.getGolsCasa()
                    && golsVisitanteApostado == resultadoReal.getGolsVisitante();
            pontos = placarExato ? 10 : 5;
        } else {
            pontos = 0;
        }

        calculada = true;
        return pontos;
    }

    @Override
    public int getPontos() { return pontos; }

    public boolean isCalculada() { return calculada; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Partida getPartida() { return partida; }
    public void setPartida(Partida partida) { this.partida = partida; }

    public GrupoAposta getGrupo() { return grupo; }
    public void setGrupo(GrupoAposta grupo) { this.grupo = grupo; }

    public int getGolsCasaApostado() { return golsCasaApostado; }
    public void setGolsCasaApostado(int golsCasaApostado) {
        this.golsCasaApostado = golsCasaApostado;
    }

    public int getGolsVisitanteApostado() { return golsVisitanteApostado; }
    public void setGolsVisitanteApostado(int golsVisitanteApostado) {
        this.golsVisitanteApostado = golsVisitanteApostado;
    }

    @Override
    public String toString() {
        String nomeCasa = partida.getCasa().getNome();
        String nomeVisitante = partida.getVisitante().getNome();
        String status = calculada ? " → " + pontos + " pts" : " [Aguardando resultado]";
        return nomeCasa + " " + golsCasaApostado + " x " + golsVisitanteApostado + " " + nomeVisitante
                + " | Grupo: " + grupo.getNome() + status;
    }
}