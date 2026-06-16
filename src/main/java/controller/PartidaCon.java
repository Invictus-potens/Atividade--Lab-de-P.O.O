package controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import dao.CampeonatoDAO;
import dao.ClubeDAO;
import dao.PartidaDAO;
import model.Campeonato;
import model.Clube;
import model.Partida;
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

        this.view.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                atualizarCampeonatos();
        }
    });

    this.atualizarCampeonatos();
    }

    private void atualizarCampeonatos() {
        try {
            List<Campeonato> campeonatos = campeonatoDao.listarAll();
            view.setCampeonato(campeonatos);
        } catch (Exception e) {
            view.exibirMensagem("Erro ao listar campeonatos" + e.getMessage(), "Erro", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void atualizarClubesC() {
        Campeonato camp = view.getCampeonatoSelecionado();
        if (camp != null) {
            try {
            List<Clube> clubesVinculados = campeonatoDao.listarClubesCampeonatos(camp.getId());
            view.setClube(clubesVinculados);
            } catch (Exception e) {
                view.exibirMensagem("Erro ao listar clubes" + e.getMessage(), "Erro", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void processarCadastro() {
        Campeonato camp = view.getCampeonatoSelecionado();
        Clube casa = view.getCasaSelecionado();
        Clube visitante = view.getVisitanteSelecionado();
        Date dataEscolhida = view.getDataHora();

        if (camp == null || casa == null || visitante == null) {
            view.exibirMensagem("Preencha todos os campos", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if(casa.getId() == visitante.getId()) {
            view.exibirMensagem("Os times não podem ser os mesmos", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDateTime dataHora = dataEscolhida.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        Partida novaPartida = new Partida(casa, visitante, dataHora, camp);

        try {
            partidaDao.cadastrar(novaPartida);
            view.exibirMensagem("Partida criada no banco", "Sucesso", JOptionPane.INFORMATION_MESSAGE); 
            atualizarCampeonatos();
        } catch (Exception ex) {
            view.exibirMensagem("Erro na partida", "Erro", JOptionPane.WARNING_MESSAGE);
        }
    }
}