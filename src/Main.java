import gui.TelaLogin;

import javax.swing.*;


/**
 *   - Encapsulamento   → atributos privados + getters/setters em todas as classes model
 *   - Construtores     → padrão (sem args) e sobrecarregado em todas as classes
 *   - Herança simples  → Usuario e Administrador herdam de Pessoa (classe abstrata)
 *   - Polimorfismo     → sobreposição de getTipo() e toString(); Aposta via Pontuavel;
 *                        Campeonato e GrupoAposta via Gerenciavel<T>
 *   - Classes abstratas→ Pessoa (define contrato getTipo())
 *   - Interfaces       → Pontuavel (calcularPontos), Gerenciavel<T> (adicionar/listar)
 *   - Interface gráfica→ Swing (JFrame, JPanel, JTabbedPane, JTable, etc.)
 *
 * Credenciais padrão do Administrador: login=admin / senha=admin123
 */
public class Main {

    public static void main(String[] args) {
        // Executar na Event Dispatch Thread do Swing (boa prática)
        SwingUtilities.invokeLater(() -> {
            try {
                // Usar o look and feel nativo do sistema operacional
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Continuar com o look and feel padrão caso falhe
                System.err.println("Não foi possível aplicar o look and feel do sistema: " + e.getMessage());
            }

            TelaLogin telaLogin = new TelaLogin();
            telaLogin.setVisible(true);
        });
    }
}
