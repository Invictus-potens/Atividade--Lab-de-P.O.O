package view;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class TelaCadastro extends JFrame {
    private JTextField txtNome;
    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JButton btnSalvar;
    private JButton btnVoltar;

    public TelaCadastro() {
        setTitle("Tigrinho UNA - Criar Conta");
        setSize(320, 280);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(30, 30, 80, 25);
        add(lblNome);

        txtNome = new JTextField(20);
        txtNome.setBounds(100, 30, 160, 25);
        add(txtNome);

        JLabel lblLogin = new JLabel("Login:");
        lblLogin.setBounds(30, 70, 80, 25);
        add(lblLogin);

        txtLogin = new JTextField(20);
        txtLogin.setBounds(100, 70, 160, 25);
        add(txtLogin);

        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setBounds(30, 110, 80, 25);
        add(lblSenha);

        txtSenha = new JPasswordField(20);
        txtSenha.setBounds(100, 110, 160, 25);
        add(txtSenha);

        btnSalvar = new JButton("Salvar");
        btnSalvar.setBounds(40, 170, 80, 25);
        add(btnSalvar);

        btnVoltar = new JButton("Voltar");
        btnVoltar.setBounds(160, 170, 90, 25);
        add(btnVoltar);
    }

    public String getNomeDigitado() {
        return txtNome.getText().trim();
    }

    public String getLoginDigitado() {
        return txtLogin.getText().trim();
    }

    public String getSenhaDigitado() {
        return new String(txtSenha.getPassword()).trim();
    }

    public void LimparCampos() {
        txtNome.setText("");
        txtLogin.setText("");
        txtSenha.setText("");
        txtNome.requestFocus();
    }

    public void exibirMensagem(String mensagem, String titulo, int tipo) {
        JOptionPane.showMessageDialog(this, mensagem, titulo, tipo);
    }

    public void adicionarListenerSalvar(ActionListener listener) {
        btnSalvar.addActionListener(listener);
    }

    public void adicionarListenerVoltar(ActionListener listener) {
        btnVoltar.addActionListener(listener);
    }
}