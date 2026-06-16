package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import dao.PessoaDAO;
import model.Pessoa;
import view.TelaLogin;
import view.TelaPrincipal;

public class LoginController {

    private TelaLogin telaLogin;
    private PessoaDAO pessoaDao;

    public LoginController(TelaLogin telaLogin) {
        this.telaLogin = telaLogin;
        this.pessoaDao = new PessoaDAO();

        this.telaLogin.adicionarListenerBotaoEntrar(new LoginListener());
        this.telaLogin.adicionarListenerBotaoCriarConta(e -> abrirTelaCadastro());
    }
    
    private void abrirTelaCadastro() {
        this.telaLogin.setVisible(false);

        view.TelaCadastro telaCadastro = new view.TelaCadastro();
        CadastroCon cadastroCon = new CadastroCon(telaCadastro, this.telaLogin);

        telaCadastro.setVisible(true);
    }

    class LoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String loginDigitado = telaLogin.getLogin();
            String senhaDigitada = telaLogin.getSenha();

            if (loginDigitado.isEmpty() || senhaDigitada.isEmpty()) {
                telaLogin.exibirMensagem("Preencha os campos ");
                return;
            }

            try {
                Pessoa userAutenticado = pessoaDao.autenticar(loginDigitado, senhaDigitada);

                if (userAutenticado != null) {
                    telaLogin.exibirMensagem("Bem-vindo, " + userAutenticado.getNome());
                    telaLogin.dispose();

                    TelaPrincipal telaPrincipal = new TelaPrincipal(telaLogin, userAutenticado);
                    TelaPrincipalCon principalCon = new TelaPrincipalCon(telaPrincipal);
                    telaPrincipal.setVisible(true);
                } else {
                    telaLogin.exibirMensagem("Login ou senha incorreto.");
                }

            } catch (Exception ex) {
                ex.printStackTrace(); //deixar para saber onde foi o erro
                telaLogin.exibirMensagem("Erro com banco login: " + ex.getMessage());
            }
        }
    }
}