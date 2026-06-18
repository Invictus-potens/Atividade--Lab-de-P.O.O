import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import controller.LoginController;
import service.SistemaGerenciador;
import view.TelaLogin;


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

            try {
                new dao.ClubeDAO().createTableSeNaoExistir();
                new dao.CampeonatoDAO().createTableSeNaoExistir();
                new dao.PessoaDAO().createTableSeNaoExistir();
                new dao.GrupoApostaDAO().createTableSeNaoExistir();
                new dao.PartidaDAO().createTableSeNaoExistir();
                new dao.ApostaDAO().createTableSeNaoExistir();
                System.out.println("SGBD SQLite inicializado com sucesso.");
            } catch (Exception e) {
                System.err.println("Erro crítico ao inicializar o banco de dados: " + e.getMessage());
            }

            SistemaGerenciador gerenciador = SistemaGerenciador.getInstance();

            TelaLogin telaLogin = new TelaLogin();

            LoginController loginController = new LoginController(telaLogin);

            telaLogin.setVisible(true);
        });
    }
}