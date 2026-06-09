package gui;

import model.Campeonato;
import model.Clube;
import service.SistemaGerenciador;

import javax.swing.*;
import java.awt.*;

public class PainelCampeonatos extends JPanel implements TelaPrincipal.Atualizavel {

    private final SistemaGerenciador sistema = SistemaGerenciador.getInstance();

    private JTextField tfNomeCampeonato;
    private JComboBox<Campeonato> cbCampeonato;
    private JComboBox<Clube> cbClube;
    private DefaultListModel<Campeonato> listModel;
    private JList<Campeonato> listaCampeonatos;
    private JTextArea taDetalhes;

    public PainelCampeonatos() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        initComponents();
        atualizar();
    }

    private void initComponents() {
        JPanel northPanel = new JPanel(new GridLayout(1, 2, 12, 0));

        JPanel formCriar = new JPanel(new GridBagLayout());
        formCriar.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Criar Campeonato"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formCriar.add(new JLabel("Nome:"), gbc);
        tfNomeCampeonato = new JTextField(16);
        tfNomeCampeonato.setFont(new Font("Arial", Font.PLAIN, 13));
        gbc.gridx = 1; gbc.weightx = 1;
        formCriar.add(tfNomeCampeonato, gbc);

        JButton btnCriar = new JButton("Criar");
        EstiloBotao.aplicarPreenchido(btnCriar, new Color(0, 140, 0), Color.WHITE);
        btnCriar.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        formCriar.add(btnCriar, gbc);
        btnCriar.addActionListener(e -> criarCampeonato());

        northPanel.add(formCriar);

        JPanel formAdicionar = new JPanel(new GridBagLayout());
        formAdicionar.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Adicionar Clube ao Campeonato"));
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formAdicionar.add(new JLabel("Campeonato:"), gbc);
        cbCampeonato = new JComboBox<>();
        gbc.gridx = 1; gbc.weightx = 1;
        formAdicionar.add(cbCampeonato, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formAdicionar.add(new JLabel("Clube:"), gbc);
        cbClube = new JComboBox<>();
        gbc.gridx = 1; gbc.weightx = 1;
        formAdicionar.add(cbClube, gbc);

        JButton btnAdicionar = new JButton("Adicionar");
        EstiloBotao.aplicarPreenchido(btnAdicionar, new Color(0, 100, 200), Color.WHITE);
        btnAdicionar.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        formAdicionar.add(btnAdicionar, gbc);
        btnAdicionar.addActionListener(e -> adicionarClube());

        northPanel.add(formAdicionar);
        add(northPanel, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(300);

        listModel = new DefaultListModel<>();
        listaCampeonatos = new JList<>(listModel);
        listaCampeonatos.setFont(new Font("Arial", Font.PLAIN, 13));
        listaCampeonatos.setFixedCellHeight(26);
        listaCampeonatos.addListSelectionListener(e -> mostrarDetalhes());

        JScrollPane scrollLista = new JScrollPane(listaCampeonatos);
        scrollLista.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Campeonatos"));
        splitPane.setLeftComponent(scrollLista);

        taDetalhes = new JTextArea();
        taDetalhes.setEditable(false);
        taDetalhes.setFont(new Font("Monospaced", Font.PLAIN, 13));
        taDetalhes.setMargin(new Insets(8, 8, 8, 8));
        JScrollPane scrollDetalhes = new JScrollPane(taDetalhes);
        scrollDetalhes.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Detalhes do Campeonato"));
        splitPane.setRightComponent(scrollDetalhes);

        add(splitPane, BorderLayout.CENTER);
    }

    private void criarCampeonato() {
        String nome = tfNomeCampeonato.getText().trim();
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Informe o nome do campeonato!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            sistema.cadastrarCampeonato(nome);
            tfNomeCampeonato.setText("");
            atualizar();
            JOptionPane.showMessageDialog(this,
                    "Campeonato '" + nome + "' criado com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void adicionarClube() {
        Campeonato camp = (Campeonato) cbCampeonato.getSelectedItem();
        Clube clube = (Clube) cbClube.getSelectedItem();

        if (camp == null || clube == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um campeonato e um clube!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            sistema.adicionarClubeAoCampeonato(camp, clube);
            atualizar();
            mostrarDetalhes();
            JOptionPane.showMessageDialog(this,
                    "Clube '" + clube.getNome() + "' adicionado ao campeonato '" + camp.getNome() + "'!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDetalhes() {
        Campeonato camp = listaCampeonatos.getSelectedValue();
        if (camp == null) {
            taDetalhes.setText("");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Campeonato: ").append(camp.getNome()).append("\n");
        sb.append("-".repeat(40)).append("\n");
        sb.append("Clubes participantes: ")
                .append(camp.listar().size()).append(" / ").append(camp.getTamanhoMaximo()).append("\n\n");

        if (camp.listar().isEmpty()) {
            sb.append("  (nenhum clube adicionado ainda)");
        } else {
            for (int i = 0; i < camp.listar().size(); i++) {
                sb.append("  ").append(i + 1).append(". ").append(camp.listar().get(i)).append("\n");
            }
        }
        taDetalhes.setText(sb.toString());
    }

    @Override
    public void atualizar() {
        listModel.clear();
        sistema.getCampeonatos().forEach(listModel::addElement);

        Campeonato selCamp = (Campeonato) cbCampeonato.getSelectedItem();
        cbCampeonato.removeAllItems();
        sistema.getCampeonatos().forEach(cbCampeonato::addItem);
        if (selCamp != null) cbCampeonato.setSelectedItem(selCamp);

        Clube selClube = (Clube) cbClube.getSelectedItem();
        cbClube.removeAllItems();
        sistema.getClubes().forEach(cbClube::addItem);
        if (selClube != null) cbClube.setSelectedItem(selClube);
    }
}
