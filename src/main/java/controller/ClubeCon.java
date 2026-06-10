package controller;

import java.util.List;

import javax.swing.JOptionPane;

import dao.ClubeDAO;
import model.Clube;
import view.PainelClubes;

public class ClubeCon {
    
    private PainelClubes view;
    private ClubeDAO dao;

    public ClubeCon(PainelClubes view) {
        this.view = view;
        this.dao = new ClubeDAO();
        this.initController();
    }

    private void initController() {
        this.view.addListenerBotao(e -> processCadastro());
        this.atualizarListaViewClubes();
    }

    private void processCadastro() {
        String nome = view.getNome();
        String cidade = view.getCidade();

        if (nome.trim().isEmpty() || cidade.trim().isEmpty()) {
            view.exibirMensagem("Preencha o nome e a cidade do clube.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Clube novoClube = new Clube(nome, cidade);

        try {
            dao.cadastrar(novoClube);
            view.limparCampos();
            atualizarListaViewClubes();
            view.exibirMensagem("Clube cadastrado no SGBD", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception ex) {
            view.exibirMensagem("Erro no Banco: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarListaViewClubes() {
        try {
            List<Clube> clubesSalvos = dao.listarAll();
            view.atualizarListaClubes(clubesSalvos);
        } catch (Exception ex) {
            System.err.println("Erro ao mostrar a lista: " + ex.getMessage());
        }
    }
}
