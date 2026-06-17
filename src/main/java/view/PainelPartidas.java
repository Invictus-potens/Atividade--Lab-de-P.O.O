package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;

import model.Campeonato;
import model.Clube;
import model.Partida;

public class PainelPartidas extends JPanel {

    private JComboBox<Campeonato> cbCampeonato;
    private JComboBox<Clube> cbCasa;
    private JComboBox<Clube> cbVisitante;
    private JSpinner spDataHora;
    private DefaultListModel<Partida> listModel;
    private JList<Partida> listaPartidas;
    private JLabel lblContador;
    private JButton btnCadastrar;

    public PainelPartidas() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        initComponents();
    }

    private void initComponents() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Cadastrar Nova Partida"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        form.add(new JLabel("Campeonato:"), gbc);
        cbCampeonato = new JComboBox<>();
        cbCampeonato.setFont(new Font("Arial", Font.PLAIN, 13));
        gbc.gridx = 1; gbc.weightx = 1;
        form.add(cbCampeonato, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        form.add(new JLabel("Clube da casa:"), gbc);
        cbCasa = new JComboBox<>();
        cbCasa.setFont(new Font("Arial", Font.PLAIN, 13));
        gbc.gridx = 1; gbc.weightx = 1;
        form.add(cbCasa, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        form.add(new JLabel("Clube visitante:"), gbc);
        cbVisitante = new JComboBox<>();
        cbVisitante.setFont(new Font("Arial", Font.PLAIN, 13));
        gbc.gridx = 1; gbc.weightx = 1;
        form.add(cbVisitante, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        form.add(new JLabel("Data e Hora:"), gbc);
        SpinnerDateModel dateModel = new SpinnerDateModel(new Date(), null, null,
                java.util.Calendar.MINUTE);
        spDataHora = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spDataHora, "dd/MM/yyyy HH:mm");
        spDataHora.setEditor(dateEditor);
        spDataHora.setFont(new Font("Arial", Font.PLAIN, 13));
        gbc.gridx = 1; gbc.weightx = 1;
        form.add(spDataHora, gbc);

        btnCadastrar = new JButton("Cadastrar Partida");
        btnCadastrar.setPreferredSize(new Dimension(200, 34));
        EstiloBotao.aplicarPreenchido(btnCadastrar, new Color(0, 140, 0), Color.WHITE);
        btnCadastrar.setFont(new Font("Arial", Font.BOLD, 13));
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        form.add(btnCadastrar, gbc);

        add(form, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        listaPartidas = new JList<>(listModel);
        listaPartidas.setFont(new Font("Arial", Font.PLAIN, 13));
        listaPartidas.setFixedCellHeight(28);

        JScrollPane scrollPane = new JScrollPane(listaPartidas);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Partidas Cadastradas"));
        add(scrollPane, BorderLayout.CENTER);

        lblContador = new JLabel("Total: 0 Partidas", SwingConstants.RIGHT);
        lblContador.setFont(new Font("Arial", Font.ITALIC, 11));
        lblContador.setForeground(Color.GRAY);
        add(lblContador, BorderLayout.SOUTH);
    }

    public Campeonato getCampeonatoSelecionado() {
        return (Campeonato) cbCampeonato.getSelectedItem();
    }

    public Clube getCasaSelecionado() {
        return (Clube) cbCasa.getSelectedItem();
    }

    public Clube getVisitanteSelecionado() {
        return (Clube) cbVisitante.getSelectedItem();
    }

    public Date getDataHora() {
        return (Date) spDataHora.getValue();
    }

    public void setCampeonato(java.util.List<Campeonato>camps) {
        cbCampeonato.removeAllItems();
        camps.forEach(cbCampeonato::addItem);
    }

    public void setClube(java.util.List<Clube> clubes) {
        cbCasa.removeAllItems();
        cbVisitante.removeAllItems();
        clubes.forEach(c -> {
            cbCasa.addItem(c);
            cbVisitante.addItem(c);
        });
    }

    public void atualizarListaPartidas(java.util.List<Partida> partidas) {
        listModel.clear();
        for (Partida p : partidas) {
            listModel.addElement(p);
        }
        lblContador.setText("Total: " + partidas.size() + " Partidas");
    }

    public void exibirMensagem(String msg, String titulo, int tipo) {
        JOptionPane.showMessageDialog(this, msg, titulo, tipo);
    }

    public void addListenerBtnCadastrar(java.awt.event.ActionListener l) {
        btnCadastrar.addActionListener(l);
    }

    public void addListenerCbCampeonato(java.awt.event.ActionListener l) {
        cbCampeonato.addActionListener(l);
    }
}
