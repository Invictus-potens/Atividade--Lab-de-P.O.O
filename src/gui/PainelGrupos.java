package gui;

import model.GrupoAposta;
import model.Pessoa;
import model.Usuario;
import service.SistemaGerenciador;

import javax.swing.*;
import java.awt.*;

public class PainelGrupos extends JPanel implements TelaPrincipal.Atualizavel {

    private final SistemaGerenciador sistema = SistemaGerenciador.getInstance();

    private JTextField tfNomeGrupo;
    private JComboBox<GrupoAposta> cbGruposDisponiveis;
    private DefaultListModel<GrupoAposta> listModel;
    private JList<GrupoAposta> listaGrupos;
    private JTextArea taDetalhes;
    private JLabel lblInfo;

    public PainelGrupos() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        initComponents();
        atualizar();
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

        JButton btnCriar = new JButton("Criar Grupo");
        btnCriar.setBackground(new Color(0, 140, 0));
        btnCriar.setForeground(Color.WHITE);
        btnCriar.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        formCriar.add(btnCriar, gbc);
        btnCriar.addActionListener(e -> criarGrupo());
        tfNomeGrupo.addActionListener(e -> criarGrupo());

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

        JButton btnEntrar = new JButton("Entrar no Grupo");
        btnEntrar.setBackground(new Color(0, 100, 200));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        formEntrar.add(btnEntrar, gbc);
        btnEntrar.addActionListener(e -> entrarNoGrupo());

        northPanel.add(formEntrar);
        add(northPanel, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(340);

        listModel = new DefaultListModel<>();
        listaGrupos = new JList<>(listModel);
        listaGrupos.setFont(new Font("Arial", Font.PLAIN, 14));
        listaGrupos.setFixedCellHeight(28);
        listaGrupos.addListSelectionListener(e -> mostrarDetalhes());

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

    private void criarGrupo() {
        Pessoa pessoa = sistema.getPessoaLogada();
        if (!(pessoa instanceof Usuario)) {
            JOptionPane.showMessageDialog(this,
                    "Apenas usuários comuns podem criar grupos!",
                    "Acesso Negado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nome = tfNomeGrupo.getText().trim();
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Informe um nome para o grupo!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            sistema.criarGrupo(nome, (Usuario) pessoa);
            tfNomeGrupo.setText("");
            atualizar();
            JOptionPane.showMessageDialog(this,
                    "Grupo '" + nome + "' criado com sucesso!\nVocê é o criador e já está participando.",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void entrarNoGrupo() {
        Pessoa pessoa = sistema.getPessoaLogada();
        if (!(pessoa instanceof Usuario)) {
            JOptionPane.showMessageDialog(this,
                    "Apenas usuários comuns podem participar de grupos!",
                    "Acesso Negado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        GrupoAposta grupo = (GrupoAposta) cbGruposDisponiveis.getSelectedItem();
        if (grupo == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um grupo!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            sistema.entrarNoGrupo(grupo, (Usuario) pessoa);
            atualizar();
            mostrarDetalhes();
            JOptionPane.showMessageDialog(this,
                    "Você entrou no grupo '" + grupo.getNome() + "' com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDetalhes() {
        GrupoAposta grupo = listaGrupos.getSelectedValue();
        if (grupo == null) {
            taDetalhes.setText("");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Grupo: ").append(grupo.getNome()).append("\n");
        sb.append("Criador: ").append(grupo.getCriador().getNome()).append("\n");
        sb.append("-".repeat(40)).append("\n");
        sb.append("Participantes: ").append(grupo.listar().size())
                .append(" / ").append(grupo.getTamanhoMaximo()).append("\n\n");

        for (int i = 0; i < grupo.listar().size(); i++) {
            String marcador = grupo.listar().get(i).equals(grupo.getCriador()) ? " (criador)" : "";
            sb.append("  ").append(i + 1).append(". ")
                    .append(grupo.listar().get(i).getNome()).append(marcador).append("\n");
        }
        taDetalhes.setText(sb.toString());
    }

    @Override
    public void atualizar() {
        listModel.clear();
        sistema.getGrupos().forEach(listModel::addElement);

        GrupoAposta sel = (GrupoAposta) cbGruposDisponiveis.getSelectedItem();
        cbGruposDisponiveis.removeAllItems();
        sistema.getGrupos().forEach(cbGruposDisponiveis::addItem);
        if (sel != null) cbGruposDisponiveis.setSelectedItem(sel);

        lblInfo.setText("Total de grupos: " + sistema.getGrupos().size() + " / 5");
    }
}
