package gui;

import model.Clube;
import service.SistemaGerenciador;

import javax.swing.*;
import java.awt.*;

public class PainelClubes extends JPanel implements TelaPrincipal.Atualizavel {

    private final SistemaGerenciador sistema = SistemaGerenciador.getInstance();

    private JTextField tfNome;
    private JTextField tfCidade;
    private DefaultListModel<Clube> listModel;
    private JList<Clube> listaClubes;
    private JLabel lblContador;

    public PainelClubes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        initComponents();
        atualizar();
    }

    private void initComponents() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Cadastrar Novo Clube"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(new JLabel("Nome do Clube:"), gbc);
        tfNome = new JTextField(22);
        tfNome.setFont(new Font("Arial", Font.PLAIN, 13));
        gbc.gridx = 1; gbc.weightx = 1;
        formPanel.add(tfNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Cidade:"), gbc);
        tfCidade = new JTextField(22);
        tfCidade.setFont(new Font("Arial", Font.PLAIN, 13));
        gbc.gridx = 1; gbc.weightx = 1;
        formPanel.add(tfCidade, gbc);

        JButton btnCadastrar = new JButton("Cadastrar Clube");
        btnCadastrar.setPreferredSize(new Dimension(180, 34));
        EstiloBotao.aplicarPreenchido(btnCadastrar, new Color(0, 140, 0), Color.WHITE);
        btnCadastrar.setFont(new Font("Arial", Font.BOLD, 13));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(btnCadastrar, gbc);

        btnCadastrar.addActionListener(e -> cadastrarClube());
        tfCidade.addActionListener(e -> cadastrarClube());

        add(formPanel, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        listaClubes = new JList<>(listModel);
        listaClubes.setFont(new Font("Arial", Font.PLAIN, 14));
        listaClubes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaClubes.setFixedCellHeight(28);

        JScrollPane scrollPane = new JScrollPane(listaClubes);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Clubes Cadastrados"));
        add(scrollPane, BorderLayout.CENTER);

        lblContador = new JLabel("Total: 0 clube(s) cadastrado(s)", SwingConstants.RIGHT);
        lblContador.setFont(new Font("Arial", Font.ITALIC, 11));
        lblContador.setForeground(Color.GRAY);
        add(lblContador, BorderLayout.SOUTH);
    }

    private void cadastrarClube() {
        String nome = tfNome.getText().trim();
        String cidade = tfCidade.getText().trim();

        if (nome.isEmpty() || cidade.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Preencha o nome e a cidade do clube!",
                    "Campos Obrigatórios", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            sistema.cadastrarClube(nome, cidade);
            tfNome.setText("");
            tfCidade.setText("");
            tfNome.requestFocus();
            atualizar();
            JOptionPane.showMessageDialog(this,
                    "Clube '" + nome + "' cadastrado com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(), "Erro ao Cadastrar", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void atualizar() {
        listModel.clear();
        sistema.getClubes().forEach(listModel::addElement);
        lblContador.setText("Total: " + sistema.getClubes().size() + " clube(s) cadastrado(s)");
    }
}
