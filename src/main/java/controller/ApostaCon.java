package controller;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import dao.ApostaDAO;
import dao.GrupoApostaDAO;
import dao.PartidaDAO;
import model.GrupoAposta;
import model.Partida;
import model.Pessoa;
import view.PainelApostas;

public class ApostaCon {
    
    private PainelApostas view;
    private Pessoa pessoaLogada;
    private ApostaDAO apostaDao;
    private GrupoApostaDAO grupoDao;
    private PartidaDAO partidaDao;

    //parâmetros view e pessoa logada precisam ser passados ao construtor, enquanto o dao são instanciados para usar as funções presentes
    public ApostaCon(PainelApostas view, Pessoa pessoaLogada) {
        this.view = view;
        this.pessoaLogada = pessoaLogada;
        this.apostaDao = new ApostaDAO();
        this.grupoDao = new GrupoApostaDAO();
        this.partidaDao = new dao.PartidaDAO();

        this.initController();
    }

    private void initController() {
        //Função lambda ou anônima sem retorno para simplificar o código, recebe e para então usar o método assim que o botão for pressionado
        this.view.addListenerBtnApostar(e -> processarAposta());

        this.view.addListenerCbPartida(e -> {
            Partida p = view.getPartidaSelecionada();
            if (p != null) {
                view.atualizarLabelsPlacar(p.getCasa().getNome(), p.getVisitante().getNome());
            }
        });

        //listener para monitorar alterações na view, sendo o que funcionou em relação ao outros listeners
        this.view.addHierarchyListener(e -> {
            if (view.isShowing()) {
                carregarDadosTela(); 
            }
        });

        carregarDadosTela();
    }

    public void carregarDadosTela() {
        try {
            List<GrupoAposta> todosGrupos = grupoDao.listarTodos();
        
        //Escrevi de duas formas caso mudar a role
        //coleção ordenada e sequencial de elementos com list e não armazer itens duplicados com array
        if (pessoaLogada.getRole().equalsIgnoreCase("Administrador") || 
            pessoaLogada.getRole().equalsIgnoreCase("Administrator")) {
            view.setGrupos(todosGrupos);
        } else {
            List<GrupoAposta> gruposUser = new ArrayList<>();
            for (GrupoAposta g : todosGrupos) {
                for(model.Usuario u : g.listar()) {
                    if(u.getId() == pessoaLogada.getId()) {
                        gruposUser.add(g);
                        break;
                    }
                }
            }
            view.setGrupos(gruposUser);
        }
        //Cria a lista vazia no carregarDadosTela, for-each cada user lista grupos e se o id for igual adiciona 

        view.setPartidas(partidaDao.listarTodas());
        view.setApostas(apostaDao.listarApostasDoUsuario(pessoaLogada.getId()));
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
            view.exibirMensagem("Só pode apostar até 20 minutos antes do início da partida. (Aposta encerrada faz o L)", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int golsCasa = view.getGolsCasa();
        int golsVisitante = view.getGolsVisitante();

        try {
            apostaDao.registrar(pessoaLogada.getId(), grupo.getId(), partida.getId(), golsCasa, golsVisitante);

            carregarDadosTela();
            view.exibirMensagem("Aposta registrada no banco", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            view.exibirMensagem(ex.getMessage(), "Erro aposta", JOptionPane.ERROR_MESSAGE);
        }
    }

}
