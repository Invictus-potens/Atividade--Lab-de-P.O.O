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
    }
}