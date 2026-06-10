package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import model.Clube;

public class PainelClubes extends JPanel {

    private JTextField tfNome;
    private JTextField tfCidade;
    private JButton btnCadastrar; 
    private DefaultListModel<Clube> listModel;
    private JList<Clube> listaClubes;
    private JLabel lblContador;

    public PainelClubes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        initComponents();
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

        btnCadastrar = new JButton("Cadastrar Clube");
        btnCadastrar.setPreferredSize(new Dimension(180, 34));
        EstiloBotao.aplicarPreenchido(btnCadastrar, new Color(0, 140, 0), Color.WHITE);
        btnCadastrar.setFont(new Font("Arial", Font.BOLD, 13));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(btnCadastrar, gbc);

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

        lblContador = new JLabel("Total: 0 clubes cadastrados", SwingConstants.RIGHT);
        lblContador.setFont(new Font("Arial", Font.ITALIC, 11));
        lblContador.setForeground(Color.GRAY);
        add(lblContador, BorderLayout.SOUTH);
    }

    public String getNome() {
        return tfNome.getText().trim();
    }

    public String getCidade() {
        return tfCidade.getText().trim();
    }

    public void limparCampos() {
        tfNome.setText("");
        tfCidade.setText("");
        tfNome.requestFocus();
    }

    public void addListenerBotao(ActionListener listener) {
        btnCadastrar.addActionListener(listener);
        tfCidade.addActionListener(listener); 
    }

    public void exibirMensagem(String mensagem, String titulo, int tipoMensagem) {
        JOptionPane.showMessageDialog(this, mensagem, titulo, tipoMensagem);
    }

    public void atualizarListaClubes(List<Clube> clubes) {
        listModel.clear();
        for (Clube c : clubes) {
            listModel.addElement(c);
        }
        lblContador.setText("Total: " + clubes.size() + " clubes cadastrados");
    }
}