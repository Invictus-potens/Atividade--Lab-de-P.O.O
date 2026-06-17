package view;

import model.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class PainelApostas extends JPanel {

    private JComboBox<GrupoAposta> cbGrupo;
    private JComboBox<Partida> cbPartida;
    private JSpinner spGolsCasa;
    private JSpinner spGolsVisitante;
    private JLabel lblCasa;
    private JLabel lblVisitante;
    private DefaultListModel<Aposta> listModel;
    private JList<Aposta> listaApostas;
    private JLabel lblInfo;
    private JButton btnApostar;

    public PainelApostas() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        initComponents();
    }

    private void initComponents() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Registrar Nova Aposta"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        form.add(new JLabel("Grupo:"), gbc);
        cbGrupo = new JComboBox<>();
        cbGrupo.setFont(new Font("Arial", Font.PLAIN, 13));
        gbc.gridx = 1; gbc.weightx = 1;
        form.add(cbGrupo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        form.add(new JLabel("Partida:"), gbc);
        cbPartida = new JComboBox<>();
        cbPartida.setFont(new Font("Arial", Font.PLAIN, 13));
        gbc.gridx = 1; gbc.weightx = 1;
        form.add(cbPartida, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        form.add(new JLabel("Placar Previsto:"), gbc);

        JPanel placarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        lblCasa = new JLabel("Casa");
        lblCasa.setFont(new Font("Arial", Font.BOLD, 12));
        spGolsCasa = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
        spGolsCasa.setPreferredSize(new Dimension(60, 28));

        JLabel lblX = new JLabel(" x ");
        lblX.setFont(new Font("Arial", Font.BOLD, 16));

        spGolsVisitante = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
        spGolsVisitante.setPreferredSize(new Dimension(60, 28));
        lblVisitante = new JLabel("Visitante");
        lblVisitante.setFont(new Font("Arial", Font.BOLD, 12));

        placarPanel.add(lblCasa);
        placarPanel.add(spGolsCasa);
        placarPanel.add(lblX);
        placarPanel.add(spGolsVisitante);
        placarPanel.add(lblVisitante);

        gbc.gridx = 1; gbc.weightx = 1;
        form.add(placarPanel, gbc);

        JLabel lblAviso = new JLabel(
                "Apostas devem ser realizadas até 20 minutos antes do início da partida.",
                SwingConstants.CENTER);
        lblAviso.setForeground(new Color(180, 100, 0));
        lblAviso.setFont(new Font("Arial", Font.PLAIN, 11));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        form.add(lblAviso, gbc);

        btnApostar = new JButton("Registrar Aposta");
        btnApostar.setPreferredSize(new Dimension(200, 36));
        EstiloBotao.aplicarPreenchido(btnApostar, new Color(0, 140, 0), Color.WHITE);
        btnApostar.setFont(new Font("Arial", Font.BOLD, 13));
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        form.add(btnApostar, gbc);

        add(form, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        listaApostas = new JList<>(listModel);
        listaApostas.setFont(new Font("Arial", Font.PLAIN, 13));
        listaApostas.setFixedCellHeight(28);

        JScrollPane scrollPane = new JScrollPane(listaApostas);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Minhas Apostas"));
        add(scrollPane, BorderLayout.CENTER);

        lblInfo = new JLabel("Total de Apostas: 0", SwingConstants.RIGHT);
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 11));
        lblInfo.setForeground(Color.GRAY);
        add(lblInfo, BorderLayout.SOUTH);
    }

    public GrupoAposta getGrupoSelecionado() {
        return (GrupoAposta) cbGrupo.getSelectedItem();
    }

    public int getGolsCasa() {
        return (Integer) spGolsCasa.getValue();
    }

    public int getGolsVisitante() {
        return (Integer) spGolsVisitante.getValue();
    }

    public void setGrupos(List<GrupoAposta> grupos) {
        cbGrupo.removeAllItems();
        grupos.forEach(cbGrupo::addItem);
    }

    public void setPartidas(List<Partida> partidas) {
        cbPartida.removeAllItems();
        partidas.forEach(cbPartida::addItem);
    }

    public void atualizarLabelsPlacar(String casa, String visitante) {
        lblCasa.setText(casa != null ? casa : "casa");
        lblVisitante.setText(visitante != null ? visitante : "Visitante");
    }

    public void exibirMensagem(String msg, String titulo, int tipo) {
        JOptionPane.showMessageDialog(this, msg, titulo, tipo);
    }

    public void addListenerBtnApostar(ActionListener l) {btnApostar.addActionListener(l); }

    public void addListenerCbPartida(ActionListener l) {cbPartida.addActionListener(l); }

    public Partida getPartidaSelecionada() {
        return (Partida) cbPartida.getSelectedItem();
    }

}
