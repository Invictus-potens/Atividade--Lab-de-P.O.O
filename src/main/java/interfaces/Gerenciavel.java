package interfaces;

import java.util.List;

/**
 * @param <T> tipo dos elementos gerenciados, ou seja, parâmetro genérico para outras classes assim não tem evita a criação de GerenciavelGrupoAposta e etc.
 */
public interface Gerenciavel<T> {
    // tratamento de exceções, se por exemplo o método listar().size() atingir o getTamanhoMaximo joga um throw new Exception
    void adicionar(T item) throws Exception; 

    List<T> listar();

    int getTamanhoMaximo();
}