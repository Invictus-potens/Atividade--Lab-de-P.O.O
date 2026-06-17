package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import model.Partida;

public class PainelResultados extends JPanel {

    private JComboBox<Partida> cbPartida;
    private JSpinner spGolsCasa;
    private JSpinner spGolsVisitante;
    private JLabel lblCasa;
    private JLabel lblVisitante;
    private JLabel lblStatusPartidas;
    private JButton btnRegistrar;

    public PainelResultados() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        initComponents();
    }

    private void initComponents() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Registrar Resultado Real da Partida"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        JLabel lblPartidaLabel = new JLabel("Partida:");
        lblPartidaLabel.setFont(new Font("Arial", Font.BOLD, 13));
        form.add(lblPartidaLabel, gbc);

        cbPartida = new JComboBox<>();
        cbPartida.setFont(new Font("Arial", Font.PLAIN, 13));
        cbPartida.setPreferredSize(new Dimension(500, 28));
        cbPartida.addActionListener(e -> atualizarLabelsIn());
        gbc.gridx = 1; gbc.weightx = 1;
        form.add(cbPartida, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        JLabel lblPlacarLabel = new JLabel("Resultado Final:");
        lblPlacarLabel.setFont(new Font("Arial", Font.BOLD, 13));
        form.add(lblPlacarLabel, gbc);

        JPanel placarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));

        lblCasa = new JLabel("Casa");
        lblCasa.setFont(new Font("Arial", Font.BOLD, 14));
        spGolsCasa = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
        spGolsCasa.setPreferredSize(new Dimension(65, 32));
        spGolsCasa.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel lblX = new JLabel(" x");
        lblX.setFont(new Font("Arial", Font.BOLD, 18));
        lblX.setForeground(new Color(100, 100, 100));

        spGolsVisitante = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
        spGolsVisitante.setPreferredSize(new Dimension(65, 32));
        spGolsVisitante.setFont(new Font("Arial", Font.BOLD, 16));

        lblVisitante = new JLabel("Visitante");
        lblVisitante.setFont(new Font("Arial", Font.BOLD, 14));

        placarPanel.add(lblCasa);
        placarPanel.add(spGolsCasa);
        placarPanel.add(lblX);
        placarPanel.add(spGolsVisitante);
        placarPanel.add(lblVisitante);

        gbc.gridx = 1; gbc.weightx = 1;
        form.add(placarPanel, gbc);

        btnRegistrar = new JButton("Registrar Resultado e Calcular Pontos");
        btnRegistrar.setPreferredSize(new Dimension(320, 40));
        EstiloBotao.aplicarPreenchido(btnRegistrar, new Color(180, 50, 0), Color.WHITE);
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        form.add(btnRegistrar, gbc);

        lblStatusPartidas = new JLabel("", SwingConstants.CENTER);
        lblStatusPartidas.setFont(new Font("Arial", Font.ITALIC, 11));
        lblStatusPartidas.setForeground(Color.GRAY);
        gbc.gridy = 3;
        form.add(lblStatusPartidas, gbc);

        add(form, BorderLayout.NORTH);

        JTextArea taRegras = new JTextArea();
        taRegras.setEditable(false);
        taRegras.setBackground(new Color(250, 250, 230));
        taRegras.setFont(new Font("Arial", Font.PLAIN, 13));
        taRegras.setMargin(new Insets(15, 20, 15, 20));
        taRegras.setText(
                "REGRAS DE PONTUAÇÃO\n" +
                "-------------------------------------------------------------\n\n" +
                "  [10 pts] Acertou o resultado E o placar exato\n" +
                "         Exemplo: apostou 2 x 1 e o jogo terminou 2 x 1\n\n" +
                "  [5 pts]  Acertou apenas o resultado (vencedor/empate)\n" +
                "         Exemplo: apostou 2 x 1 e o jogo terminou 3 x 0 (mesma equipe venceu)\n\n" +
                "  [0 pts]  Errou o resultado\n" +
                "         Exemplo: apostou vitória da casa mas ocorreu empate\n\n" +
                "-------------------------------------------------------------\n" +
                "  - Ao registrar o resultado, os pontos são calculados automaticamente\n" +
                "  - para todos os participantes que apostaram nesta partida.\n" +
                "  - Acesse a aba 'Classificação' para ver o ranking atualizado."
        );
        taRegras.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Regras de Pontuação"));
        add(taRegras, BorderLayout.CENTER);
    }

    public Partida getPartidaSelecionada() {
        return (Partida) cbPartida.getSelectedItem();
    }

    private void atualizarLabelsIn() {
        Partida partida = (Partida) cbPartida.getSelectedItem();
        if (partida != null) {
            lblCasa.setText(partida.getCasa().getNome());
            lblVisitante.setText(partida.getVisitante().getNome());
        } else {
            lblCasa.setText("Casa");
            lblVisitante.setText("Visitante");
        }
    }

    public int getGolsCasa() {
        return (Integer) spGolsCasa.getValue();
    }

    public int getGolsVisitante() {
        return (Integer) spGolsVisitante.getValue();
    }

    public void atualizarLabels(String casa, String visitante) {
        lblCasa.setText(casa);
        lblVisitante.setText(visitante);
    }

    public void atualizarListaPartidas(List<Partida> partidas) {
        cbPartida.removeAllItems();
        for (Partida p : partidas) {
            cbPartida.addItem(p);
        }
        lblStatusPartidas.setText("Total de partidas: " + partidas.size());
    }

    public void exibirMensagem(String msg, String titulo, int tipo) {
        JOptionPane.showMessageDialog(this, msg, titulo, tipo);
    }

    public int confirmarOperacao(String msg, String titulo) {
        return JOptionPane.showConfirmDialog(this, msg, titulo, JOptionPane.YES_NO_OPTION);
    }

    public void addListenerBtnRegistrar(java.awt.event.ActionListener l) {
        btnRegistrar.addActionListener(l);
    }

    public void addListenerCbPartida(java.awt.event.ActionListener l) {
        cbPartida.addActionListener(l);
    }

}
