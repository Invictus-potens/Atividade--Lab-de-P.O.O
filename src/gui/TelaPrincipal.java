package gui;

import model.Pessoa;
import service.SistemaGerenciador;

import javax.swing.*;
import java.awt.*;

public class TelaPrincipal extends JFrame {

    public interface Atualizavel {
        void atualizar();
    }

    private final JFrame telaAnterior;
    private final SistemaGerenciador sistema;
    private final Pessoa pessoaLogada;
    private JTabbedPane tabbedPane;

    public TelaPrincipal(JFrame telaAnterior) {
        this.telaAnterior = telaAnterior;
        this.sistema = SistemaGerenciador.getInstance();
        this.pessoaLogada = sistema.getPessoaLogada();

        setTitle("ChampionBet — " + pessoaLogada.getNome() + " [" + pessoaLogada.getTipo() + "]");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 680);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0, 100, 0));
        header.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        JLabel lblTitulo = new JLabel("ChampionBet");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        header.add(lblTitulo, BorderLayout.WEST);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        userPanel.setOpaque(false);

        JLabel lblUsuario = new JLabel("Usuário: " + pessoaLogada.getNome()
                + "  |  Perfil: " + pessoaLogada.getTipo());
        lblUsuario.setForeground(Color.WHITE);
        lblUsuario.setFont(new Font("Arial", Font.PLAIN, 12));

        JButton btnSair = new JButton("Sair");
        btnSair.setFont(new Font("Arial", Font.BOLD, 11));
        btnSair.addActionListener(e -> sair());

        userPanel.add(lblUsuario);
        userPanel.add(btnSair);
        header.add(userPanel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 13));

        if (sistema.isAdmin()) {
            tabbedPane.addTab("Clubes", new PainelClubes());
            tabbedPane.addTab("Campeonatos", new PainelCampeonatos());
            tabbedPane.addTab("Partidas", new PainelPartidas());
        }

        tabbedPane.addTab("Grupos", new PainelGrupos());
        tabbedPane.addTab("Apostas", new PainelApostas());

        if (sistema.isAdmin()) {
            tabbedPane.addTab("Resultados", new PainelResultados());
        }

        tabbedPane.addTab("Classificação", new PainelClassificacao());

        tabbedPane.addChangeListener(e -> {
            Component painel = tabbedPane.getSelectedComponent();
            if (painel instanceof Atualizavel) {
                ((Atualizavel) painel).atualizar();
            }
        });

        add(tabbedPane, BorderLayout.CENTER);

        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBar.setBackground(new Color(240, 240, 240));
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        JLabel lblStatus = new JLabel("  Sistema ChampionBet — Laboratório de P.O.O");
        lblStatus.setFont(new Font("Arial", Font.ITALIC, 11));
        lblStatus.setForeground(Color.GRAY);
        statusBar.add(lblStatus);
        add(statusBar, BorderLayout.SOUTH);
    }

    private void sair() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Deseja realmente sair do sistema?",
                "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            sistema.logout();
            this.dispose();
            telaAnterior.setVisible(true);
        }
    }
}
