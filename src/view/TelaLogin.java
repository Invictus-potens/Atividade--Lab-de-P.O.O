package view;

import javax.swing.*;

import java.awt.event.ActionListener;
import model.Pessoa;
import service.SistemaGerenciador;

import java.awt.*;

public class TelaLogin extends JFrame {
    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JButton btnEntrar;

    public TelaLogin() {
        setTitle("Sistema de Apostas - Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblLogin = new JLabel("Login:");
        lblLogin.setBounds(30, 30, 80, 25);
        add(lblLogin);

        txtLogin = new JTextField(20);
        txtLogin.setBounds(100, 30, 160, 25);
        add(txtLogin);

        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setBounds(30, 70, 80, 25);
        add(lblSenha);

        txtSenha = new JPasswordField(20);
        txtSenha.setBounds(100, 70, 160, 25);
        add(txtSenha);

        btnEntrar = new JButton("Entrar");
        btnEntrar.setBounds(100, 110, 100, 25);
        add(btnEntrar);
    }

    public String getLogin() {
        return txtLogin.getText();
    }

    public String getSenha() {
        return new String(txtSenha.getPassword());
    }

    public void adicionarListenerBotaoEntrar(ActionListener listener) {
        btnEntrar.addActionListener(listener);
    }

    public void exibirMensagem(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem);
    }
}