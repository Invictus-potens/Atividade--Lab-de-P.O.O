import controller.LoginController;
import service.SistemaGerenciador;
import view.TelaLogin;

import javax.swing.*;


/*
 * Credenciais padrão do Administrador: login=admin / senha=admin123
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Não foi possível aplicar o look and feel do sistema: " + e.getMessage());
            }

            SistemaGerenciador gerenciador = SistemaGerenciador.getInstance();

            TelaLogin telaLogin = new TelaLogin();

            LoginController loginController = new LoginController(telaLogin, gerenciador);

            telaLogin.setVisible(true);
        });
    }
}