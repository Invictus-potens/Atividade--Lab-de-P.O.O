package controller;

import javax.swing.JOptionPane;

import dao.PessoaDAO;
import model.Usuario;
import view.TelaCadastro;

public class CadastroCon {
    private TelaCadastro telaCadastro;
    private javax.swing.JFrame telaLoginOri;
    private PessoaDAO pessoadao;

    public CadastroCon(TelaCadastro telaCadastro, javax.swing.JFrame telaLoginOri) {
        this.telaCadastro = telaCadastro;
        this.telaLoginOri = telaLoginOri;
        this.pessoadao = new PessoaDAO();

        this.initController();
    }

    private void initController() {
        this.telaCadastro.adicionarListenerSalvar(e -> processarCadastro());
        this.telaCadastro.adicionarListenerVoltar(e -> voltarLogin());
    }

    private void processarCadastro() {
        String nome = telaCadastro.getNomeDigitado();
        String login = telaCadastro.getLoginDigitado();
        String senha = telaCadastro.getSenhaDigitado();

        if (nome.isEmpty() || senha.isEmpty() || login.isEmpty()) {
            telaCadastro.exibirMensagem("Preencha os campos", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario novoUsuario = new Usuario(nome, login, senha);

        try {
            pessoadao.cadastrarP(novoUsuario);

            telaCadastro.exibirMensagem("Conta criada", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            voltarLogin();
        } catch (Exception e) {
            e.printStackTrace();
            telaCadastro.exibirMensagem("Erro na conta", "Erro", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void voltarLogin() {
        this.telaCadastro.dispose();
        this.telaLoginOri.setVisible(true);
    }
}
