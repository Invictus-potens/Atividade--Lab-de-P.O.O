package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import model.Pessoa;

public class TelaPrincipal extends JFrame {

    public interface Atualizavel {
        void atualizar();
    }

    private final JFrame telaAnterior;
    private final Pessoa pessoaLogada;
    private JTabbedPane tabbedPane;

    private PainelPartidas painelPartidas;
    private PainelCampeonatos painelCampeonatos;
    private PainelClubes painelClubes;

    public TelaPrincipal(JFrame telaAnterior, Pessoa pessoaLogada) {
        this.telaAnterior = telaAnterior;
        this.pessoaLogada = pessoaLogada;

        setTitle("Tigrinho UNA — " + pessoaLogada.getNome() + " [" + pessoaLogada.getRole() + "]");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 680);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0, 100, 0));
        header.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        JLabel lblTitulo = new JLabel("Tigrinho UNA");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        header.add(lblTitulo, BorderLayout.WEST);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        userPanel.setOpaque(false);

        JLabel lblUsuario = new JLabel("Usuário: " + pessoaLogada.getNome()
                + "  |  Perfil: " + pessoaLogada.getRole());
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

        
        boolean isAdmin = pessoaLogada.getRole().equals("Administrador") || pessoaLogada.getRole().equals("Administrator");

        if (isAdmin) {
            this.painelPartidas = new PainelPartidas();
            tabbedPane.addTab("Partidas", painelPartidas);
            this.painelCampeonatos = new PainelCampeonatos();
            tabbedPane.addTab("Campeonatos", painelCampeonatos);
            this.painelClubes = new PainelClubes();
            tabbedPane.addTab("Clubes", painelClubes);

            //tabbedPane.addTab("Resultados", new PainelResultados());
        }

        tabbedPane.addChangeListener(e -> {
            Component painel = tabbedPane.getSelectedComponent();
            if (painel instanceof Atualizavel) {
                ((Atualizavel) painel).atualizar();
            }
        });

        add(tabbedPane, BorderLayout.CENTER);

        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBar.setBackground(new Color(254, 254, 254));
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        JLabel lblStatus = new JLabel(" SistemaTigrinho UNA - Lab P.O.O");
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
            this.dispose();
            telaAnterior.setVisible(true);
        }
    }

    public PainelPartidas getPainelPartidas() {
        return painelPartidas;
    }

    public PainelCampeonatos getPainelCampeonatos() {
        return painelCampeonatos;
    }

    public PainelClubes getPainelClubes() {
        return painelClubes;
    }
}
