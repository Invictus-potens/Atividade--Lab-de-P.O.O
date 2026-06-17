package view;

import model.GrupoAposta;
import model.Pessoa;
import model.Usuario;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.event.ListSelectionListener;
import java.util.List;

public class PainelGrupos extends JPanel {

    private JTextField tfNomeGrupo;
    private JComboBox<GrupoAposta> cbGruposDisponiveis;
    private DefaultListModel<GrupoAposta> listModel;
    private JList<GrupoAposta> listaGrupos;
    private JTextArea taDetalhes;
    private JLabel lblInfo;
    private JButton btnCriar;
    private JButton btnEntrar;

    public PainelGrupos() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        initComponents();
    }

    private void initComponents() {
        JPanel northPanel = new JPanel(new GridLayout(1, 2, 12, 0));

        JPanel formCriar = new JPanel(new GridBagLayout());
        formCriar.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Criar Novo Grupo"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formCriar.add(new JLabel("Nome do Grupo:"), gbc);
        tfNomeGrupo = new JTextField(16);
        tfNomeGrupo.setFont(new Font("Arial", Font.PLAIN, 13));
        gbc.gridx = 1; gbc.weightx = 1;
        formCriar.add(tfNomeGrupo, gbc);

        btnCriar = new JButton("Criar Grupo");
        EstiloBotao.aplicarPreenchido(btnCriar, new Color(0, 140, 0), Color.WHITE);
        btnCriar.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        formCriar.add(btnCriar, gbc);

        northPanel.add(formCriar);

        JPanel formEntrar = new JPanel(new GridBagLayout());
        formEntrar.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Entrar em um Grupo Existente"));
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formEntrar.add(new JLabel("Grupo:"), gbc);
        cbGruposDisponiveis = new JComboBox<>();
        gbc.gridx = 1; gbc.weightx = 1;
        formEntrar.add(cbGruposDisponiveis, gbc);

        btnEntrar = new JButton("Entrar no Grupo");
        EstiloBotao.aplicarPreenchido(btnEntrar, new Color(0, 100, 200), Color.WHITE);
        btnEntrar.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        formEntrar.add(btnEntrar, gbc);

        northPanel.add(formEntrar);
        add(northPanel, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(340);

        listModel = new DefaultListModel<>();
        listaGrupos = new JList<>(listModel);
        listaGrupos.setFont(new Font("Arial", Font.PLAIN, 14));
        listaGrupos.setFixedCellHeight(28);

        JScrollPane scrollLista = new JScrollPane(listaGrupos);
        scrollLista.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Grupos de Apostas (máx. 5)"));
        splitPane.setLeftComponent(scrollLista);

        taDetalhes = new JTextArea();
        taDetalhes.setEditable(false);
        taDetalhes.setFont(new Font("Monospaced", Font.PLAIN, 13));
        taDetalhes.setMargin(new Insets(8, 8, 8, 8));
        JScrollPane scrollDetalhes = new JScrollPane(taDetalhes);
        scrollDetalhes.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Detalhes do Grupo"));
        splitPane.setRightComponent(scrollDetalhes);

        add(splitPane, BorderLayout.CENTER);

        lblInfo = new JLabel("", SwingConstants.CENTER);
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 11));
        lblInfo.setForeground(Color.GRAY);
        add(lblInfo, BorderLayout.SOUTH);
    }

    public String getNomeGrupoDigitado() {
        return tfNomeGrupo.getText().trim();
    }

    public void limparNome() {
        tfNomeGrupo.setText("");
    }

    public GrupoAposta getGrupoSelecionadoCombo() {
        return (GrupoAposta) cbGruposDisponiveis.getSelectedItem();
    }

    public GrupoAposta getGrupoSelecionadoLista() {
        return listaGrupos.getSelectedValue();
    }

    public void setDetalheText(String texto) {
        taDetalhes.setText(texto);
    }

    public void atualizarR(List<GrupoAposta> grupos, int totalUsuario) {
        listModel.clear();
        cbGruposDisponiveis.removeAllItems();

        for (GrupoAposta g : grupos) {
            listModel.addElement(g);
            cbGruposDisponiveis.addItem(g);
        }
        lblInfo.setText("Seus grupos: " + totalUsuario + " /5");
    }

    public void exibirMensagem(String msg, String titulo, int tipo) {
        JOptionPane.showMessageDialog(this, msg, titulo, tipo);
    }


    public void addListenerBtnCriar(ActionListener l) {
        btnCriar.addActionListener(l); tfNomeGrupo.addActionListener(l);
    }

    public void addListenerBtnEntrar(ActionListener l) {
        btnEntrar.addActionListener(l); 
    }

    public void addListenerListaSelecao(ListSelectionListener l) {
        listaGrupos.addListSelectionListener(l); 
    }

}    