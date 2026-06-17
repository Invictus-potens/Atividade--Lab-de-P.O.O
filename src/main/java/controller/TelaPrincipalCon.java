package controller;

import view.TelaPrincipal;

public class TelaPrincipalCon {
    private TelaPrincipal telaPrincipal;

    public TelaPrincipalCon(TelaPrincipal telaPrincipal) {
        this.telaPrincipal = telaPrincipal;
        
        iniciarPaineis();
    }

    private void iniciarPaineis() {
        if (this.telaPrincipal.getPainelPartidas() != null) {
            new PartidaCon(this.telaPrincipal.getPainelPartidas());
        }

        if (this.telaPrincipal.getPainelCampeonatos() != null) {
            new CampeonatoCon(this.telaPrincipal.getPainelCampeonatos());
        }

        if (this.telaPrincipal.getPainelClubes() != null) {
            new ClubeCon(this.telaPrincipal.getPainelClubes());
        }

        if (this.telaPrincipal.getPainelGrupos() != null) {
            new GrupoCon(this.telaPrincipal.getPainelGrupos(), this.telaPrincipal.getPessoaLogada());
        }

        if (this.telaPrincipal.getPainelApostas() != null) {
            new ApostaCon(this.telaPrincipal.getPainelApostas(), this.telaPrincipal.getPessoaLogada());
        }

        if (this.telaPrincipal.getPainelResultados() != null) {
            new ResultadoCon(this.telaPrincipal.getPainelResultados());
        }

        if (this.telaPrincipal.getPainelGrupos() != null) {
            GrupoCon grupoC = new GrupoCon(this.telaPrincipal.getPainelGrupos(), this.telaPrincipal.getPessoaLogada());
        }
    }
}