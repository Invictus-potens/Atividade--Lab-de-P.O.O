package controller;

import dao.CampeonatoDAO;
import dao.ClubeDAO;
import dao.PartidaDAO;
import view.PainelPartidas;

public class PartidaCon {
    
    private PainelPartidas view;
    private PartidaDAO partidaDao;
    private CampeonatoDAO campeonatoDao;
    private ClubeDAO clubeDao;

    public PartidaCon(PainelPartidas view) {
        this.view = view;
        this.partidaDao = new PartidaDAO();
        this.campeonatoDao = new CampeonatoDAO();
        this.clubeDao = new ClubeDAO();

        this.initController();
    }

    private void initController() {
        this.view.addListenerBtnCadastrar(e -> processarCadastro());

        this.view.addListenerCbCampeonato(e -> atualizarClubesC());

        this.view.addComponentsListener(new java.awt.event.ComponentEvent () {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                atualizarCampeonatos();
        }
    });

    
}
