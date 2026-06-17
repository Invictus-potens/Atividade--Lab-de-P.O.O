package controller;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import dao.ApostaDAO;
import dao.GrupoApostaDAO;
import model.GrupoAposta;
import model.Partida;
import model.Pessoa;
import view.PainelApostas;

public class ApostaCon {
    
    private PainelApostas view;
    private Pessoa pessoaLogada;
    private ApostaDAO apostaDao;
    private GrupoApostaDAO grupoDao;

    public ApostaCon(PainelApostas view, Pessoa pessoaLogada) {
        this.view = view;
        this.pessoaLogada = pessoaLogada;
        this.apostaDao = new ApostaDAO();
        this.grupoDao = new GrupoApostaDAO();

        this.initController();
    }

    private void initController() {
        this.view.addListenerBtnApostar(e -> processarAposta());

        this.view.addListenerCbPartida(e -> {
            Partida p = view.getPartidaSelecionada();
            if (p != null) {
                view.atualizarLabelsPlacar(p.getCasa().getNome(), p.getVisitante().getNome());
            }
        });

        this.view.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                carregarDadosTela();
            }
        });

        carregarDadosTela();
    }

    public void carregarDadosTela() {
        try {
            List<GrupoAposta> todosGrupos = grupoDao.listarTodos();
            List<GrupoAposta> gruposUser = new ArrayList<>();
            for (GrupoAposta g : todosGrupos) {
                if (g.listar().contains(pessoaLogada)) {
                    gruposUser.add(g);
                }
            }
            view.setGrupos(gruposUser);
        } catch (Exception e) {
            view.exibirMensagem("Erro dados " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void processarAposta() {
        if (pessoaLogada.getRole().equalsIgnoreCase("Administrador") || pessoaLogada.getRole().equalsIgnoreCase("Administrator")) {
            view.exibirMensagem("Somente users podem palpitar", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        GrupoAposta grupo = view.getGrupoSelecionado();
        Partida partida = view.getPartidaSelecionada();

        if (grupo == null || partida == null) {
            view.exibirMensagem("Selecione partidas válidas", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!partida.podeApostar()) {
            view.exibirMensagem("Aposta encerrada faz o L", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int golsCasa = view.getGolsCasa();
        int golsVisitante = view.getGolsVisitante();

        try {
            apostaDao.registrar(pessoaLogada.getId(), grupo.getId(), partida.getId(), golsCasa, golsVisitante);
            view.exibirMensagem("Aposta registrada no banco", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            view.exibirMensagem(ex.getMessage(), "Erro aposta", JOptionPane.ERROR_MESSAGE);
        }
    }

}
