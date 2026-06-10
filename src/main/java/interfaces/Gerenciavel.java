package interfaces;

import java.util.List;

/**
 * @param <T> tipo dos elementos gerenciados
 */
public interface Gerenciavel<T> {

    void adicionar(T item) throws Exception;

    List<T> listar();

    int getTamanhoMaximo();
}