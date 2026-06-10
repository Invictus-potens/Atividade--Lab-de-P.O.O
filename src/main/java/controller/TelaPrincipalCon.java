package controller;

import service.SistemaGerenciador;
import view.TelaPrincipal;

public class TelaPrincipalCon {
    private TelaPrincipal telaPrincipal;
    private SistemaGerenciador sistemaGerenciador;

    public TelaPrincipalCon(TelaPrincipal telaPrincipal, SistemaGerenciador sistemaGerenciador) {
        this.telaPrincipal = telaPrincipal;
        this.sistemaGerenciador = sistemaGerenciador;
        
        inicializarPaineis();
    }

    private void inicializarPaineis() {
        // Passo seguinte: ligar cada painel (Clubes, Apostas) ao seu próprio controlador
    }
}