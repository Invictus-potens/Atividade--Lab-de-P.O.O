package controller;

import model.Pessoa;
import service.SistemaGerenciador;
import view.TelaLogin;
import view.TelaPrincipal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import dao.PessoaDAO;

public class LoginController {

    private TelaLogin telaLogin;
    private SistemaGerenciador sistemaGerenciador;
    private PessoaDAO pessoaDao;

    public LoginController(TelaLogin telaLogin, SistemaGerenciador sistemaGerenciador) {
        this.telaLogin = telaLogin;
        this.sistemaGerenciador = sistemaGerenciador;
        this.pessoaDao = new PessoaDAO();

        this.telaLogin.adicionarListenerBotaoEntrar(new LoginListener());
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
                    sistemaGerenciador.setPessoaLogada(userAutenticado);
                    telaLogin.exibirMensagem("Bem-vindo, " + userAutenticado.getNome());
                    telaLogin.dispose();

                    TelaPrincipal telaPrincipal = new TelaPrincipal(telaLogin);
                    TelaPrincipalCon principalController = new TelaPrincipalCon(telaPrincipal, sistemaGerenciador);
                    telaPrincipal.setVisible(true);
                } else {
                    telaLogin.exibirMensagem("Login ou senha incorreto.");
                }

            } catch (Exception ex) {
                telaLogin.exibirMensagem("Erro com banco login: " + ex.getMessage());
            }
        }
    }
}