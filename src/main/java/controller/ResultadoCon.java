package controller;

import java.util.List;

import javax.swing.JOptionPane;

import dao.ApostaDAO;
import dao.PartidaDAO;
import model.Aposta;
import model.Partida;
import view.PainelResultados;

public class ResultadoCon {
    
    private PainelResultados view;
    private PartidaDAO partidaDao;
    private ApostaDAO apostaDao;

    public ResultadoCon(PainelResultados view) {
        this.view = view;
        this.partidaDao = new PartidaDAO();
        this.apostaDao = new ApostaDAO();
        this.initController();
    }

    private void initController() {
        this.view.addListenerBtnRegistrar(e -> registrarPPlacar());

        this.view.addListenerCbPartida(e -> {
            Partida p = view.getPartidaSelecionada();
            if(p!= null) {
                view.atualizarLabels(p.getCasa().getNome(), p.getVisitante().getNome());
            } else {
                view.atualizarLabels("Casa", "Visitante");
            }
        });

        this.view.addAncestorListener(new javax.swing.event.AncestorListener() {
            @Override
            public void ancestorAdded(javax.swing.event.AncestorEvent event) {
                carregarPartidas();
            }
            @Override
            public void ancestorRemoved(javax.swing.event.AncestorEvent event) {}
            
            @Override
            public void ancestorMoved(javax.swing.event.AncestorEvent event) {}
        });

        this.carregarPartidas();
    }

    private void carregarPartidas() {
        try {
            List<Partida> pendentes = partidaDao.listarTodas();
            view.atualizarListaPartidas(pendentes);
        } catch (Exception e) {
            view.exibirMensagem("Erro caregar partidas: " +e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarPPlacar() {
        Partida partida = view.getPartidaSelecionada();
        if (partida == null) {
            view.exibirMensagem("Selecione uma partida: ", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int golsCasaReal = view.getGolsCasa();
        int golsVisitanteReal = view.getGolsVisitante();

        String msgConfirm = "Confirmar placar? \n\n" + 
                            partida.getCasa().getNome() + " " + golsCasaReal + " x " + golsVisitanteReal + " " + partida.getVisitante().getNome() + "\n\n OS PONTOS SERÃO DISTRIBUIDOS";
        int escolha = view.confirmarOperacao(msgConfirm, "Confirmar Resultado");
        if (escolha != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            partidaDao.registrarResultadoOfc(partida.getId(), golsCasaReal, golsVisitanteReal);

            List<Aposta> apostasPartida = apostaDao.listarApostaPartida(partida.getId());
            int apostasPro = 0;

            for (Aposta aposta : apostasPartida) {
                int pontosMais = calcularPontosAposta(aposta, golsCasaReal, golsVisitanteReal);

                if (pontosMais > 0) {
                    apostaDao.salvarPontos(aposta.getId(), pontosMais);
                }
                apostasPro++;
            }

            view.exibirMensagem("O placar foi submitado! \nTotal de " + apostasPro + " apostas calculadas.", "sucesso", JOptionPane.INFORMATION_MESSAGE);
            carregarPartidas();
        } catch (Exception ex) {
            view.exibirMensagem("Falha nos resultados: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int calcularPontosAposta(Aposta aposta, int golsCasaReal, int golsVisitanteReal) {
        int apostadoC = aposta.getGolsCasaApostado();
        int apostadoV = aposta.getGolsVisitante();

        if (apostadoC == golsCasaReal && apostadoV == golsVisitanteReal) {
            return 10;
        }

        int saldoReal = golsCasaReal - golsVisitanteReal;
        int saldoApostado = apostadoC - apostadoV;

        if ((saldoReal > 0 && saldoApostado > 0) ||(saldoReal < 0 && saldoApostado < 0) || (saldoReal == 0 && saldoApostado == 0)) {
            return 5;
        }

        return 0;
    }

}
