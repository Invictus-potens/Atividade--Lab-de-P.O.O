package gui;

import model.Campeonato;
import model.Clube;
import model.Partida;
import service.SistemaGerenciador;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class PainelPartidas extends JPanel implements TelaPrincipal.Atualizavel {

    private final SistemaGerenciador sistema = SistemaGerenciador.getInstance();

    private JComboBox<Campeonato> cbCampeonato;
    private JComboBox<Clube> cbCasa;
    private JComboBox<Clube> cbVisitante;
    private JSpinner spDataHora;
    private DefaultListModel<Partida> listModel;
    private JList<Partida> listaPartidas;
    private JLabel lblContador;

    public PainelPartidas() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        initComponents();
        atualizar();
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
        cbCampeonato.addActionListener(e -> carregarClubesDoCampeonato());
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

        JButton btnCadastrar = new JButton("Cadastrar Partida");
        btnCadastrar.setPreferredSize(new Dimension(200, 34));
        btnCadastrar.setBackground(new Color(0, 140, 0));
        btnCadastrar.setForeground(Color.WHITE);
        btnCadastrar.setFont(new Font("Arial", Font.BOLD, 13));
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        form.add(btnCadastrar, gbc);
        btnCadastrar.addActionListener(e -> cadastrarPartida());

        add(form, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        listaPartidas = new JList<>(listModel);
        listaPartidas.setFont(new Font("Arial", Font.PLAIN, 13));
        listaPartidas.setFixedCellHeight(28);

        JScrollPane scrollPane = new JScrollPane(listaPartidas);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Partidas Cadastradas"));
        add(scrollPane, BorderLayout.CENTER);

        lblContador = new JLabel("", SwingConstants.RIGHT);
        lblContador.setFont(new Font("Arial", Font.ITALIC, 11));
        lblContador.setForeground(Color.GRAY);
        add(lblContador, BorderLayout.SOUTH);
    }

    private void carregarClubesDoCampeonato() {
        Campeonato camp = (Campeonato) cbCampeonato.getSelectedItem();
        cbCasa.removeAllItems();
        cbVisitante.removeAllItems();
        if (camp != null) {
            camp.listar().forEach(c -> {
                cbCasa.addItem(c);
                cbVisitante.addItem(c);
            });
            if (cbVisitante.getItemCount() > 1) {
                cbVisitante.setSelectedIndex(1);
            }
        }
    }

    private void cadastrarPartida() {
        Campeonato camp = (Campeonato) cbCampeonato.getSelectedItem();
        Clube casa = (Clube) cbCasa.getSelectedItem();
        Clube visitante = (Clube) cbVisitante.getSelectedItem();
        Date dataEscolhida = (Date) spDataHora.getValue();

        if (camp == null || casa == null || visitante == null) {
            JOptionPane.showMessageDialog(this,
                    "Preencha todos os campos!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDateTime dataHora = dataEscolhida.toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();

        try {
            sistema.cadastrarPartida(camp, casa, visitante, dataHora);
            atualizar();
            JOptionPane.showMessageDialog(this,
                    "Partida cadastrada: " + casa.getNome() + " vs " + visitante.getNome(),
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void atualizar() {
        Campeonato selCamp = (Campeonato) cbCampeonato.getSelectedItem();
        cbCampeonato.removeAllItems();
        sistema.getCampeonatos().forEach(cbCampeonato::addItem);
        if (selCamp != null) cbCampeonato.setSelectedItem(selCamp);
        else carregarClubesDoCampeonato();

        listModel.clear();
        sistema.getPartidas().forEach(listModel::addElement);
        lblContador.setText("Total: " + sistema.getPartidas().size() + " partida(s)");
    }
}
