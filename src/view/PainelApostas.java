package gui;

import model.*;
import service.SistemaGerenciador;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PainelApostas extends JPanel implements TelaPrincipal.Atualizavel {

    private final SistemaGerenciador sistema = SistemaGerenciador.getInstance();

    private JComboBox<GrupoAposta> cbGrupo;
    private JComboBox<Partida> cbPartida;
    private JSpinner spGolsCasa;
    private JSpinner spGolsVisitante;
    private JLabel lblCasa;
    private JLabel lblVisitante;
    private DefaultListModel<Aposta> listModel;
    private JList<Aposta> listaApostas;
    private JLabel lblInfo;

    public PainelApostas() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        initComponents();
        atualizar();
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
        cbPartida.addActionListener(e -> atualizarLabelsPartida());
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
                "<html><i>Apostas devem ser realizadas até 20 minutos antes do início da partida.</i></html>",
                SwingConstants.CENTER);
        lblAviso.setForeground(new Color(180, 100, 0));
        lblAviso.setFont(new Font("Arial", Font.PLAIN, 11));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        form.add(lblAviso, gbc);

        JButton btnApostar = new JButton("Registrar Aposta");
        btnApostar.setPreferredSize(new Dimension(200, 36));
        EstiloBotao.aplicarPreenchido(btnApostar, new Color(0, 140, 0), Color.WHITE);
        btnApostar.setFont(new Font("Arial", Font.BOLD, 13));
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        form.add(btnApostar, gbc);
        btnApostar.addActionListener(e -> registrarAposta());

        add(form, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        listaApostas = new JList<>(listModel);
        listaApostas.setFont(new Font("Arial", Font.PLAIN, 13));
        listaApostas.setFixedCellHeight(28);

        JScrollPane scrollPane = new JScrollPane(listaApostas);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Minhas Apostas"));
        add(scrollPane, BorderLayout.CENTER);

        lblInfo = new JLabel("", SwingConstants.RIGHT);
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 11));
        lblInfo.setForeground(Color.GRAY);
        add(lblInfo, BorderLayout.SOUTH);
    }

    private void atualizarLabelsPartida() {
        Partida partida = (Partida) cbPartida.getSelectedItem();
        if (partida != null) {
            lblCasa.setText(partida.getCasa().getNome());
            lblVisitante.setText(partida.getVisitante().getNome());
        } else {
            lblCasa.setText("Casa");
            lblVisitante.setText("Visitante");
        }
    }

    private void registrarAposta() {
        Pessoa pessoa = sistema.getPessoaLogada();
        if (!(pessoa instanceof Usuario)) {
            JOptionPane.showMessageDialog(this,
                    "Apenas usuários comuns podem realizar apostas!",
                    "Acesso Negado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        GrupoAposta grupo = (GrupoAposta) cbGrupo.getSelectedItem();
        Partida partida = (Partida) cbPartida.getSelectedItem();

        if (grupo == null || partida == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecione o grupo e a partida!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int golsCasa = (Integer) spGolsCasa.getValue();
        int golsVisitante = (Integer) spGolsVisitante.getValue();

        try {
            sistema.registrarAposta((Usuario) pessoa, partida, grupo, golsCasa, golsVisitante);
            atualizar();
            JOptionPane.showMessageDialog(this,
                    "Aposta registrada com sucesso!\n\n" +
                    "Partida: " + partida.getCasa().getNome() + " vs " + partida.getVisitante().getNome() + "\n" +
                    "Placar previsto: " + golsCasa + " x " + golsVisitante + "\n" +
                    "Grupo: " + grupo.getNome(),
                    "Aposta Confirmada", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(), "Erro ao Registrar Aposta", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void atualizar() {
        Pessoa pessoa = sistema.getPessoaLogada();

        GrupoAposta selGrupo = (GrupoAposta) cbGrupo.getSelectedItem();
        cbGrupo.removeAllItems();
        if (pessoa instanceof Usuario) {
            List<GrupoAposta> gruposUsuario = sistema.getGruposDoUsuario((Usuario) pessoa);
            gruposUsuario.forEach(cbGrupo::addItem);
        }
        if (selGrupo != null) cbGrupo.setSelectedItem(selGrupo);

        Partida selPartida = (Partida) cbPartida.getSelectedItem();
        cbPartida.removeAllItems();
        sistema.getPartidasDisponiveisParaAposta().forEach(cbPartida::addItem);
        if (selPartida != null) cbPartida.setSelectedItem(selPartida);

        atualizarLabelsPartida();

        listModel.clear();
        if (pessoa instanceof Usuario) {
            List<Aposta> apostas = sistema.getApostasDoUsuario((Usuario) pessoa);
            apostas.forEach(listModel::addElement);
            lblInfo.setText("Total de apostas: " + apostas.size());
        }
    }
}
