package controller;

import model.Pessoa;
import service.SistemaGerenciador;
import view.TelaLogin;
import view.TelaPrincipal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import controller.LoginController.LoginListener;

public class LoginController {

    private TelaLogin telaLogin;
    private SistemaGerenciador sistemaGerenciador;

    public LoginController(TelaLogin telaLogin, SistemaGerenciador sistemaGerenciador) {
        this.telaLogin = telaLogin;
        this.sistemaGerenciador = sistemaGerenciador;

        this.telaLogin.adicionarListenerBotaoEntrar(new LoginListener());
    }

    class LoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String loginDigitado = telaLogin.getLogin();
            String senhaDigitada = telaLogin.getSenha();

            Pessoa pessoaAutenticada = sistemaGerenciador.login(loginDigitado, senhaDigitada);

                if (pessoaAutenticada != null) {
                sistemaGerenciador.setPessoaLogada(pessoaAutenticada);
                telaLogin.exibirMensagem("Bem-vindo(a), " + pessoaAutenticada.getNome() + "!");
                
                telaLogin.dispose(); // Fecha a tela
                
                TelaPrincipal telaPrincipal = new TelaPrincipal();
                TelaPrincipalCon principalController = new TelaPrincipalCon(telaPrincipal, sistemaGerenciador);
                
                telaPrincipal.setVisible(true); // Exibe a tela 
            } else {
                telaLogin.exibirMensagem("Login ou senha incorretos.");
            }
        }
    }
}