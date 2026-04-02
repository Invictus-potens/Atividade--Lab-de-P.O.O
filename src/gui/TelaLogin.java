package gui;

import model.Pessoa;
import service.SistemaGerenciador;

import javax.swing.*;
import java.awt.*;

public class TelaLogin extends JFrame {

    private JTextField tfLogin;
    private JPasswordField pfSenha;
    private JButton btnEntrar;
    private JButton btnCadastrar;

    public TelaLogin() {
        setTitle("ChampionBet — Sistema de Apostas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 320);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        mainPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 5, 6, 5);

        JLabel lblTitulo = new JLabel("ChampionBet", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(0, 120, 0));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        mainPanel.add(lblTitulo, gbc);

        JLabel lblSub = new JLabel("Sistema de Apostas de Campeonato", SwingConstants.CENTER);
        lblSub.setFont(new Font("Arial", Font.ITALIC, 12));
        lblSub.setForeground(Color.GRAY);
        gbc.gridy = 1;
        mainPanel.add(lblSub, gbc);

        JSeparator sep = new JSeparator();
        gbc.gridy = 2;
        mainPanel.add(sep, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(new JLabel("Login:"), gbc);
        tfLogin = new JTextField(15);
        tfLogin.setFont(new Font("Arial", Font.PLAIN, 13));
        gbc.gridx = 1;
        mainPanel.add(tfLogin, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        mainPanel.add(new JLabel("Senha:"), gbc);
        pfSenha = new JPasswordField(15);
        pfSenha.setFont(new Font("Arial", Font.PLAIN, 13));
        gbc.gridx = 1;
        mainPanel.add(pfSenha, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        btnPanel.setOpaque(false);

        btnEntrar = new JButton("Entrar");
        btnEntrar.setPreferredSize(new Dimension(100, 32));
        EstiloBotao.aplicarPreenchido(btnEntrar, new Color(0, 120, 60), Color.WHITE);
        btnEntrar.setFont(new Font("Arial", Font.BOLD, 13));

        btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setPreferredSize(new Dimension(100, 32));
        btnCadastrar.setFont(new Font("Arial", Font.PLAIN, 13));

        btnPanel.add(btnEntrar);
        btnPanel.add(btnCadastrar);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        mainPanel.add(btnPanel, gbc);

        JLabel lblDica = new JLabel("Admin padrão: login=admin / senha=admin123", SwingConstants.CENTER);
        lblDica.setFont(new Font("Arial", Font.ITALIC, 10));
        lblDica.setForeground(Color.LIGHT_GRAY);
        gbc.gridy = 6;
        mainPanel.add(lblDica, gbc);

        btnEntrar.addActionListener(e -> fazerLogin());
        pfSenha.addActionListener(e -> fazerLogin());
        btnCadastrar.addActionListener(e -> abrirCadastro());

        add(mainPanel);
    }

    private void fazerLogin() {
        String login = tfLogin.getText().trim();
        String senha = new String(pfSenha.getPassword());

        if (login.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Preencha o login e a senha!",
                    "Campos Obrigatórios", JOptionPane.WARNING_MESSAGE);
            return;
        }

        SistemaGerenciador sistema = SistemaGerenciador.getInstance();
        Pessoa pessoa = sistema.login(login, senha);

        if (pessoa == null) {
            JOptionPane.showMessageDialog(this,
                    "Login ou senha incorretos. Tente novamente.",
                    "Erro de Autenticação", JOptionPane.ERROR_MESSAGE);
            pfSenha.setText("");
            tfLogin.requestFocus();
            return;
        }

        sistema.setPessoaLogada(pessoa);
        this.setVisible(false);

        TelaPrincipal principal = new TelaPrincipal(this);
        principal.setVisible(true);

        tfLogin.setText("");
        pfSenha.setText("");
    }

    private void abrirCadastro() {
        JTextField tfNome = new JTextField(18);
        JTextField tfLoginNovo = new JTextField(18);
        JPasswordField pfSenhaNova = new JPasswordField(18);

        JPanel panel = new JPanel(new GridLayout(3, 2, 8, 8));
        panel.add(new JLabel("Nome completo:"));
        panel.add(tfNome);
        panel.add(new JLabel("Login:"));
        panel.add(tfLoginNovo);
        panel.add(new JLabel("Senha:"));
        panel.add(pfSenhaNova);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Cadastro de Novo Usuário",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String nome = tfNome.getText().trim();
            String login = tfLoginNovo.getText().trim();
            String senha = new String(pfSenhaNova.getPassword());

            if (nome.isEmpty() || login.isEmpty() || senha.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Todos os campos são obrigatórios!",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                SistemaGerenciador.getInstance().cadastrarUsuario(nome, login, senha);
                JOptionPane.showMessageDialog(this,
                        "Usuário '" + nome + "' cadastrado com sucesso!\nAgora faça o login.",
                        "Cadastro Realizado", JOptionPane.INFORMATION_MESSAGE);
                tfLogin.setText(login);
                pfSenha.requestFocus();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(), "Erro no Cadastro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
