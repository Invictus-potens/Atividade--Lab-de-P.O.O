package controller;

import java.util.List;

import javax.swing.JOptionPane;

import dao.ClassificacaoDAO;
import dao.GrupoApostaDAO;
import model.Classificacao;
import model.GrupoAposta;
import view.PainelClassificacao;

public class ClassificacaoCon {

    private final PainelClassificacao view;
    private final ClassificacaoDAO classificacaoDao;
    private final GrupoApostaDAO grupoDao;

    public ClassificacaoCon(PainelClassificacao view) {
        this.view = view;
        this.classificacaoDao = new ClassificacaoDAO();
        this.grupoDao = new GrupoApostaDAO();
        initController();
    }

    private void initController() {
        view.setController(this);
        view.addListenerCbGrupo(e -> carregarClassificacao());
        view.addListenerBtnAtualizar(e -> carregarTudo());

        view.addHierarchyListener(e -> {
            if (view.isShowing()) carregarTudo();
        });

        carregarTudo();
    }

    public void carregarTudo() {
        try {
            List<GrupoAposta> grupos = grupoDao.listarTodos();
            view.setGrupos(grupos);
            carregarClassificacao();
        } catch (Exception e) {
            view.exibirMensagem("Erro ao carregar grupos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarClassificacao() {
        GrupoAposta grupo = view.getGrupoSelecionado();
        if (grupo == null) {
            view.limparTabela();
            return;
        }
        try {
            List<Classificacao> classificacao = classificacaoDao.listarPorGrupo(grupo.getId());
            view.mostrarClassificacao(classificacao, grupo);
        } catch (Exception e) {
            view.exibirMensagem("Erro ao carregar classificação: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}