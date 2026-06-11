package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import model.Campeonato;
import model.Clube;

public class PainelCampeonatos extends JPanel {

    private JTextField tfNomeCampeonato;
    private JComboBox<Campeonato> cbCampeonato;
    private JComboBox<Clube> cbClube;
    private DefaultListModel<Campeonato> listModel;
    private JList<Campeonato> listaCampeonatos;
    private JTextArea taDetalhes;
    private JButton btnCriar;
    private JButton btnAdicionar;;

    public PainelCampeonatos() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        initComponents();
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

        btnCriar = new JButton("Criar");
        EstiloBotao.aplicarPreenchido(btnCriar, new Color(0, 140, 0), Color.WHITE);
        btnCriar.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        formCriar.add(btnCriar, gbc);

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

        btnAdicionar = new JButton("Adicionar");
        EstiloBotao.aplicarPreenchido(btnAdicionar, new Color(0, 100, 200), Color.WHITE);
        btnAdicionar.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        formAdicionar.add(btnAdicionar, gbc);

        northPanel.add(formAdicionar);
        add(northPanel, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(300);

        listModel = new DefaultListModel<>();
        listaCampeonatos = new JList<>(listModel);
        listaCampeonatos.setFont(new Font("Arial", Font.PLAIN, 13));
        listaCampeonatos.setFixedCellHeight(26);

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

    public String getNome() {
        return tfNomeCampeonato.getText().trim();
    }

    public Campeonato getCampeonato() {
        return (Campeonato) cbCampeonato.getSelectedItem();
    }

    public Clube getClube() {
        return (Clube) cbClube.getSelectedItem();
    }

    public Campeonato getCampeonatoLista() {
        return listaCampeonatos.getSelectedValue();
    }

    public void LimparCampos() {
        tfNomeCampeonato.setText("");
        tfNomeCampeonato.requestFocus();
    }

    public void atualizarListaCampeonatos(java.util.List<Campeonato> campeonatos) {
        listModel.clear();
        cbCampeonato.removeAllItems();
        for (Campeonato c : campeonatos) {
            listModel.addElement(c);
            cbCampeonato.addItem(c);
        }
    }

    public void atualizarClubes(List<Clube> clubes) {
        cbClube.removeAllItems();
        for (Clube c : clubes) {
            cbClube.addItem(c);
        }
    }

    public void attDetalhes(String texto) {
        taDetalhes.setText(texto);
    }

    public void exibirMensagem(String mensagem, String titulo, int tipo) {
        JOptionPane.showMessageDialog(this, mensagem, titulo, tipo);
    }

    public void addListenerbotao(ActionListener listener) {
        btnCriar.addActionListener(listener);
    }

    public void addListenerbotaoAdd(ActionListener listener) {
        btnAdicionar.addActionListener(listener);
    }

    public void addListenerLista(javax.swing.event.ListSelectionListener listener) {
        listaCampeonatos.addListSelectionListener(listener);
    }
}
