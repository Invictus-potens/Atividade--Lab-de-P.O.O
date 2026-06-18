package controller;

import java.util.List;

import javax.swing.JOptionPane;

import dao.GrupoApostaDAO;
import model.GrupoAposta;
import model.Pessoa;
import view.PainelGrupos;

public class GrupoCon {
    private PainelGrupos view;
    private GrupoApostaDAO grupoDao;
    private Pessoa pessoaLogada;

    public GrupoCon(PainelGrupos view, Pessoa pessoaLogada) {
        this.view = view;
        this.pessoaLogada = pessoaLogada;
        this.grupoDao = new GrupoApostaDAO();

        this.initController();
    }

    public void initController() {
        this.view.addListenerBtnCriar(e -> processarCria());
        this.view.addListenerBtnEntrar(e -> processarEntra());
        this.view.addListenerListaSelecao(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarDetalhesGrupo();
            }
        });

        //atualiza sempre que abrir
        this.view.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                renderizarDadosTela();
            }
        });

        renderizarDadosTela();
    }

    private void renderizarDadosTela() {
        try {
            List <GrupoAposta> todosGrupos = grupoDao.listarTodos();

            //Filtra grupo e users
            int contadorUser = 0;
            for (GrupoAposta g : todosGrupos) {
                if (g.listar().contains(pessoaLogada)) {
                    contadorUser++;
                }
            }

            view.atualizarR(todosGrupos, contadorUser);
        } catch (Exception e) {
            view.exibirMensagem("Erro grupos: " + e.getMessage(), "erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void processarCria() {
        if (pessoaLogada.getRole().equalsIgnoreCase("Administrador") || pessoaLogada.getRole().equalsIgnoreCase("Administrator")) {
            view.exibirMensagem("Só os usuarios comuns podem betar", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nome = view.getNomeGrupoDigitado();
        if (nome == null || nome.trim().isEmpty()) {
            view.exibirMensagem("Insira um nome", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            grupoDao.salvar(nome, pessoaLogada.getId());
            view.limparNome();
            renderizarDadosTela();
            view.exibirMensagem("Grupo " + nome + " criado e registrado no banco", "Sucess", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            view.exibirMensagem("Erro ao criar grupo: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void processarEntra() {
        if (pessoaLogada.getRole().equalsIgnoreCase("Administrador") || pessoaLogada.getRole().equalsIgnoreCase("Administrator")) {
            view.exibirMensagem("Admins não podem participar de grupos", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        GrupoAposta grupo = view.getGrupoSelecionadoCombo();
        if (grupo == null) {
            view.exibirMensagem("Selecione um grupo", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            grupoDao.entrarGrupo(grupo.getId(), pessoaLogada.getId());
            renderizarDadosTela();
            view.exibirMensagem("Entrou no grupo ", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            view.exibirMensagem(ex.getMessage(), "Erro", JOptionPane.ERROR);
        }
    }

    private void mostrarDetalhesGrupo() {
        GrupoAposta grupo = view.getGrupoSelecionadoLista();
        if (grupo == null) {
            view.setDetalheText("");
            return;
        }

        //Concatena strings
        StringBuilder sb = new StringBuilder();
        sb.append("Grupo: ").append(grupo.getNome()).append("\n");
        sb.append("Criador: ").append(grupo.getCriador().getNome()).append("\n");
        sb.append("Participantes: ").append(grupo.listar().size()).append("\n\n");

        for (int i = 0; i < grupo.listar().size(); i++) {
            Pessoa p = grupo.listar().get(i);
            String marcador = p.getId() == grupo.getCriador().getId() ? "(Criador)" : "";
            sb.append(" ").append(i + 1).append(". ").append(p.getNome()).append(marcador).append("\n");
        }

        view.setDetalheText(sb.toString());
    }

}
