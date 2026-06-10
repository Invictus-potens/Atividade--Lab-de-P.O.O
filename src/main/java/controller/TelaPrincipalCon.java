package controller;

import service.SistemaGerenciador;
import view.TelaPrincipal;
import view.PainelClubes;
import controller.ClubeCon;

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
        this.telaPrincipal.addPainel("Clubes", painelClubes);
    }
}