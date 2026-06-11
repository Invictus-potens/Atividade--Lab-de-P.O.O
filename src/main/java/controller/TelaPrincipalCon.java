package controller;

import service.SistemaGerenciador;
import view.PainelCampeonatos;
import view.PainelClubes;
import view.TelaPrincipal;

public class TelaPrincipalCon {
    private TelaPrincipal telaPrincipal;
    private SistemaGerenciador sistemaGerenciador;

    public TelaPrincipalCon(TelaPrincipal telaPrincipal, SistemaGerenciador sistemaGerenciador) {
        this.telaPrincipal = telaPrincipal;
        this.sistemaGerenciador = sistemaGerenciador;
        
        iniciarPaineis();
    }

    private void iniciarPaineis() {
        PainelClubes painelClubes = new PainelClubes();
        ClubeCon clubesCon = new ClubeCon(painelClubes);
        PainelCampeonatos painelCampeonatos = new PainelCampeonatos();
        CampeonatoCon campeonatoCon = new CampeonatoCon(painelCampeonatos);
        this.telaPrincipal.addPainel("Clubes", painelClubes);
        this.telaPrincipal.addPainel("Campeonatos", painelCampeonatos);
    }
}