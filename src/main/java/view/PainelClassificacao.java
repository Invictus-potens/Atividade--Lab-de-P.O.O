package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import controller.ClassificacaoCon;
import model.Classificacao;
import model.GrupoAposta;

public class PainelClassificacao extends JPanel implements TelaPrincipal.Atualizavel {

    private ClassificacaoCon controller;

    private JComboBox<GrupoAposta> cbGrupo;
    private JButton btnAtualizar;
    private DefaultTableModel tableModel;
    private JTable tabela;
    private JLabel lblTotalApostas;
    private JLabel lblGrupoInfo;

    public PainelClassificacao() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        initComponents();
    }

    private void initComponents() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 5));
        topPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Selecionar Grupo"));

        topPanel.add(new JLabel("Grupo:"));
        cbGrupo = new JComboBox<>();
        cbGrupo.setPreferredSize(new Dimension(280, 28));
        cbGrupo.setFont(new Font("Arial", Font.PLAIN, 13));
        topPanel.add(cbGrupo);

        btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setFont(new Font("Arial", Font.BOLD, 12));
        topPanel.add(btnAtualizar);

        lblGrupoInfo = new JLabel("");
        lblGrupoInfo.setFont(new Font("Arial", Font.ITALIC, 12));
        lblGrupoInfo.setForeground(Color.GRAY);
        topPanel.add(lblGrupoInfo);

        add(topPanel, BorderLayout.NORTH);

        String[] colunas = {"Posição", "Participante", "Total de Pontos"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tabela = new JTable(tableModel);
        tabela.setFont(new Font("Arial", Font.PLAIN, 15));
        tabela.setRowHeight(36);
        tabela.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tabela.getTableHeader().setBackground(new Color(0, 100, 0));
        tabela.getTableHeader().setForeground(Color.WHITE);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setGridColor(new Color(220, 220, 220));
        tabela.setShowHorizontalLines(true);
        tabela.setShowVerticalLines(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tabela.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tabela.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tabela.getColumnModel().getColumn(0).setPreferredWidth(80);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(300);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(150);

        tabela.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    if (row == 0) {
                        c.setBackground(new Color(255, 215, 0, 100));
                        c.setFont(getFont().deriveFont(Font.BOLD));
                    } else if (row == 1) {
                        c.setBackground(new Color(192, 192, 192, 80));
                        c.setFont(getFont().deriveFont(Font.PLAIN));
                    } else if (row == 2) {
                        c.setBackground(new Color(205, 127, 50, 60));
                        c.setFont(getFont().deriveFont(Font.PLAIN));
                    } else {
                        c.setBackground(Color.WHITE);
                        c.setFont(getFont().deriveFont(Font.PLAIN));
                    }
                }
                setHorizontalAlignment(column == 1 ? SwingConstants.LEFT : SwingConstants.CENTER);
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Classificação do Grupo"));
        add(scrollPane, BorderLayout.CENTER);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));

        lblTotalApostas = new JLabel("", SwingConstants.LEFT);
        lblTotalApostas.setFont(new Font("Arial", Font.ITALIC, 11));
        lblTotalApostas.setForeground(Color.GRAY);
        footer.add(lblTotalApostas, BorderLayout.WEST);

        JLabel lblLegenda = new JLabel("1º / 2º / 3º lugar (cores na tabela)", SwingConstants.RIGHT);
        lblLegenda.setFont(new Font("Arial", Font.PLAIN, 11));
        lblLegenda.setForeground(Color.GRAY);
        footer.add(lblLegenda, BorderLayout.EAST);

        add(footer, BorderLayout.SOUTH);
    }

    public void setController(ClassificacaoCon controller) {
        this.controller = controller;
    }

    public void setGrupos(List<GrupoAposta> grupos) {
        GrupoAposta sel = (GrupoAposta) cbGrupo.getSelectedItem();
        cbGrupo.removeAllItems();
        grupos.forEach(cbGrupo::addItem);
        if (sel != null) cbGrupo.setSelectedItem(sel);
    }

    public GrupoAposta getGrupoSelecionado() {
        return (GrupoAposta) cbGrupo.getSelectedItem();
    }

    public void mostrarClassificacao(List<Classificacao> classificacao, GrupoAposta grupo) {
        tableModel.setRowCount(0);
    for (int i = 0; i < classificacao.size(); i++) {
        Classificacao item = classificacao.get(i);
        tableModel.addRow(new Object[]{
            (i + 1) + "º", 
            item.getNomeParticipante(), 
            item.getTotalPontos() + " pts"
        });
    }
        lblGrupoInfo.setText("  |  " + grupo.listar().size() + " participante(s)  |  criado por: "
                + grupo.getCriador().getNome());
        lblTotalApostas.setText("Pontos de partidas encerradas. Apostas em andamento não contabilizadas.");
    }

    public void limparTabela() {
        tableModel.setRowCount(0);
        lblGrupoInfo.setText("");
        lblTotalApostas.setText("");
    }

    public void exibirMensagem(String msg, String titulo, int tipo) {
        JOptionPane.showMessageDialog(this, msg, titulo, tipo);
    }

    public void addListenerCbGrupo(ActionListener listener) {
        cbGrupo.addActionListener(listener);
    }

    public void addListenerBtnAtualizar(ActionListener listener) {
        btnAtualizar.addActionListener(listener);
    }

    @Override
    public void atualizar() {
        if (controller != null) controller.carregarTudo();
    }
}