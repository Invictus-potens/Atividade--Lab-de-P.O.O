package controller;

import java.util.List;

import javax.swing.JOptionPane;

import dao.CampeonatoDAO;
import dao.ClubeDAO;
import model.Campeonato;
import model.Clube;
import view.PainelCampeonatos;

public class CampeonatoCon {
    
    private PainelCampeonatos view;
    private CampeonatoDAO campeonatoDao;
    private ClubeDAO clubeDao;

    public CampeonatoCon(PainelCampeonatos view) {
        this.view = view;
        this.campeonatoDao = new CampeonatoDAO();
        this.clubeDao = new ClubeDAO();
        this.initController();
    }

    private void initController() {
        this.view.addListenerbotao(e -> processCadastro());
        this.view.addListenerbotaoAdd(e -> processarJuncao());

        this.view.addListenerLista(e -> {
            if (!e.getValueIsAdjusting()) {
                atualizarDetalhes();
            }
        });
        this.atualizarListaViewCampeonatos();
    }

    private void processCadastro() {
        String nome = view.getNome();

        if (nome.trim().isEmpty()) {
            view.exibirMensagem("Preencha o nome campeonato.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Campeonato novoCampeonato = new Campeonato(nome);

        try {
            campeonatoDao.cadastrar(novoCampeonato);
            view.LimparCampos();
            atualizarListaViewCampeonatos();
            view.exibirMensagem("Campeonato cadastrado no SGBD", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception ex) {
            view.exibirMensagem("Erro no Banco: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void processarJuncao() {
        Campeonato camp = view.getCampeonato();
        Clube clube = view.getClube();

        if (camp == null || clube == null) {
            view.exibirMensagem("Selecione um campeonato e clube", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            campeonatoDao.clubeCamp(camp, clube);
            view.exibirMensagem("Clube veinculado", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            atualizarDetalhes();
        } catch (Exception ex) {
            view.exibirMensagem(ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarListaViewCampeonatos() {
        try {
            List<Campeonato> camps = campeonatoDao.listarAll();
            view.atualizarListaCampeonatos(camps);

            List<Clube> clubes = clubeDao.listarAll();
            view.atualizarClubes(clubes);
        } catch (Exception ex) {
            System.err.println("Erro ao mostrar a lista: " + ex.getMessage());
        }
    }

    private void atualizarDetalhes() {
        Campeonato campSelecionado = view.getCampeonatoLista();
        
        // Se o usuário não clicou em nada, limpa a caixa de texto
        if (campSelecionado == null) {
            view.attDetalhes("");
            return;
        }

        try {
            List<Clube> clubes = campeonatoDao.listarClubesCampeonatos(campSelecionado.getId());

            StringBuilder sb = new StringBuilder();
            sb.append("Campeonato: ").append(campSelecionado.getNome()).append("\n");
            sb.append("-".repeat(40)).append("\n");
            sb.append("Clubes participantes: ").append(clubes.size()).append(" / 8\n\n");

            if (clubes.isEmpty()) {
                sb.append("  (nenhum clube adicionado ainda)");
            } else {
                for (int i = 0; i < clubes.size(); i++) {
                    sb.append("  ").append(i + 1).append(". ")
                      .append(clubes.get(i).getNome())
                      .append(" (").append(clubes.get(i).getCidade()).append(")\n");
                }
            }
            
            view.attDetalhes(sb.toString());

        } catch (Exception ex) {
            view.exibirMensagem("Erro ao carregar os detalhes: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
